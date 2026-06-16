package net.meander.subtlyd.client;

import net.meander.subtlyd.client.camera.shake.CameraShake;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ClientEventsSD {
    public static void registration() {
        registerTickEvents();
    }

    private static void registerTickEvents() {
        ClientTickEvents.START_LEVEL_TICK.register(_ -> {
            CameraShake.tick();
        });
    }
}
