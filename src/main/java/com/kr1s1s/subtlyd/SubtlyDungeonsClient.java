package com.kr1s1s.subtlyd;

import com.kr1s1s.subtlyd.mobs.zombie.DrownedRendererSD;
import com.kr1s1s.subtlyd.mobs.zombie.HuskRendererSD;
import com.kr1s1s.subtlyd.mobs.zombie.ZombieRendererSD;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.world.entity.EntityType;

public class SubtlyDungeonsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityType.ZOMBIE, ZombieRendererSD::new);
        EntityRendererRegistry.register(EntityType.HUSK, HuskRendererSD::new);
        EntityRendererRegistry.register(EntityType.DROWNED, DrownedRendererSD::new);
    }
}
