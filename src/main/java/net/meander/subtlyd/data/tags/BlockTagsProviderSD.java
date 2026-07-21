package net.meander.subtlyd.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.meander.subtlyd.references.BlockItemIdsSD;
import net.meander.subtlyd.tags.BlockTagsSD;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockIds;
import net.minecraft.references.BlockItemIds;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.DoublePlantBlock;

import java.util.concurrent.CompletableFuture;

/**
 * @see net.minecraft.data.tags.VanillaBlockTagsProvider
 */
public class BlockTagsProviderSD extends FabricTagsProvider.BlockTagsProvider {
    public BlockTagsProviderSD(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override protected void addTags(HolderLookup.Provider registries) {
        tag(BlockTagsSD.SNOW_BRICKS)
                .add(BlockItemIdsSD.SNOW_BRICKS.block())
                .add(BlockItemIdsSD.SNOW_BRICK_STAIRS.block())
                .add(BlockItemIdsSD.SNOW_BRICK_SLAB.block())
                .add(BlockItemIdsSD.SNOW_BRICK_WALL.block());
        tag(BlockTagsSD.STONE_TILES)
                .add(BlockItemIdsSD.STONE_TILES.block())
                .add(BlockItemIdsSD.STONE_TILE_STAIRS.block())
                .add(BlockItemIdsSD.STONE_TILE_SLAB.block())
                .add(BlockItemIdsSD.STONE_TILE_WALL.block());
        tag(BlockTagsSD.DRIPSTONE)
                .add(BlockItemIdsSD.CHISELED_POLISHED_DRIPSTONE.block())
                .add(BlockItemIdsSD.POLISHED_DRIPSTONE.block())
                .add(BlockItemIdsSD.POLISHED_DRIPSTONE_STAIRS.block())
                .add(BlockItemIdsSD.POLISHED_DRIPSTONE_SLAB.block())
                .add(BlockItemIdsSD.POLISHED_DRIPSTONE_WALL.block());
        tag(BlockTags.WALLS)
                .add(BlockItemIdsSD.SNOW_BRICK_WALL.block())
                .add(BlockItemIdsSD.POLISHED_DRIPSTONE_WALL.block())
                .add(BlockItemIdsSD.STONE_TILE_WALL.block());
        tag(BlockTags.STAIRS)
                .add(BlockItemIdsSD.SNOW_BRICK_STAIRS.block())
                .add(BlockItemIdsSD.POLISHED_DRIPSTONE_STAIRS.block())
                .add(BlockItemIdsSD.STONE_TILE_STAIRS.block());
        tag(BlockTags.SLABS)
                .add(BlockItemIdsSD.SNOW_BRICK_SLAB.block())
                .add(BlockItemIdsSD.POLISHED_DRIPSTONE_SLAB.block())
                .add(BlockItemIdsSD.STONE_TILE_SLAB.block()).add(BlockItemIdsSD.SNOW_BRICK_SLAB.block())
                .add(BlockItemIdsSD.POLISHED_DRIPSTONE_SLAB.block())
                .add(BlockItemIdsSD.STONE_TILE_SLAB.block());
        tag(BlockTags.WOODEN_SLABS)
                .add(BlockItemIdsSD.OAK_WOOD_SLAB.block())
                .add(BlockItemIdsSD.SPRUCE_WOOD_SLAB.block())
                .add(BlockItemIdsSD.BIRCH_WOOD_SLAB.block())
                .add(BlockItemIdsSD.JUNGLE_WOOD_SLAB.block())
                .add(BlockItemIdsSD.ACACIA_WOOD_SLAB.block())
                .add(BlockItemIdsSD.DARK_OAK_WOOD_SLAB.block())
                .add(BlockItemIdsSD.MANGROVE_WOOD_SLAB.block())
                .add(BlockItemIdsSD.CHERRY_WOOD_SLAB.block())
                .add(BlockItemIdsSD.PALE_OAK_WOOD_SLAB.block())
                .add(BlockItemIdsSD.POPLAR_WOOD_SLAB.block())
                .add(BlockItemIdsSD.CRIMSON_HYPHAE_SLAB.block())
                .add(BlockItemIdsSD.WARPED_HYPHAE_SLAB.block())
                .add(BlockItemIdsSD.STRIPPED_OAK_WOOD_SLAB.block())
                .add(BlockItemIdsSD.STRIPPED_SPRUCE_WOOD_SLAB.block())
                .add(BlockItemIdsSD.STRIPPED_BIRCH_WOOD_SLAB.block())
                .add(BlockItemIdsSD.STRIPPED_JUNGLE_WOOD_SLAB.block())
                .add(BlockItemIdsSD.STRIPPED_ACACIA_WOOD_SLAB.block())
                .add(BlockItemIdsSD.STRIPPED_DARK_OAK_WOOD_SLAB.block())
                .add(BlockItemIdsSD.STRIPPED_MANGROVE_WOOD_SLAB.block())
                .add(BlockItemIdsSD.STRIPPED_CHERRY_WOOD_SLAB.block())
                .add(BlockItemIdsSD.STRIPPED_PALE_OAK_WOOD_SLAB.block())
                .add(BlockItemIdsSD.STRIPPED_POPLAR_WOOD_SLAB.block())
                .add(BlockItemIdsSD.STRIPPED_CRIMSON_HYPHAE_SLAB.block())
                .add(BlockItemIdsSD.STRIPPED_WARPED_HYPHAE_SLAB.block());
        tag(BlockTags.WOODEN_STAIRS)
                .add(BlockItemIdsSD.OAK_WOOD_STAIRS.block())
                .add(BlockItemIdsSD.SPRUCE_WOOD_STAIRS.block())
                .add(BlockItemIdsSD.BIRCH_WOOD_STAIRS.block())
                .add(BlockItemIdsSD.JUNGLE_WOOD_STAIRS.block())
                .add(BlockItemIdsSD.ACACIA_WOOD_STAIRS.block())
                .add(BlockItemIdsSD.DARK_OAK_WOOD_STAIRS.block())
                .add(BlockItemIdsSD.MANGROVE_WOOD_STAIRS.block())
                .add(BlockItemIdsSD.CHERRY_WOOD_STAIRS.block())
                .add(BlockItemIdsSD.PALE_OAK_WOOD_STAIRS.block())
                .add(BlockItemIdsSD.POPLAR_WOOD_STAIRS.block())
                .add(BlockItemIdsSD.CRIMSON_HYPHAE_STAIRS.block())
                .add(BlockItemIdsSD.WARPED_HYPHAE_STAIRS.block())
                .add(BlockItemIdsSD.STRIPPED_OAK_WOOD_STAIRS.block())
                .add(BlockItemIdsSD.STRIPPED_SPRUCE_WOOD_STAIRS.block())
                .add(BlockItemIdsSD.STRIPPED_BIRCH_WOOD_STAIRS.block())
                .add(BlockItemIdsSD.STRIPPED_JUNGLE_WOOD_STAIRS.block())
                .add(BlockItemIdsSD.STRIPPED_ACACIA_WOOD_STAIRS.block())
                .add(BlockItemIdsSD.STRIPPED_DARK_OAK_WOOD_STAIRS.block())
                .add(BlockItemIdsSD.STRIPPED_MANGROVE_WOOD_STAIRS.block())
                .add(BlockItemIdsSD.STRIPPED_CHERRY_WOOD_STAIRS.block())
                .add(BlockItemIdsSD.STRIPPED_PALE_OAK_WOOD_STAIRS.block())
                .add(BlockItemIdsSD.STRIPPED_POPLAR_WOOD_STAIRS.block())
                .add(BlockItemIdsSD.STRIPPED_CRIMSON_HYPHAE_STAIRS.block())
                .add(BlockItemIdsSD.STRIPPED_WARPED_HYPHAE_STAIRS.block());
        tag(BlockTags.CAULDRONS)
                .add(BlockItemIdsSD.POTION_CAULDRON.block())
                .add(BlockItemIdsSD.STEW_CAULDRON.block());
        tag(BlockTagsSD.TRIGGERS_AMBIENT_WIND_BLOCK_SOUNDS)
                .add(BlockItemIds.SNOW.block())
                .add(BlockItemIds.SNOW_BLOCK.block())
                .add(BlockItemIds.POWDER_SNOW.block())
                .add(BlockItemIds.ICE.block())
                .add(BlockItemIds.BLUE_ICE.block())
                .add(BlockItemIds.PACKED_ICE.block())
                .add(BlockIds.FROSTED_ICE)
                .add(BlockItemIds.STONE.block())
                .add(BlockItemIds.CALCITE.block());
        tag(BlockTagsSD.TRIGGERS_AMBIENT_BUSH_BLOCK_SOUNDS)
                .add(BlockItemIds.BUSH.block())
                .add(BlockItemIds.RED_SHRUB.block());
        tag(BlockTagsSD.TRIGGERS_AMBIENT_GRASS_BLOCK_SOUNDS)
                .add(BlockItemIds.TALL_GRASS.block())
                .add(BlockItemIds.LARGE_FERN.block());
        tag(BlockTagsSD.TALL_PLANTS)
                .addAll(registries.lookupOrThrow(Registries.BLOCK).listElements().filter(b -> b.value() instanceof DoublePlantBlock).map(Holder.Reference::key));
        tag(BlockTags.REPLACEABLE_BY_MUSHROOMS)
                .add(BlockItemIdsSD.REEDS.block());
        tag(BlockTags.REPLACEABLE_BY_TREES)
                .add(BlockItemIdsSD.REEDS.block());
        tag(BlockTags.UNDERWATER_BONEMEALS)
                .add(BlockItemIdsSD.REEDS.block());
        tag(BlockTags.ENCHANTMENT_POWER_TRANSMITTER)
                .forceAddTag(BlockTags.WOOL_CARPETS)
                .forceAddTag(BlockTags.CANDLES)
                .forceAddTag(BlockTags.FLOWER_POTS)
                .forceAddTag(BlockTags.CAN_GLIDE_THROUGH)
                .forceAddTag(BlockTags.CHAINS)
                .add(BlockItemIds.CAULDRON.block())
                .add(BlockItemIds.BREWING_STAND.block())
                .add(BlockItemIds.CHISELED_BOOKSHELF.block())
                .add(BlockItemIds.MOSS_CARPET.block())
                .add(BlockItemIds.PALE_MOSS_CARPET.block());
        tag(BlockTags.SUPPORTS_WARPED_ROOTS)
                .add(BlockItemIds.WARPED_WART_BLOCK.block());
        tag(BlockTags.OVERRIDES_MUSHROOM_LIGHT_REQUIREMENT)
                .forceAddTag(BlockTags.OVERWORLD_NATURAL_LOGS);
        tag(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
                .add(BlockItemIdsSD.PERSE_WILDFLOWERS.block());
        tag(BlockTags.BEE_ATTRACTIVE)
                .add(BlockItemIdsSD.PERSE_WILDFLOWERS.block());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(BlockTagsSD.SNOW_BRICKS)
                .addTag(BlockTagsSD.STONE_TILES)
                .addTag(BlockTagsSD.DRIPSTONE)
                .add(BlockItemIdsSD.STONE_PILLAR.block())
                .add(BlockItemIdsSD.CHARCOAL_BLOCK.block())
                .add(BlockItemIdsSD.IRON_GRATE.block())
                .add(BlockItemIdsSD.BASALT_SLAB.block());
        tag(BlockTags.MINEABLE_WITH_AXE)
                .forceAddTag(BlockTags.SKULLS);
        tag(BlockTags.SHEARS_EXTREME_BREAKING_SPEED)
                .add(BlockItemIdsSD.WARPED_OVERHANG.block());
        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(BlockItemIdsSD.IRON_GRATE.block());
        tag(BlockTags.DRAGON_IMMUNE)
                .add(BlockItemIdsSD.IRON_GRATE.block());
        tag(BlockTagsSD.SILENT_FOLIAGE)
                .add(BlockItemIds.PALE_OAK_LEAVES.block());
    }
}
