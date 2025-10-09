package com.kr1s1s.subtlyd;

import com.kr1s1s.subtlyd.client.entity.render.layers.TentRenderer;
import com.kr1s1s.subtlyd.client.entity.render.layers.zombie.DrownedRendererSD;
import com.kr1s1s.subtlyd.client.entity.render.layers.zombie.HuskRendererSD;
import com.kr1s1s.subtlyd.client.entity.render.layers.zombie.ZombieRendererSD;
import com.kr1s1s.subtlyd.client.entity.render.model.TentModel;
import com.kr1s1s.subtlyd.world.entity.EntitySD;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;

public class SubtlyDungeonsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerEntities();
        registerModels();
    }

    public void registerEntities() {
        EntityRenderers.register(EntityType.ZOMBIE, ZombieRendererSD::new);
        EntityRenderers.register(EntityType.HUSK, HuskRendererSD::new);
        EntityRenderers.register(EntityType.DROWNED, DrownedRendererSD::new);
        EntityRenderers.register(EntitySD.TENT, TentRenderer::new);
    }

    public void registerModels() {
        EntityModelLayerRegistry.registerModelLayer(TentModel.LAYER_LOCATION, TentModel::createBodyLayer);
    }
}
