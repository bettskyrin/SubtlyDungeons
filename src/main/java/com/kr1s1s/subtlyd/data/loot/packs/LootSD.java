package com.kr1s1s.subtlyd.data.loot.packs;

import com.kr1s1s.subtlyd.world.item.ItemsSD;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class LootSD {
    public static void modify() {
        LootTableEvents.MODIFY.register((resourceKey, tableBuilder, lootTableSource, provider) -> {
            EntityPredicate onFirePredicate = EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true)).build();
            if (BuiltInLootTables.VILLAGE_PLAINS_HOUSE.equals(resourceKey)) {
                LootPool.Builder chestPool = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ItemsSD.APPLE_PIE).setWeight(97))
                        .add(LootItem.lootTableItem(Items.AIR).setWeight(903)); // Simulate Pumpkin Pie's 9.7% chest loot weight
                tableBuilder.pool(chestPool.build());
            } else if (BuiltInLootTables.FARMER_GIFT.equals(resourceKey)) {
                tableBuilder.modifyPools(poolBuilder -> {
                    poolBuilder.setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(ItemsSD.APPLE_PIE));
                    poolBuilder.build();
                });
            } else if (resourceKey.equals(BuiltInLootTables.VILLAGE_FISHER)) {
                tableBuilder.modifyPools(poolBuilder -> {
                    poolBuilder.add(LootItem.lootTableItem(ItemsSD.CALAMARI).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))).build(); // Add Calamari
                });
            } else if (resourceKey.equals(BuiltInLootTables.FISHING_FISH)) {
                tableBuilder.modifyPools(poolBuilder -> {
                    poolBuilder.add(LootItem.lootTableItem(ItemsSD.CALAMARI).setWeight(13)).build();
                });
            } else if (resourceKey.equals(BuiltInLootTables.FISHERMAN_GIFT)) {
                tableBuilder.modifyPools(poolBuilder -> {
                    poolBuilder.add(LootItem.lootTableItem(ItemsSD.CALAMARI)).build();
                });
            } else if (resourceKey.equals(EntityType.SQUID.getDefaultLootTable().orElseThrow()) || resourceKey.equals(EntityType.GLOW_SQUID.getDefaultLootTable().orElseThrow())) {
                LootPool.Builder squidPool = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ItemsSD.CALAMARI)
                                .apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, onFirePredicate))));
                tableBuilder.withPool(squidPool).build();
            } else if (resourceKey.equals(EntityType.POLAR_BEAR.getDefaultLootTable().orElseThrow())) {
                tableBuilder.modifyPools(poolBuilder -> {
                    poolBuilder.add(LootItem.lootTableItem(ItemsSD.CALAMARI)
                            .apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, onFirePredicate)))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                            .apply(EnchantedCountIncreaseFunction.lootingMultiplier(provider, UniformGenerator.between(0.0F, 1.0F)))).build();
                });
            }
        });
    }
}
