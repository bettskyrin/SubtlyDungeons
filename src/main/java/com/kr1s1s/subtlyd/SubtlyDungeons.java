package com.kr1s1s.subtlyd;

import com.kr1s1s.subtlyd.data.BlockEvents;
import com.kr1s1s.subtlyd.data.loot_table.LootSD;
import com.kr1s1s.subtlyd.data.loot_table.gameplay.FishingLootSD;
import com.kr1s1s.subtlyd.world.block.BlocksSD;
import com.kr1s1s.subtlyd.world.item.ItemsSD;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubtlyDungeons implements ModInitializer {
	public static final String MOD_ID = "subtlyd";
	public static final Logger LOGGER = LoggerFactory.getLogger("Subtly Dungeons");

    @SuppressWarnings("unused")
    public static void debug (String s) {
        LOGGER.info("Debug: {}", s);
    }

    public static ResourceLocation resourceLocation(String string) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, string);
    }

    @Override
	public void onInitialize() {
        LOGGER.info("Initializing Subtly Dungeons");
        BlocksSD.init();
        BlockEvents.run();
        ItemsSD.init();
        LootSD.generate();
        FishingLootSD.generate();
    }
}