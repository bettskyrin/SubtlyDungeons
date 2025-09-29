package com.kr1s1s.subtlyd;

import net.fabricmc.api.ModInitializer;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.monster.Zombie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubtlyDungeons implements ModInitializer {
	public static final String MOD_ID = "subtly-dungeons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final EntityDataAccessor<Boolean> DATA_LEADER_ID = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.BOOLEAN);

    public static void debug (String s) {
        LOGGER.info("Debug: " + s);
    }


    @Override
	public void onInitialize() {
        LOGGER.info("Initializing Subtly Dungeons");
	}
}