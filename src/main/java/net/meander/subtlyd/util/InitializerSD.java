package net.meander.subtlyd.util;

import net.fabricmc.api.ModInitializer;
import net.meander.subtlyd.camera.CameraShakeEvents;
import net.meander.subtlyd.core.component.DataComponentsSD;
import net.meander.subtlyd.data.loot_table.LootSD;
import net.meander.subtlyd.network.PacketNetworking;
import net.meander.subtlyd.network.syncher.SynchedEntityDataSD;
import net.meander.subtlyd.world.GameEventsSD;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.meander.subtlyd.world.item.alchemy.PotionsSD;
import net.meander.subtlyd.world.level.GameRulesSD;
import net.meander.subtlyd.world.level.levelgen.BiomesSD;

public class InitializerSD implements ModInitializer {
    @Override public void onInitialize() {
        Util.LOGGER.info("Initializing Subtly Dungeons");
        GameRulesSD.registration();
        DataComponentsSD.registration();
        SynchedEntityDataSD.createEntityData();
        PacketNetworking.registerCommon();

        Util.LOGGER.info("Registering items and blocks");
        PotionsSD.registration();
        BlocksSD.registration();
        ItemsSD.registration();

        Util.LOGGER.info("Registering world events");
        GameEventsSD.registration();
        CameraShakeEvents.registration();
        BiomesSD.init();
        LootSD.registration();
    }
}