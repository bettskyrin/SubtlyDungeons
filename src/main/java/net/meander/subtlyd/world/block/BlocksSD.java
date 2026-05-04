package net.meander.subtlyd.world.block;

import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.level.block.sounds.SoundTypeSD;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;

public class BlocksSD {
    public static final Block SNOW_BRICKS = register("snow_bricks", BlockBehaviour.Properties.of()
            .mapColor(MapColor.SNOW)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .strength(1.0F, 0.5F)
            .sound(SoundTypeSD.SNOW_BRICKS));
    public static final Block SNOW_BRICK_STAIRS = registerStair("snow_brick_stairs", SNOW_BRICKS);
    public static final Block SNOW_BRICK_SLAB = registerSlab("snow_brick_slab", SNOW_BRICKS);
    public static final Block SNOW_BRICK_WALL = registerWall("snow_brick_wall", SNOW_BRICKS);
    public static final Block CHARCOAL_BLOCK = register("charcoal_block", BlockBehaviour.Properties.ofFullCopy(Blocks.COAL_BLOCK));
    public static final Block IRON_GRATE = register("iron_grate", IronGrateBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops().strength(5.0F, 6.0F)
            .sound(SoundType.IRON)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never));
    public static final Block STONE_PILLAR = register("stone_pillar", RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE));
    public static final Block STONE_TILES = register("stone_tiles", BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE));
    public static final Block STONE_TILE_STAIRS = registerStair("stone_tile_stairs", STONE_TILES);
    public static final Block STONE_TILE_SLAB = registerSlab("stone_tile_slab", STONE_TILES);
    public static final Block STONE_TILE_WALL = registerWall("stone_tile_wall", STONE_TILES);
    public static final Block POLISHED_DRIPSTONE = register("polished_dripstone", BlockBehaviour.Properties.ofFullCopy(Blocks.DRIPSTONE_BLOCK));
    public static final Block POLISHED_DRIPSTONE_STAIRS = registerStair("polished_dripstone_stairs", POLISHED_DRIPSTONE);
    public static final Block POLISHED_DRIPSTONE_SLAB = registerSlab("polished_dripstone_slab", POLISHED_DRIPSTONE);
    public static final Block POLISHED_DRIPSTONE_WALL = registerWall("polished_dripstone_wall", POLISHED_DRIPSTONE);
    public static final Block CHISELED_POLISHED_DRIPSTONE = register("chiseled_polished_dripstone", BlockBehaviour.Properties.ofFullCopy(POLISHED_DRIPSTONE));
    public static final Block REEDS = register("reeds", ReedsBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.WATER)
            .replaceable()
            .noCollision()
            .instabreak()
            .sound(SoundType.WET_GRASS)
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .pushReaction(PushReaction.DESTROY));
    public static final Block WARPED_OVERHANG = register("warped_overhang", WarpedOverhangBlock::new , BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_WART_BLOCK)
            .noCollision()
            .noOcclusion()
            .instabreak()
            .pushReaction(PushReaction.DESTROY)
            .replaceable()
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never));
    public static final Block BASALT_SLAB = registerSlab("basalt_slab", Blocks.BASALT);

    public static void registration() { }
    private static ResourceKey<Block> resourceKey(String name) {
        return ResourceKey.create(Registries.BLOCK, Util.identifier(name));
    }

    private static Block register(String string, BlockBehaviour.Properties properties) {
        return Blocks.register(resourceKey(string), properties);
    }

    public static Block register(String string, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties properties) {
        return Blocks.register(resourceKey(string), function, properties);
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
