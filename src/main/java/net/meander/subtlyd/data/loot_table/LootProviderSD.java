package net.meander.subtlyd.data.loot_table;

import net.meander.subtlyd.data.loot_table.chests.GenericChestLootSD;
import net.meander.subtlyd.data.loot_table.chests.VillageLootSD;
import net.meander.subtlyd.data.loot_table.entities.EntityLootSD;
import net.meander.subtlyd.data.loot_table.gameplay.FishingLootSD;
import net.meander.subtlyd.data.loot_table.gameplay.SwampHutLoot;
import net.meander.subtlyd.data.loot_table.gameplay.VillageHeroLootSD;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class LootProviderSD {
    public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        return new LootTableProvider(
                output,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(GameplayLootTables::new, LootContextParamSets.EMPTY)),
                registries
        );
    }

    public static void registration() {
        VillageLootSD.register();
        VillageHeroLootSD.register();
        EntityLootSD.register();
        FishingLootSD.register();
        GenericChestLootSD.register();
    }

    public static class GameplayLootTables implements LootTableSubProvider {
        public GameplayLootTables(HolderLookup.Provider provider) {}

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> writer) {
            SwampHutLoot.register(writer);
        }
    }
}
