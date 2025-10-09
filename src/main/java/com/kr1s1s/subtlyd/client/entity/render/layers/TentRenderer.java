package com.kr1s1s.subtlyd.client.entity.render.layers;

import com.google.common.collect.Lists;
import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.client.entity.render.model.TentModel;
import com.kr1s1s.subtlyd.world.entity.TentEntitySD;
import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TentRenderer extends EntityRenderer<TentEntitySD, TentRenderState> implements RenderLayerParent<TentRenderState, TentModel> {
    protected TentModel model;
    protected DyeColor color;
    protected final List<RenderLayer<TentRenderState, TentModel>> layers = Lists.newArrayList();
    private static final ResourceLocation WHITE_TENT_TEXTURE = ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, "textures/entity/tent/white_tent.png");

    public TentRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new TentModel(context.bakeLayer(TentModel.LAYER_LOCATION));
        this.shadowRadius = 1.9F;
    }

    @Override
    public TentModel getModel() {
        return this.model;
    }


    public ResourceLocation getTextureLocation(TentRenderState renderState) {
        renderState.color = DyeColor.WHITE; // TODO
        return ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, "/entity/tent/" + renderState.color.toString() + "_tent.png");
    }

    @Nullable
    protected RenderType getRenderType(TentRenderState renderState, boolean bl, boolean bl2, boolean bl3) {
        ResourceLocation resourceLocation = this.getTextureLocation(renderState);
        if (bl2) {
            return RenderType.itemEntityTranslucentCull(resourceLocation);
        } else if (bl) {
            return this.model.renderType(resourceLocation);
        } else {
            return bl3 ? RenderType.outline(resourceLocation) : null;
        }
    }

    @Override
    public TentRenderState createRenderState() {
        TentRenderState renderState = new TentRenderState();
        renderState.color = DyeColor.WHITE;
        return renderState;
    }

    public void extractRenderState(TentEntitySD tent, TentRenderState renderState, float partialTicks) {
        super.extractRenderState(tent, renderState, partialTicks);
        renderState.color = tent.color;
        renderState.yRot = 0;
        renderState.xRot = renderState.getXRot(partialTicks);
    }

    public void submit(TentRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();

        float g = renderState.scale;
        poseStack.scale(g, g, g);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(renderState, poseStack);
        poseStack.translate(0.0F, -1.501F, 0.0F);
        RenderType renderType = this.getRenderType(renderState, false, false, renderState.appearsGlowing());
        if (renderType != null) {
            int i = getOverlayCoords(renderState, this.getWhiteOverlayProgress(renderState));
            int j =  -1;
            int k = ARGB.multiply(j, this.getModelTint(renderState));
            submitNodeCollector.submitModel(
                    this.model, renderState, poseStack, renderType, renderState.lightCoords, i, k, null, renderState.outlineColor, null
            );
        }

        if (this.shouldRenderLayers(renderState) && !this.layers.isEmpty()) {
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

    protected float getWhiteOverlayProgress(TentRenderState renderState) {
        return 0.0F;
    }

    protected int getModelTint(TentRenderState renderState) {
        return -1;
    }

    public static int getOverlayCoords(TentRenderState renderState, float f) {
        return OverlayTexture.pack(OverlayTexture.u(f), OverlayTexture.v(renderState.hasRedOverlay));
    }

    protected void scale(TentRenderState renderState, PoseStack poseStack) {
    }

    protected boolean shouldRenderLayers(TentRenderState tentRenderState) {
        return true;
    }
}
