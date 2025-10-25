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

import java.util.concurrent.CompletableFuture;

public class BlockTagsSD extends FabricTagProvider.BlockTagProvider {
    public BlockTagsSD(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    public static final TagKey<Block> SNOW_BRICKS = create("snow_bricks");

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        valueLookupBuilder(SNOW_BRICKS)
                .add(BlocksSD.SNOW_BRICKS)
                .add(BlocksSD.SNOW_BRICK_STAIRS)
                .add(BlocksSD.SNOW_BRICK_SLAB);
        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(SNOW_BRICKS);
    }


    private static TagKey<Block> create(String string) {
        return TagKey.create(Registries.BLOCK, SubtlyDungeons.resourceLocation(string));
    }
}
