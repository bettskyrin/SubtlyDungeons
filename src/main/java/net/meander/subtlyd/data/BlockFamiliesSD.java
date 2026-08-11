package net.meander.subtlyd.data;

import net.meander.subtlyd.world.level.block.BlocksSD;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ColorCollection;

import static net.minecraft.data.BlockFamilies.familyBuilder;

/**
 * @see net.minecraft.data.BlockFamilies
 */
public class BlockFamiliesSD {
    public static final BlockFamily SNOW = familyBuilder(Blocks.SNOW_BLOCK)
            .bricks(BlocksSD.SNOW_BRICKS)
            .dontGenerateModel()
            .getFamily();
    public static final BlockFamily SNOW_BRICKS = familyBuilder(BlocksSD.SNOW_BRICKS)
            .stairs(BlocksSD.SNOW_BRICK_STAIRS)
            .slab(BlocksSD.SNOW_BRICK_SLAB)
            .wall(BlocksSD.SNOW_BRICK_WALL)
            .generateStonecutterRecipe()
            .getFamily();
    public static final BlockFamily DRIPSTONE = familyBuilder(Blocks.DRIPSTONE_BLOCK)
            .polished(BlocksSD.POLISHED_DRIPSTONE)
            .dontGenerateModel()
            .generateStonecutterRecipe()
            .getFamily();
    public static final BlockFamily POLISHED_DRIPSTONE = familyBuilder(BlocksSD.POLISHED_DRIPSTONE)
            .chiseled(BlocksSD.CHISELED_POLISHED_DRIPSTONE)
            .stairs(BlocksSD.POLISHED_DRIPSTONE_STAIRS)
            .slab(BlocksSD.POLISHED_DRIPSTONE_SLAB)
            .wall(BlocksSD.POLISHED_DRIPSTONE_WALL)
            .generateStonecutterRecipe()
            .getFamily();
    public static final BlockFamily STONE_TILES = familyBuilder(BlocksSD.STONE_TILES)
            .stairs(BlocksSD.STONE_TILE_STAIRS)
            .slab(BlocksSD.STONE_TILE_SLAB)
            .wall(BlocksSD.STONE_TILE_WALL)
            .generateStonecutterRecipe()
            .getFamily();
    public static final BlockFamily BASALT = familyBuilder(Blocks.BASALT)
            .slab(BlocksSD.BASALT_SLAB)
            .dontGenerateModel()
            .generateStonecutterRecipe()
            .getFamily();
    public static final BlockFamily OAK_WOOD = familyBuilder(Blocks.OAK_WOOD)
            .stairs(BlocksSD.OAK_WOOD_STAIRS)
            .slab(BlocksSD.OAK_WOOD_SLAB)
            .getFamily();
    public static final BlockFamily STRIPPED_OAK_WOOD = familyBuilder(Blocks.STRIPPED_OAK_WOOD)
            .stairs(BlocksSD.STRIPPED_OAK_WOOD_STAIRS)
            .slab(BlocksSD.STRIPPED_OAK_WOOD_SLAB)
            .getFamily();
    public static final BlockFamily BIRCH_WOOD = familyBuilder(Blocks.BIRCH_WOOD)
            .stairs(BlocksSD.BIRCH_WOOD_STAIRS)
            .slab(BlocksSD.BIRCH_WOOD_SLAB)
            .getFamily();
    public static final BlockFamily STRIPPED_BIRCH_WOOD = familyBuilder(Blocks.STRIPPED_BIRCH_WOOD)
            .stairs(BlocksSD.STRIPPED_BIRCH_WOOD_STAIRS)
            .slab(BlocksSD.STRIPPED_BIRCH_WOOD_SLAB)
            .getFamily();
    public static final BlockFamily SPRUCE_WOOD = familyBuilder(Blocks.SPRUCE_WOOD)
            .stairs(BlocksSD.SPRUCE_WOOD_STAIRS)
            .slab(BlocksSD.SPRUCE_WOOD_SLAB)
            .getFamily();
    public static final BlockFamily STRIPPED_SPRUCE_WOOD = familyBuilder(Blocks.STRIPPED_SPRUCE_WOOD)
            .stairs(BlocksSD.STRIPPED_SPRUCE_WOOD_STAIRS)
            .slab(BlocksSD.STRIPPED_SPRUCE_WOOD_SLAB)
            .getFamily();
    public static final BlockFamily JUNGLE_WOOD = familyBuilder(Blocks.JUNGLE_WOOD)
            .stairs(BlocksSD.JUNGLE_WOOD_STAIRS)
            .slab(BlocksSD.JUNGLE_WOOD_SLAB)
            .getFamily();
    public static final BlockFamily STRIPPED_JUNGLE_WOOD = familyBuilder(Blocks.STRIPPED_JUNGLE_WOOD)
            .stairs(BlocksSD.STRIPPED_JUNGLE_WOOD_STAIRS)
            .slab(BlocksSD.STRIPPED_JUNGLE_WOOD_SLAB)
            .getFamily();
    public static final BlockFamily ACACIA_WOOD = familyBuilder(Blocks.ACACIA_WOOD)
            .stairs(BlocksSD.ACACIA_WOOD_STAIRS)
            .slab(BlocksSD.ACACIA_WOOD_SLAB)
            .getFamily();
    public static final BlockFamily STRIPPED_ACACIA_WOOD = familyBuilder(Blocks.STRIPPED_ACACIA_WOOD)
            .stairs(BlocksSD.STRIPPED_ACACIA_WOOD_STAIRS)
            .slab(BlocksSD.STRIPPED_ACACIA_WOOD_SLAB)
            .getFamily();
    public static final BlockFamily DARK_OAK_WOOD = familyBuilder(Blocks.DARK_OAK_WOOD)
            .stairs(BlocksSD.DARK_OAK_WOOD_STAIRS)
            .slab(BlocksSD.DARK_OAK_WOOD_SLAB)
            .getFamily();
    public static final BlockFamily STRIPPED_DARK_OAK_WOOD = familyBuilder(Blocks.STRIPPED_DARK_OAK_WOOD)
            .stairs(BlocksSD.STRIPPED_DARK_OAK_WOOD_STAIRS)
            .slab(BlocksSD.STRIPPED_DARK_OAK_WOOD_SLAB)
            .getFamily();
    public static final BlockFamily MANGROVE_WOOD = familyBuilder(Blocks.MANGROVE_WOOD)
            .stairs(BlocksSD.MANGROVE_WOOD_STAIRS)
            .slab(BlocksSD.MANGROVE_WOOD_SLAB)
            .getFamily();
    public static final BlockFamily STRIPPED_MANGROVE_WOOD = familyBuilder(Blocks.STRIPPED_MANGROVE_WOOD)
            .stairs(BlocksSD.STRIPPED_MANGROVE_WOOD_STAIRS)
            .slab(BlocksSD.STRIPPED_MANGROVE_WOOD_SLAB)
            .getFamily();
    public static final BlockFamily CHERRY_WOOD = familyBuilder(Blocks.CHERRY_WOOD)
            .stairs(BlocksSD.CHERRY_WOOD_STAIRS)
            .slab(BlocksSD.CHERRY_WOOD_SLAB)
            .getFamily();
    public static final BlockFamily STRIPPED_CHERRY_WOOD = familyBuilder(Blocks.STRIPPED_CHERRY_WOOD)
            .stairs(BlocksSD.STRIPPED_CHERRY_WOOD_STAIRS)
            .slab(BlocksSD.STRIPPED_CHERRY_WOOD_SLAB)
            .getFamily();
    public static final BlockFamily PALE_OAK_WOOD = familyBuilder(Blocks.PALE_OAK_WOOD)
            .stairs(BlocksSD.PALE_OAK_WOOD_STAIRS)
            .slab(BlocksSD.PALE_OAK_WOOD_SLAB)
            .getFamily();
    public static final BlockFamily STRIPPED_PALE_OAK_WOOD = familyBuilder(Blocks.STRIPPED_PALE_OAK_WOOD)
            .stairs(BlocksSD.STRIPPED_PALE_OAK_WOOD_STAIRS)
            .slab(BlocksSD.STRIPPED_PALE_OAK_WOOD_SLAB)
            .getFamily();
    public static final BlockFamily POPLAR_WOOD = familyBuilder(Blocks.POPLAR_WOOD)
            .stairs(BlocksSD.POPLAR_WOOD_STAIRS)
            .slab(BlocksSD.POPLAR_WOOD_SLAB)
            .getFamily();
    public static final BlockFamily STRIPPED_POPLAR_WOOD = familyBuilder(Blocks.STRIPPED_POPLAR_WOOD)
            .stairs(BlocksSD.STRIPPED_POPLAR_WOOD_STAIRS)
            .slab(BlocksSD.STRIPPED_POPLAR_WOOD_SLAB)
            .getFamily();
    public static final BlockFamily CRIMSON_HYPHAE = familyBuilder(Blocks.CRIMSON_HYPHAE)
            .stairs(BlocksSD.CRIMSON_HYPHAE_STAIRS)
            .slab(BlocksSD.CRIMSON_HYPHAE_SLAB)
            .getFamily();
    public static final BlockFamily STRIPPED_CRIMSON_HYPHAE = familyBuilder(Blocks.STRIPPED_CRIMSON_HYPHAE)
            .stairs(BlocksSD.STRIPPED_CRIMSON_HYPHAE_STAIRS)
            .slab(BlocksSD.STRIPPED_CRIMSON_HYPHAE_SLAB)
            .getFamily();
    public static final BlockFamily WARPED_HYPHAE = familyBuilder(Blocks.WARPED_HYPHAE)
            .stairs(BlocksSD.WARPED_HYPHAE_STAIRS)
            .slab(BlocksSD.WARPED_HYPHAE_SLAB)
            .getFamily();
    public static final BlockFamily STRIPPED_WARPED_HYPHAE = familyBuilder(Blocks.STRIPPED_WARPED_HYPHAE)
            .stairs(BlocksSD.STRIPPED_WARPED_HYPHAE_STAIRS)
            .slab(BlocksSD.STRIPPED_WARPED_HYPHAE_SLAB)
            .getFamily();
    public static final ColorCollection<BlockFamily> DYED_TERRACOTTA = ColorCollection.VALUES
            .map(
                    color -> familyBuilder(Blocks.DYED_TERRACOTTA.pick(color))
                            .stairs(BlocksSD.DYED_TERRACOTTA_STAIRS.pick(color))
                            .slab(BlocksSD.DYED_TERRACOTTA_SLAB.pick(color))
                            .recipeGroupPrefix("terracotta")
                            .generateStonecutterRecipe()
                            .getFamily()
            );
    public static final BlockFamily TERRACOTTA = familyBuilder(Blocks.TERRACOTTA)
            .stairs(BlocksSD.TERRACOTTA_STAIRS)
            .slab(BlocksSD.TERRACOTTA_SLAB)
            .generateStonecutterRecipe()
            .getFamily();

    public static void init() {
        DYED_TERRACOTTA.forEach(_ -> {});
    }
}
