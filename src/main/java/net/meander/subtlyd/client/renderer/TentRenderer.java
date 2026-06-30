package net.meander.subtlyd.client.renderer;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.meander.subtlyd.client.model.TentModel;
import net.meander.subtlyd.client.renderer.state.TentRenderState;
import net.meander.subtlyd.world.entity.TentEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
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
        model = new TentModel(context.bakeLayer(modelLayerLocation));
        texture = modelLayerLocation.model().withPath(tent -> "textures/entity/tent/" + tent + ".png");
        shadowRadius = 1.8F;
    }

    @Override public @NotNull TentModel getModel() {
        return model;
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
        renderState.yRot = Mth.rotLerp(partialTicks, tent.yRotO, tent.getYRot());
        renderState.xRot = renderState.getXRot(partialTicks);

        renderState.hurtTime = (float) tent.getHurtTime() - partialTicks;
        renderState.damage = tent.getDamage() - partialTicks;
        renderState.hurtDir = tent.getHurtDir();
    }

    @Nullable protected RenderType getRenderType(boolean bl3) {
        Identifier resourceLocation = texture;
        return bl3 ? RenderTypes.outline(resourceLocation) : model.renderType(resourceLocation);
    }

    public void submit(TentRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        float damage = renderState.damage;

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(270.0F - renderState.yRot));

        if (damage < 0) {
            damage = 0;
        }

        if (renderState.hurtTime > 0F) {
            poseStack.mulPose(Axis.XP.rotationDegrees(((Mth.sin(renderState.hurtTime) * renderState.hurtTime * damage) / 10F) * (float) renderState.hurtDir));
        }

        float scale = renderState.scale;
        poseStack.scale(scale, scale, scale);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.501F, 0.0F);
        RenderType renderType = getRenderType(renderState.appearsGlowing());
        if (renderType != null) {
            int overlayCoords = getOverlayCoords(renderState, getWhiteOverlayProgress());
            int j =  -1;
            int tintedColor = ARGB.multiply(j, getModelTint());
            submitNodeCollector.submitModel(model, renderState, poseStack, renderType, renderState.lightCoords, overlayCoords, tintedColor, null, renderState.outlineColor);
        }

        if (shouldRenderLayers() && !layers.isEmpty()) {
            model.setupAnim(renderState);

            for (RenderLayer<TentRenderState, TentModel> renderLayer : layers) {
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
