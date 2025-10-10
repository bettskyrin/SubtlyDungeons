package com.kr1s1s.subtlyd.client.renderer;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.client.entity.render.layers.DrownedOuterLayerSD;
import com.kr1s1s.subtlyd.client.renderer.state.ZombieRenderStateSD;
import com.kr1s1s.subtlyd.client.entity.mosnter.ZombieSD;
import com.kr1s1s.subtlyd.client.model.mob.zombie.DrownedModelSD;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Drowned;
import org.jetbrains.annotations.NotNull;

public class DrownedRendererSD extends AbstractZombieRenderer<Drowned, ZombieRenderStateSD, DrownedModelSD> {
    private static final ResourceLocation DROWNED_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/zombie/drowned.png");
    private static final ResourceLocation DROWNED_LEADER_LOCATION = ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, "textures/entity/zombie/drowned_leader.png");

    public DrownedRendererSD(EntityRendererProvider.Context context) {
        super(context, new DrownedModelSD(context.bakeLayer(ModelLayers.DROWNED)), new DrownedModelSD(context.bakeLayer(ModelLayers.DROWNED_BABY)), ArmorModelSet.bake(ModelLayers.DROWNED_ARMOR, context.getModelSet(), DrownedModelSD::new), ArmorModelSet.bake(ModelLayers.DROWNED_BABY_ARMOR, context.getModelSet(), DrownedModelSD::new));
        this.addLayer(new DrownedOuterLayerSD(this, context.getModelSet()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(ZombieRenderStateSD zombieRenderState) {
        if (zombieRenderState.isLeader) {
            return DROWNED_LEADER_LOCATION;
        }
        return DROWNED_LOCATION;
    }

    @Override
    public @NotNull ZombieRenderStateSD createRenderState() {
        return new ZombieRenderStateSD();
    }

    @Override
    public void extractRenderState(Drowned drowned, ZombieRenderStateSD zombieRenderState, float f) {
        super.extractRenderState(drowned, zombieRenderState, f);
        zombieRenderState.isLeader = ZombieSD.isLeader(drowned);
    }
}
