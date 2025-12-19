package com.kr1s1s.subtlyd.client.entity.render.layers;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.client.renderer.state.ZombieRenderStateSD;
import com.kr1s1s.subtlyd.client.model.mob.zombie.DrownedModelSD;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.monster.zombie.DrownedModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;

// TODO
//public class DrownedOuterLayerSD extends RenderLayer<ZombieRenderStateSD, DrownedModelSD> {
//    private static final Identifier DROWNED_OUTER_LAYER_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/drowned_outer_layer.png");
//    private static final Identifier DROWNED_LEADER_OUTER_LAYER_LOCATION = SubtlyDungeons.resourceLocation("textures/entity/zombie/drowned_leader_outer_layer.png");
//    private final DrownedModel model;
//    private final DrownedModel babyModel;
//
//    public DrownedOuterLayerSD(RenderLayerParent<ZombieRenderStateSD, DrownedModelSD> renderLayerParent, EntityModelSet entityModelSet) {
//        super(renderLayerParent);
//        this.model = new DrownedModel(entityModelSet.bakeLayer(ModelLayers.DROWNED_OUTER_LAYER));
//        this.babyModel = new DrownedModel(entityModelSet.bakeLayer(ModelLayers.DROWNED_BABY_OUTER_LAYER));
//    }
//
//    @Override public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, ZombieRenderStateSD zombieRenderState, float f, float g) {
//        Identifier layerLocation = zombieRenderState.isLeader ? DROWNED_LEADER_OUTER_LAYER_LOCATION : DROWNED_OUTER_LAYER_LOCATION;
//        DrownedModel drownedModel = zombieRenderState.isBaby ? this.babyModel : this.model;
//        coloredCutoutModelCopyLayerRender(drownedModel, layerLocation, poseStack, submitNodeCollector, i, zombieRenderState, -1, 1);
//    }
//}
