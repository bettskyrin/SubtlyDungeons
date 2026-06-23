package net.meander.subtlyd.world.level.storage.loot.chests;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class VillageLootSD {
    public static void register() {
        LootTableEvents.MODIFY.register((resourceKey, tableBuilder, _, _) -> {
            if (BuiltInLootTables.VILLAGE_PLAINS_HOUSE.equals(resourceKey)) {
                tableBuilder.pool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(ItemsSD.APPLE_PIE).setWeight(97))
                        .add(EmptyLootItem.emptyItem().setWeight(903))  // Simulate Pumpkin Pie's 9.7% chest loot weight
                        .setRolls(ConstantValue.exactly(1)).build());
            } else if (resourceKey.equals(BuiltInLootTables.VILLAGE_FISHER)) {
                tableBuilder.modifyPools(poolBuilder -> poolBuilder
                        .add(LootItem.lootTableItem(ItemsSD.CALAMARI).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .build());
            } else if (resourceKey.equals(BuiltInLootTables.VILLAGE_WEAPONSMITH)) {
                tableBuilder.modifyPools(poolBuilder -> poolBuilder
                        .add(LootItem.lootTableItem(ItemsSD.IRON_DAGGER).setWeight(2)).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        .add(LootItem.lootTableItem(ItemsSD.COPPER_DAGGER).setWeight(3)).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                        .build();
            }
        });
    }
}
