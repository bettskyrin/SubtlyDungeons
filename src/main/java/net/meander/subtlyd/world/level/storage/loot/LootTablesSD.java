package net.meander.subtlyd.world.level.storage.loot;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * @see net.minecraft.world.level.storage.loot.BuiltInLootTables
 */
public class LootTablesSD {
    public static final ResourceKey<LootTable> SWAMP_HUT_CAULDRON = register("gameplay/swamp_hut_cauldron");

    private static ResourceKey<LootTable> register(final String location) {
        return BuiltInLootTables.register(ResourceKey.create(Registries.LOOT_TABLE, UtilSD.identifier(location)));
    }
}
