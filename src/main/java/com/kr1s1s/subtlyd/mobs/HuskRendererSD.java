package com.kr1s1s.subtlyd.mobs;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;

public class HuskRendererSD extends HuskRenderer {
    private static final ResourceLocation ZOMBIE_LEADER_LOCATION = ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, "textures/entity/zombie/zombie_leader.png");
    private static final ResourceLocation DROWNED_LEADER_LOCATION = ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, "textures/entity/zombie/drowned_leader.png");
    private static final ResourceLocation HUSK_LEADER_LOCATION = ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, "textures/entity/zombie/husk_leader.png");

    public HuskRendererSD(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieRenderState zombieRenderState) {
        if (((ZombieRenderStateSD) zombieRenderState).isLeader) {
            return HUSK_LEADER_LOCATION;
        }
        return super.getTextureLocation(zombieRenderState);
    }

    @Override
    public ZombieRenderStateSD createRenderState() {
        return new ZombieRenderStateSD();
    }
}
