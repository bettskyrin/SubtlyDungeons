package com.kr1s1s.subtlyd.mobs;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import net.minecraft.client.model.DrownedModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.DrownedOuterLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Drowned;

public class DrownedRendererSD extends AbstractZombieRenderer<Drowned, ZombieRenderStateSD, DrownedModelSD> {
    private static final ResourceLocation ZOMBIE_LEADER_LOCATION = ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, "textures/entity/zombie/zombie_leader.png");
    private static final ResourceLocation DROWNED_LEADER_LOCATION = ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, "textures/entity/zombie/drowned_leader.png");
    private static final ResourceLocation HUSK_LEADER_LOCATION = ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, "textures/entity/zombie/husk_leader.png");

    public DrownedRendererSD(EntityRendererProvider.Context context) {
        super(context, new DrownedModelSD(context.bakeLayer(ModelLayers.DROWNED)), new DrownedModelSD(context.bakeLayer(ModelLayers.DROWNED_BABY)), ArmorModelSet.bake(ModelLayers.DROWNED_ARMOR, context.getModelSet(), DrownedModelSD::new), ArmorModelSet.bake(ModelLayers.DROWNED_BABY_ARMOR, context.getModelSet(), DrownedModelSD::new));
        this.addLayer((new DrownedOuterLayerSD(this, context.getModelSet())));
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieRenderStateSD zombieRenderState) {
        if (((ZombieRenderStateSD) zombieRenderState).isLeader) {
            return DROWNED_LEADER_LOCATION;
        }
        return super.getTextureLocation(zombieRenderState);
    }

    @Override
    public ZombieRenderStateSD createRenderState() {
        return new ZombieRenderStateSD();
    }
}
