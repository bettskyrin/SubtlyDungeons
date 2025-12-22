package com.kr1s1s.subtlyd.world.entity;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.data.tags.DamageTypeTagsSD;
import com.kr1s1s.subtlyd.network.syncher.SynchedEntityDataSD;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class TentEntity extends Entity {
    public long lastHit;
    public boolean occupied;
    private final Supplier<Item> dropItem;

    public TentEntity(EntityType<?> entityType, Level level, Supplier<Item> supplier) {
        super(entityType, level);
        this.dropItem = supplier;
        this.occupied = false;
    }

    public static Identifier getLocation(DyeColor color) {
        return SubtlyDungeons.identifier(color.toString() + "_tent");
    }

    public static ResourceKey<@NotNull EntityType<?>> getResourceKey(DyeColor color) {
        return ResourceKey.create(Registries.ENTITY_TYPE, getLocation(color));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SynchedEntityDataSD.DATA_ID_HURT, 0);
        builder.define(SynchedEntityDataSD.DATA_ID_HURTDIR, 1);
        builder.define(SynchedEntityDataSD.DATA_ID_DAMAGE, 0.0F);
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

    private void setHurtDir(int i) {
        this.entityData.set(SynchedEntityDataSD.DATA_ID_HURTDIR, i);
    }

    private void setHurtTime(int i) {
        this.entityData.set(SynchedEntityDataSD.DATA_ID_HURT, i);
    }

    private void setDamage(float f) {
        this.entityData.set(SynchedEntityDataSD.DATA_ID_DAMAGE, f);
    }

    private float getDamage() {
        return this.entityData.get(SynchedEntityDataSD.DATA_ID_DAMAGE);
    }

    private int getHurtDir() {
        return this.entityData.get(SynchedEntityDataSD.DATA_ID_HURTDIR);
    }

    private int getHurtTime() {
        return this.entityData.get(SynchedEntityDataSD.DATA_ID_HURT);
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel serverLevel, @NotNull DamageSource damageSource, float f) {
        if (this.isRemoved()) {
            return false;
        } else if (!serverLevel.getGameRules().get(GameRules.MOB_GRIEFING) && damageSource.getEntity() instanceof Mob) {
            return false;
        } else if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            this.kill(serverLevel);
            return false;
        } else if (damageSource.is(DamageTypeTags.IS_EXPLOSION)) {
            this.broken();
            this.showBreakingParticles();
            this.kill(serverLevel);
            return false;
        } else if (damageSource.is(DamageTypeTagsSD.IGNITES_TENTS)) {
            if (this.isOnFire()) {
                this.setDamage(0.15F);
            } else {
                this.igniteForSeconds(5.0F);
            }
            return false;
        } else if (damageSource.is(DamageTypeTagsSD.BURNS_TENTS)) {
            this.setDamage(4.0F);
            return false;
        } else {
            boolean bl = damageSource.is(DamageTypeTagsSD.CAN_BREAK_TENT);
            boolean bl2 = damageSource.is(DamageTypeTagsSD.ALWAYS_KILLS_TENT);
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

    @Override protected void readAdditionalSaveData(@NotNull ValueInput valueInput) { }

    @Override protected void addAdditionalSaveData(@NotNull ValueOutput valueOutput) { }

    protected void pushEntities() {
        List<Entity> list = this.level().getPushableEntities(this, this.getBoundingBox());
        if (!list.isEmpty()) {
            if (this.level() instanceof ServerLevel serverLevel) {
                int i = serverLevel.getGameRules().get(GameRules.MAX_ENTITY_CRAMMING);
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
        ItemStack itemStack = new ItemStack(this.dropItem.get());
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

    /**
     * Called when a player interacts with the tent
     * @param vec3 Tent location
     * @return Server success
     */
    @Override
    public @NotNull InteractionResult interactAt(Player player, @NotNull Vec3 vec3, @NotNull InteractionHand interactionHand) {
        if (!player.level().isClientSide()) {
            ServerPlayerSD.startSleepInTent(this.blockPosition(), this, (ServerPlayer) player).ifLeft(tentSleepingProblem -> {
                if (tentSleepingProblem.getMessage() != null) {
                    player.displayClientMessage(tentSleepingProblem.getMessage(), true);
                }
            });
        }
        return InteractionResult.SUCCESS_SERVER;
    }

    /**
     * Checks if an entity is within tent range (2 blocks)
     * @param entity Entity to check
     * @return True if in range
     */
    public static boolean inTentRange(Entity entity) {
        AABB box = entity.getBoundingBox().inflate(2.0);
        return !(entity.level().getEntitiesOfClass(TentEntity.class, box).isEmpty());
    }

    /**
     * Checks if an entity is inside a tent
     * @param entity Entity to check
     * @return True if inside a tent
     */
    public static boolean inTent(Entity entity) {
        AABB box = entity.getBoundingBox().deflate(1.0);
        return !(entity.level().getEntitiesOfClass(TentEntity.class, box).isEmpty());
    }

    /**
     * Checks if an entity is inside a specific tent
     * @param entity Entity to check
     * @param tent Tent to check
     * @return True if inside the specified tent
     */
    public static boolean inTent(Entity entity, TentEntity tent) {
        AABB box = entity.getBoundingBox().deflate(1.0);
        return !(entity.level().getEntities(tent, box).isEmpty());
    }

    /**
     * Increases the render distance of tents to be higher than other entities by a factor of 4
     * @param d Squared distance
     * @return True if squared distance is within render distance
     */
    @Override
    public boolean shouldRenderAtSqrDistance(double d) {
        double e = this.getBoundingBox().getSize();
        if (Double.isNaN(e) || e == 0.0) {
            e = 4.0;
        }
        e *= 64.0 * getViewScale();
        return d < e * e;
    }

    private void playBrokenSound() {
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WOOL_BREAK, this.getSoundSource(), 1.0F, 1.0F);
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return explosion.getIndirectSourceEntity() instanceof Mob && !explosion.level().getGameRules().get(GameRules.MOB_GRIEFING);
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
