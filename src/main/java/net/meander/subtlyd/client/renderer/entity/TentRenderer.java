package net.meander.subtlyd.client.renderer.entity;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.meander.subtlyd.client.model.object.tent.TentModel;
import net.meander.subtlyd.client.renderer.entity.state.TentRenderState;
import net.meander.subtlyd.util.UtilSD;
import net.meander.subtlyd.world.entity.decoration.Tent;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.item.DyeColor;

import java.util.EnumMap;
import java.util.List;

public class TentRenderer extends EntityRenderer<Tent, TentRenderState> implements RenderLayerParent<TentRenderState, TentModel> {
    private static final EnumMap<DyeColor, Identifier> TEXTURES_BY_COLOR = Util.make(new EnumMap<>(DyeColor.class), textures -> {
        for (DyeColor color : DyeColor.values()) {
            textures.put(color, UtilSD.identifier("textures/entity/tent/" + color.getName() + "_tent.png"));
        }
    });
    private final TentModel model;
    protected final List<RenderLayer<TentRenderState, TentModel>> layers = Lists.newArrayList();

    public TentRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelLayerLocation) {
        super(context);

        model = new TentModel(context.bakeLayer(modelLayerLocation));
        shadowRadius = 1.8F;
    }

    @Override public TentModel getModel() {
        return model;
    }

    @Override public TentRenderState createRenderState() {
        return new TentRenderState();
    }

    /**
     * Assists in tent rendering. Allows for the tent model to shake when damaged.
     * @param tent The rendering tent entity
     */
    @Override
    public void extractRenderState(Tent tent, TentRenderState state, float partialTicks) {
        super.extractRenderState(tent, state, partialTicks);

        state.scale = 1.0F;
        state.yRot = Mth.rotLerp(partialTicks, tent.yRotO, tent.getYRot());
        state.xRot = state.getXRot(partialTicks);

        state.hurtTime = (float) tent.getHurtTime() - partialTicks;
        state.damage = tent.getDamage() - partialTicks;
        state.hurtDir = tent.getHurtDir();
        state.texture = TEXTURES_BY_COLOR.get(tent.getColor());
    }

    @Override
    public void submit(TentRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        float scale = state.scale;

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(270.0F - state.yRot));

        if (state.hurtTime > 0) {
            poseStack.mulPose(Axis.XP.rotationDegrees(((Mth.sin(state.hurtTime) * state.hurtTime * Math.max(0F, state.damage)) / 10F) * (float) state.hurtDir));
        }

        poseStack.scale(scale, scale, scale);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.501F, 0.0F);
        submitNodeCollector.submitModel(model, state, poseStack, model.renderType(state.texture), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);

        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
