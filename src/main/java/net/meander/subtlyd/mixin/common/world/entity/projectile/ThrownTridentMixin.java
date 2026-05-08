package net.meander.subtlyd.mixin.common.world.entity.projectile;

import net.meander.subtlyd.client.renderer.ChargedTridentState;
import net.meander.subtlyd.network.syncher.SynchedEntityDataSD;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin implements ChargedTridentState.Accessor {
    @Override
    public boolean subtlyd$isCharged() {
        ThrownTrident trident = (ThrownTrident) (Object) this;

        return trident.getEntityData().get(SynchedEntityDataSD.DATA_ID_CHARGED_TRIDENT);
    }

    @Override
    public void subtlyd$setCharged(boolean charged) {
        ThrownTrident trident = (ThrownTrident) (Object) this;

        trident.getEntityData().set(SynchedEntityDataSD.DATA_ID_CHARGED_TRIDENT, charged);
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerId(CallbackInfo ci) {
        SynchedEntityDataSD.DATA_ID_CHARGED_TRIDENT = SynchedEntityData.defineId(ThrownTrident.class, EntityDataSerializers.BOOLEAN);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineSynchedData(SynchedEntityData.Builder entityData, CallbackInfo ci) {
        entityData.define(SynchedEntityDataSD.DATA_ID_CHARGED_TRIDENT, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(ValueOutput output, CallbackInfo ci) {
        output.putBoolean("IsCharged",  subtlyd$isCharged());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(ValueInput input, CallbackInfo ci) {
        ThrownTrident trident = (ThrownTrident) (Object) this;

        if (input.contains("IsCharged")) {
            trident.getEntityData().set(SynchedEntityDataSD.DATA_ID_CHARGED_TRIDENT, input.getBooleanOr("IsCharged", false));
        }
    }

    @Inject(method = "onHitEntity", at = @At("TAIL"))
    private void chargedStrikeOnEntity(EntityHitResult hitResult, CallbackInfo ci) {
        handleStrike(hitResult.getEntity().blockPosition());
    }

    @Inject(method = "hitBlockEnchantmentEffects", at = @At("TAIL"))
    private void chargedStrike(ServerLevel level, BlockHitResult hitResult, ItemStack weapon, CallbackInfo ci) {
        handleStrike(hitResult.getBlockPos());
    }

    private void handleStrike(BlockPos hitPos) {
        ThrownTrident trident = (ThrownTrident) (Object) this;

        if (subtlyd$isCharged() && !trident.level().isClientSide()) {
            LightningBolt bolt = EntityTypes.LIGHTNING_BOLT.create(trident.level(), EntitySpawnReason.TRIGGERED);

            if (bolt != null && bolt.level().canHaveWeather()) {
                bolt.moveOrInterpolateTo(Vec3.atBottomCenterOf(hitPos));
                bolt.setCause(trident.getOwner() instanceof ServerPlayer player ? player : null);
                trident.level().addFreshEntity(bolt);
                trident.level().playSound(null, hitPos, SoundEvents.TRIDENT_THUNDER.value(), SoundSource.PLAYERS);
            }
            subtlyd$setCharged(false);
        }
    }
}
