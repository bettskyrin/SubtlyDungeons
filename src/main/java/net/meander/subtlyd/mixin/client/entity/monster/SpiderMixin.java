package net.meander.subtlyd.mixin.client.entity.monster;

import net.meander.subtlyd.client.entity.monster.ClimberAccessor;
import net.meander.subtlyd.util.Util;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.spider.Spider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Spider.class)
public abstract class SpiderMixin implements ClimberAccessor {
    private final LivingEntity livingEntity = ((LivingEntity) (Object) this);
    private float progOld; // Animation progress
    private float progNew;
    private float rotOld; // Rotation progress
    private float rotNew;
    private double yOld;
    
    @Shadow public abstract boolean isClimbing();

    /**
     * Animates the transition between crawling on the ground and wall. Also calls the walk animation and sound while climbing on walls.
     * The step sound's frequency is determined by determining if the original position value rounded up is greater than the current position value rounded down.
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void tickClimbingAnim(CallbackInfo ci) {
        final float ANIM_RATE = 0.2F;
        final float SPEED_MULTIPLIER = 8.0F;
        double ySpeed = Math.abs(livingEntity.getDeltaMovement().y());
        Direction nearestWall = Util.Logic.getNearestWall(livingEntity);
        float targetRot = nearestWall != null ? nearestWall.toYRot() : livingEntity.getYRot();
        progOld = progNew;

        if (isClimbing()) {
            progNew = Math.min(1.0F, progNew + ANIM_RATE);

            if (isChangingHeight()) {
                float animationSpeed = (float) ySpeed * SPEED_MULTIPLIER;
                RandomSource randomSource = RandomSource.create();

                livingEntity.walkAnimation.update(animationSpeed, 0.4F, 1.0F);
                if (randomSource.nextFloat() < 0.5F) {
                    livingEntity.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
                }
            }
        } else {
            progNew = Math.max(0.0F, progNew - ANIM_RATE);
        }
        yOld = livingEntity.getY();
        subtlyDungeons$tickRotation(targetRot);
    }

    public boolean isChangingHeight() {
        return livingEntity.getY() != yOld;
    }

    /**
     * Used for getting smoothed climber animations.
     * @param partialTicks The partial ticks.
     * @return Value from 0.0 to 1.0 Representing the animation's completion.
     */
    @Override
    public float subtlyDungeons$getClimbTransition(float partialTicks) {
        return Mth.lerp(partialTicks, progOld, progNew);
    }

    /**
     * Used for getting smoothed climber rotation animations.
     * @param partialTicks The partial ticks.
     * @return Value from 0.0 to 1.0 Representing the animation's completion.
     */
    @Override
    public float subtlyDungeons$getRotation(float partialTicks) {
        return Mth.rotLerp(partialTicks, rotOld, rotNew);
    }

    /**
     * Used to move the targeted climber angle based on the targeted angle.
     * @param targetRot The target rotation.
     */
    @Override
    public void subtlyDungeons$tickRotation(float targetRot) {
        rotOld = rotNew;
        rotNew = Mth.rotLerp(0.2F, rotOld, targetRot);
    }
}