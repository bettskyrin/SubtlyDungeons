package com.kr1s1s.subtlyd;

import com.kr1s1s.subtlyd.client.renderer.*;
import com.kr1s1s.subtlyd.client.util.GroundShake;
import com.kr1s1s.subtlyd.sounds.SoundEventsSD;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class SubtlyDungeonsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRenderersSD.runRegistration();
        SoundEventsSD.init();
        ClientTickEvents.START_WORLD_TICK.register(client -> {
            if (client != null) {
                GroundShake.tick();
            }
        });
    }
}
