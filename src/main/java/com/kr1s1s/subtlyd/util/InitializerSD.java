package com.kr1s1s.subtlyd.util;

import com.kr1s1s.subtlyd.network.syncher.SynchedEntityDataSD;
import com.kr1s1s.subtlyd.world.block.BlocksSD;
import com.kr1s1s.subtlyd.world.item.ItemsSD;
import com.kr1s1s.subtlyd.world.level.GameRulesSD;
import com.kr1s1s.subtlyd.world.level.levelgen.BiomesSD;
import net.fabricmc.api.ModInitializer;

public class InitializerSD implements ModInitializer {
    @Override public void onInitialize() {
        Util.LOGGER.info("Initializing Subtly Dungeons");
        GameRulesSD.registration();
        SynchedEntityDataSD.createEntityData();
        BlocksSD.registration();
        ItemsSD.registration();
        GameplayEventsSD.registration();
        BiomesSD.init();
    }
}