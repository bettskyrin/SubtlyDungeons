package net.meander.subtlyd.data.tags;

import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.block.BlocksSD;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BlockTagsSD extends FabricTagsProvider.BlockTagsProvider {
    public BlockTagsSD(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    public static final TagKey<Block> SNOW_BRICKS = bind("snow_bricks");
    public static final TagKey<Block> SKULL_BLOCK = bind("skull_block");
    public static final TagKey<Block> STONE_TILES = bind("stone_tiles");
    public static final TagKey<Block> DRIPSTONE = bind("dripstone");
    public static final TagKey<Block> TRIGGERS_AMBIENT_WIND_BLOCK_SOUNDS = bind("triggers_ambient_wind_block_sounds");

    @Override protected void addTags(HolderLookup.Provider registries) {
        valueLookupBuilder(SNOW_BRICKS)
                .add(BlocksSD.SNOW_BRICKS)
                .add(BlocksSD.SNOW_BRICK_STAIRS)
                .add(BlocksSD.SNOW_BRICK_SLAB)
                .add(BlocksSD.SNOW_BRICK_WALL);
        valueLookupBuilder(STONE_TILES)
                .add(BlocksSD.STONE_TILES)
                .add(BlocksSD.STONE_TILE_STAIRS)
                .add(BlocksSD.STONE_TILE_SLAB)
                .add(BlocksSD.STONE_TILE_WALL);
        valueLookupBuilder(DRIPSTONE)
                .add(BlocksSD.CHISELED_POLISHED_DRIPSTONE)
                .add(BlocksSD.POLISHED_DRIPSTONE)
                .add(BlocksSD.POLISHED_DRIPSTONE_STAIRS)
                .add(BlocksSD.POLISHED_DRIPSTONE_SLAB)
                .add(BlocksSD.POLISHED_DRIPSTONE_WALL);
        valueLookupBuilder(BlockTags.WALLS)
                .add(BlocksSD.SNOW_BRICK_WALL)
                .add(BlocksSD.POLISHED_DRIPSTONE_WALL)
                .add(BlocksSD.STONE_TILE_WALL);
        valueLookupBuilder(BlockTags.STAIRS)
                .add(BlocksSD.SNOW_BRICK_STAIRS)
                .add(BlocksSD.POLISHED_DRIPSTONE_STAIRS)
                .add(BlocksSD.STONE_TILE_STAIRS);
        valueLookupBuilder(BlockTags.SLABS)
                .add(BlocksSD.SNOW_BRICK_SLAB)
                .add(BlocksSD.POLISHED_DRIPSTONE_SLAB)
                .add(BlocksSD.STONE_TILE_SLAB);
        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(SNOW_BRICKS)
                .addTag(STONE_TILES)
                .addTag(DRIPSTONE)
                .add(BlocksSD.STONE_PILLAR)
                .add(BlocksSD.CHARCOAL_BLOCK)
                .add(BlocksSD.IRON_GRATE);
        valueLookupBuilder(SKULL_BLOCK)
                .add(Blocks.SKELETON_SKULL)
                .add(Blocks.SKELETON_WALL_SKULL)
                .add(Blocks.CREEPER_HEAD)
                .add(Blocks.CREEPER_WALL_HEAD)
                .add(Blocks.DRAGON_HEAD)
                .add(Blocks.DRAGON_WALL_HEAD)
                .add(Blocks.ZOMBIE_HEAD)
                .add(Blocks.ZOMBIE_WALL_HEAD)
                .add(Blocks.WITHER_SKELETON_SKULL)
                .add(Blocks.WITHER_SKELETON_WALL_SKULL)
                .add(Blocks.PLAYER_HEAD)
                .add(Blocks.PLAYER_WALL_HEAD)
                .add(Blocks.PIGLIN_HEAD)
                .add(Blocks.PIGLIN_WALL_HEAD);
        valueLookupBuilder(BlockTags.MINEABLE_WITH_AXE)
                .addTag(SKULL_BLOCK);
        valueLookupBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(BlocksSD.IRON_GRATE);
        valueLookupBuilder(TRIGGERS_AMBIENT_WIND_BLOCK_SOUNDS)
                .add(Blocks.SNOW, Blocks.SNOW_BLOCK, Blocks.POWDER_SNOW)
                .add(Blocks.ICE, Blocks.BLUE_ICE, Blocks.PACKED_ICE, Blocks.FROSTED_ICE)
                .add(Blocks.STONE)
                .add(Blocks.CALCITE);
        valueLookupBuilder(BlockTags.REPLACEABLE_BY_MUSHROOMS)
                .add(BlocksSD.REEDS);
        valueLookupBuilder(BlockTags.UNDERWATER_BONEMEALS)
                .add(BlocksSD.REEDS);
        valueLookupBuilder(BlockTags.ENCHANTMENT_POWER_TRANSMITTER)
                .forceAddTag(BlockTags.WOOL_CARPETS)
                .forceAddTag(BlockTags.CANDLES)
                .forceAddTag(BlockTags.FLOWER_POTS)
                .forceAddTag(BlockTags.CAN_GLIDE_THROUGH)
                .forceAddTag(BlockTags.CHAINS)
                .add(Blocks.CAULDRON, Blocks.BREWING_STAND)
                .add(Blocks.CHISELED_BOOKSHELF)
                .add(Blocks.MOSS_CARPET, Blocks.PALE_MOSS_CARPET);
        valueLookupBuilder(BlockTags.SUPPORTS_WARPED_ROOTS)
                .add(Blocks.WARPED_WART_BLOCK);
        valueLookupBuilder(BlockTags.OVERRIDES_MUSHROOM_LIGHT_REQUIREMENT)
                .forceAddTag(BlockTags.OVERWORLD_NATURAL_LOGS);
    }

    private static TagKey<Block> bind(String string) {
        return TagKey.create(Registries.BLOCK, Util.identifier(string));
    }

    /**
     * Can be used to get a list of blocks by their block tag. Cannot be used within data generator classes.
     * @param tag The specified tag to search.
     * @return A list of blocks with the specified block tag.
     */
    public static List<Block> getBlocks(TagKey<Block> tag) {
        Iterable<Holder<Block>> holders = BuiltInRegistries.BLOCK.getTagOrEmpty(tag);
        List<Block> blocks = new java.util.ArrayList<>(List.of());
        for  (Holder<Block> holder : holders) {
            blocks.add(holder.value());
        }
        return blocks;
    }
}
