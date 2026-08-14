package net.meander.subtlyd.server.level;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

/**
 * @see net.minecraft.server.level.ServerLevel
 */
public class ServerLevelSD {
    public static void registerEvent(ServerLifecycleEvents.ServerStarting event) {
        ServerLifecycleEvents.SERVER_STARTING.register(event);
    }
}
