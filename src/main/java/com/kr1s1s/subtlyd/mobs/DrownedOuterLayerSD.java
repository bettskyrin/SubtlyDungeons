package com.kr1s1s.subtlyd.mobs;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.DrownedModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class DrownedOuterLayerSD extends RenderLayer<ZombieRenderStateSD, DrownedModelSD> {
    private static final ResourceLocation DROWNED_OUTER_LAYER_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/zombie/drowned_outer_layer.png");
    private static final ResourceLocation DROWNED_LEADER_OUTER_LAYER_LOCATION = ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID,"textures/entity/zombie/drowned_leader_outer_layer.png");

    private final DrownedModel model;
    private final DrownedModel babyModel;

    public DrownedOuterLayerSD(RenderLayerParent<ZombieRenderStateSD, DrownedModelSD> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.model = new DrownedModel(entityModelSet.bakeLayer(ModelLayers.DROWNED_OUTER_LAYER));
        this.babyModel = new DrownedModel(entityModelSet.bakeLayer(ModelLayers.DROWNED_BABY_OUTER_LAYER));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, ZombieRenderStateSD zombieRenderState, float f, float g) {
        ResourceLocation layerLocation = zombieRenderState.isLeader ? DROWNED_LEADER_OUTER_LAYER_LOCATION : DROWNED_OUTER_LAYER_LOCATION;
        DrownedModel drownedModel = zombieRenderState.isBaby ? this.babyModel : this.model;
        coloredCutoutModelCopyLayerRender(drownedModel, layerLocation, poseStack, submitNodeCollector, i, zombieRenderState, -1, 1);
    }
}
