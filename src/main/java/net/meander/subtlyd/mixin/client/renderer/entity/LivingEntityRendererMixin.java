package net.meander.subtlyd.mixin.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.meander.subtlyd.client.OptionsSD;
import net.meander.subtlyd.client.entity.ScansorialEntity;
import net.meander.subtlyd.client.renderer.entity.state.LivingEntityRenderStateSD;
import net.meander.subtlyd.world.entity.EntitySD;
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
    private static final float climbProgressThreshold = 0.0F; // Minimum amount of progress that must occur before the animation may begin

    /**
     * Determines the render state to extract.
     * @param entity The entity to test.
     * @param state The entity's render state.
     * @param partialTicks The partial ticks.
     */
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void determineRenderState(T entity, S state, float partialTicks, CallbackInfo ci) {
        if (OptionsSD.advancedEntityAnimations().get()) {
            if (state instanceof LivingEntityRenderStateSD stateSD) {
                if (entity instanceof ScansorialEntity scansorialEntity) {
                    extractScansorialEntityRenderState(entity, scansorialEntity, state, stateSD, partialTicks);
                } else if (entity.getVehicle() instanceof ScansorialEntity scansorialEntity) {
                    extractScansorialEntityJockeyState(entity.getVehicle(), scansorialEntity, state, stateSD, partialTicks);
                }
            }
        }
    }


    /**
     * Determines the rotations that should be set up.
     * @param state The render state to test.
     */
    @Inject(method = "setupRotations", at = @At("TAIL"))
    private  void determineRotations(S state, PoseStack poseStack, float bodyRot, float entityScale, CallbackInfo ci) {
        if (OptionsSD.advancedEntityAnimations().get()) {
            if (state instanceof LivingEntityRenderStateSD stateSD) {
                setupScansorialEntityRotations(stateSD, poseStack, state.boundingBoxHeight);
            }
        }
    }

    /**
     * Extracts the render state for climbing entities (e.g. spiders).
     * @param entity The climbing entity.
     * @param scansorialEntity Interface for obtaining scansorialEntity data.
     * @param state The render state.
     * @param partialTicks The partial ticks.
     */
    private void extractScansorialEntityRenderState(Entity entity, ScansorialEntity scansorialEntity, S state, LivingEntityRenderStateSD stateSD, float partialTicks) {
        float progress = scansorialEntity.getClimbAnimationProgress(partialTicks);
        Direction nearestWall = EntitySD.getNearestWallDirection(entity);

        stateSD.setClimbProgress(progress);

        if (progress > climbProgressThreshold && nearestWall != null) {
            float oldYaw = stateSD.getClimbYaw();
            float yaw = EntitySD.getScansorialEntityYaw(entity, nearestWall, oldYaw);

            state.bodyRot = Mth.rotLerp(progress, state.bodyRot, scansorialEntity.getRoll(partialTicks));
            state.yRot = Mth.rotLerp(progress, state.yRot, 0.0F);
            state.xRot = Mth.rotLerp(progress, state.xRot, yaw);

            stateSD.setClimbYaw(yaw);
            scansorialEntity.tickAndLerpRoll(scansorialEntity.getRoll(partialTicks));
        }
    }

    /**
     * Extracts the render state for jockeys.
     * @param entity The entity being ridden.
     * @param scansorialEntity Interface for obtaining scansorialEntity data.
     * @param state The render state.
     * @param stateSD Interface for obtaining render state data.
     * @param partialTicks The partial ticks.
     */
    private void extractScansorialEntityJockeyState(Entity entity, ScansorialEntity scansorialEntity, S state, LivingEntityRenderStateSD stateSD, float partialTicks) {
        float progress = scansorialEntity.getClimbAnimationProgress(partialTicks);
        Direction nearestWall = EntitySD.getNearestWallDirection(entity);

        stateSD.setIsJockey(true);
        stateSD.setClimbProgress(progress);

        if (progress > climbProgressThreshold && nearestWall != null) {
            float oldYaw = stateSD.getClimbYaw();
            state.bodyRot = Mth.rotLerp(progress, state.bodyRot, scansorialEntity.getRoll(partialTicks));
            state.xRot = Mth.rotLerp(progress, state.xRot, EntitySD.getScansorialEntityYaw(entity, nearestWall, oldYaw));
        }
    }

    /**
     * Sets up visual rotations and offsets for climbing entities.
     * @param stateSD Interface for arthropod render stateSD information.
     * @param poseStack The pose stack of the climbing entity.
     * @param boundingBoxHeight The bounding box height of the entity. Used for relative translations.
     */
    private void setupScansorialEntityRotations(LivingEntityRenderStateSD stateSD, PoseStack poseStack, float boundingBoxHeight) {
        float progress = stateSD.getClimbProgress();

        if (progress > climbProgressThreshold) {
            float yOffset;
            float zOffset;

            if (!stateSD.isJockey()) {
                yOffset = 0.6F * progress;
                zOffset = ((-1.06F * boundingBoxHeight + 0.2F) * 0.25F) * progress; // Linear function to prevent cave spiders from clipping into walls.
            } else {
                yOffset = 0;
                zOffset = -1.25F * progress;
            }

            poseStack.translate(0, yOffset, zOffset);
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F * progress));
            poseStack.mulPose(Axis.YP.rotationDegrees(stateSD.getClimbYaw() * progress)); // TODO Smooth & Downward crawling
        }
    }
}