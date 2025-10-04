package com.kr1s1s.subtlyd;

import com.kr1s1s.subtlyd.mobs.zombie.DrownedRendererSD;
import com.kr1s1s.subtlyd.mobs.zombie.HuskRendererSD;
import com.kr1s1s.subtlyd.mobs.zombie.ZombieRendererSD;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;

public class SubtlyDungeonsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRenderers.register(EntityType.ZOMBIE, ZombieRendererSD::new);
        EntityRenderers.register(EntityType.HUSK, HuskRendererSD::new);
        EntityRenderers.register(EntityType.DROWNED, DrownedRendererSD::new);
    }
}
