package com.kr1s1s.subtlyd.data.tags;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.world.block.BlocksSD;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class BlockTagsSD extends FabricTagProvider.BlockTagProvider {
    public BlockTagsSD(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    public static final TagKey<Block> SNOW_BRICKS = create("snow_bricks");
    public static final TagKey<Block> SKULL_BLOCK = create("skull_block");

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        valueLookupBuilder(SNOW_BRICKS)
                .add(BlocksSD.SNOW_BRICKS)
                .add(BlocksSD.SNOW_BRICK_STAIRS)
                .add(BlocksSD.SNOW_BRICK_SLAB);
        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(BlocksSD.CHARCOAL_BLOCK)
                .addTag(SNOW_BRICKS);
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
    }


    private static TagKey<Block> create(String string) {
        return TagKey.create(Registries.BLOCK, SubtlyDungeons.resourceLocation(string));
    }
}
