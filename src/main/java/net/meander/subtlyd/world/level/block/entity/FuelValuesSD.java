package net.meander.subtlyd.world.level.block.entity;

import net.fabricmc.fabric.api.registry.FuelValueEvents;
import net.meander.subtlyd.tags.ItemTagsSD;
import net.meander.subtlyd.world.item.ItemsSD;

public class FuelValuesSD {
    public static void registerFuelValues() {
        FuelValueEvents.BUILD.register(((builder, _) -> {
            builder.add(ItemsSD.CHARCOAL_BLOCK, 200 * 8 * 10);
            builder.add(ItemTagsSD.TENTS, 200 * 3);
        }));
    }
}
