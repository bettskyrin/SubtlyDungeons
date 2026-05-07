package net.meander.subtlyd.world.entity;

import net.meander.subtlyd.data.tags.DamageTypeTagsSD;
import net.meander.subtlyd.network.syncher.SynchedEntityDataSD;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.function.Supplier;

public class TentEntity extends Entity {
    public long lastHit;
    public boolean occupied;
    private final Supplier<Item> dropItem;

    public TentEntity(EntityType<?> entityType, Level level, Supplier<Item> supplier) {
        super(entityType, level);
        dropItem = supplier;
        occupied = false;
    }

    public boolean isOccupied() {
        return occupied;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SynchedEntityDataSD.DATA_ID_HURT, 0);
        builder.define(SynchedEntityDataSD.DATA_ID_HURTDIR, 1);
        builder.define(SynchedEntityDataSD.DATA_ID_DAMAGE, 0.0F);
    }

    @Override
    public void tick() {
        if (getHurtTime() > 0) {
            setHurtTime(getHurtTime() - 1);
        }

        if (getDamage() > 0.0F) {
            setDamage(getDamage() - 1.0F);
        }

        if (!isRemoved()) {
            pushEntities();
        }
        super.tick();
    }

    @Override
    public void animateHurt(float f) {
        setHurtDir(-getHurtDir());
        setHurtTime(10);
        setDamage(getDamage());
    }

    public void setHurtDir(int hurtDir) {
        entityData.set(SynchedEntityDataSD.DATA_ID_HURTDIR, hurtDir);
    }

    public void setHurtTime(int hurtTime) {
        entityData.set(SynchedEntityDataSD.DATA_ID_HURT, hurtTime);
    }

    public void setDamage(float damage) {
        entityData.set(SynchedEntityDataSD.DATA_ID_DAMAGE, damage);
    }

    public float getDamage() {
        return entityData.get(SynchedEntityDataSD.DATA_ID_DAMAGE);
    }

    public int getHurtDir() {
        return entityData.get(SynchedEntityDataSD.DATA_ID_HURTDIR);
    }

    public int getHurtTime() {
        return entityData.get(SynchedEntityDataSD.DATA_ID_HURT);
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel serverLevel, @NotNull DamageSource damageSource, float f) {
        if (isRemoved()) {
            return false;
        } else if (!serverLevel.getGameRules().get(GameRules.MOB_GRIEFING) && damageSource.getEntity() instanceof Mob) {
            return false;
        } else if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            kill(serverLevel);
            return false;
        } else if (damageSource.is(DamageTypeTags.IS_EXPLOSION)) {
            broken();
            showBreakingParticles();
            kill(serverLevel);
            return false;
        } else if (damageSource.is(DamageTypeTagsSD.IGNITES_TENTS)) {
            if (isOnFire()) {
                setDamage(0.15F);
            } else {
                igniteForSeconds(5.0F);
            }
            return false;
        } else if (damageSource.is(DamageTypeTagsSD.BURNS_TENTS)) {
            setDamage(4.0F);
            return false;
        } else {
            boolean canBreak = damageSource.is(DamageTypeTagsSD.CAN_BREAK_TENT);
            boolean alwaysKills = damageSource.is(DamageTypeTagsSD.ALWAYS_KILLS_TENT);
            if (!(canBreak || alwaysKills)) {
                return false;
            } else if (damageSource.getEntity() instanceof Player player && !player.getAbilities().mayBuild) {
                return false;
            } else if (damageSource.isCreativePlayer()) {
                playBrokenSound();
                showBreakingParticles();
                kill(serverLevel);
                return true;
            } else {
                long time = serverLevel.getGameTime();

                if (time - lastHit > 5L && !alwaysKills) {
                    if (damageSource.getEntity() != null) {
                        setHurtDir(1);
                    }
                    setHurtTime(10);
                    setDamage(10);
                    markHurt();

                    serverLevel.broadcastEntityEvent(this, (byte) 32);
                    gameEvent(GameEvent.ENTITY_DAMAGE, damageSource.getEntity());
                    lastHit = time;
                    showBreakingParticles();
                } else {
                    broken();
                    showBreakingParticles();
                    kill(serverLevel);
                }
                return true;
            }
        }
    }

    @Override protected void readAdditionalSaveData(@NotNull ValueInput valueInput) { }

    @Override protected void addAdditionalSaveData(@NotNull ValueOutput valueOutput) { }

    protected void pushEntities() {
        List<Entity> list = level().getPushableEntities(this, getBoundingBox());

        if (!list.isEmpty()) {
            if (level() instanceof ServerLevel serverLevel) {
                int maxCramming = serverLevel.getGameRules().get(GameRules.MAX_ENTITY_CRAMMING);

                if (maxCramming > 0 && list.size() > maxCramming - 1 && random.nextInt(4) == 0) {
                    int count = 0;

                    for (Entity entity : list) {
                        if (!entity.isPassenger()) {
                            count++;
                        }
                    }

                    if (count > maxCramming - 1) {
                        hurtServer(serverLevel, damageSources().cramming(), 6.0F);
                    }
                }
            }

            for (Entity entity2 : list) {
                doPush(entity2);
            }
        }
    }

    protected void doPush(Entity entity) {
        entity.push(this);
    }

    private void broken() {
        ItemStack itemStack = new ItemStack(dropItem.get());
        itemStack.set(DataComponents.CUSTOM_NAME, getCustomName());
        Block.popResource(level(), blockPosition(), itemStack);
        playBrokenSound();
    }

    private void showBreakingParticles() {
        if (level() instanceof ServerLevel) {
            ((ServerLevel)level())
                    .sendParticles(
                            new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SPRUCE_FENCE.defaultBlockState()),
                            getX(),
                            getY(0.6666666666666666),
                            getZ(),
                            10,
                            getBbWidth() / 4.0F,
                            getBbHeight() / 4.0F,
                            getBbWidth() / 4.0F,
                            0.05
                    );
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 32) {
            if (level().isClientSide()) {
                level().playLocalSound(getX(), getY(), getZ(), SoundEvents.WOOL_BREAK, getSoundSource(), 0.3F, 1.0F, false);
                lastHit = level().getGameTime();
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    /**
     * Called when a player interacts with the tent
     * @param location Tent location
     * @return Server success
     */
    @Override
    public @NotNull InteractionResult interact(final Player player, final @NonNull InteractionHand hand, final @NonNull Vec3 location) {
        if (!player.level().isClientSide()) {
            ServerPlayerSD.startSleepInTent(blockPosition(), this, (ServerPlayer) player).ifLeft(tentSleepingProblem -> {
                if (tentSleepingProblem.message() != null) {
                    player.sendOverlayMessage(tentSleepingProblem.message());
                }
            });
        }
        return InteractionResult.SUCCESS_SERVER;
    }


    /**
     * Used for testing for or getting the tent an entity (player) is using.
     * @param livingEntity The entity to check.
     * @return The tent the entity is actively using.
     */
    public static TentEntity getTent(LivingEntity livingEntity, boolean isSleeping) {
        int bB = isSleeping ? -1 : 2;
        TentEntity tent = livingEntity.level().getEntitiesOfClass(TentEntity.class, livingEntity.getBoundingBox().inflate(bB)).stream().findFirst().orElse(null);

        if (isSleeping && !livingEntity.isSleeping()) {
            return null;
        }
        return tent;
    }

    /**
     * Increases the render distance of tents to be higher than other entities by a factor of 4
     * @param distance Squared distance
     * @return True if squared distance is within render distance
     */
    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double size = getBoundingBox().getSize();
        if (Double.isNaN(size) || size == 0.0) {
            size = 4.0;
        }
        size *= 64.0 * getViewScale();
        return distance < size * size;
    }

    private void playBrokenSound() {
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.WOOL_BREAK, getSoundSource(), 1.0F, 1.0F);
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return explosion.getIndirectSourceEntity() instanceof Mob && !explosion.level().getGameRules().get(GameRules.MOB_GRIEFING);
    }

    @Override
    public boolean isPickable() {
        return !isRemoved();
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(dropItem.get());
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }
}