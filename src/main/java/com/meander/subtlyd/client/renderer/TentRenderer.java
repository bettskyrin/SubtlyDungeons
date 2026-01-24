package com.meander.subtlyd.client.renderer;

import com.google.common.collect.Lists;
import com.meander.subtlyd.client.renderer.state.TentRenderState;
import com.meander.subtlyd.client.model.TentModel;
import com.meander.subtlyd.world.entity.TentEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TentRenderer extends EntityRenderer<TentEntity, TentRenderState> implements RenderLayerParent<TentRenderState, TentModel> {
    private final TentModel model;
    private final Identifier texture;
    protected final List<RenderLayer<TentRenderState, TentModel>> layers = Lists.newArrayList();

    public TentRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelLayerLocation) {
        super(context);
        this.model = new TentModel(context.bakeLayer(modelLayerLocation));
        this.texture = modelLayerLocation.model().withPath(color -> "textures/entity/" + color + ".png");
        this.shadowRadius = 1.8F;
    }

    @Override public @NotNull TentModel getModel() {
        return this.model;
    }

    @Override public @NotNull TentRenderState createRenderState() {
        return new TentRenderState();
    }

    /**
     * Assists in tent rendering. Allows for the tent model to shake when damaged.
     * @param tent The rendering tent entity
     */
    public void extractRenderState(TentEntity tent, TentRenderState renderState, float partialTicks) {
        super.extractRenderState(tent, renderState, partialTicks);
        renderState.scale = 1.0F;
        renderState.yRot = Mth.lerp(partialTicks, tent.yRotO, tent.getYRot());
        renderState.xRot = renderState.getXRot(partialTicks);

        renderState.hurtTime = (float) tent.getHurtTime() - partialTicks;
        renderState.damage = tent.getDamage() - partialTicks;
        renderState.hurtDir = tent.getHurtDir();
    }

    @Nullable protected RenderType getRenderType(boolean bl3) {
        Identifier resourceLocation = this.texture;
        return bl3 ? RenderTypes.outline(resourceLocation) : this.model.renderType(resourceLocation);
    }

    public void submit(TentRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(270.0F - renderState.yRot));

        float damage = renderState.damage;
        if (damage < 0) {
            damage = 0;
        }

        if (renderState.hurtTime > 0F) {
            poseStack.mulPose(Axis.XP.rotationDegrees((float) (((Math.sin(renderState.hurtTime) * renderState.hurtTime * damage) / 10F) * (float) renderState.hurtDir)));
        }

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
