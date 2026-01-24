package com.meander.subtlyd.util.data.loot_table.gameplay;

import com.meander.subtlyd.world.item.ItemsSD;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class VillageHeroLootSD {
    public static void register() {
        LootTableEvents.MODIFY.register((resourceKey, tableBuilder, lootTableSource, provider) -> {
            if (BuiltInLootTables.FARMER_GIFT.equals(resourceKey)) {
                tableBuilder.modifyPools(poolBuilder -> {
                    poolBuilder.setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(ItemsSD.APPLE_PIE));
                    poolBuilder.build();
                });
            } else if (resourceKey.equals(BuiltInLootTables.FISHERMAN_GIFT)) {
                tableBuilder.modifyPools(poolBuilder -> {
                    poolBuilder.add(LootItem.lootTableItem(ItemsSD.CALAMARI));
                    poolBuilder.build();
                });
            }
        });
    }
}
