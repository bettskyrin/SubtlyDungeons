package net.meander.subtlyd.world.level.storage.loot;

import net.meander.subtlyd.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * @see net.minecraft.world.level.storage.loot.BuiltInLootTables
 */
public class LootTablesSD {
    public static final ResourceKey<LootTable> SWAMP_HUT_CAULDRON = ResourceKey.create(Registries.LOOT_TABLE, Util.identifier("gameplay/swamp_hut_cauldron"));
}
