package net.meander.subtlyd.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.meander.subtlyd.client.model.object.equipment.QuiverModel;
import net.meander.subtlyd.client.renderer.state.QuiverRenderState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.HumanoidArm;

public class QuiverLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
    public QuiverModel quiverModel;
    private static final float X_ROT = 45.0F;
    private static final float Y_OFFSET = 0.25F;
    private static final float Z_OFFSET = -0.315F;

    public QuiverLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, QuiverModel quiverModel) {
        super(renderer);

        this.quiverModel = quiverModel;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
        if (((QuiverRenderState) state).hasQuiver()) {
            PlayerModel parentModel = getParentModel();

            boolean isRightHanded = state.mainArm == HumanoidArm.RIGHT;
            float xOffset = isRightHanded ? 0.40F : -0.15F;
            ModelPart legPart = isRightHanded ? parentModel.leftLeg : parentModel.rightLeg;

            poseStack.pushPose();
            parentModel.root().translateAndRotate(poseStack);
            legPart.translateAndRotate(poseStack);
            poseStack.translate(xOffset, Y_OFFSET, Z_OFFSET);
            poseStack.mulPose(Axis.XP.rotationDegrees(X_ROT));
            submitNodeCollector.order(1).submitModel(
                    quiverModel,
                    state,
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