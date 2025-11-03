package com.kr1s1s.subtlyd;

import com.kr1s1s.subtlyd.data.GameplayEventsSD;
import com.kr1s1s.subtlyd.network.syncher.SynchedEntityDataSD;
import com.kr1s1s.subtlyd.world.block.BlocksSD;
import com.kr1s1s.subtlyd.world.item.ItemsSD;
import com.kr1s1s.subtlyd.world.level.levelgen.BiomesSD;
import com.kr1s1s.subtlyd.world.level.GameRulesSD;
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
        GameRulesSD.registration();
        SynchedEntityDataSD.createEntityData();
        BlocksSD.registration();
        ItemsSD.registration();
        GameplayEventsSD.registration();
        BiomesSD.init();
    }
}