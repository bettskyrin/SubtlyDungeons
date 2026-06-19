package net.meander.subtlyd.references;

import net.meander.subtlyd.util.Util;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;

public class BlockItemIdsSD {
    public static final BlockItemId UNLIT_CAMPFIRE = create("unlit_campfire");
    public static final BlockItemId UNLIT_SOUL_CAMPFIRE = create("unlit_soul_campfire");
    public static final BlockItemId SNOW_BRICKS = create("snow_bricks");
    public static final BlockItemId SNOW_BRICK_STAIRS = create("snow_brick_stairs");
    public static final BlockItemId SNOW_BRICK_SLAB = create("snow_brick_slab");
    public static final BlockItemId SNOW_BRICK_WALL = create("snow_brick_wall");
    public static final BlockItemId CHARCOAL_BLOCK = create("charcoal_block");
    public static final BlockItemId IRON_GRATE = create("iron_grate");
    public static final BlockItemId STONE_PILLAR = create("stone_pillar");
    public static final BlockItemId STONE_TILES = create("stone_tiles");
    public static final BlockItemId STONE_TILE_STAIRS = create("stone_tile_stairs");
    public static final BlockItemId STONE_TILE_SLAB = create("stone_tile_slab");
    public static final BlockItemId STONE_TILE_WALL = create("stone_tile_wall");
    public static final BlockItemId POLISHED_DRIPSTONE = create("polished_dripstone");
    public static final BlockItemId POLISHED_DRIPSTONE_STAIRS = create("polished_dripstone_stairs");
    public static final BlockItemId POLISHED_DRIPSTONE_SLAB = create("polished_dripstone_slab");
    public static final BlockItemId POLISHED_DRIPSTONE_WALL = create("polished_dripstone_wall");
    public static final BlockItemId CHISELED_POLISHED_DRIPSTONE = create("chiseled_polished_dripstone");
    public static final BlockItemId REEDS = create("reeds");
    public static final BlockItemId WARPED_OVERHANG = create("warped_overhang");
    public static final BlockItemId BASALT_SLAB = create("basalt_slab");
    public static final BlockItemId SOUL_JACK_O_LANTERN = create("soul_jack_o_lantern");
    public static final BlockItemId POTION_CAULDRON = create("potion_cauldron");
    public static final BlockItemId PERSE_WILDFLOWERS = create("perse_wildflowers");

    public static BlockItemId create(final String name) {
        Identifier id = Util.identifier(name);

        return BlockItemId.create(id, id);
    }
}
