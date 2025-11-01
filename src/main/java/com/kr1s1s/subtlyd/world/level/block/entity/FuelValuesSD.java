package com.kr1s1s.subtlyd.world.level.block.entity;

import com.kr1s1s.subtlyd.data.tags.ItemTagsSD;
import com.kr1s1s.subtlyd.world.item.ItemsSD;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;

public class FuelValuesSD {
    public static void init() {
        FuelRegistryEvents.BUILD.register(((builder, context) -> {
            builder.add(ItemsSD.CHARCOAL_BLOCK, 200 * 8 * 10);
			builder.add(ItemTagsSD.TENTS, 200 * 3);
        }));
    }
}
