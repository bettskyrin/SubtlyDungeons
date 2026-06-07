package net.meander.subtlyd.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.meander.subtlyd.references.BlockItemIdsSD;
import net.meander.subtlyd.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockIds;
import net.minecraft.references.BlockItemIds;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BlockTagsSD extends FabricTagsProvider.BlockTagsProvider {
    public static final TagKey<Block> SNOW_BRICKS = bind("snow_bricks");
    public static final TagKey<Block> SKULL_BLOCK = bind("skull_block");
    public static final TagKey<Block> STONE_TILES = bind("stone_tiles");
    public static final TagKey<Block> DRIPSTONE = bind("dripstone");
    public static final TagKey<Block> TRIGGERS_AMBIENT_WIND_BLOCK_SOUNDS = bind("triggers_ambient_wind_block_sounds");
    public static final TagKey<Block> TRIGGERS_AMBIENT_BUSH_BLOCK_SOUNDS = bind("triggers_ambient_bush_block_sounds");

    public BlockTagsSD(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override protected void addTags(HolderLookup.Provider registries) {
        tag(SNOW_BRICKS)
                .add(BlockItemIdsSD.SNOW_BRICKS.block())
                .add(BlockItemIdsSD.SNOW_BRICK_STAIRS.block())
                .add(BlockItemIdsSD.SNOW_BRICK_SLAB.block())
                .add(BlockItemIdsSD.SNOW_BRICK_WALL.block());
        tag(STONE_TILES)
                .add(BlockItemIdsSD.STONE_TILES.block())
                .add(BlockItemIdsSD.STONE_TILE_STAIRS.block())
                .add(BlockItemIdsSD.STONE_TILE_SLAB.block())
                .add(BlockItemIdsSD.STONE_TILE_WALL.block());
        tag(DRIPSTONE)
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
                .add(BlockItemIdsSD.STONE_TILE_SLAB.block());
        tag(SKULL_BLOCK)
                .add(BlockItemIds.SKELETON_SKULL.block())
                .add(BlockIds.SKELETON_WALL_SKULL)
                .add(BlockItemIds.CREEPER_HEAD.block())
                .add(BlockIds.CREEPER_WALL_HEAD)
                .add(BlockItemIds.DRAGON_HEAD.block())
                .add(BlockIds.DRAGON_WALL_HEAD)
                .add(BlockItemIds.ZOMBIE_HEAD.block())
                .add(BlockIds.ZOMBIE_WALL_HEAD)
                .add(BlockItemIds.WITHER_SKELETON_SKULL.block())
                .add(BlockIds.WITHER_SKELETON_WALL_SKULL)
                .add(BlockItemIds.PLAYER_HEAD.block())
                .add(BlockIds.PLAYER_WALL_HEAD)
                .add(BlockItemIds.PIGLIN_HEAD.block())
                .add(BlockIds.PIGLIN_WALL_HEAD);
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(SNOW_BRICKS)
                .addTag(STONE_TILES)
                .addTag(DRIPSTONE)
                .add(BlockItemIdsSD.STONE_PILLAR.block())
                .add(BlockItemIdsSD.CHARCOAL_BLOCK.block())
                .add(BlockItemIdsSD.IRON_GRATE.block())
                .add(BlockItemIdsSD.BASALT_SLAB.block());
        tag(BlockTags.MINEABLE_WITH_AXE)
                .addTag(SKULL_BLOCK);
        tag(BlockTags.SHEARS_EXTREME_BREAKING_SPEED)
                .add(BlockItemIdsSD.WARPED_OVERHANG.block());
        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(BlockItemIdsSD.IRON_GRATE.block());
        tag(TRIGGERS_AMBIENT_WIND_BLOCK_SOUNDS)
                .add(BlockItemIds.SNOW.block())
                .add(BlockItemIds.SNOW_BLOCK.block())
                .add(BlockItemIds.POWDER_SNOW.block())
                .add(BlockItemIds.ICE.block())
                .add(BlockItemIds.BLUE_ICE.block())
                .add(BlockItemIds.PACKED_ICE.block())
                .add(BlockIds.FROSTED_ICE)
                .add(BlockItemIds.STONE.block())
                .add(BlockItemIds.CALCITE.block());
        tag(TRIGGERS_AMBIENT_BUSH_BLOCK_SOUNDS)
                .add(BlockItemIds.BUSH.block());
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
