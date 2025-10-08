package com.kr1s1s.subtlyd.mixin.entity;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.world.entity.TentEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.kr1s1s.subtlyd.world.entity.LivingEntitySD.findStandUpPosition;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    LivingEntity livingEntity = (LivingEntity) (Object) this;
    @Inject(method = "stopSleeping", at = @At("RETURN"))
    public void stopSleepingTent(CallbackInfo ci) {
        SubtlyDungeons.debug("Called"); // TODO
        livingEntity.getSleepingPos().filter(livingEntity.level()::hasChunkAt).ifPresent(blockPos -> {
            BlockState blockState = livingEntity.level().getBlockState(blockPos);
            Direction direction = Direction.NORTH; // tent.getDirection();
            TentEntity.occupied = false;
            Vec3 vec3x = findStandUpPosition(livingEntity.getType(), livingEntity.level(), blockPos, direction, livingEntity.getYRot()).orElseGet(() -> {
                BlockPos blockPos2 = blockPos.above();
                return new Vec3(blockPos2.getX() + 0.5, blockPos2.getY() + 0.1, blockPos2.getZ() + 0.5);
            });
            Vec3 vec32 = Vec3.atBottomCenterOf(blockPos).subtract(vec3x).normalize();
            float f = (float) Mth.wrapDegrees(Mth.atan2(vec32.z, vec32.x) * 180.0F / (float) Math.PI - 90.0);
            livingEntity.setPos(vec3x.x, vec3x.y, vec3x.z);
            livingEntity.setYRot(f);
            livingEntity.setXRot(0.0F);
        });
        Vec3 vec3 = livingEntity.position();
        livingEntity.setPose(Pose.STANDING);
        livingEntity.setPos(vec3.x, vec3.y, vec3.z);
        livingEntity.clearSleepingPos();
    }

    @Inject(method = "checkBedExists" , at = @At("HEAD"), cancellable = true)
    private void checkBedExists(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
