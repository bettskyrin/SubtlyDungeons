package com.kr1s1s.subtlyd.data.loot.packs;

import com.kr1s1s.subtlyd.world.item.ItemsSD;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.packs.VanillaFishingLoot;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;

public class LootSD {
    public static void modify() {
        LootTableEvents.MODIFY.register((resourceKey, tableBuilder, lootTableSource, provider) -> {
            EntityPredicate onFirePredicate = EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true)).build();
            if (BuiltInLootTables.VILLAGE_PLAINS_HOUSE.equals(resourceKey)) { /* VILLAGES */
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
            } else if (resourceKey.equals(BuiltInLootTables.FISHERMAN_GIFT)) {
                tableBuilder.modifyPools(poolBuilder -> {
                    poolBuilder.add(LootItem.lootTableItem(ItemsSD.CALAMARI)).build();
                });
            } else if (resourceKey.equals(EntityType.SQUID.getDefaultLootTable().orElseThrow()) || resourceKey.equals(EntityType.GLOW_SQUID.getDefaultLootTable().orElseThrow())) { /* SQUID */
                LootPool.Builder squidPool = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ItemsSD.CALAMARI)
                                .apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, onFirePredicate))));
                tableBuilder.withPool(squidPool).build();
            } else if (resourceKey.equals(EntityType.POLAR_BEAR.getDefaultLootTable().orElseThrow())) { /* POLAR BEAR */
                tableBuilder.modifyPools(poolBuilder -> {
                    poolBuilder.add(LootItem.lootTableItem(ItemsSD.CALAMARI)
                            .apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, onFirePredicate)))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                            .apply(EnchantedCountIncreaseFunction.lootingMultiplier(provider, UniformGenerator.between(0.0F, 1.0F)))).build();
                });
            }
        });
    }

    public static void replace() {
        LootTableEvents.REPLACE.register(((key, original, source, registries) -> {
            HolderLookup.RegistryLookup<Biome> registryLookup = registries.lookupOrThrow(Registries.BIOME);
            LootTable.Builder newFishingTable = LootTable.lootTable()
                    .withPool(
                            LootPool.lootPool()
                                    .add(LootItem.lootTableItem(Items.COD).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                                    registryLookup.getOrThrow(Biomes.COLD_OCEAN),
                                                    registryLookup.getOrThrow(Biomes.DEEP_COLD_OCEAN),
                                                    registryLookup.getOrThrow(Biomes.FROZEN_OCEAN),
                                                    registryLookup.getOrThrow(Biomes.SNOWY_BEACH),
                                                    registryLookup.getOrThrow(Biomes.DEEP_FROZEN_OCEAN)
                                            ))))
                                            .setWeight(50))
                                    .add(LootItem.lootTableItem(Items.COD).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                                    registryLookup.getOrThrow(Biomes.OCEAN),
                                                    registryLookup.getOrThrow(Biomes.DEEP_OCEAN)
                                            ))))
                                            .setWeight(60))
                                    .add(LootItem.lootTableItem(Items.COD).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                                    registryLookup.getOrThrow(Biomes.FROZEN_RIVER),
                                                    registryLookup.getOrThrow(Biomes.BEACH)
                                            ))))
                                            .setWeight(30))
                                    .add(LootItem.lootTableItem(Items.COD).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                                    registryLookup.getOrThrow(Biomes.LUKEWARM_OCEAN)
                                            ))))
                                            .setWeight(10))
                                    .add(LootItem.lootTableItem(Items.SALMON).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                                    registryLookup.getOrThrow(Biomes.RIVER),
                                                    registryLookup.getOrThrow(Biomes.FROZEN_RIVER)
                                            ))))
                                            .setWeight(60))
                                    .add(LootItem.lootTableItem(Items.SALMON).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                                    registryLookup.getOrThrow(Biomes.COLD_OCEAN),
                                                    registryLookup.getOrThrow(Biomes.DEEP_COLD_OCEAN),
                                                    registryLookup.getOrThrow(Biomes.FROZEN_OCEAN),
                                                    registryLookup.getOrThrow(Biomes.SNOWY_BEACH),
                                                    registryLookup.getOrThrow(Biomes.DEEP_FROZEN_OCEAN)
                                            ))))
                                            .setWeight(40))
                                    .add(LootItem.lootTableItem(Items.SALMON).setWeight(1))
                                    .add(LootItem.lootTableItem(ItemsSD.CALAMARI).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                                    registryLookup.getOrThrow(Biomes.RIVER)
                                            ))))
                                            .setWeight(40))
                                    .add(LootItem.lootTableItem(ItemsSD.CALAMARI).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                            registryLookup.getOrThrow(Biomes.FROZEN_RIVER),
                                            registryLookup.getOrThrow(Biomes.FROZEN_OCEAN),
                                            registryLookup.getOrThrow(Biomes.SNOWY_BEACH),
                                            registryLookup.getOrThrow(Biomes.DEEP_FROZEN_OCEAN),
                                            registryLookup.getOrThrow(Biomes.COLD_OCEAN),
                                            registryLookup.getOrThrow(Biomes.DEEP_COLD_OCEAN)
                                    )))).setWeight(10))
                                    .add(LootItem.lootTableItem(ItemsSD.CALAMARI).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                            registryLookup.getOrThrow(Biomes.LUKEWARM_OCEAN),
                                            registryLookup.getOrThrow(Biomes.WARM_OCEAN),
                                            registryLookup.getOrThrow(Biomes.DEEP_LUKEWARM_OCEAN)
                                    )))).setWeight(20))
                                    .add(LootItem.lootTableItem(ItemsSD.CALAMARI).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                                    registryLookup.getOrThrow(Biomes.OCEAN),
                                                    registryLookup.getOrThrow(Biomes.DEEP_OCEAN)
                                            ))))
                                            .setWeight(40))
                                    .add(LootItem.lootTableItem(Items.TROPICAL_FISH).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                            registryLookup.getOrThrow(Biomes.WARM_OCEAN),
                                            registryLookup.getOrThrow(Biomes.LUKEWARM_OCEAN),
                                            registryLookup.getOrThrow(Biomes.DEEP_LUKEWARM_OCEAN)
                                    )))).setWeight(60))
                                    .add(LootItem.lootTableItem(Items.TROPICAL_FISH).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                            registryLookup.getOrThrow(Biomes.LUSH_CAVES),
                                            registryLookup.getOrThrow(Biomes.MANGROVE_SWAMP)
                                    )))).setWeight(100))
                                    .add(LootItem.lootTableItem(Items.PUFFERFISH).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                            registryLookup.getOrThrow(Biomes.WARM_OCEAN),
                                            registryLookup.getOrThrow(Biomes.LUKEWARM_OCEAN),
                                            registryLookup.getOrThrow(Biomes.DEEP_LUKEWARM_OCEAN)
                                    )))).setWeight(20))

                            // Other Situations
                                    .add(LootItem.lootTableItem(Items.COD).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                            registryLookup.getOrThrow(BiomeTags.IS_END).stream().toList()
                                    )))).setWeight(0))
                                    .add(LootItem.lootTableItem(Items.SALMON).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                            registryLookup.getOrThrow(BiomeTags.IS_END).stream().toList()
                                    )))).setWeight(0))
                                    .add(LootItem.lootTableItem(ItemsSD.CALAMARI).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                            registryLookup.getOrThrow(BiomeTags.IS_END).stream().toList()
                                    )))).setWeight(0))
                                    .add(LootItem.lootTableItem(Items.TROPICAL_FISH).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                            registryLookup.getOrThrow(BiomeTags.IS_END).stream().toList()
                                    )))).setWeight(0))
                                    .add(LootItem.lootTableItem(Items.PUFFERFISH).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                            registryLookup.getOrThrow(BiomeTags.IS_END).stream().toList()
                                    )))).setWeight(0))
                                    .add(LootItem.lootTableItem(Items.SALMON).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                            registryLookup.getOrThrow(Biomes.LUSH_CAVES)
                                    )))).setWeight(0))
                    );
            return newFishingTable.build();
        }));
    }
}
