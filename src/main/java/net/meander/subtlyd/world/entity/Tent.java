package net.meander.subtlyd.world.entity;

import com.mojang.serialization.Codec;
import net.meander.subtlyd.core.component.DataComponentsSD;
import net.meander.subtlyd.stats.StatsSD;
import net.meander.subtlyd.util.UtilSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class Tent extends Entity {
    public long lastHit;
    public boolean isOccupied;
    private static final DyeColor DEFAULT_COLOR;
    private static final EntityDataAccessor<DyeColor> DATA_COLOR;
    private static final EntityDataAccessor<Integer> DATA_ID_HURT = SynchedEntityData.defineId(Tent.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ID_HURTDIR = SynchedEntityData.defineId(Tent.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_ID_DAMAGE = SynchedEntityData.defineId(Tent.class, EntityDataSerializers.FLOAT);

    public Tent(final EntityType<Tent> entityType, final Level level) {
        super(entityType, level);

        isOccupied = false;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public DyeColor getColor() {
        return entityData.get(DATA_COLOR);
    }

    public void setColor(final DyeColor color) {
        entityData.set(DATA_COLOR, color);
    }

    public void dropItem(final ServerLevel level, final @Nullable Entity causedBy) {
        playBrokenSound();
        showBreakingParticles();

        if (level.getGameRules().get(GameRules.ENTITY_DROPS)) {
            if (causedBy instanceof Player player) {
                if (player.hasInfiniteMaterials()) {
                    kill(level);
                    return;
                }
            }

            ItemEntity itemEntity = spawnAtLocation(level, getTentItemStackWithData());

            if (itemEntity != null && causedBy instanceof LightningBolt) {
                itemEntity.setInvulnerableFor(20);
            }
        }

        kill(level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_ID_HURT, 0);
        builder.define(DATA_ID_HURTDIR, 1);
        builder.define(DATA_ID_DAMAGE, 0.0F);
        builder.define(DATA_COLOR, DEFAULT_COLOR);
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
        entityData.set(DATA_ID_HURTDIR, hurtDir);
    }

    public void setHurtTime(int hurtTime) {
        entityData.set(DATA_ID_HURT, hurtTime);
    }

    public void setDamage(float damage) {
        entityData.set(DATA_ID_DAMAGE, damage);
    }

    public float getDamage() {
        return entityData.get(DATA_ID_DAMAGE);
    }

    public int getHurtDir() {
        return entityData.get(DATA_ID_HURTDIR);
    }

    public int getHurtTime() {
        return entityData.get(DATA_ID_HURT);
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel serverLevel, @NotNull DamageSource damageSource, float f) {
        if (isRemoved()) {
            return false;
        } else if (!serverLevel.getGameRules().get(GameRules.MOB_GRIEFING) && damageSource.getEntity() instanceof Mob) {
            return false;
        } else if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            dropItem(serverLevel, damageSource.getEntity());
            return false;
        } else if (damageSource.is(DamageTypeTags.IS_EXPLOSION)) {
            dropItem(serverLevel, damageSource.getEntity());
            return false;
        } else if (damageSource.is(DamageTypeTags.IS_FIRE)) {
            if (isOnFire()) {
                setDamage(0.15F);
            } else {
                igniteForSeconds(5.0F);
            }
            return false;
        } else {
            boolean sourceCanBreakTent = damageSource.is(DamageTypeTags.IS_PLAYER_ATTACK) || damageSource.is(DamageTypeTags.IS_EXPLOSION);
            boolean sourceIsProjectile = damageSource.is(DamageTypeTags.IS_PROJECTILE);

            if (!(sourceCanBreakTent || sourceIsProjectile)) {
                return false;
            } else if (damageSource.getEntity() instanceof Player player && !player.getAbilities().mayBuild) {
                return false;
            } else if (damageSource.isCreativePlayer()) {
                dropItem(serverLevel, damageSource.getEntity());
                return true;
            } else {
                long time = serverLevel.getGameTime();

                if (time - lastHit > 5L && !sourceIsProjectile) {
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
                    dropItem(serverLevel, damageSource.getEntity());
                }
                return true;
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        setColor(input.read("color", DyeColor.CODEC).orElse(DEFAULT_COLOR));
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        output.store("color", DyeColor.CODEC, getColor());
        output.store("DataVersionSD", Codec.INT, UtilSD.DATA_VERSION);
    }

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
            ServerPlayerSD.startSleepInTent(this, (ServerPlayer) player).ifLeft(tentSleepingProblem -> {
                if (tentSleepingProblem.message() != null) {
                    player.sendOverlayMessage(tentSleepingProblem.message());
                }
            }).ifRight(_ -> player.awardStat(StatsSD.SLEEP_IN_TENT));
        }
        return InteractionResult.SUCCESS_SERVER;
    }


    /**
     * Used for testing for or getting the tent an entity (player) is using.
     * @param livingEntity The entity to check.
     * @return The tent the entity is actively using.
     */
    public static Tent getTent(LivingEntity livingEntity, boolean isSleeping) {
        int bB = isSleeping ? -1 : 2;
        Tent tent = livingEntity.level().getEntitiesOfClass(Tent.class, livingEntity.getBoundingBox().inflate(bB)).stream().findFirst().orElse(null);

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
        return new ItemStack(ItemsSD.TENT.pick(getColor()));
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public <T> @Nullable T get(final DataComponentType<? extends T> type) {
        return type == DataComponentsSD.TENT_COLOR ? castComponentValue(type, getColor()) : get(type);
    }

    @Override
    protected void applyImplicitComponents(final DataComponentGetter components) {
        this.applyImplicitComponentIfPresent(components, DataComponentsSD.TENT_COLOR);
        super.applyImplicitComponents(components);
    }

    @Override
    protected <T> boolean applyImplicitComponent(final DataComponentType<T> type, final T value) {
        if (type == DataComponentsSD.TENT_COLOR) {
            setColor(castComponentValue(DataComponentsSD.TENT_COLOR, value));
            return true;
        } else {
            return super.applyImplicitComponent(type, value);
        }
    }

    private ItemStack getTentItemStackWithData() {
        ItemStack itemStack = new ItemStack(ItemsSD.TENT.pick(getColor()));

        itemStack.set(DataComponents.CUSTOM_NAME, getCustomName());
        return itemStack;
    }

    static {
        DEFAULT_COLOR = DyeColor.WHITE;
        DATA_COLOR = SynchedEntityData.defineId(Tent.class, EntityDataSerializers.DYE_COLOR);
    }
}