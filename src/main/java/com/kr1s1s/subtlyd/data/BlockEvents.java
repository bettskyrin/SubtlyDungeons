package com.kr1s1s.subtlyd.data;

import com.kr1s1s.subtlyd.world.entity.TentEntity;
import com.kr1s1s.subtlyd.world.level.block.UnlitCampfireFunction;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class BlockEvents {
    public static void run() {
        UseBlockCallback.EVENT.register(new UnlitCampfireFunction());
        TentEntity.allowTentSleep();
    }
}
