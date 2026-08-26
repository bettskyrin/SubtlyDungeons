package net.meander.subtlyd.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.meander.subtlyd.client.model.object.equipment.QuiverModel;
import net.meander.subtlyd.client.renderer.state.QuiverRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.object.armorstand.ArmorStandModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.HumanoidArm;

public class QuiverLayer<S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends RenderLayer<S, M> {
    public QuiverModel quiverModel;
    private static final float X_ROT = 45.0F;
    private static final float Y_OFFSET = 0.25F;
    private static final float Z_OFFSET = -0.315F;

    public QuiverLayer(RenderLayerParent<S, M> renderer, QuiverModel quiverModel) {
        super(renderer);

        this.quiverModel = quiverModel;
    }


    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
        if (((QuiverRenderState) state).hasQuiver()) {
            EntityModel<?> parentModel = getParentModel();
            QuiverPlacement quiverSide = getQuiverPlacement(state, parentModel);

            if (quiverSide != null) {
                poseStack.pushPose();
                parentModel.root().translateAndRotate(poseStack);
                quiverSide.attachedLeg().translateAndRotate(poseStack);
                poseStack.translate(quiverSide.xOffset(), Y_OFFSET, Z_OFFSET);
                poseStack.rotateDegrees(Axis.XP, X_ROT);
                submitNodeCollector.order(1).submitModel(
                        quiverModel,
                        (HumanoidRenderState) state,
                        poseStack,
                        RenderTypes.armorCutoutNoCull(((QuiverRenderState) state).getQuiverTexture()),
                        lightCoords,
                        OverlayTexture.NO_OVERLAY,
                        -1,
                        null,
                        state.outlineColor
                );
                poseStack.popPose();
            }
        }
    }

    private static <S extends LivingEntityRenderState> QuiverPlacement getQuiverPlacement(S state, EntityModel<?> parentModel) {
        float xOffset;
        ModelPart legPart;

        if (state instanceof AvatarRenderState avatarState && parentModel instanceof PlayerModel playerModel) {
            boolean isRightHanded = avatarState.mainArm == HumanoidArm.RIGHT;
            legPart = isRightHanded ? playerModel.leftLeg : playerModel.rightLeg;
            xOffset = isRightHanded ? 0.40F : -0.15F;
        } else if (parentModel instanceof ArmorStandModel armorStandModel) {
            xOffset = 0.60F;
            legPart = armorStandModel.rightLeg;
        } else {
            return null;
        }

        return new QuiverPlacement(legPart, xOffset);
    }

    private record QuiverPlacement(ModelPart attachedLeg, float xOffset) {
    }
}