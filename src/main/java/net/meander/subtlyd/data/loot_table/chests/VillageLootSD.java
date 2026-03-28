package net.meander.subtlyd.data.loot_table.chests;

import net.meander.subtlyd.world.item.ItemsSD;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class VillageLootSD {
    public static void register() {
        LootTableEvents.MODIFY.register((resourceKey, tableBuilder, _, _) -> {
            if (BuiltInLootTables.VILLAGE_PLAINS_HOUSE.equals(resourceKey)) {
                LootPool.Builder chestPool = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ItemsSD.APPLE_PIE).setWeight(97))
                        .add(LootItem.lootTableItem(Items.AIR).setWeight(903)); // Simulate Pumpkin Pie's 9.7% chest loot_table weight
                tableBuilder.pool(chestPool.build());
            } else if (resourceKey.equals(BuiltInLootTables.VILLAGE_FISHER)) {
                tableBuilder.modifyPools(poolBuilder -> {
                    poolBuilder.add(LootItem.lootTableItem(ItemsSD.CALAMARI).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))).build(); // Add Calamari
                });
            }
        });
    }
}
