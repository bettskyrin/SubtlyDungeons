package net.meander.subtlyd.world.level.storage.loot.chests;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.meander.subtlyd.world.item.ItemsSD;
import net.meander.subtlyd.world.level.storage.loot.functions.DaggerLootFunction;
import net.meander.subtlyd.world.level.storage.loot.functions.EnchantNonHumanoidArmorFunction;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class StructureChestLootSD {
    public static void register() {
        LootTableEvents.MODIFY.register((resourceKey, tableBuilder, _, _) -> {
            if (BuiltInLootTables.ANCIENT_CITY.equals(resourceKey)) {
                tableBuilder.apply(EnchantNonHumanoidArmorFunction.builder());
            } else if (BuiltInLootTables.NETHER_BRIDGE.equals(resourceKey)) {
                tableBuilder.apply(EnchantNonHumanoidArmorFunction.builder());
                tableBuilder.apply(DaggerLootFunction.builder());
            } else if (BuiltInLootTables.RUINED_PORTAL.equals(resourceKey)) {
                tableBuilder.apply(DaggerLootFunction.builder());
            } else if (BuiltInLootTables.STRONGHOLD_CORRIDOR.equals(resourceKey)) {
                tableBuilder.apply(EnchantNonHumanoidArmorFunction.builder());
                tableBuilder.apply(DaggerLootFunction.builder());
            } else if (BuiltInLootTables.END_CITY_TREASURE.equals(resourceKey)) {
                tableBuilder.apply(EnchantNonHumanoidArmorFunction.builder());
                tableBuilder.apply(DaggerLootFunction.builder());
            } else if (BuiltInLootTables.BASTION_BRIDGE.equals(resourceKey)) {
                tableBuilder.apply(DaggerLootFunction.builder());
            } else if (BuiltInLootTables.BASTION_OTHER.equals(resourceKey)) {
                tableBuilder.apply(DaggerLootFunction.builder());
            } else if (BuiltInLootTables.BASTION_TREASURE.equals(resourceKey)) {
                tableBuilder.apply(DaggerLootFunction.builder());
            } else if (BuiltInLootTables.BURIED_TREASURE.equals(resourceKey)) {
                tableBuilder.apply(DaggerLootFunction.builder());
            } else if (BuiltInLootTables.PILLAGER_OUTPOST.equals(resourceKey)) {
                LootPool.Builder chestPool = LootPool.lootPool()
                        .add(LootItem.lootTableItem(ItemsSD.IRON_DAGGER).setWeight(1))
                        .add(EmptyLootItem.emptyItem()
                                .setWeight(19)) // 5% Chance
                        .setRolls(ConstantValue.exactly(1));
                tableBuilder.pool(chestPool.build());
            }
        });
    }
}
