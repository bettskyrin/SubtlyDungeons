package com.kr1s1s.subtlyd.client;

import com.kr1s1s.subtlyd.client.util.CameraShake;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ClientTickEventsSD {
    public static void registration() {
        ClientTickEvents.START_WORLD_TICK.register(client -> {
            CameraShake.tick();
        });
    }
}
