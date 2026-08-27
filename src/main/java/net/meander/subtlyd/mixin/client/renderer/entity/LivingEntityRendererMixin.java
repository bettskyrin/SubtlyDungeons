package net.meander.subtlyd.mixin.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.meander.subtlyd.client.entity.ScansorialEntity;
import net.meander.subtlyd.client.model.geom.ModelLayersSD;
import net.meander.subtlyd.client.model.object.equipment.QuiverModel;
import net.meander.subtlyd.client.renderer.entity.state.LivingEntityRenderStateSD;
import net.meander.subtlyd.client.renderer.layer.QuiverLayer;
import net.meander.subtlyd.client.renderer.state.QuiverRenderState;
import net.meander.subtlyd.tags.ItemTagsSD;
import net.meander.subtlyd.util.UtilSD;
import net.meander.subtlyd.world.entity.EntitySD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
    private static final float climbProgressThreshold = 0.0F;

    @SuppressWarnings("unchecked")
    @Inject(method = "<init>", at = @At("RETURN"))
    private void addLayers(EntityRendererProvider.Context context, M model, float shadow, CallbackInfo ci) {
        LivingEntityRenderer<T, S, M> entityRenderer = (LivingEntityRenderer<T, S, M>) (Object) this;
        QuiverModel quiverModel = new QuiverModel(context.bakeLayer(ModelLayersSD.QUIVER));

        entityRenderer.addLayer(new QuiverLayer<>(entityRenderer, quiverModel));
    }

    /**
     * Determines the render state to extract.
     * @param entity The entity to test.
     * @param state The entity's render state.
     * @param partialTicks The partial ticks.
     */
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void determineRenderState(T entity, S state, float partialTicks, CallbackInfo ci) {
        if (Minecraft.getInstance().options.fancyEntities().get()) {
            if (state instanceof LivingEntityRenderStateSD stateSD) {
                if (entity instanceof ScansorialEntity scansorialEntity) {
                    extractScansorialEntityRenderState(entity, scansorialEntity, state, stateSD, partialTicks);
                } else if (entity.getVehicle() instanceof ScansorialEntity scansorialEntity) {
                    extractScansorialEntityJockeyState(entity.getVehicle(), scansorialEntity, state, stateSD, partialTicks);
                }
            }
        }

        if (state instanceof QuiverRenderState quiverRenderState) {
            extractQuiverRenderState(entity, quiverRenderState);
        }
    }


    /**
     * Determines the rotations that should be set up.
     * @param state The render state to test.
     */
    @Inject(method = "setupRotations", at = @At("TAIL"))
    private  void determineRotations(S state, PoseStack poseStack, float bodyRot, float entityScale, CallbackInfo ci) {
        if (Minecraft.getInstance().options.fancyEntities().get()) {
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

    private void extractQuiverRenderState(T entity, QuiverRenderState quiverRenderState) {
        ItemStack legsStack = entity.getItemBySlot(EquipmentSlot.LEGS);
        Item item = legsStack.getItem();

        if (legsStack.is(ItemTagsSD.QUIVERS)) {
            quiverRenderState.setHasQuiver(true);

            if (legsStack.is(ItemsSD.QUIVER)) {
                quiverRenderState.setQuiverTexture(UtilSD.identifier("textures/entity/equipment/quiver/quiver.png"));
            } else {
                Identifier itemId = BuiltInRegistries.ITEM.getKey(item);

                quiverRenderState.setQuiverTexture(UtilSD.identifier("textures/entity/equipment/quiver/" + itemId.getPath() + ".png"));
            }
        } else {
            quiverRenderState.setHasQuiver(false);
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
                yOffset = boundingBoxHeight * 0.5F * progress;
                zOffset = ((-1.06F * boundingBoxHeight + 0.2F) * 0.25F) * progress; // Linear function to prevent cave spiders from clipping into walls.
            } else {
                yOffset = progress;
                zOffset = -1.25F * progress;
            }

            poseStack.translate(0, yOffset, zOffset);
            poseStack.rotateDegrees(Axis.XP, 90.0F * progress);
            poseStack.rotateDegrees(Axis.YP, stateSD.getClimbYaw() * progress);
        }
    }
}