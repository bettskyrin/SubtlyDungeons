package com.kr1s1s.subtlyd.mixin.client.entity.monster;

import com.kr1s1s.subtlyd.util.ClimberUtil;
import com.kr1s1s.subtlyd.client.entity.monster.SpiderAnimationAccessor;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Spider.class)
public abstract class SpiderMixin implements SpiderAnimationAccessor {
    LivingEntity livingEntity = ((LivingEntity) (Object) this);

    private float climbProgress0;
    private float climbProgress1;
    private float climbRot0;
    private float climbRot1;
    @Shadow public abstract boolean isClimbing();

    /**
     * Animates the transition between crawling on the ground and wall. Also calls the walk animation and sound while climbing on walls.
     * The step sound's frequency is determined by determining if the original position value rounded up is greater than the current position value rounded down.
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void tickClimbingAnim(CallbackInfo ci) {
        Vec3 velocity = livingEntity.getDeltaMovement();
        float transitionRate = 0.2F;
        Direction nearestWall = ClimberUtil.getNearestWall(livingEntity);

        float pitch = nearestWall != null ? nearestWall.toYRot() : livingEntity.getYRot();
        this.climbProgress0 = this.climbProgress1;

        if (this.isClimbing()) {
            float speedMultiplier = 8.0F;
            float animationSpeed = (float) Math.min(velocity.length() * speedMultiplier, 1.0F);
            float legPosition0 = livingEntity.walkAnimation.position();

            this.climbProgress1 = Math.min(1.0F, this.climbProgress1 + transitionRate);
            livingEntity.walkAnimation.update(animationSpeed, 0.4F, 1.0F);

            if (Mth.floor(livingEntity.walkAnimation.position()) < Mth.ceil(legPosition0 + 0.1F)) {
                livingEntity.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
            }
        } else {
            this.climbProgress1 = Math.max(0.0F, this.climbProgress1 - transitionRate);
        }

        this.tickRotation(pitch);
    }

    /**
     * @see SpiderAnimationAccessor
     */
    @Override
    public float getClimbTransition(float partialTicks) {
        return Mth.lerp(partialTicks, this.climbProgress0, this.climbProgress1);
    }

    @Override
    public float getRotation(float partialTicks) {
        return Mth.rotLerp(partialTicks, this.climbRot0, this.climbRot1);
    }

    @Override
    public void tickRotation(float rotation) {
        this.climbRot0 = this.climbRot1;
        this.climbRot1 = Mth.rotLerp(0.2F, this.climbRot0, rotation);
    }
}
