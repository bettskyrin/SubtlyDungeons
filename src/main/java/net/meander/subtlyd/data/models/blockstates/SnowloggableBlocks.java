package net.meander.subtlyd.data.models.blockstates;

import net.meander.subtlyd.world.block.BlocksSD;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

/**
 * Tags cannot exist during model generation so a list was made.
 */
public class SnowloggableBlocks {
    public static final int MAX_LAYERS = 8;

    public static List<Block> FENCES = List.of(
            Blocks.OAK_FENCE,
            Blocks.SPRUCE_FENCE,
            Blocks.BIRCH_FENCE,
            Blocks.JUNGLE_FENCE,
            Blocks.ACACIA_FENCE,
            Blocks.DARK_OAK_FENCE,
            Blocks.MANGROVE_FENCE,
            Blocks.CHERRY_FENCE,
            Blocks.BAMBOO_FENCE,
            Blocks.CRIMSON_FENCE,
            Blocks.WARPED_FENCE,
            Blocks.NETHER_BRICK_FENCE,
            Blocks.PALE_OAK_FENCE
    );

    public static List<Block> FENCE_GATES = List.of(
            Blocks.OAK_FENCE_GATE,
            Blocks.SPRUCE_FENCE_GATE,
            Blocks.BIRCH_FENCE_GATE,
            Blocks.JUNGLE_FENCE_GATE,
            Blocks.ACACIA_FENCE_GATE,
            Blocks.DARK_OAK_FENCE_GATE,
            Blocks.MANGROVE_FENCE_GATE,
            Blocks.CHERRY_FENCE_GATE,
            Blocks.BAMBOO_FENCE_GATE,
            Blocks.CRIMSON_FENCE_GATE,
            Blocks.WARPED_FENCE_GATE,
            Blocks.PALE_OAK_FENCE_GATE
    );

    public static List<Block> WALLS = List.of(
            Blocks.COBBLESTONE_WALL,
            Blocks.MOSSY_COBBLESTONE_WALL,
            Blocks.STONE_BRICK_WALL,
            Blocks.MOSSY_STONE_BRICK_WALL,
            Blocks.GRANITE_WALL,
            Blocks.DIORITE_WALL,
            Blocks.ANDESITE_WALL,
            Blocks.COBBLED_DEEPSLATE_WALL,
            Blocks.POLISHED_DEEPSLATE_WALL,
            Blocks.DEEPSLATE_BRICK_WALL,
            Blocks.DEEPSLATE_TILE_WALL,
            Blocks.BRICK_WALL,
            Blocks.PRISMARINE_WALL,
            Blocks.RED_SANDSTONE_WALL,
            Blocks.SANDSTONE_WALL,
            Blocks.END_STONE_BRICK_WALL,
            Blocks.BLACKSTONE_WALL,
            Blocks.POLISHED_BLACKSTONE_WALL,
            Blocks.POLISHED_BLACKSTONE_BRICK_WALL,
            Blocks.CINNABAR_WALL,
            Blocks.CINNABAR_BRICK_WALL,
            Blocks.POLISHED_CINNABAR_WALL,
            Blocks.SULFUR_WALL,
            Blocks.SULFUR_BRICK_WALL,
            Blocks.POLISHED_SULFUR_WALL,
            Blocks.RESIN_BRICK_WALL,
            BlocksSD.POLISHED_DRIPSTONE_WALL,
            BlocksSD.STONE_TILE_WALL,
            BlocksSD.SNOW_BRICK_WALL
    );

    public static List<Block> SIMPLE_VEGETATION = List.of(
            Blocks.SHORT_DRY_GRASS,
            Blocks.TALL_DRY_GRASS,
            Blocks.SHORT_GRASS,
            Blocks.BUSH,
            Blocks.FIREFLY_BUSH,
            Blocks.FERN,
            Blocks.DANDELION,
            Blocks.POPPY,
            Blocks.CORNFLOWER,
            Blocks.ALLIUM,
            Blocks.AZURE_BLUET,
            Blocks.BLUE_ORCHID,
            Blocks.GOLDEN_DANDELION,
            Blocks.ORANGE_TULIP,
            Blocks.PINK_TULIP,
            Blocks.RED_TULIP,
            Blocks.WHITE_TULIP,
            Blocks.OXEYE_DAISY,
            Blocks.LILY_OF_THE_VALLEY,
            Blocks.WITHER_ROSE,
            Blocks.CLOSED_EYEBLOSSOM,
            Blocks.OPEN_EYEBLOSSOM,
            Blocks.BROWN_MUSHROOM,
            Blocks.RED_MUSHROOM,
            Blocks.CRIMSON_FUNGUS,
            Blocks.WARPED_FUNGUS,
            Blocks.CRIMSON_ROOTS,
            Blocks.WARPED_ROOTS,
            Blocks.NETHER_SPROUTS,
            Blocks.ACACIA_SAPLING,
            Blocks.BIRCH_SAPLING,
            Blocks.CHERRY_SAPLING,
            Blocks.DARK_OAK_SAPLING,
            Blocks.JUNGLE_SAPLING,
            Blocks.OAK_SAPLING,
            Blocks.PALE_OAK_SAPLING,
            Blocks.SPRUCE_SAPLING
    );

    public static List<Block> AGING_VEGETATION = List.of(
            Blocks.SWEET_BERRY_BUSH,
            Blocks.TORCHFLOWER_CROP
    );

    public static List<Block> TALL_VEGETATION = List.of(
            Blocks.TALL_GRASS,
            Blocks.LARGE_FERN
    );

    public static List<Block> SEGMENTABLE_VEGETATION = List.of(
            Blocks.LEAF_LITTER,
            Blocks.WILDFLOWERS,
            Blocks.PINK_PETALS,
            BlocksSD.PERSE_WILDFLOWERS
    );

    public static List<Block> CROSS_BLOCKS = new ArrayList<>(List.of(
            Blocks.IRON_BARS,
            Blocks.GLASS_PANE
    ));


    public static void addToList() {
        CROSS_BLOCKS.addAll(Blocks.STAINED_GLASS_PANE.asList());
    }
}
