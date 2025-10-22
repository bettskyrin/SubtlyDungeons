package com.kr1s1s.subtlyd.world.block;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class BlocksSD {
    public static final Block SNOW_BRICKS = Blocks.register(resourceKey("snow_bricks"), BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.0F, 0.5F).sound(SoundType.SNOW));
    public static final Block SNOW_BRICK_STAIRS = Blocks.register(resourceKey("snow_brick_stairs"), BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.0F, 0.5F).sound(SoundType.SNOW));
    public static final Block SNOW_BRICK_SLAB = Blocks.register(resourceKey("snow_brick_slab"), BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.0F, 0.5F).sound(SoundType.SNOW));

    public static void registerBlocks() {}
    private static ResourceKey<Block> resourceKey(String name) {
        return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, name));
    }
}
