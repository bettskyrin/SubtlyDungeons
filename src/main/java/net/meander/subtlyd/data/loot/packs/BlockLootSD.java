package net.meander.subtlyd.data.loot.packs;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.meander.subtlyd.world.item.ItemsSD;
import net.meander.subtlyd.world.level.block.BlocksSD;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.MatchBlock;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.concurrent.CompletableFuture;

/**
 * @see net.minecraft.data.loot.packs.VanillaBlockLoot
 */
public class BlockLootSD extends FabricBlockLootSubProvider {
    public BlockLootSD(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture);
    }

    @Override
    public void generate() {
        HolderGetter<Block> blockHolderGetter = BuiltInRegistries.BLOCK;

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
        add(BlocksSD.WARPED_OVERHANG, this::createShearsOrSilkTouchOnlyDrop);
        add(BlocksSD.REEDS, this::createShearsOrSilkTouchOnlyDrop);
        dropSelf(BlocksSD.BASALT_SLAB);
        dropSelf(BlocksSD.SOUL_JACK_O_LANTERN);
        add(Blocks.CAMPFIRE, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(AlternativesEntry.alternatives(
                                LootItem.lootTableItem(Items.CAMPFIRE)
                                        .when(hasSilkTouch())
                                        .when(MatchBlock.blockMatches(blockHolderGetter, Blocks.CAMPFIRE, StatePropertiesPredicate.Builder.properties().hasProperty(CampfireBlock.LIT, true))),
                                LootItem.lootTableItem(ItemsSD.UNLIT_CAMPFIRE)
                                        .when(hasSilkTouch()),
                                LootItem.lootTableItem(Items.CHARCOAL)
                                        .apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(2)))
                        ))
                ));
        add(Blocks.SOUL_CAMPFIRE, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(AlternativesEntry.alternatives(
                                LootItem.lootTableItem(Items.SOUL_CAMPFIRE)
                                        .when(hasSilkTouch())
                                        .when(MatchBlock.blockMatches(blockHolderGetter, Blocks.SOUL_CAMPFIRE, StatePropertiesPredicate.Builder.properties().hasProperty(CampfireBlock.LIT, true))),
                                LootItem.lootTableItem(ItemsSD.UNLIT_SOUL_CAMPFIRE)
                                        .when(hasSilkTouch()),
                                LootItem.lootTableItem(Items.SOUL_SOIL)
                                        .apply(SetItemCountFunction.setCount(ContextIntProviders.exactly(1)))
                        ))
                ));
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
        dropSelf(BlocksSD.TERRACOTTA_STAIRS);
        add(BlocksSD.TERRACOTTA_SLAB, this::createSlabItemTable);
        BlocksSD.DYED_TERRACOTTA_STAIRS.forEach(this::dropSelf);
        BlocksSD.DYED_TERRACOTTA_SLAB.forEach(block -> add(block, this::createSlabItemTable));
    }
}
