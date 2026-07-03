package net.meander.subtlyd.world.level.storage.loot;

import net.meander.subtlyd.data.loot.packs.ChestLootSD;
import net.meander.subtlyd.data.loot.packs.FishingLootSD;
import net.meander.subtlyd.data.loot.packs.GiftLootSD;
import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.level.storage.loot.entities.EntityLootSD;
import net.meander.subtlyd.data.loot.packs.BlockEntityLoot;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
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

/**
 * @see net.minecraft.world.level.storage.loot.BuiltInLootTables
 */
public class LootTablesSD {
    public static final ResourceKey<LootTable> SWAMP_HUT_CAULDRON = ResourceKey.create(Registries.LOOT_TABLE, Util.identifier("gameplay/swamp_hut_cauldron"));

    public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        return new LootTableProvider(output, Set.of(), List.of(new LootTableProvider.SubProviderEntry(GameplayLootTables::new, LootContextParamSets.EMPTY)), registries);
    }

    public static void registration() {
        GiftLootSD.register();
        EntityLootSD.register();
        FishingLootSD.register();
        ChestLootSD.register();
    }

    public static class GameplayLootTables implements LootTableSubProvider {
        public GameplayLootTables(HolderLookup.Provider provider) {}

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> writer) {
            BlockEntityLoot.register(writer);
        }
    }
}
