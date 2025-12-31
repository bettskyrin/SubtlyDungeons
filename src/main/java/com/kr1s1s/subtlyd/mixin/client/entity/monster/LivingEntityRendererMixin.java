package com.kr1s1s.subtlyd.mixin.client.entity.monster;

import com.kr1s1s.subtlyd.util.ClimberUtil;
import com.kr1s1s.subtlyd.client.entity.monster.SpiderAnimationAccessor;
import com.kr1s1s.subtlyd.client.renderer.state.SpiderRenderStateAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState> {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
            at = @At("TAIL"))
    public void determineRenderState(T entity, S state, float partialTicks, CallbackInfo ci) {
        if (state instanceof SpiderRenderStateAccessor stateAccessor) {
            if (entity instanceof SpiderAnimationAccessor spider) {
                extractClimberState(entity, spider, state, stateAccessor, partialTicks);
            } else if (entity.getVehicle() instanceof SpiderAnimationAccessor spider) {
                extractClimberJockeyState(entity, spider, state, stateAccessor, partialTicks);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void extractClimberJockeyState(T entity, SpiderAnimationAccessor spider, S state, SpiderRenderStateAccessor stateAccessor, float partialTicks) {
        float transition = spider.getClimbTransition(partialTicks);

        stateAccessor.setJockey(true);
        stateAccessor.setClimbProgress(transition);
        if (transition > 0) {
            float spiderRoll = spider.getRotation(partialTicks);
            Direction nearestWall = ClimberUtil.getNearestWall(entity.getVehicle());
            float yaw = nearestWall != null ? ClimberUtil.getClimberYaw(entity.getVehicle(), nearestWall) : 0.0F;

            stateAccessor.setClimbYaw(yaw);
            state.bodyRot = Mth.rotLerp(transition, state.bodyRot, spiderRoll);
            state.yRot = Mth.rotLerp(transition, state.bodyRot, 0.0F);
            state.xRot = Mth.rotLerp(transition, state.xRot, yaw);
        }
    }

    public void extractClimberState(T entity, SpiderAnimationAccessor spider, S state, SpiderRenderStateAccessor stateAccessor, float partialTicks) {
        float transition = spider.getClimbTransition(partialTicks);

        stateAccessor.setClimbProgress(transition);
        if (transition > 0) {
            Direction nearestWall = ClimberUtil.getNearestWall(entity);

            if (nearestWall != null) {
                float yaw = ClimberUtil.getClimberYaw(entity, nearestWall);
                float targetRot = spider.getRotation(partialTicks);

                spider.tickRotation(targetRot);
                stateAccessor.setClimbYaw(yaw);
                state.bodyRot = Mth.rotLerp(transition, state.bodyRot, targetRot);
                state.yRot = Mth.rotLerp(transition, state.bodyRot, 0.0F);
                state.xRot = Mth.rotLerp(transition, state.xRot, yaw);
            }
        }
    }

    @Inject(method = "setupRotations", at = @At("TAIL"))
    public void setupRotations (S state, PoseStack poseStack, float bodyRot, float entityScale, CallbackInfo ci) {
        setupClimberRotations(state, poseStack);
    }

    public void setupClimberRotations(S state, PoseStack poseStack) {
        if (state instanceof SpiderRenderStateAccessor accessor) {
            float progress = accessor.getClimbProgress();
            float yaw = accessor.getClimbYaw();

            if (progress > 0.0F) {
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F * progress));
                poseStack.mulPose(Axis.YP.rotationDegrees(-yaw * progress));
                if (!accessor.isJockey()) {
                    poseStack.translate(0, -0.6F * progress, -0.75F * progress);
                } else {
                    poseStack.translate(0, -1.5F * progress, -0.75F * progress);
                }
            }
        }
    }
}
