package com.kr1s1s.subtlyd.client.entity.render.layers;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.client.entity.render.model.TentModel;
import com.kr1s1s.subtlyd.world.entity.TentEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class TentRenderer extends LivingEntityRenderer<TentEntity, TentRenderState, TentModel> {
    private static final ResourceLocation WHITE_TENT_TEXTURE = ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, "textures/entity/white_tent.png");

    public TentRenderer(EntityRendererProvider.Context context) {
        super(context, new TentModel(context.bakeLayer(TentModel.LAYER_LOCATION)), 1.9F);
    }

    @Override
    public TentRenderState createRenderState() {
        return new TentRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(TentRenderState livingEntityRenderState) {
        return WHITE_TENT_TEXTURE;
    }

    public void extractRenderState(TentEntity tent, TentRenderState renderState, float partialTicks) {
        super.extractRenderState(tent, renderState, partialTicks);
    }
}
