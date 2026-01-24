package com.meander.subtlyd.client;

import com.meander.subtlyd.util.ScreenShake;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ClientTickEventsSD {
    public static void registration() {
        ClientTickEvents.START_LEVEL_TICK.register(_ -> {
            ScreenShake.tick();
        });
    }
}
