package com.kr1s1s.subtlyd.world.level.block.entity;

import com.kr1s1s.subtlyd.world.block.BlocksSD;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;

public class FuelValuesSD {
    public static void init() {
        FuelRegistryEvents.BUILD.register(((builder, context) -> {
            builder.add(BlocksSD.CHARCOAL_BLOCK, 200 * 8 * 10);
        }));
    }
}
