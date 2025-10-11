package com.kr1s1s.subtlyd.world.entity;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class TentEntity extends Entity {
    public long lastHit;
    private boolean occupied;
    private final Supplier<Item> dropItem;
    public static DyeColor color;
    protected static final EntityDataAccessor<Integer> DATA_ID_HURT = SynchedEntityData.defineId(TentEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> DATA_ID_HURTDIR = SynchedEntityData.defineId(TentEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Float> DATA_ID_DAMAGE = SynchedEntityData.defineId(TentEntity.class, EntityDataSerializers.FLOAT);

    public TentEntity(EntityType<?> entityType, Level level, Supplier<Item> supplier, DyeColor dyeColor) {
        super(entityType, level);
        this.dropItem = supplier;
        color = dyeColor;
        setOccupied(false);
    }

    public static ResourceLocation getLocation(DyeColor color) {
        return ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, color.toString() + "_tent");
    }

    public static ResourceKey<EntityType<?>> getResourceKey(DyeColor color) {
        return ResourceKey.create(Registries.ENTITY_TYPE, getLocation(color));
    }

    public Boolean getOccupied() { return occupied; }

    public void setOccupied(Boolean bl) { occupied = bl; }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_ID_HURT, 0);
        builder.define(DATA_ID_HURTDIR, 1);
        builder.define(DATA_ID_DAMAGE, 0.0F);
    }

    @Override
    public void tick() {
        if (this.getHurtTime() > 0) {
            this.setHurtTime(this.getHurtTime() - 1);
        }

        if (this.getDamage() > 0.0F) {
            this.setDamage(this.getDamage() - 1.0F);
        }

        if (!this.isRemoved()) {
            this.pushEntities();
        }
        super.tick();
    }

    @Override
    public void animateHurt(float f) {
        this.setHurtDir(-this.getHurtDir());
        this.setHurtTime(10);
        this.setDamage(this.getDamage() * 11.0F);
    }

    public void setHurtDir(int i) {
        this.entityData.set(DATA_ID_HURTDIR, i);
    }

    public void setHurtTime(int i) {
        this.entityData.set(DATA_ID_HURT, i);
    }

    public void setDamage(float f) {
        this.entityData.set(DATA_ID_DAMAGE, f);
    }

    public float getDamage() {
        return this.entityData.get(DATA_ID_DAMAGE);
    }

    public int getHurtDir() {
        return this.entityData.get(DATA_ID_HURTDIR);
    }

    public int getHurtTime() {
        return this.entityData.get(DATA_ID_HURT);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float f) {
        if (this.isRemoved()) {
            return false;
        } else if (!serverLevel.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && damageSource.getEntity() instanceof Mob) {
            return false;
        } else if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            this.kill(serverLevel);
            return false;
        } else if (damageSource.is(DamageTypeTags.IS_EXPLOSION)) {
            this.broken();
            this.showBreakingParticles();
            this.kill(serverLevel);
            return false;
        } else if (damageSource.is(DamageTypeTags.IGNITES_ARMOR_STANDS)) {
            if (this.isOnFire()) {
                this.setDamage(0.15F);
            } else {
                this.igniteForSeconds(5.0F);
            }
            return false;
        } else if (damageSource.is(DamageTypeTags.BURNS_ARMOR_STANDS)) {
            this.setDamage(4.0F);
            return false;
        } else {
            boolean bl = damageSource.is(DamageTypeTags.CAN_BREAK_ARMOR_STAND);
            boolean bl2 = damageSource.is(DamageTypeTags.ALWAYS_KILLS_ARMOR_STANDS);
            if (!(bl || bl2)) {
                return false;
            } else if (damageSource.getEntity() instanceof Player player && !player.getAbilities().mayBuild) {
                return false;
            } else if (damageSource.isCreativePlayer()) {
                this.playBrokenSound();
                this.showBreakingParticles();
                this.kill(serverLevel);
                return true;
            } else {
                long l = serverLevel.getGameTime();
                if (l - this.lastHit > 5L && !bl2) {
                    serverLevel.broadcastEntityEvent(this, (byte)32);
                    this.gameEvent(GameEvent.ENTITY_DAMAGE, damageSource.getEntity());
                    this.lastHit = l;
                    this.showBreakingParticles();
                } else {
                    this.broken();
                    this.showBreakingParticles();
                    this.kill(serverLevel);
                }
                return true;
            }
        }
    }

    @Override protected void readAdditionalSaveData(ValueInput valueInput) { }

    @Override protected void addAdditionalSaveData(ValueOutput valueOutput) { }

    protected void pushEntities() {
        List<Entity> list = this.level().getPushableEntities(this, this.getBoundingBox());
        if (!list.isEmpty()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                int i = serverLevel.getGameRules().getInt(GameRules.RULE_MAX_ENTITY_CRAMMING);
                if (i > 0 && list.size() > i - 1 && this.random.nextInt(4) == 0) {
                    int j = 0;

                    for (Entity entity : list) {
                        if (!entity.isPassenger()) {
                            j++;
                        }
                    }

                    if (j > i - 1) {
                        this.hurtServer(serverLevel, this.damageSources().cramming(), 6.0F);
                    }
                }
            }

            for (Entity entity2 : list) {
                this.doPush(entity2);
            }
        }
    }

    protected void doPush(Entity entity) {
        entity.push(this);
    }

    private void broken() {
        ItemStack itemStack = new ItemStack(dropItem.get());
        itemStack.set(DataComponents.CUSTOM_NAME, this.getCustomName());
        Block.popResource(this.level(), this.blockPosition(), itemStack);
        this.playBrokenSound();
    }

    private void showBreakingParticles() {
        if (this.level() instanceof ServerLevel) {
            ((ServerLevel)this.level())
                    .sendParticles(
                            new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SPRUCE_FENCE.defaultBlockState()),
                            this.getX(),
                            this.getY(0.6666666666666666),
                            this.getZ(),
                            10,
                            this.getBbWidth() / 4.0F,
                            this.getBbHeight() / 4.0F,
                            this.getBbWidth() / 4.0F,
                            0.05
                    );
        }
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 32) {
            if (this.level().isClientSide()) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.WOOL_BREAK, this.getSoundSource(), 0.3F, 1.0F, false);
                this.lastHit = this.level().getGameTime();
            }
        } else {
            super.handleEntityEvent(b);
        }
    }

    @Override
    public @NotNull InteractionResult interactAt(Player player, Vec3 vec3, InteractionHand interactionHand) {
        if (player.level().isClientSide()) {
            return InteractionResult.SUCCESS_SERVER;
        } else {
            ServerPlayerSD.startSleepInTent(this.blockPosition(), this, (ServerPlayer) player).ifLeft(tentSleepingProblem -> {
                if (tentSleepingProblem.getMessage() != null) {
                    player.displayClientMessage(tentSleepingProblem.getMessage(), true);
                }
            });
            return InteractionResult.SUCCESS_SERVER;
        }
    }

    public static void allowTentSleep() {
        EntitySleepEvents.ALLOW_BED.register((entity, sleepingPos, state, vanillaResult) -> {
            if (inTentRange(entity)) {
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });

        EntitySleepEvents.ALLOW_RESETTING_TIME.register((entity) -> true);
    }

    public static boolean inTentRange(Entity entity) {
        AABB box = entity.getBoundingBox().inflate(2.0);
        return !(entity.level().getEntitiesOfClass(TentEntity.class, box).isEmpty());
    }

    public static boolean inTent(Entity entity) {
        AABB box = entity.getBoundingBox().deflate(1.0);
        return !(entity.level().getEntitiesOfClass(TentEntity.class, box).isEmpty());
    }

    public static boolean inTent(Entity entity, TentEntity tent) {
        AABB box = entity.getBoundingBox().deflate(1.0);
        return !(entity.level().getEntities(tent, box).isEmpty());
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double d) {
        double e = this.getBoundingBox().getSize() * 4.0;
        if (Double.isNaN(e) || e == 0.0) {
            e = 4.0;
        }

        e *= 64.0;
        return d < e * e;
    }

    private void playBrokenSound() {
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WOOL_BREAK, this.getSoundSource(), 1.0F, 1.0F);
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return explosion.getIndirectSourceEntity() instanceof Mob && !explosion.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
    }

    @Override
    public boolean isPickable() { return !this.isRemoved(); }

    @Override
    public ItemStack getPickResult() { return new ItemStack(this.dropItem.get()); }

    @Override
    public boolean isPushedByFluid() { return false; }

    @Override
    public @NotNull Component getDisplayName() { return Component.empty(); }
}
