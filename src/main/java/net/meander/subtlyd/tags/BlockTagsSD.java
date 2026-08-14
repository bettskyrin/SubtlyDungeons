package net.meander.subtlyd.tags;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/**
 * @see net.minecraft.tags.BlockTags
 */
public class BlockTagsSD {
    public static final TagKey<Block> SNOW_BRICKS = create("snow_bricks");
    public static final TagKey<Block> STONE_TILES = create("stone_tiles");
    public static final TagKey<Block> DRIPSTONE = create("dripstone");
    public static final TagKey<Block> TRIGGERS_AMBIENT_WIND_BLOCK_SOUNDS = create("triggers_ambient_wind_block_sounds");
    public static final TagKey<Block> TRIGGERS_AMBIENT_BUSH_BLOCK_SOUNDS = create("triggers_ambient_bush_block_sounds");
    public static final TagKey<Block> TRIGGERS_AMBIENT_GRASS_BLOCK_SOUNDS = create("triggers_ambient_grass_block_sounds");
    public static final TagKey<Block> TALL_PLANTS = create("tall_plants");
    public static final TagKey<Block> SILENT_FOLIAGE = create("silent_foliage");
    public static final TagKey<Block> ARROW_FLAMMABLE = create("arrow_flammable");

    private static TagKey<Block> create(String string) {
        return TagKey.create(Registries.BLOCK, UtilSD.identifier(string));
    }
}
