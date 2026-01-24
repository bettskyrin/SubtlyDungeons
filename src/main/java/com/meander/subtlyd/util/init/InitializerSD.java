package com.meander.subtlyd.util.init;

import com.meander.subtlyd.network.PacketNetworking;
import com.meander.subtlyd.network.syncher.SynchedEntityDataSD;
import com.meander.subtlyd.util.GameplayEventsSD;
import com.meander.subtlyd.util.Util;
import com.meander.subtlyd.util.data.loot_table.LootSD;
import com.meander.subtlyd.world.block.BlocksSD;
import com.meander.subtlyd.world.item.ItemsSD;
import com.meander.subtlyd.world.level.GameRulesSD;
import com.meander.subtlyd.world.level.levelgen.BiomesSD;
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
        LootSD.registration();
        PacketNetworking.registerCommon();
    }
}