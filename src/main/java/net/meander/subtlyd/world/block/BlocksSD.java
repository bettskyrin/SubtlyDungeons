package net.meander.subtlyd.world.block;

import net.meander.subtlyd.core.CauldronInteractionsSD;
import net.meander.subtlyd.references.BlockItemIdsSD;
import net.meander.subtlyd.world.level.block.sounds.SoundTypeSD;
import net.minecraft.references.BlockItemId;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;

public class BlocksSD {
    public static final Block SNOW_BRICKS = register(BlockItemIdsSD.SNOW_BRICKS, BlockBehaviour.Properties.of()
            .mapColor(MapColor.SNOW)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .strength(1.0F, 0.5F)
            .sound(SoundTypeSD.SNOW_BRICKS));
    public static final Block SNOW_BRICK_STAIRS = registerStair(BlockItemIdsSD.SNOW_BRICK_STAIRS, SNOW_BRICKS);
    public static final Block SNOW_BRICK_SLAB = registerSlab(BlockItemIdsSD.SNOW_BRICK_SLAB, SNOW_BRICKS);
    public static final Block SNOW_BRICK_WALL = registerWall(BlockItemIdsSD.SNOW_BRICK_WALL, SNOW_BRICKS);
    public static final Block CHARCOAL_BLOCK = register(BlockItemIdsSD.CHARCOAL_BLOCK, BlockBehaviour.Properties.ofFullCopy(Blocks.COAL_BLOCK));
    public static final Block IRON_GRATE = register(BlockItemIdsSD.IRON_GRATE, IronGrateBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops().strength(5.0F, 6.0F)
            .sound(SoundType.IRON)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never));
    public static final Block STONE_PILLAR = register(BlockItemIdsSD.STONE_PILLAR, RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE));
    public static final Block STONE_TILES = register(BlockItemIdsSD.STONE_TILES, BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE));
    public static final Block STONE_TILE_STAIRS = registerStair(BlockItemIdsSD.STONE_TILE_STAIRS, STONE_TILES);
    public static final Block STONE_TILE_SLAB = registerSlab(BlockItemIdsSD.STONE_TILE_SLAB, STONE_TILES);
    public static final Block STONE_TILE_WALL = registerWall(BlockItemIdsSD.STONE_TILE_WALL, STONE_TILES);
    public static final Block POLISHED_DRIPSTONE = register(BlockItemIdsSD.POLISHED_DRIPSTONE, BlockBehaviour.Properties.ofFullCopy(Blocks.DRIPSTONE_BLOCK));
    public static final Block POLISHED_DRIPSTONE_STAIRS = registerStair(BlockItemIdsSD.POLISHED_DRIPSTONE_STAIRS, POLISHED_DRIPSTONE);
    public static final Block POLISHED_DRIPSTONE_SLAB = registerSlab(BlockItemIdsSD.POLISHED_DRIPSTONE_SLAB, POLISHED_DRIPSTONE);
    public static final Block POLISHED_DRIPSTONE_WALL = registerWall(BlockItemIdsSD.POLISHED_DRIPSTONE_WALL, POLISHED_DRIPSTONE);
    public static final Block CHISELED_POLISHED_DRIPSTONE = register(BlockItemIdsSD.CHISELED_POLISHED_DRIPSTONE, BlockBehaviour.Properties.ofFullCopy(POLISHED_DRIPSTONE));
    public static final Block REEDS = register(BlockItemIdsSD.REEDS, ReedsBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.WATER)
            .replaceable()
            .noCollision()
            .instabreak()
            .sound(SoundType.WET_GRASS)
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .pushReaction(PushReaction.DESTROY));
    public static final Block WARPED_OVERHANG = register(BlockItemIdsSD.WARPED_OVERHANG, WarpedOverhangBlock::new , BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_WART_BLOCK)
            .noCollision()
            .noOcclusion()
            .instabreak()
            .pushReaction(PushReaction.DESTROY)
            .replaceable()
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
            .isViewBlocking(Blocks::never));
    public static final Block BASALT_SLAB = registerSlab(BlockItemIdsSD.BASALT_SLAB, Blocks.BASALT);
    public static final Block SOUL_JACK_O_LANTERN = register(BlockItemIdsSD.SOUL_JACK_O_LANTERN, CarvedPumpkinBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).strength(1.0F).sound(SoundType.WOOD).lightLevel(_ -> 10)
                    .isValidSpawn(Blocks::always)
                    .pushReaction(PushReaction.DESTROY));
    public static final Block POTION_CAULDRON = register(BlockItemIdsSD.POTION_CAULDRON, properties -> new PotionCauldronBlock(properties, CauldronInteractionsSD.POTION), BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON));

    public static void bootstrap() {}

    private static Block register(final BlockItemId id, final BlockBehaviour.Properties properties) {
        return Blocks.register(id.block(), properties);
    }

    private static Block register(final BlockItemId id, final Function<BlockBehaviour.Properties, Block> factory, final BlockBehaviour.Properties properties) {
        return Blocks.register(id.block(), factory, properties);
    }

    private static Block registerStair(final BlockItemId id, final Block baseBlock) {
        return register(id, properties -> new StairBlock(baseBlock.defaultBlockState(), properties), BlockBehaviour.Properties.ofFullCopy(baseBlock));
    }

    private static Block registerSlab(final BlockItemId id, final Block baseBlock) {
        return register(id, SlabBlock::new, BlockBehaviour.Properties.ofFullCopy(baseBlock));
    }

    private static Block registerWall(final BlockItemId id, final Block baseBlock) {
        return register(id, WallBlock::new, BlockBehaviour.Properties.ofFullCopy(baseBlock));
    }
}
