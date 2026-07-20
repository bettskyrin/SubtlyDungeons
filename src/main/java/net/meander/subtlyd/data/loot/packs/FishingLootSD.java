package net.meander.subtlyd.data.loot.packs;

import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.WritableRegistry;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @see net.minecraft.data.loot.packs.VanillaFishingLoot
 */
public class FishingLootSD {
    public static void register(WritableRegistry<LootTable> registry, HolderLookup.Provider lookupProvider) {
        try {
            modify(registry, table -> overwriteFishPool(table, lookupProvider));
        } catch (Exception e) {
            Util.LOGGER.error("Failed registry lookup: {}", e.getMessage());
        }
    }

    private static void modify(WritableRegistry<LootTable> registry, Consumer<LootTable> modifier) {
        registry.get(BuiltInLootTables.FISHING_FISH).ifPresent(lootTableReference -> modifier.accept(lootTableReference.value()));
    }

    private static void overwriteFishPool(LootTable table, HolderLookup.Provider lookupProvider) {
        HolderLookup.RegistryLookup<Biome> biomeLookup = lookupProvider.lookupOrThrow(Registries.BIOME);
        LootPool customPool = LootPool.lootPool()
                .add(LootItem.lootTableItem(Items.COD)
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                biomeLookup.getOrThrow(Biomes.COLD_OCEAN),
                                biomeLookup.getOrThrow(Biomes.DEEP_COLD_OCEAN),
                                biomeLookup.getOrThrow(Biomes.FROZEN_OCEAN),
                                biomeLookup.getOrThrow(Biomes.SNOWY_BEACH),
                                biomeLookup.getOrThrow(Biomes.DEEP_FROZEN_OCEAN)))))
                        .setWeight(50))
                .add(LootItem.lootTableItem(Items.COD)
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                biomeLookup.getOrThrow(Biomes.OCEAN),
                                biomeLookup.getOrThrow(Biomes.DEEP_OCEAN)))))
                        .setWeight(60))
                .add(LootItem.lootTableItem(Items.COD)
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                biomeLookup.getOrThrow(Biomes.FROZEN_RIVER),
                                biomeLookup.getOrThrow(Biomes.BEACH)))))
                        .setWeight(30))
                .add(LootItem.lootTableItem(Items.COD)
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                biomeLookup.getOrThrow(Biomes.LUKEWARM_OCEAN)))))
                        .setWeight(10))
                .add(LootItem.lootTableItem(Items.SALMON)
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                biomeLookup.getOrThrow(BiomeTags.IS_OVERWORLD).stream().toList()))))
                        .setWeight(1))
                .add(LootItem.lootTableItem(Items.SALMON)
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                biomeLookup.getOrThrow(Biomes.RIVER),
                                biomeLookup.getOrThrow(Biomes.FROZEN_RIVER)))))
                        .setWeight(60))
                .add(LootItem.lootTableItem(Items.SALMON)
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                biomeLookup.getOrThrow(Biomes.COLD_OCEAN),
                                biomeLookup.getOrThrow(Biomes.DEEP_COLD_OCEAN),
                                biomeLookup.getOrThrow(Biomes.FROZEN_OCEAN),
                                biomeLookup.getOrThrow(Biomes.SNOWY_BEACH),
                                biomeLookup.getOrThrow(Biomes.DEEP_FROZEN_OCEAN)))))
                        .setWeight(40))
                .add(LootItem.lootTableItem(ItemsSD.CALAMARI)
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                biomeLookup.getOrThrow(Biomes.RIVER)))))
                        .setWeight(40))
                .add(LootItem.lootTableItem(ItemsSD.CALAMARI)
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                biomeLookup.getOrThrow(Biomes.FROZEN_RIVER),
                                biomeLookup.getOrThrow(Biomes.FROZEN_OCEAN),
                                biomeLookup.getOrThrow(Biomes.SNOWY_BEACH),
                                biomeLookup.getOrThrow(Biomes.DEEP_FROZEN_OCEAN),
                                biomeLookup.getOrThrow(Biomes.COLD_OCEAN),
                                biomeLookup.getOrThrow(Biomes.DEEP_COLD_OCEAN)))))
                        .setWeight(10))
                .add(LootItem.lootTableItem(ItemsSD.CALAMARI)
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                biomeLookup.getOrThrow(Biomes.LUKEWARM_OCEAN),
                                biomeLookup.getOrThrow(Biomes.WARM_OCEAN),
                                biomeLookup.getOrThrow(Biomes.DEEP_LUKEWARM_OCEAN)))))
                        .setWeight(20))
                .add(LootItem.lootTableItem(ItemsSD.CALAMARI)
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                biomeLookup.getOrThrow(Biomes.OCEAN),
                                biomeLookup.getOrThrow(Biomes.DEEP_OCEAN)))))
                        .setWeight(40))
                .add(LootItem.lootTableItem(Items.TROPICAL_FISH)
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                biomeLookup.getOrThrow(Biomes.WARM_OCEAN),
                                biomeLookup.getOrThrow(Biomes.LUKEWARM_OCEAN),
                                biomeLookup.getOrThrow(Biomes.DEEP_LUKEWARM_OCEAN)))))
                        .setWeight(60))
                .add(LootItem.lootTableItem(Items.TROPICAL_FISH)
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                biomeLookup.getOrThrow(Biomes.LUSH_CAVES),
                                biomeLookup.getOrThrow(Biomes.MANGROVE_SWAMP)))))
                        .setWeight(100))
                .add(LootItem.lootTableItem(Items.PUFFERFISH)
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                biomeLookup.getOrThrow(Biomes.WARM_OCEAN),
                                biomeLookup.getOrThrow(Biomes.LUKEWARM_OCEAN),
                                biomeLookup.getOrThrow(Biomes.DEEP_LUKEWARM_OCEAN)))))
                        .setWeight(20))

                /* Other Situations */
                .add(LootItem.lootTableItem(Items.COD)
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                biomeLookup.getOrThrow(BiomeTags.IS_END).stream().toList()))))
                        .setWeight(0))
                .add(LootItem.lootTableItem(Items.SALMON)
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                biomeLookup.getOrThrow(BiomeTags.IS_END).stream().toList()))))
                        .setWeight(0))
                .add(LootItem.lootTableItem(ItemsSD.CALAMARI)
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                biomeLookup.getOrThrow(BiomeTags.IS_END).stream().toList()))))
                        .setWeight(0))
                .add(LootItem.lootTableItem(Items.TROPICAL_FISH)
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                biomeLookup.getOrThrow(BiomeTags.IS_END).stream().toList()))))
                        .setWeight(0))
                .add(LootItem.lootTableItem(Items.PUFFERFISH)
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                biomeLookup.getOrThrow(BiomeTags.IS_END).stream().toList()))))
                        .setWeight(0))
                .add(LootItem.lootTableItem(Items.SALMON)
                        .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(HolderSet.direct(
                                biomeLookup.getOrThrow(Biomes.LUSH_CAVES)))))
                        .setWeight(0))
                .build();
        List<LootPool> newPools = new ArrayList<>();

        newPools.add(customPool);

        table.pools = newPools;
    }
}
