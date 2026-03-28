package net.meander.subtlyd.client;

import net.meander.subtlyd.camera.CameraShake;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ClientTickEventsSD {
    public static void registration() {
        ClientTickEvents.START_LEVEL_TICK.register(_ -> {
            CameraShake.tick();
        });
    }
}
