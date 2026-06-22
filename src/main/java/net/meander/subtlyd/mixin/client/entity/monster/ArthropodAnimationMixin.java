package net.meander.subtlyd.mixin.client.entity.monster;

import net.meander.subtlyd.client.entity.monster.ClimberAccessor;
import net.meander.subtlyd.world.entity.EntitySD;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Implements your ClimberAccessor Duck Interface onto the target bugs, 
 * calculating a smooth X-axis pitch rotation when they scale walls.
 */
@Mixin({Silverfish.class, Endermite.class})
public class ArthropodAnimationMixin implements ClimberAccessor {
    @Unique private static final float ANIM_RATE = 0.2F;
    private float progOld;
    private float progNew;
    private float rotOld;
    private float rotNew;
    private double yOld;

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickClimbingAnimation(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        boolean touchingWall = EntitySD.getNearestWall(livingEntity) != null;
        boolean isMovingUp = livingEntity.getY() > yOld;
        boolean isClimbing = touchingWall && isMovingUp;
        float targetProg = isClimbing ? 1.0F : 0.0F;
        progOld = progNew;
        progNew = Mth.approach(progNew, targetProg, ANIM_RATE);

        if (isClimbing) {
            tickRotation(livingEntity.yBodyRot);

            if (livingEntity.tickCount % 8 == 0) {
                livingEntity.playStepSound(livingEntity.blockPosition(), livingEntity.level().getBlockState(livingEntity.blockPosition().offset(livingEntity.getDirection().getUnitVec3i())));
            }
        }

        yOld = livingEntity.getY();
    }

    @Override
    public float getClimbTransition(float partialTicks) {
        return Mth.lerp(partialTicks, progOld, progNew);
    }

    @Override
    public float getRotation(float partialTicks) {
        return Mth.rotLerp(partialTicks, rotOld, rotNew);
    }

    @Override
    public void tickRotation(float targetRot) {
        rotOld = rotNew;
        rotNew = Mth.approachDegrees(rotOld, targetRot, 25.0F);
    }
}