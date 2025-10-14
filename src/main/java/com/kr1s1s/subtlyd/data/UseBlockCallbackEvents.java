package com.kr1s1s.subtlyd.data;

import com.kr1s1s.subtlyd.world.block.UnlitCampfireFunction;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class UseBlockCallbackEvents {
    public static void run() {
        UseBlockCallback.EVENT.register(new UnlitCampfireFunction());
    }
}
