package com.kr1s1s.subtlyd.util;

import com.kr1s1s.subtlyd.client.ClientTickEventsSD;
import com.kr1s1s.subtlyd.client.renderer.EntityRenderersSD;
import com.kr1s1s.subtlyd.client.renderer.state.BlockRendering;
import com.kr1s1s.subtlyd.sounds.SoundEventsSD;
import com.kr1s1s.subtlyd.world.level.block.ColorProviderSD;
import net.fabricmc.api.ClientModInitializer;

public class ClientInitializerSD implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRenderersSD.registration();
        SoundEventsSD.registration();
        ClientTickEventsSD.registration();
        BlockRendering.init();
        ColorProviderSD.init();
    }
}
