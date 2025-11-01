package com.kr1s1s.subtlyd.world.block;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.world.level.block.sounds.SoundTypeSD;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class BlocksSD {
    public static final Block SNOW_BRICKS = Blocks.register(resourceKey("snow_bricks"), BlockBehaviour.Properties.of()
            .mapColor(MapColor.SNOW)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .strength(1.0F, 0.5F)
            .sound(SoundTypeSD.SNOW_BRICKS));
    public static final Block SNOW_BRICK_STAIRS = registerStair("snow_brick_stairs", SNOW_BRICKS);
    public static final Block SNOW_BRICK_SLAB = registerSlab("snow_brick_slab", SNOW_BRICKS);
    public static final Block SNOW_BRICK_WALL = registerWall("snow_brick_wall", SNOW_BRICKS);
    public static final Block CHARCOAL_BLOCK = Blocks.register(resourceKey("charcoal_block"), BlockBehaviour.Properties.ofFullCopy(Blocks.COAL_BLOCK));
    public static final Block IRON_GRATE = Blocks.register(resourceKey("iron_grate"), IronGrateBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops().strength(5.0F, 6.0F)
            .sound(SoundType.IRON)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never));
    public static final Block CHISELED_STONE = Blocks.register(resourceKey("chiseled_stone"), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE));
    public static final Block STONE_PILLAR = Blocks.register(resourceKey("stone_pillar"), RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE));
    public static final Block POLISHED_STONE = Blocks.register(resourceKey("polished_stone"), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE));
    public static final Block POLISHED_STONE_STAIRS = registerStair("polished_stone_stairs", POLISHED_STONE);
    public static final Block POLISHED_STONE_SLAB = registerSlab("polished_stone_slab", POLISHED_STONE);
    public static final Block POLISHED_STONE_WALL = registerWall("polished_stone_wall", POLISHED_STONE);
    public static final Block STONE_TILES = Blocks.register(resourceKey("stone_tiles"), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE));
    public static final Block STONE_TILE_STAIRS = registerStair("stone_tile_stairs", STONE_TILES);
    public static final Block STONE_TILE_SLAB = registerSlab("stone_tile_slab", STONE_TILES);
    public static final Block STONE_TILE_WALL = registerWall("stone_tile_wall", STONE_TILES);
    public static final Block CHISELED_DRIPSTONE = Blocks.register(resourceKey("chiseled_dripstone"), BlockBehaviour.Properties.ofFullCopy(Blocks.DRIPSTONE_BLOCK));
    public static final Block POLISHED_DRIPSTONE = Blocks.register(resourceKey("polished_dripstone"), BlockBehaviour.Properties.ofFullCopy(Blocks.DRIPSTONE_BLOCK));
    public static final Block POLISHED_DRIPSTONE_STAIRS = registerStair("polished_dripstone_stairs", POLISHED_DRIPSTONE);
    public static final Block POLISHED_DRIPSTONE_SLAB = registerSlab("polished_dripstone_slab", POLISHED_DRIPSTONE);
    public static final Block POLISHED_DRIPSTONE_WALL = registerWall("polished_dripstone_wall", POLISHED_DRIPSTONE);

    public static void init() { }
    private static ResourceKey<Block> resourceKey(String name) {
        return ResourceKey.create(Registries.BLOCK, SubtlyDungeons.resourceLocation(name));
    }

    private static Block registerStair(String string, Block block) {
        return Blocks.register(resourceKey(string), properties -> new StairBlock(block.defaultBlockState(), properties), BlockBehaviour.Properties.ofFullCopy(block));
    }

    private static Block registerSlab(String string, Block block) {
        return Blocks.register(resourceKey(string), SlabBlock::new, BlockBehaviour.Properties.ofFullCopy(block));
    }

    private static Block registerWall(String string, Block block) {
        return Blocks.register(resourceKey(string), WallBlock::new, BlockBehaviour.Properties.ofFullCopy(block));
    }
}
