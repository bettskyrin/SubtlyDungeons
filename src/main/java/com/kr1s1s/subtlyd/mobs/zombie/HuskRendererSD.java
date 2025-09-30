package com.kr1s1s.subtlyd.mobs.zombie;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Husk;

public class HuskRendererSD extends AbstractZombieRenderer<Husk, ZombieRenderStateSD, ZombieModel<ZombieRenderStateSD>> {
    private static final ResourceLocation HUSK_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/zombie/husk.png");
    private static final ResourceLocation HUSK_LEADER_LOCATION = ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, "textures/entity/zombie/husk_leader.png");

    public HuskRendererSD(EntityRendererProvider.Context context) {
        super(context, new ZombieModel<>(context.bakeLayer(ModelLayers.HUSK)), new ZombieModel<>(context.bakeLayer(ModelLayers.HUSK_BABY)), ArmorModelSet.bake(ModelLayers.HUSK_ARMOR, context.getModelSet(), ZombieModel::new), ArmorModelSet.bake(ModelLayers.HUSK_BABY_ARMOR, context.getModelSet(), ZombieModel::new));
    }


    @Override
    public ResourceLocation getTextureLocation(ZombieRenderStateSD zombieRenderState) {
        if (zombieRenderState.isLeader) {
            return HUSK_LEADER_LOCATION;
        }
         return HUSK_LOCATION;
    }

    @Override
    public ZombieRenderStateSD createRenderState() {
        return new ZombieRenderStateSD();
    }

    @Override
    public void extractRenderState(Husk husk, ZombieRenderStateSD zombieRenderState, float f) {
        super.extractRenderState(husk, zombieRenderState, f);
        zombieRenderState.isLeader = ZombieSD.isLeader(husk);
    }
}
