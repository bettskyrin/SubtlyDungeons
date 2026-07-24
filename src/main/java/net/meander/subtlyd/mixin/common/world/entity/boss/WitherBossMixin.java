package net.meander.subtlyd.mixin.common.world.entity.boss;

import net.meander.subtlyd.sounds.SoundEventsSD;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
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
    private static final EntityDataAccessor<Boolean> DATA_ID_WITHER_DIVE = SynchedEntityData.defineId(WitherBoss.class, EntityDataSerializers.BOOLEAN);

    @ModifyConstant(method = "createAttributes", constant = @Constant(doubleValue = 300.0))
    private static double setBaseHealth(double health) {
        return 600.0;
    }

    @Inject(method = "customServerAiStep", at = @At("TAIL"))
    private void modifyAiStep(ServerLevel level, CallbackInfo ci) {
        WitherBoss wither = (WitherBoss) (Object) this;

        if (wither.getInvulnerableTicks() <= 0) {
            boolean shouldDiveBomb = wither.getEntityData().get(DATA_ID_WITHER_DIVE);

            if (wither.isPowered()) {
                if (level.getDifficulty().getId() > 1 && shouldDiveBomb) {
                    Vec3 movement = wither.getDeltaMovement();

                    wither.setDeltaMovement(new Vec3(movement.x(), -1.0, movement.z()));

                    if (!wither.getBlockStateOn().is(BlockTags.REPLACEABLE)) {
                        wither.playSound(SoundEventsSD.WITHER_SKELETONS_SUMMONED);
                        level.explode(wither, wither.getX(), wither.getY(), wither.getZ(), 7.0F, Level.ExplosionInteraction.MOB);

                        for (int i = 0; i < 3; i++) {
                            EntityTypes.WITHER_SKELETON.spawn(level, wither.blockPosition(), EntitySpawnReason.MOB_SUMMONED);
                        }

                        wither.getEntityData().set(DATA_ID_WITHER_DIVE, false);
                    }
                }
            } else if (!shouldDiveBomb) {
                wither.getEntityData().set(DATA_ID_WITHER_DIVE, true);
            }
        }
    }

    /**
     * Sets the health gain rate for the wither at the start of the battle.
     * @return The amount of health to heal per second (?)
     */
    @ModifyConstant(method = "customServerAiStep", constant = @Constant(floatValue = 10.0F))
    private float modifyHealingRate(float health) {
        return ((WitherBoss) (Object) this).getMaxHealth() / 30.0F;
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineSynchedData(SynchedEntityData.Builder entityData, CallbackInfo ci) {
        entityData.define(DATA_ID_WITHER_DIVE, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(ValueOutput output, CallbackInfo ci) {
        WitherBoss wither = (WitherBoss) (Object) this;

        output.putBoolean("ShouldDiveBomb", wither.getEntityData().get(DATA_ID_WITHER_DIVE));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAddtionalSaveSada(ValueInput input, CallbackInfo ci) {
        WitherBoss wither = (WitherBoss) (Object) this;

        wither.getEntityData().set(DATA_ID_WITHER_DIVE, input.getBooleanOr("ShouldDiveBomb", false));
    }
}
