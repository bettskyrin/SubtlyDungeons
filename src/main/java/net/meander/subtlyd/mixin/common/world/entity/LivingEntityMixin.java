package net.meander.subtlyd.mixin.common.world.entity;

import net.meander.subtlyd.sounds.SoundEventsSD;
import net.meander.subtlyd.stats.StatsSD;
import net.meander.subtlyd.tags.EntityTypeTagsSD;
import net.meander.subtlyd.tags.ItemTagsSD;
import net.meander.subtlyd.world.entity.LivingEntitySD;
import net.meander.subtlyd.world.entity.MobSD;
import net.meander.subtlyd.world.entity.ai.attributes.AttributesSD;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    private LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    /**
     * Runs when an entity takes damage.
     */
    @Inject(method = "hurtServer", at = @At("RETURN"))
    private void panicFromDamage(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;

        if (!livingEntity.level().isClientSide() && cir.getReturnValue() && livingEntity.is(EntityTypeTagsSD.CAN_BE_SCARED)) {
            if (source.is(DamageTypeTags.PANIC_CAUSES) && (!source.is(DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES)
            || source.is(DamageTypes.LIGHTNING_BOLT)) && source.getEntity() instanceof LivingEntity attacker) {
                shareHerdPanic(livingEntity, attacker);
            }
        }
    }

    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true, name = "source")
    private DamageSource roastedByDragon(DamageSource source) {
        if (source.getDirectEntity() instanceof AreaEffectCloud cloud) {
            if (cloud.getParticle().getType() == ParticleTypes.DRAGON_BREATH) {
                LivingEntity livingEntity = (LivingEntity) (Object) this;
                Holder<DamageType> holder = livingEntity.damageSources().dragonBreath().typeHolder();

                return new DamageSource(holder, source.getDirectEntity(), source.getEntity());
            }
        }
        return source;
    }

    /**
     * Broadcasts that an animal was attacked, and that any nearby animals should flee.
     * @param victim The animal being attacked.
     * @param attacker The attacking entity.
     */
    private void shareHerdPanic(LivingEntity victim, LivingEntity attacker) {
        final double RADIUS = 16.0F;
        AABB searchAabb = victim.getBoundingBox().inflate(RADIUS);
        List<? extends LivingEntity> herd = victim.level().getEntitiesOfClass(Animal.class, searchAabb);

        for (LivingEntity animal : herd) {
            if (animal.is(EntityTypeTagsSD.CAN_BE_SCARED) && animal != victim && !animal.isAlliedTo(attacker)) {
                if (animal instanceof PathfinderMob mob) {
                    Vec3 pos = DefaultRandomPos.getPosAway(mob, 16, 4, attacker.position());
                    int tries = 0;

                    while (tries < 10 && (pos == null || (pos.x == mob.getX() && pos.z == mob.getZ()))) {
                        pos = DefaultRandomPos.getPosAway(mob, 16, 4, attacker.position());
                        tries++;
                    }

                    if (pos != null) {
                        mob.getNavigation().moveTo(pos.x, pos.y, pos.z, LivingEntitySD.getPanicSpeed(mob));
                    }
                }
            }
        }
    }

    @Inject(method = "die", at = @At("HEAD"))
    private void addHunterCooldown(DamageSource source, CallbackInfo ci) {
        if (source.getEntity() instanceof Mob predator && predator.is(EntityTypeTagsSD.CAN_BE_FULL)) {
            long cooldownTicks = predator.is(EntityTypeTagsSD.FEAST_OR_FAMINE_HUNTER) ? 72000 : 12000;

            ((MobSD) predator).setHuntingCooldownTicks(predator.level().getGameTime() + cooldownTicks);
        }
    }

    /**
     * Makes predators consume meat from their prey.
     */
    @Inject(method = "dropAllDeathLoot", at = @At("TAIL"))
    private void consumePrey(ServerLevel level, DamageSource source, CallbackInfo ci) {
        if (source.getEntity() instanceof Mob predator && predator.is(EntityTypeTagsSD.CAN_BE_FULL)) {
            if ((predator instanceof TamableAnimal tamableAnimal && !tamableAnimal.isTame()) || !(predator instanceof TamableAnimal)) {
                LivingEntity livingEntity = (LivingEntity) (Object) this;
                List<ItemEntity> drops = level.getEntitiesOfClass(ItemEntity.class, livingEntity.getBoundingBox().inflate(1.0F));

                for (ItemEntity droppedItem : drops) {
                    ItemStack stack = droppedItem.getItem();

                    if (stack.has(DataComponents.FOOD) && (stack.is(ItemTags.MEAT) || stack.is(ItemTags.FISHES))) {
                        FoodProperties food = stack.get(DataComponents.FOOD);

                        if (food != null) {
                            predator.heal(food.nutrition() * stack.getCount());
                        }
                        droppedItem.discard();
                    }
                }
            }
        }
    }

    /**
     * Alter the maximum health of an entity based on difficulty.
     */
    @Inject(method = "getMaxHealth", at = @At("RETURN"), cancellable = true)
    private void setDifficultyHealth(CallbackInfoReturnable<Float> cir) {
        if (((LivingEntity) (Object) this) instanceof WitherBoss witherBoss) {
            float maxHealth = cir.getReturnValue();
            int difficultyLevel = witherBoss.level().getDifficulty().getId();

            if (difficultyLevel > 2) {
                maxHealth = 600.0F;
            } else if (difficultyLevel == 2) {
                maxHealth = 450.0F;
            }
            cir.setReturnValue(maxHealth);
        }
    }

    @Inject(method = "getVisibilityPercent", at = @At("RETURN"), cancellable = true)
    @SuppressWarnings("DataFlowIssue")
    private void modifyStealthSystem(Entity targetingEntity, CallbackInfoReturnable<Double> cir) {
        if (((LivingEntity) (Object) this) instanceof Player player && targetingEntity != null) {
            boolean isPlayerObvious = player.hasEffect(MobEffects.GLOWING);
            boolean isPlayerSuperDiscrete = (player.isDiscrete() || player.isInvisible() || player.isVisuallyCrawling()) && !isPlayerObvious;
            boolean isTargetBlind = targetingEntity.asLivingEntity().hasEffect(MobEffects.BLINDNESS);
            boolean recentlyAttacked = (player.tickCount - player.getLastHurtMobTimestamp()) < 100;

            if ((isPlayerSuperDiscrete && !recentlyAttacked) || isTargetBlind) {
                Vec3 directionToPlayer = player.position().subtract(targetingEntity.position()).normalize();
                Vec3 observerLookVector = targetingEntity.getLookAngle();
                double alignment = observerLookVector.dot(directionToPlayer);

                if (alignment < 0.45) {
                    cir.setReturnValue(0.0);
                }
            }

            if (isPlayerObvious) {
                cir.setReturnValue(1.0);
            }
        }
    }

    @Inject(method = "onClimbable", at = @At("HEAD"), cancellable = true)
    private void applyArthropodWallClimbing(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof Silverfish || entity instanceof Endermite) {
            if (entity.horizontalCollision) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void createLivingAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.getReturnValue().add(AttributesSD.SHIELD_STRENGTH);
    }

    @Inject(method = "applyItemBlocking", at = @At("RETURN"), cancellable = true)
    private void applyShieldPassthrough(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Float> cir) {
        float blockedAmount = cir.getReturnValue();

        if (blockedAmount > 0.0F && !source.is(DamageTypeTags.IS_PROJECTILE)) {
            LivingEntity entity = (LivingEntity) (Object) this;
            float shieldStrength = (float) entity.getAttributeValue(AttributesSD.SHIELD_STRENGTH);

            if (blockedAmount > shieldStrength) {
                cir.setReturnValue(shieldStrength);
            }
        }
    }

    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    private void interceptForParry(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity defender = (LivingEntity) (Object) this;

        if (source.getDirectEntity() instanceof LivingEntity attacker) {
            Vec3 look = defender.getLookAngle();
            Vec3 viewVector = new Vec3(look.x, 0.0, look.z).normalize();
            Vec3 attackVector = attacker.position().subtract(defender.position());
            Vec3 directionToAttacker = new Vec3(attackVector.x, 0.0, attackVector.z).normalize();
            ItemStack attackerItem = attacker.getMainHandItem();
            ItemStack defenderItem = defender.getMainHandItem();
            boolean isFacingAttacker = viewVector.dot(directionToAttacker) > 0.0;
            boolean isSwordFight = attackerItem.is(ItemTagsSD.CAN_PARRY_SWORDS) && defenderItem.is(ItemTagsSD.CAN_PARRY_SWORDS);
            boolean isKnifeFIght = attackerItem.is(ItemTagsSD.CAN_PARRY_DAGGERS) && defenderItem.is(ItemTagsSD.CAN_PARRY_DAGGERS);

            if (isFacingAttacker && (isSwordFight || isKnifeFIght)) {
                if (defender.swinging && defender.swingTime > 0 && defender.swingTime <= 10) {
                    boolean hasWoodenWeapon = hasWoodenWeapon(attackerItem, defenderItem);
                    float pitch = hasWoodenWeapon ? 0.7F : 1.5F + (level.getRandom().nextFloat() * 0.2F);
                    SoundEvent soundEffect = hasWoodenWeapon ? SoundEventsSD.BLADE_WOOD_CLASH : SoundEventsSD.BLADE_CLASH;

                    level.playSound(null, defender.getX(), defender.getY(), defender.getZ(), soundEffect, SoundSource.PLAYERS, 0.5F, pitch);
                    level.sendParticles(ParticleTypes.ELECTRIC_SPARK, defender.getX(), defender.getY(0.5), defender.getZ(), 2, 0, 0, 0, 0);
                    attackerItem.hurtAndBreak(1, level, attacker instanceof ServerPlayer p ? p : null, (item) -> attacker.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
                    defenderItem.hurtAndBreak(1, level, defender instanceof ServerPlayer p ? p : null, (item) -> defender.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));

                    if (defender instanceof ServerPlayer player) {
                        player.awardStat(StatsSD.DAMAGE_BLOCKED_BY_WEAPON, Math.round(damage * 10.0F));
                    }

                    cir.setReturnValue(false);
                }
            }
        }
    }

    private boolean hasWoodenWeapon(ItemStack attackerItem, ItemStack defenderItem) {
        for (ItemStack weapon : List.of(attackerItem, defenderItem)) {
            for (Item material : ItemTagsSD.getItems(ItemTags.WOODEN_TOOL_MATERIALS)) {
                if (weapon.isValidRepairItem(material.getDefaultInstance())) {
                    return true;
                }
            }
        }
        return false;
    }
}