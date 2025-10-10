package com.kr1s1s.subtlyd.world.entity;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class TentEntity extends Entity {
    public long lastHit;
    private Boolean occupied;
    private final Supplier<Item> dropItem;
    public static DyeColor color;

    public TentEntity(EntityType<?> entityType, Level level, Supplier<Item> supplier, DyeColor dyeColor) {
        super(entityType, level);
        this.dropItem = supplier;
        color = dyeColor;
        setOccupied(false);
    }

    public Boolean getOccupied() { return occupied; }
    public void setOccupied(Boolean bl) { occupied = bl; }

    public static ResourceKey<EntityType<?>> getResourceKey(DyeColor color) {
        return ResourceKey.create(Registries.ENTITY_TYPE, getLocation(color));
    }

    public static ResourceLocation getLocation(DyeColor color) {
        return ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, color.toString() + "_tent");
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

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
            this.broken(serverLevel, damageSource);
            this.kill(serverLevel);
            return false;
        } else if (damageSource.is(DamageTypeTags.IGNITES_ARMOR_STANDS)) {
            if (this.isOnFire()) {
                this.causeDamage(serverLevel, damageSource, 0.15F);
            } else {
                this.igniteForSeconds(5.0F);
            }

            return false;
        } else if (damageSource.is(DamageTypeTags.BURNS_ARMOR_STANDS)) {
            this.causeDamage(serverLevel, damageSource, 4.0F);
            return false;
        } else {
            boolean bl = damageSource.is(DamageTypeTags.CAN_BREAK_ARMOR_STAND);
            boolean bl2 = damageSource.is(DamageTypeTags.ALWAYS_KILLS_ARMOR_STANDS);
            if (!bl && !bl2) {
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
                } else {
                    this.broken(serverLevel, damageSource);
                    this.showBreakingParticles();
                    this.kill(serverLevel);
                }

                return true;
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {

    }

    private void causeDamage(ServerLevel serverLevel, DamageSource damageSource, float f) {
        if (this.asLivingEntity() == null) {
            SubtlyDungeons.debug("Null as living entity");
            return;
        }

        float g = this.asLivingEntity().getHealth();
        g -= f;
        if (g <= 0.5F) {
            this.broken(serverLevel, damageSource);
            this.kill(serverLevel);
        } else {
            //this.setHealth(g);
            this.gameEvent(GameEvent.ENTITY_DAMAGE, damageSource.getEntity());
        }
    }

    private void broken(ServerLevel serverLevel, DamageSource damageSource) {
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
    public InteractionResult interactAt(Player player, Vec3 vec3, InteractionHand interactionHand) { // TODO Review
        if (player.level().isClientSide()) {
            return InteractionResult.SUCCESS_SERVER;
        } else {

            if (getOccupied()) {
                player.displayClientMessage(Component.translatable("entity.subtlyd.tent.occupied"), true);
                return InteractionResult.SUCCESS_SERVER;
            } else {
                if (!BedBlock.canSetSpawn(level())) {
                    // TODO Random time if others are sleeping
                }
                // player.startSleepingTent(this, player);
                SubtlyDungeons.debug("Starting sleep");
                return InteractionResult.SUCCESS_SERVER;
            }
        }
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
        SubtlyDungeons.debug(this.getEncodeId());
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WOOL_BREAK, this.getSoundSource(), 1.0F, 1.0F);
    }

    @Override
    public boolean ignoreExplosion(Explosion explosion) {
        return explosion.getIndirectSourceEntity() instanceof Mob && !explosion.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
    }

    @Override
    public boolean isPickable() { return true; }

    @Override
    public ItemStack getPickResult() { return new ItemStack(this.dropItem.get()); }

    @Override
    public boolean isPushedByFluid() { return false; }

    @Override
    public @NotNull Component getDisplayName() { return Component.empty(); }
}
