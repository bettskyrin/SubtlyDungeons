package com.kr1s1s.subtlyd.mobs;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;

public class ZombieRendererSD extends AbstractZombieRenderer<Zombie, ZombieRenderStateSD, ZombieModel<ZombieRenderStateSD>> {
    private static final ResourceLocation ZOMBIE_LEADER_LOCATION = ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, "textures/entity/zombie/zombie_leader.png");
    private static final ResourceLocation DROWNED_LEADER_LOCATION = ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, "textures/entity/zombie/drowned_leader.png");
    private static final ResourceLocation HUSK_LEADER_LOCATION = ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, "textures/entity/zombie/husk_leader.png");

    public ZombieRendererSD(EntityRendererProvider.Context context) {
        super(context,
                new ZombieModel<>(context.bakeLayer(ModelLayers.ZOMBIE)),
                new ZombieModel<>(context.bakeLayer(ModelLayers.ZOMBIE_BABY)),
                ArmorModelSet.bake(ModelLayers.ZOMBIE_ARMOR, context.getModelSet(), ZombieModel::new),
                ArmorModelSet.bake(ModelLayers.ZOMBIE_BABY_ARMOR, context.getModelSet(), ZombieModel::new)
        );
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieRenderStateSD zombieRenderState) {
        if (zombieRenderState.isLeader) {
            return ZOMBIE_LEADER_LOCATION;
        }
        return super.getTextureLocation(zombieRenderState);
    }

    @Override
    public ZombieRenderStateSD createRenderState() {
        return new ZombieRenderStateSD();
    }

    @Override
    public void extractRenderState(Zombie zombie, ZombieRenderStateSD zombieRenderState, float f) {
        super.extractRenderState(zombie, zombieRenderState, f);
        zombieRenderState.isLeader = ZombieSD.isLeader(zombie);
    }
}
