package net.meander.subtlyd.mixin.client.entity;

import net.meander.subtlyd.client.OptionsSD;
import net.meander.subtlyd.client.entity.ScansorialEntity;
import net.meander.subtlyd.world.entity.EntitySD;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Spider.class, Silverfish.class, Endermite.class})
public class ScansorialEntityMixin implements ScansorialEntity {
    private static final float ANIM_RATE = 0.2F;
    private static final float SPEED_MULTIPLIER = 8.0F;
    private float progOld;
    private float progNew;
    private float rotOld;
    private float rotNew;

    private void playClimbSound(LivingEntity livingEntity) {
        if (livingEntity.tickCount % 8 == 0) {
            Vec3 vel = livingEntity.getDeltaMovement();

            if (isClimbingUpward() || (!(livingEntity instanceof Spider) && (livingEntity.onGround() && vel.length() > 0))) {
                BlockPos pos = livingEntity.blockPosition();

                livingEntity.playStepSound(pos, livingEntity.level().getBlockState(pos.offset(livingEntity.getDirection().getUnitVec3i())));
            }
        }
    }

    public boolean isClimbingUpward() {
        LivingEntity livingEntity = (LivingEntity) (Object) this;

        return livingEntity.getDeltaMovement().y() > 0;
    }

    private void tickClimbingAnimation() {
        if (OptionsSD.advancedEntityAnimations().get()) {
            LivingEntity livingEntity = (LivingEntity) (Object) this;

            Direction nearestWall = EntitySD.getNearestWallDirection(livingEntity);
            float targetRoll = nearestWall != null ? nearestWall.toYRot() : livingEntity.getYRot();
            progOld = progNew;

            if (isClimbingUpward() && nearestWall != null) {
                float ySpeed = Mth.abs((float) livingEntity.getDeltaMovement().y());
                float animationSpeed = ySpeed * SPEED_MULTIPLIER;
                progNew = Math.min(1.0F, progNew + ANIM_RATE);

                livingEntity.walkAnimation.update(animationSpeed, 0.4F, 1.0F);
                playClimbSound(livingEntity);
            } else {
                progNew = Math.max(0.0F, progNew - ANIM_RATE);
            }

            tickAndLerpRoll(targetRoll);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        tickClimbingAnimation();
    }

    @Override
    public float getClimbAnimationProgress(float partialTicks) {
        return Mth.lerp(partialTicks, progOld, progNew);
    }

    @Override
    public float getRoll(float partialTicks) {
        return Mth.rotLerp(partialTicks, rotOld, rotNew);
    }

    @Override
    public void tickAndLerpRoll(float targetRoll) {
        rotOld = rotNew;
        rotNew = Mth.rotLerp(0.2F, rotOld, targetRoll);
    }
}