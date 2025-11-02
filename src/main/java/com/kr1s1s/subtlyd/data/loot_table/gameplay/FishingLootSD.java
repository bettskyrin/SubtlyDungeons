package com.kr1s1s.subtlyd.data.loot_table.gameplay;

import com.kr1s1s.subtlyd.world.item.ItemsSD;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;

public class FishingLootSD {
    public static void generate() {
        LootTableEvents.REPLACE.register(((key, original, source, registries) -> {
            if (key.equals(BuiltInLootTables.FISHING_FISH)) {
                HolderLookup.RegistryLookup<Biome> registryLookup = registries.lookupOrThrow(Registries.BIOME);
                LootTable.Builder newFishingTable = LootTable.lootTable().withPool(LootPool.lootPool()
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
                                    registryLookup.getOrThrow(BiomeTags.IS_OVERWORLD).stream().toList()
                            )))).setWeight(1))
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

                    /* Other Situations */
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
            }
            return null;
        }));
    }
}
