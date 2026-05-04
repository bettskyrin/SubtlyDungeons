package net.meander.subtlyd.data.loot_table.chests;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.meander.subtlyd.world.level.storage.loot.functions.EnchantNonHumanoidArmorFunction;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class GenericChestLootSD {
    public static void register() {
        LootTableEvents.MODIFY.register((resourceKey, tableBuilder, _, _) -> {
            if (BuiltInLootTables.ANCIENT_CITY.equals(resourceKey) || BuiltInLootTables.NETHER_BRIDGE.equals(resourceKey)
            || BuiltInLootTables.STRONGHOLD_CORRIDOR.equals(resourceKey) || BuiltInLootTables.END_CITY_TREASURE.equals(resourceKey)) {
                tableBuilder.apply(EnchantNonHumanoidArmorFunction.builder());
            }
        });
    }
}
