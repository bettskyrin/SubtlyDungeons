package com.kr1s1s.subtlyd;

import com.kr1s1s.subtlyd.client.util.GroundShake;
import com.kr1s1s.subtlyd.world.item.ItemsSD;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.monster.Zombie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubtlyDungeons implements ModInitializer {
	public static final String MOD_ID = "subtlyd";
	public static final Logger LOGGER = LoggerFactory.getLogger("Subtly Dungeons");

    public static void debug (String s) {
        LOGGER.info("Debug: {}", s);
    }

    public static final EntityDataAccessor<Boolean> DATA_LEADER_ID = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.BOOLEAN);

    @Override
	public void onInitialize() {
        LOGGER.info("Initializing Subtly Dungeons");
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.level != null) {
                GroundShake.tick();
            }
        });

        ItemsSD.registerItems();
    }
}