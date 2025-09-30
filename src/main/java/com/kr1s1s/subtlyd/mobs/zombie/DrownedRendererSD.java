package com.kr1s1s.subtlyd.mobs.zombie;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Drowned;

public class DrownedRendererSD extends AbstractZombieRenderer<Drowned, ZombieRenderStateSD, DrownedModelSD> {
    private static final ResourceLocation DROWNED_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/zombie/drowned.png");
    private static final ResourceLocation DROWNED_LEADER_LOCATION = ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, "textures/entity/zombie/drowned_leader.png");
    private final DrownedOuterLayerSD renderLayer;

    public DrownedRendererSD(EntityRendererProvider.Context context) {
        super(context, new DrownedModelSD(context.bakeLayer(ModelLayers.DROWNED)), new DrownedModelSD(context.bakeLayer(ModelLayers.DROWNED_BABY)), ArmorModelSet.bake(ModelLayers.DROWNED_ARMOR, context.getModelSet(), DrownedModelSD::new), ArmorModelSet.bake(ModelLayers.DROWNED_BABY_ARMOR, context.getModelSet(), DrownedModelSD::new));
        renderLayer = new DrownedOuterLayerSD(this, context.getModelSet());
        this.addLayer(renderLayer);
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieRenderStateSD zombieRenderState) {
        if (zombieRenderState.isLeader) {
            return DROWNED_LEADER_LOCATION;
        }
        return DROWNED_LOCATION;
    }

    @Override
    public ZombieRenderStateSD createRenderState() {
        return new ZombieRenderStateSD();
    }

    @Override
    public void extractRenderState(Drowned drowned, ZombieRenderStateSD zombieRenderState, float f) {
        super.extractRenderState(drowned, zombieRenderState, f);
        zombieRenderState.isLeader = ZombieSD.isLeader(drowned);
    }
}
