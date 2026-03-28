package net.meander.subtlyd.util;

import net.fabricmc.api.ModInitializer;
import net.meander.subtlyd.data.loot_table.LootSD;
import net.meander.subtlyd.network.PacketNetworking;
import net.meander.subtlyd.network.syncher.SynchedEntityDataSD;
import net.meander.subtlyd.world.WorldEventsSD;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.meander.subtlyd.world.item.alchemy.PotionsSD;
import net.meander.subtlyd.world.level.GameRulesSD;
import net.meander.subtlyd.world.level.levelgen.BiomesSD;

public class InitializerSD implements ModInitializer {
    @Override public void onInitialize() {
        Util.LOGGER.info("Initializing Subtly Dungeons");
        GameRulesSD.registration();
        SynchedEntityDataSD.createEntityData();
        PotionsSD.registration();
        BlocksSD.registration();
        ItemsSD.registration();
        WorldEventsSD.registration();
        BiomesSD.init();
        LootSD.registration();
        PacketNetworking.registerCommon();
    }
}