package net.meander.subtlyd.mixin.common.world.entity;

import net.meander.subtlyd.tags.EntityTypeTagsSD;
import net.meander.subtlyd.world.entity.LivingEntitySD;
import net.meander.subtlyd.world.entity.MobSD;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.food.FoodProperties;
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
}