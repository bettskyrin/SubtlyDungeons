package net.meander.subtlyd.data.loot.packs;

import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.core.Holder;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @see net.minecraft.data.loot.packs.VanillaGiftLoot
 */
public class GiftLootSD {
    public static void register(WritableRegistry<LootTable> registry) {
        modify(registry, BuiltInLootTables.FARMER_GIFT, GiftLootSD::farmer);
        modify(registry, BuiltInLootTables.FISHERMAN_GIFT, GiftLootSD::fisherman);
    }

    private static void modify(WritableRegistry<LootTable> registry, ResourceKey<LootTable> key, Consumer<LootTable> modifier) {
        Optional<Holder.Reference<LootTable>> reference = registry.get(key);

        reference.ifPresent(lootTableReference -> modifier.accept(lootTableReference.value()));
    }

    private static void farmer(LootTable table) {
        List<LootPool> newPools = new ArrayList<>(table.pools);

        newPools.add(LootPool.lootPool()
                .setRolls(ContextIntProviders.exactly(1))
                .add(LootItem.lootTableItem(ItemsSD.APPLE_PIE))
                .build());

        table.pools = newPools;
    }

    private static void fisherman(LootTable table) {
        List<LootPool> newPools = new ArrayList<>(table.pools);

        newPools.add(LootPool.lootPool()
                .add(LootItem.lootTableItem(ItemsSD.CALAMARI))
                .build());

        table.pools = newPools;
    }
}
