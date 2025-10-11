package com.kr1s1s.subtlyd.client.renderer;

import com.google.common.collect.Lists;
import com.kr1s1s.subtlyd.client.renderer.state.TentRenderState;
import com.kr1s1s.subtlyd.client.model.TentModel;
import com.kr1s1s.subtlyd.world.entity.TentEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TentRenderer extends EntityRenderer<TentEntity, TentRenderState> implements RenderLayerParent<TentRenderState, TentModel> {
    private final TentModel model;
    private final ResourceLocation texture;
    protected final List<RenderLayer<TentRenderState, TentModel>> layers = Lists.newArrayList();

    public TentRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelLayerLocation) {
        super(context);
        this.model = new TentModel(context.bakeLayer(modelLayerLocation));
        this.texture = modelLayerLocation.model().withPath(string -> "textures/entity/" + string + ".png");
        this.shadowRadius = 1.8F;
    }

    @Override
    public @NotNull TentModel getModel() {
        return this.model;
    }

    @Override
    public @NotNull TentRenderState createRenderState() {
        return new TentRenderState();
    }

    public void extractRenderState(TentEntity tent, TentRenderState renderState, float partialTicks) {
        super.extractRenderState(tent, renderState, partialTicks);
        renderState.scale = 1.0f;
        renderState.yRot = Mth.lerp(partialTicks, tent.yRotO, tent.getYRot());
        renderState.xRot = renderState.getXRot(partialTicks);
    }

    @Nullable
    protected RenderType getRenderType(boolean bl3) {
        ResourceLocation resourceLocation = this.texture;
        return bl3 ? RenderType.outline(resourceLocation) : this.model.renderType(resourceLocation);
    }

    public void submit(TentRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(270.0F - renderState.yRot));

        float g = renderState.scale;
        poseStack.scale(g, g, g);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.501F, 0.0F);
        RenderType renderType = this.getRenderType(renderState.appearsGlowing());
        if (renderType != null) {
            int i = getOverlayCoords(renderState, this.getWhiteOverlayProgress());
            int j =  -1;
            int k = ARGB.multiply(j, this.getModelTint());
            submitNodeCollector.submitModel(
                    this.model, renderState, poseStack, renderType, renderState.lightCoords, i, k, null, renderState.outlineColor, null
            );
        }

        if (this.shouldRenderLayers() && !this.layers.isEmpty()) {
            this.model.setupAnim(renderState);

            for (RenderLayer<TentRenderState, TentModel> renderLayer : this.layers) {
                renderLayer.submit(
                        poseStack, submitNodeCollector, renderState.lightCoords, renderState, renderState.yRot, renderState.xRot
                );
            }
        }

        poseStack.popPose();
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);
    }

    protected float getWhiteOverlayProgress() {
        return 0.0F;
    }

    protected int getModelTint() {
        return -1;
    }

    public static int getOverlayCoords(TentRenderState renderState, float f) {
        return OverlayTexture.pack(OverlayTexture.u(f), OverlayTexture.v(renderState.hasRedOverlay));
    }

    protected boolean shouldRenderLayers() {
        return true;
    }
}
