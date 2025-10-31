package com.kr1s1s.subtlyd.world.block;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.world.level.block.sounds.SoundTypeSD;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class BlocksSD {
    public static final Block CHARCOAL_BLOCK = Blocks.register(resourceKey("charcoal_block"), BlockBehaviour.Properties.ofFullCopy(Blocks.COAL_BLOCK));
    public static final Block SNOW_BRICKS = Blocks.register(resourceKey("snow_bricks"), BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.BASEDRUM).strength(1.0F, 0.5F).sound(SoundTypeSD.SNOW_BRICKS));
    public static final Block SNOW_BRICK_STAIRS = registerStair(resourceKey("snow_brick_stairs"), SNOW_BRICKS);
    public static final Block SNOW_BRICK_SLAB = registerSlab(resourceKey("snow_brick_slab"), SNOW_BRICKS);
    public static final Block SHORT_GRASS_BLOCK_SNOWY = Blocks.register(resourceKey("short_grass_snowy"),
            TallGrassBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.SNOW)
                    .randomTicks()
                    .strength(0.6F)
                    .sound(SoundType.GRASS)
                    .replaceable()
                    .noCollision()
                    .dynamicShape()
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY));
    public static final Block TALL_GRASS_BLOCK_SNOWY = Blocks.register(resourceKey("tall_grass_snowy"),
            TallGrassBlock::new,
            BlockBehaviour.Properties.ofFullCopy(SHORT_GRASS_BLOCK_SNOWY));

    public static void init() { }
    private static ResourceKey<Block> resourceKey(String name) {
        return ResourceKey.create(Registries.BLOCK, SubtlyDungeons.resourceLocation(name));
    }

    private static Block registerStair(ResourceKey<Block> resourceKey, Block block) {
        return Blocks.register(resourceKey, properties -> new StairBlock(block.defaultBlockState(), properties), BlockBehaviour.Properties.ofFullCopy(block));
    }

    private static Block registerSlab(ResourceKey<Block> resourceKey, Block block) {
        return Blocks.register(resourceKey, SlabBlock::new, BlockBehaviour.Properties.ofFullCopy(block));
    }
}
