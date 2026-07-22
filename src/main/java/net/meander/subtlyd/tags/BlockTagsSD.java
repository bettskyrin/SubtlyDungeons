package net.meander.subtlyd.tags;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

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

    private static TagKey<Block> create(String string) {
        return TagKey.create(Registries.BLOCK, UtilSD.identifier(string));
    }

    /**
     * Can be used to get a list of blocks by their block tag. Cannot be used within data generator classes.
     * @param tag The specified tag to search.
     * @return A list of blocks with the specified block tag.
     */
    public static List<Block> getBlocks(TagKey<Block> tag) {
        Iterable<Holder<Block>> holders = BuiltInRegistries.BLOCK.getTagOrEmpty(tag);
        List<Block> blocks = new ArrayList<>(List.of());

        for  (Holder<Block> holder : holders) {
            blocks.add(holder.value());
        }
        return blocks;
    }
}
