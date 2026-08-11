package net.meander.subtlyd.world.level.block;

import net.meander.subtlyd.core.cauldron.CauldronInteractionsSD;
import net.meander.subtlyd.references.BlockItemIdsSD;
import net.meander.subtlyd.util.UtilSD;
import net.meander.subtlyd.world.level.block.sounds.SoundTypeSD;
import net.minecraft.references.BlockItemId;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

/**
 * @see Blocks
 */
public class BlocksSD {
    public static final Block SNOW_BRICKS = Blocks.register(BlockItemIdsSD.SNOW_BRICKS, BlockBehaviour.Properties.of()
            .mapColor(MapColor.SNOW)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .strength(1.0F, 0.5F)
            .sound(SoundTypeSD.SNOW_BRICKS)
    );
    public static final Block SNOW_BRICK_STAIRS = registerStair(BlockItemIdsSD.SNOW_BRICK_STAIRS, SNOW_BRICKS);
    public static final Block SNOW_BRICK_SLAB = registerSlab(BlockItemIdsSD.SNOW_BRICK_SLAB, SNOW_BRICKS);
    public static final Block SNOW_BRICK_WALL = registerWall(BlockItemIdsSD.SNOW_BRICK_WALL, SNOW_BRICKS);
    public static final Block CHARCOAL_BLOCK = Blocks.register(BlockItemIdsSD.CHARCOAL_BLOCK, BlockBehaviour.Properties.ofFullCopy(Blocks.COAL_BLOCK));
    public static final Block IRON_GRATE = Blocks.register(BlockItemIdsSD.IRON_GRATE, IronGrateBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops()
            .strength(5.0F, 6.0F)
            .sound(SoundType.IRON)
            .noOcclusion()
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
    );
    public static final Block STONE_PILLAR = Blocks.register(BlockItemIdsSD.STONE_PILLAR, RotatedPillarBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE));
    public static final Block STONE_TILES = Blocks.register(BlockItemIdsSD.STONE_TILES, BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE));
    public static final Block STONE_TILE_STAIRS = registerStair(BlockItemIdsSD.STONE_TILE_STAIRS, STONE_TILES);
    public static final Block STONE_TILE_SLAB = registerSlab(BlockItemIdsSD.STONE_TILE_SLAB, STONE_TILES);
    public static final Block STONE_TILE_WALL = registerWall(BlockItemIdsSD.STONE_TILE_WALL, STONE_TILES);
    public static final Block POLISHED_DRIPSTONE = Blocks.register(BlockItemIdsSD.POLISHED_DRIPSTONE, BlockBehaviour.Properties.ofFullCopy(Blocks.DRIPSTONE_BLOCK));
    public static final Block POLISHED_DRIPSTONE_STAIRS = registerStair(BlockItemIdsSD.POLISHED_DRIPSTONE_STAIRS, POLISHED_DRIPSTONE);
    public static final Block POLISHED_DRIPSTONE_SLAB = registerSlab(BlockItemIdsSD.POLISHED_DRIPSTONE_SLAB, POLISHED_DRIPSTONE);
    public static final Block POLISHED_DRIPSTONE_WALL = registerWall(BlockItemIdsSD.POLISHED_DRIPSTONE_WALL, POLISHED_DRIPSTONE);
    public static final Block CHISELED_POLISHED_DRIPSTONE = Blocks.register(BlockItemIdsSD.CHISELED_POLISHED_DRIPSTONE, BlockBehaviour.Properties.ofFullCopy(POLISHED_DRIPSTONE));
    public static final Block REEDS = Blocks.register(BlockItemIdsSD.REEDS, ReedsBlock::new, BlockBehaviour.Properties.of()
            .mapColor(MapColor.WATER)
            .replaceable()
            .noCollision()
            .instabreak()
            .sound(SoundType.WET_GRASS)
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .pushReaction(PushReaction.POPPED)
    );
    public static final Block WARPED_OVERHANG = Blocks.register(BlockItemIdsSD.WARPED_OVERHANG, WarpedOverhangBlock::new , BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_WART_BLOCK)
            .noCollision()
            .noOcclusion()
            .instabreak()
            .pushReaction(PushReaction.POPPED)
            .replaceable()
            .isValidSpawn(Blocks::never)
            .isRedstoneConductor(Blocks::never)
            .isSuffocating(Blocks::never)
    );
    public static final Block BASALT_SLAB = registerSlab(BlockItemIdsSD.BASALT_SLAB, Blocks.BASALT);
    public static final Block SOUL_JACK_O_LANTERN = Blocks.register(BlockItemIdsSD.SOUL_JACK_O_LANTERN, CarvedPumpkinBlock::new,
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE)
                    .strength(1.0F)
                    .sound(SoundType.WOOD)
                    .lightLevel(_ -> 10)
                    .isValidSpawn(Blocks::always)
                    .pushReaction(PushReaction.POPPED)
    );
    public static final Block POTION_CAULDRON = Blocks.register(BlockItemIdsSD.POTION_CAULDRON, p -> new PotionCauldronBlock(p, CauldronInteractionsSD.POTION), BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON));
    public static final Block PERSE_WILDFLOWERS = Blocks.register(BlockItemIdsSD.PERSE_WILDFLOWERS, p -> new FlowerBedBlock(p, 3), BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT)
            .noCollision()
            .sound(SoundType.PINK_PETALS)
            .pushReaction(PushReaction.POPPED)
    );
    public static final Block STEW_CAULDRON = Blocks.register(BlockItemIdsSD.STEW_CAULDRON, StewCauldronBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON));
    public static final Block OAK_WOOD_STAIRS = registerStair(BlockItemIdsSD.OAK_WOOD_STAIRS, Blocks.OAK_WOOD);
    public static final Block SPRUCE_WOOD_STAIRS = registerStair(BlockItemIdsSD.SPRUCE_WOOD_STAIRS, Blocks.SPRUCE_WOOD);
    public static final Block BIRCH_WOOD_STAIRS = registerStair(BlockItemIdsSD.BIRCH_WOOD_STAIRS, Blocks.BIRCH_WOOD);
    public static final Block JUNGLE_WOOD_STAIRS = registerStair(BlockItemIdsSD.JUNGLE_WOOD_STAIRS, Blocks.JUNGLE_WOOD);
    public static final Block ACACIA_WOOD_STAIRS = registerStair(BlockItemIdsSD.ACACIA_WOOD_STAIRS, Blocks.ACACIA_WOOD);
    public static final Block DARK_OAK_WOOD_STAIRS = registerStair(BlockItemIdsSD.DARK_OAK_WOOD_STAIRS, Blocks.DARK_OAK_WOOD);
    public static final Block MANGROVE_WOOD_STAIRS = registerStair(BlockItemIdsSD.MANGROVE_WOOD_STAIRS, Blocks.MANGROVE_WOOD);
    public static final Block POPLAR_WOOD_STAIRS = registerStair(BlockItemIdsSD.POPLAR_WOOD_STAIRS, Blocks.POPLAR_WOOD);
    public static final Block CHERRY_WOOD_STAIRS = registerStair(BlockItemIdsSD.CHERRY_WOOD_STAIRS, Blocks.CHERRY_WOOD);
    public static final Block PALE_OAK_WOOD_STAIRS = registerStair(BlockItemIdsSD.PALE_OAK_WOOD_STAIRS, Blocks.PALE_OAK_WOOD);
    public static final Block CRIMSON_HYPHAE_STAIRS = registerStair(BlockItemIdsSD.CRIMSON_HYPHAE_STAIRS, Blocks.CRIMSON_HYPHAE);
    public static final Block WARPED_HYPHAE_STAIRS = registerStair(BlockItemIdsSD.WARPED_HYPHAE_STAIRS, Blocks.WARPED_HYPHAE);
    public static final Block OAK_WOOD_SLAB = registerSlab(BlockItemIdsSD.OAK_WOOD_SLAB, Blocks.OAK_WOOD);
    public static final Block SPRUCE_WOOD_SLAB = registerSlab(BlockItemIdsSD.SPRUCE_WOOD_SLAB, Blocks.SPRUCE_WOOD);
    public static final Block BIRCH_WOOD_SLAB = registerSlab(BlockItemIdsSD.BIRCH_WOOD_SLAB, Blocks.BIRCH_WOOD);
    public static final Block JUNGLE_WOOD_SLAB = registerSlab(BlockItemIdsSD.JUNGLE_WOOD_SLAB, Blocks.JUNGLE_WOOD);
    public static final Block ACACIA_WOOD_SLAB = registerSlab(BlockItemIdsSD.ACACIA_WOOD_SLAB, Blocks.ACACIA_WOOD);
    public static final Block DARK_OAK_WOOD_SLAB = registerSlab(BlockItemIdsSD.DARK_OAK_WOOD_SLAB, Blocks.DARK_OAK_WOOD);
    public static final Block MANGROVE_WOOD_SLAB = registerSlab(BlockItemIdsSD.MANGROVE_WOOD_SLAB, Blocks.MANGROVE_WOOD);
    public static final Block POPLAR_WOOD_SLAB = registerSlab(BlockItemIdsSD.POPLAR_WOOD_SLAB, Blocks.POPLAR_WOOD);
    public static final Block CHERRY_WOOD_SLAB = registerSlab(BlockItemIdsSD.CHERRY_WOOD_SLAB, Blocks.CHERRY_WOOD);
    public static final Block PALE_OAK_WOOD_SLAB = registerSlab(BlockItemIdsSD.PALE_OAK_WOOD_SLAB, Blocks.PALE_OAK_WOOD);
    public static final Block CRIMSON_HYPHAE_SLAB = registerSlab(BlockItemIdsSD.CRIMSON_HYPHAE_SLAB, Blocks.CRIMSON_HYPHAE);
    public static final Block WARPED_HYPHAE_SLAB = registerSlab(BlockItemIdsSD.WARPED_HYPHAE_SLAB, Blocks.WARPED_HYPHAE);
    public static final Block STRIPPED_OAK_WOOD_STAIRS = registerStair(BlockItemIdsSD.STRIPPED_OAK_WOOD_STAIRS, Blocks.STRIPPED_OAK_WOOD);
    public static final Block STRIPPED_SPRUCE_WOOD_STAIRS = registerStair(BlockItemIdsSD.STRIPPED_SPRUCE_WOOD_STAIRS, Blocks.STRIPPED_SPRUCE_WOOD);
    public static final Block STRIPPED_BIRCH_WOOD_STAIRS = registerStair(BlockItemIdsSD.STRIPPED_BIRCH_WOOD_STAIRS, Blocks.STRIPPED_BIRCH_WOOD);
    public static final Block STRIPPED_JUNGLE_WOOD_STAIRS = registerStair(BlockItemIdsSD.STRIPPED_JUNGLE_WOOD_STAIRS, Blocks.STRIPPED_JUNGLE_WOOD);
    public static final Block STRIPPED_ACACIA_WOOD_STAIRS = registerStair(BlockItemIdsSD.STRIPPED_ACACIA_WOOD_STAIRS, Blocks.STRIPPED_ACACIA_WOOD);
    public static final Block STRIPPED_DARK_OAK_WOOD_STAIRS = registerStair(BlockItemIdsSD.STRIPPED_DARK_OAK_WOOD_STAIRS, Blocks.STRIPPED_DARK_OAK_WOOD);
    public static final Block STRIPPED_MANGROVE_WOOD_STAIRS = registerStair(BlockItemIdsSD.STRIPPED_MANGROVE_WOOD_STAIRS, Blocks.STRIPPED_MANGROVE_WOOD);
    public static final Block STRIPPED_POPLAR_WOOD_STAIRS = registerStair(BlockItemIdsSD.STRIPPED_POPLAR_WOOD_STAIRS, Blocks.STRIPPED_POPLAR_WOOD);
    public static final Block STRIPPED_CHERRY_WOOD_STAIRS = registerStair(BlockItemIdsSD.STRIPPED_CHERRY_WOOD_STAIRS, Blocks.STRIPPED_CHERRY_WOOD);
    public static final Block STRIPPED_PALE_OAK_WOOD_STAIRS = registerStair(BlockItemIdsSD.STRIPPED_PALE_OAK_WOOD_STAIRS, Blocks.STRIPPED_PALE_OAK_WOOD);
    public static final Block STRIPPED_CRIMSON_HYPHAE_STAIRS = registerStair(BlockItemIdsSD.STRIPPED_CRIMSON_HYPHAE_STAIRS, Blocks.STRIPPED_CRIMSON_HYPHAE);
    public static final Block STRIPPED_WARPED_HYPHAE_STAIRS = registerStair(BlockItemIdsSD.STRIPPED_WARPED_HYPHAE_STAIRS, Blocks.STRIPPED_WARPED_HYPHAE);
    public static final Block STRIPPED_OAK_WOOD_SLAB = registerSlab(BlockItemIdsSD.STRIPPED_OAK_WOOD_SLAB, Blocks.STRIPPED_OAK_WOOD);
    public static final Block STRIPPED_SPRUCE_WOOD_SLAB = registerSlab(BlockItemIdsSD.STRIPPED_SPRUCE_WOOD_SLAB, Blocks.STRIPPED_SPRUCE_WOOD);
    public static final Block STRIPPED_BIRCH_WOOD_SLAB = registerSlab(BlockItemIdsSD.STRIPPED_BIRCH_WOOD_SLAB, Blocks.STRIPPED_BIRCH_WOOD);
    public static final Block STRIPPED_JUNGLE_WOOD_SLAB = registerSlab(BlockItemIdsSD.STRIPPED_JUNGLE_WOOD_SLAB, Blocks.STRIPPED_JUNGLE_WOOD);
    public static final Block STRIPPED_ACACIA_WOOD_SLAB = registerSlab(BlockItemIdsSD.STRIPPED_ACACIA_WOOD_SLAB, Blocks.STRIPPED_ACACIA_WOOD);
    public static final Block STRIPPED_DARK_OAK_WOOD_SLAB = registerSlab(BlockItemIdsSD.STRIPPED_DARK_OAK_WOOD_SLAB, Blocks.STRIPPED_DARK_OAK_WOOD);
    public static final Block STRIPPED_MANGROVE_WOOD_SLAB = registerSlab(BlockItemIdsSD.STRIPPED_MANGROVE_WOOD_SLAB, Blocks.STRIPPED_MANGROVE_WOOD);
    public static final Block STRIPPED_POPLAR_WOOD_SLAB = registerSlab(BlockItemIdsSD.STRIPPED_POPLAR_WOOD_SLAB, Blocks.STRIPPED_POPLAR_WOOD);
    public static final Block STRIPPED_CHERRY_WOOD_SLAB = registerSlab(BlockItemIdsSD.STRIPPED_CHERRY_WOOD_SLAB, Blocks.STRIPPED_CHERRY_WOOD);
    public static final Block STRIPPED_PALE_OAK_WOOD_SLAB = registerSlab(BlockItemIdsSD.STRIPPED_PALE_OAK_WOOD_SLAB, Blocks.STRIPPED_PALE_OAK_WOOD);
    public static final Block STRIPPED_CRIMSON_HYPHAE_SLAB = registerSlab(BlockItemIdsSD.STRIPPED_CRIMSON_HYPHAE_SLAB, Blocks.STRIPPED_CRIMSON_HYPHAE);
    public static final Block STRIPPED_WARPED_HYPHAE_SLAB = registerSlab(BlockItemIdsSD.STRIPPED_WARPED_HYPHAE_SLAB, Blocks.STRIPPED_WARPED_HYPHAE);
    public static final Block TERRACOTTA_STAIRS = registerStair(BlockItemIdsSD.TERRACOTTA_STAIRS, Blocks.TERRACOTTA);
    public static final Block TERRACOTTA_SLAB = registerSlab(BlockItemIdsSD.TERRACOTTA_SLAB, Blocks.TERRACOTTA);
    public static final ColorCollection<Block> DYED_TERRACOTTA_STAIRS = ColorCollection.zipMap(
            ColorCollection.VALUES, BlockItemIdsSD.DYED_TERRACOTTA_STAIRS, (color, id) -> registerStair(id, Blocks.DYED_TERRACOTTA.pick(color))
    );
    public static final ColorCollection<Block> DYED_TERRACOTTA_SLAB = ColorCollection.zipMap(
            ColorCollection.VALUES, BlockItemIdsSD.DYED_TERRACOTTA_SLAB, (color, id) -> registerSlab(id, Blocks.DYED_TERRACOTTA.pick(color))
    );

    public static void registration() {
        UtilSD.LOGGER.debug("Registering blocks...");
        BlockSD.registerEvents();
    }

    private static Block registerStair(final BlockItemId id, final Block baseBlock) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(baseBlock).mapColor(baseBlock.defaultMapColor());

        return Blocks.register(id, p -> new StairBlock(baseBlock.defaultBlockState(), p), properties);
    }

    private static Block registerSlab(final BlockItemId id, final Block baseBlock) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(baseBlock).mapColor(baseBlock.defaultMapColor());

        return Blocks.register(id, SlabBlock::new, properties);
    }

    private static Block registerWall(final BlockItemId id, final Block baseBlock) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(baseBlock).mapColor(baseBlock.defaultMapColor());

        return Blocks.register(id, WallBlock::new, properties);
    }
}
