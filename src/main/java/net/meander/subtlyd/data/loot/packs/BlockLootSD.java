package net.meander.subtlyd.data.loot.packs;

import net.meander.subtlyd.world.level.block.BlocksSD;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Collections;
import java.util.List;

/**
 * @see net.minecraft.data.loot.packs.VanillaBlockLoot
 */
public class BlockLootSD extends BlockLootSubProvider {
    public BlockLootSD(LootTableSubProvider.Context context) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), context);
    }

    public static void registration(BootstrapContext<LootTable> context) {
        List<LootTableProvider.SubProviderEntry> subProviders = List.of(
                new LootTableProvider.SubProviderEntry(BlockLootSD::new, LootContextParamSets.BLOCK)
        );

        new LootTableProvider(Collections.emptySet(), subProviders).run(context);
    }

    @Override
    public void generate() {
        dropSelf(BlocksSD.SNOW_BRICKS);
        dropSelf(BlocksSD.SNOW_BRICK_STAIRS);
        dropSelf(BlocksSD.SNOW_BRICK_SLAB);
        dropSelf(BlocksSD.SNOW_BRICK_WALL);
        dropSelf(BlocksSD.CHARCOAL_BLOCK);
        dropSelf(BlocksSD.IRON_GRATE);
        dropSelf(BlocksSD.CHISELED_POLISHED_DRIPSTONE);
        dropSelf(BlocksSD.POLISHED_DRIPSTONE);
        dropSelf(BlocksSD.POLISHED_DRIPSTONE_STAIRS);
        dropSelf(BlocksSD.POLISHED_DRIPSTONE_SLAB);
        dropSelf(BlocksSD.POLISHED_DRIPSTONE_WALL);
        dropSelf(BlocksSD.STONE_TILES);
        dropSelf(BlocksSD.STONE_TILE_STAIRS);
        dropSelf(BlocksSD.STONE_TILE_SLAB);
        dropSelf(BlocksSD.STONE_TILE_WALL);
        dropSelf(BlocksSD.STONE_PILLAR);
//        add(BlocksSD.WARPED_OVERHANG, this::createShearsOrSilkTouchOnlyDrop); // TODO
//        add(BlocksSD.REEDS, this::createShearsOrSilkTouchOnlyDrop);
        dropSelf(BlocksSD.WARPED_OVERHANG);
        dropSelf(BlocksSD.REEDS);
        dropSelf(BlocksSD.BASALT_SLAB);
        dropSelf(BlocksSD.SOUL_JACK_O_LANTERN);
//        add(Blocks.CAMPFIRE, LootTable.lootTable() // TODO
//                .withPool(LootPool.lootPool()
//                        .setRolls(ConstantValue.exactly(1.0F))
//                        .add(AlternativesEntry.alternatives(
//                                LootItem.lootTableItem(Items.CAMPFIRE)
//                                        .when(hasSilkTouch())
//                                        .when(MatchBlock.blockMatches(blockLookup, Blocks.CAMPFIRE, StatePropertiesPredicate.Builder.properties().hasProperty(CampfireBlock.LIT, true))),
//                                LootItem.lootTableItem(ItemsSD.UNLIT_CAMPFIRE)
//                                        .when(hasSilkTouch()),
//                                LootItem.lootTableItem(Items.CHARCOAL)
//                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))
//                        ))
//                ));
//        add(Blocks.SOUL_CAMPFIRE, LootTable.lootTable()
//                .withPool(LootPool.lootPool()
//                        .setRolls(ConstantValue.exactly(1.0F))
//                        .add(AlternativesEntry.alternatives(
//                                LootItem.lootTableItem(Items.SOUL_CAMPFIRE)
//                                        .when(MatchBlock.blockMatches(blockLookup, Blocks.SOUL_CAMPFIRE, StatePropertiesPredicate.Builder.properties().hasProperty(CampfireBlock.LIT, true))),
//                                LootItem.lootTableItem(ItemsSD.UNLIT_SOUL_CAMPFIRE)
//                                        .when(hasSilkTouch()),
//                                LootItem.lootTableItem(Items.SOUL_SOIL)
//                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
//                        ))
//                ));
        dropSelf(BlocksSD.PERSE_WILDFLOWERS);
        dropSelf(BlocksSD.OAK_WOOD_SLAB);
        dropSelf(BlocksSD.SPRUCE_WOOD_SLAB);
        dropSelf(BlocksSD.BIRCH_WOOD_SLAB);
        dropSelf(BlocksSD.JUNGLE_WOOD_SLAB);
        dropSelf(BlocksSD.ACACIA_WOOD_SLAB);
        dropSelf(BlocksSD.DARK_OAK_WOOD_SLAB);
        dropSelf(BlocksSD.MANGROVE_WOOD_SLAB);
        dropSelf(BlocksSD.CHERRY_WOOD_SLAB);
        dropSelf(BlocksSD.PALE_OAK_WOOD_SLAB);
        dropSelf(BlocksSD.POPLAR_WOOD_SLAB);
        dropSelf(BlocksSD.CRIMSON_HYPHAE_SLAB);
        dropSelf(BlocksSD.WARPED_HYPHAE_SLAB);
        dropSelf(BlocksSD.STRIPPED_OAK_WOOD_SLAB);
        dropSelf(BlocksSD.STRIPPED_SPRUCE_WOOD_SLAB);
        dropSelf(BlocksSD.STRIPPED_BIRCH_WOOD_SLAB);
        dropSelf(BlocksSD.STRIPPED_JUNGLE_WOOD_SLAB);
        dropSelf(BlocksSD.STRIPPED_ACACIA_WOOD_SLAB);
        dropSelf(BlocksSD.STRIPPED_DARK_OAK_WOOD_SLAB);
        dropSelf(BlocksSD.STRIPPED_MANGROVE_WOOD_SLAB);
        dropSelf(BlocksSD.STRIPPED_CHERRY_WOOD_SLAB);
        dropSelf(BlocksSD.STRIPPED_PALE_OAK_WOOD_SLAB);
        dropSelf(BlocksSD.STRIPPED_POPLAR_WOOD_SLAB);
        dropSelf(BlocksSD.STRIPPED_CRIMSON_HYPHAE_SLAB);
        dropSelf(BlocksSD.STRIPPED_WARPED_HYPHAE_SLAB);
        dropSelf(BlocksSD.OAK_WOOD_STAIRS);
        dropSelf(BlocksSD.SPRUCE_WOOD_STAIRS);
        dropSelf(BlocksSD.BIRCH_WOOD_STAIRS);
        dropSelf(BlocksSD.JUNGLE_WOOD_STAIRS);
        dropSelf(BlocksSD.ACACIA_WOOD_STAIRS);
        dropSelf(BlocksSD.DARK_OAK_WOOD_STAIRS);
        dropSelf(BlocksSD.MANGROVE_WOOD_STAIRS);
        dropSelf(BlocksSD.CHERRY_WOOD_STAIRS);
        dropSelf(BlocksSD.PALE_OAK_WOOD_STAIRS);
        dropSelf(BlocksSD.POPLAR_WOOD_STAIRS);
        dropSelf(BlocksSD.CRIMSON_HYPHAE_STAIRS);
        dropSelf(BlocksSD.WARPED_HYPHAE_STAIRS);
        dropSelf(BlocksSD.STRIPPED_OAK_WOOD_STAIRS);
        dropSelf(BlocksSD.STRIPPED_SPRUCE_WOOD_STAIRS);
        dropSelf(BlocksSD.STRIPPED_BIRCH_WOOD_STAIRS);
        dropSelf(BlocksSD.STRIPPED_JUNGLE_WOOD_STAIRS);
        dropSelf(BlocksSD.STRIPPED_ACACIA_WOOD_STAIRS);
        dropSelf(BlocksSD.STRIPPED_DARK_OAK_WOOD_STAIRS);
        dropSelf(BlocksSD.STRIPPED_MANGROVE_WOOD_STAIRS);
        dropSelf(BlocksSD.STRIPPED_CHERRY_WOOD_STAIRS);
        dropSelf(BlocksSD.STRIPPED_PALE_OAK_WOOD_STAIRS);
        dropSelf(BlocksSD.STRIPPED_POPLAR_WOOD_STAIRS);
        dropSelf(BlocksSD.STRIPPED_CRIMSON_HYPHAE_STAIRS);
        dropSelf(BlocksSD.STRIPPED_WARPED_HYPHAE_STAIRS);
    }
}
