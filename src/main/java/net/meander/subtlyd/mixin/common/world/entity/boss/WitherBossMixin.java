package net.meander.subtlyd.mixin.common.world.entity.boss;

import net.meander.subtlyd.network.syncher.SynchedEntityDataSD;
import net.meander.subtlyd.sounds.SoundEventsSD;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherBoss.class)
public class WitherBossMixin {
    private boolean shouldDoDiveBomb = true;

    /**
     * Determines that the maximum amount of health a wither can have naturally, is 600 health points.
     * @param health The orignal amount of health.
     * @return 600 health points
     */
    @ModifyConstant(method = "createAttributes", constant = @Constant(doubleValue = 300.0))
    private static double updatedHealth(double health) {
        return 600.0;
    }

    /**
     * Adds new Wither Boss behavior.
     */
    @Inject(method = "customServerAiStep", at = @At("TAIL"))
    private void alteredBossBehavior(ServerLevel level, CallbackInfo ci) {
        WitherBoss wither = (WitherBoss) (Object) this;
        Difficulty difficulty = wither.level().getDifficulty();

        if (wither.getInvulnerableTicks() <= 0) {
            if (wither.isPowered()) {
                shouldDoDiveBomb = wither.getEntityData().get(SynchedEntityDataSD.DATA_ID_WITHER_DIVE);
                if (difficulty != Difficulty.EASY && shouldDoDiveBomb) {
                    Vec3 movement = wither.getDeltaMovement();

                    wither.setDeltaMovement(new Vec3(movement.x(), -1.0, movement.z()));
                    if (!wither.getBlockStateOn().is(BlockTags.REPLACEABLE)) {
                        wither.playSound(SoundEventsSD.WITHER_SKELETONS_SUMMONED);
                        level.explode(wither, wither.getX(), wither.getY(), wither.getZ(), 3.0F, Level.ExplosionInteraction.MOB);

                        for (int i = 0; i < 3; i++) {
                            EntityType.WITHER_SKELETON.spawn(level, wither.blockPosition(), EntitySpawnReason.MOB_SUMMONED);
                        }
                        shouldDoDiveBomb = false;
                    }
                }
            } else {
                if (!shouldDoDiveBomb) {
                    shouldDoDiveBomb = true;
                }
            }
            wither.getEntityData().set(SynchedEntityDataSD.DATA_ID_WITHER_DIVE, shouldDoDiveBomb);
        }
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineSynchedData(SynchedEntityData.Builder entityData, CallbackInfo ci) {
        entityData.define(SynchedEntityDataSD.DATA_ID_WITHER_DIVE, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(ValueOutput output, CallbackInfo ci) {
        WitherBoss wither = (WitherBoss) (Object) this;
        output.putBoolean("ShouldDiveBomb", wither.getEntityData().get(SynchedEntityDataSD.DATA_ID_WITHER_DIVE));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAddtionalSaveSada(ValueInput input, CallbackInfo ci) {
        WitherBoss wither = (WitherBoss) (Object) this;

        if (input.contains("ShouldDiveBomb")) {
            wither.getEntityData().set(SynchedEntityDataSD.DATA_ID_WITHER_DIVE, input.getBooleanOr("ShouldDiveBomb", false));
        }
    }
}
