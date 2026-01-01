package com.kr1s1s.subtlyd.mixin.client.entity.monster;

import com.kr1s1s.subtlyd.client.entity.monster.ClimberAccessor;
import com.kr1s1s.subtlyd.client.renderer.state.LivingEntityRenderStateAccessor;
import com.kr1s1s.subtlyd.util.Util;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState> {

    /**
     * Determines the render state to extract.
     * @param entity The entity to test.
     * @param state The entity's render state.
     * @param partialTicks The partial ticks.
     */
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
            at = @At("TAIL"))
    private void determineRenderState(T entity, S state, float partialTicks, CallbackInfo ci) {
        if (state instanceof LivingEntityRenderStateAccessor stateAccessor) {

            if (entity instanceof ClimberAccessor climberAccessor) {
                extractClimberState(entity, climberAccessor, state, stateAccessor, partialTicks);
            } else if (entity.getVehicle() instanceof ClimberAccessor spider) {
                extractClimberJockeyState(entity.getVehicle(), spider, state, stateAccessor, partialTicks);
            }
        }
    }

    /**
     * Determines the rotations that should be set up.
     * @param state The render state to test.
     */
    @Inject(method = "setupRotations", at = @At("TAIL"))
    private  void determineRotations(S state, PoseStack poseStack, float bodyRot, float entityScale, CallbackInfo ci) {
        if (state instanceof LivingEntityRenderStateAccessor accessor) {
            setupClimberRotations(accessor, poseStack, entityScale);
        }
    }

    /**
     * Extracts the render state for climbing entities (e.g. spiders).
     * @param entity The climbing entity.
     * @param climberAccessor Interface for obtaining climber data.
     * @param state The render state.
     * @param partialTicks The partial ticks.
     */
    private void extractClimberState(Entity entity, ClimberAccessor climberAccessor, S state, LivingEntityRenderStateAccessor stateAccessor, float partialTicks) {
        float progress = climberAccessor.subtlyDungeons$getClimbTransition(partialTicks);

        stateAccessor.subtlyDungeons$setClimbProgress(progress);
        if (progress > 0) {
            Direction nearestWall = Util.Logic.getNearestWall(entity);

            if (nearestWall != null) {
                float yaw = Util.Logic.getClimberRotation(entity, nearestWall);

                state.bodyRot = Mth.rotLerp(progress, entity.getYRot(), climberAccessor.subtlyDungeons$getRotation(partialTicks));
                state.yRot = Mth.rotLerp(progress, state.yRot, 0.0F);
                state.xRot = Mth.rotLerp(progress, state.xRot, yaw);
                stateAccessor.subtlyDungeons$setClimbRotation(yaw);
                climberAccessor.subtlyDungeons$tickRotation(climberAccessor.subtlyDungeons$getRotation(partialTicks));
            }
        }
    }

    /**
     * Extracts the render state for jockeys.
     * @param entity The entity being ridden.
     * @param climberAccessor Interface for obtaining climber data.
     * @param state The render state.
     * @param stateAccessor Interface for obtaining render state data.
     * @param partialTicks The partial ticks.
     */
    private void extractClimberJockeyState(Entity entity, ClimberAccessor climberAccessor, S state, LivingEntityRenderStateAccessor stateAccessor, float partialTicks) {
        float progress = climberAccessor.subtlyDungeons$getClimbTransition(partialTicks);
        Direction nearestWall = Util.Logic.getNearestWall(entity);

        stateAccessor.subtlyDungeons$setJockey(true);
        stateAccessor.subtlyDungeons$setClimbProgress(progress);
        if (progress > 0 && nearestWall != null) {
            state.bodyRot = Mth.rotLerp(progress, state.bodyRot, climberAccessor.subtlyDungeons$getRotation(partialTicks));
            state.xRot = Mth.rotLerp(progress, state.xRot, Util.Logic.getClimberRotation(entity, nearestWall));
        }
    }

    /**
     * Sets up visual rotations and offsets for climbing entities.
     * @param accessor Interface for spider render state information.
     * @param poseStack The pose stack of the climbing entity.
     * @param entityScale The scale of the entity.
     */
    private void setupClimberRotations(LivingEntityRenderStateAccessor accessor, PoseStack poseStack, float entityScale) {
        float progress = accessor.subtlyDungeons$getClimbProgress();
        float entityZOffset = -0.5F * Mth.cube(entityScale) * progress;

        if (progress > 0.0F) {
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F * progress));
            poseStack.mulPose(Axis.YP.rotationDegrees(accessor.subtlyDungeons$getClimbRotation() * progress));
            if (!accessor.subtlyDungeons$isJockey()) {
                poseStack.translate(0, -0.6F * progress, entityZOffset);
            } else {
                poseStack.translate(0, -1.5F * progress, -0.75F * progress);
            }
        }
    }
}