package net.meander.subtlyd.client.data.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.meander.subtlyd.client.data.models.model.ModelTemplatesSD;
import net.meander.subtlyd.world.level.block.BlocksSD;
import net.meander.subtlyd.world.level.block.PotionCauldronBlock;
import net.meander.subtlyd.world.level.block.StewCauldronBlock;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * @see BlockModelGenerators
 */
@Environment(EnvType.CLIENT)
public class BlockModelGeneratorsSD {
    /**
     * Builds a slab model from a vanilla pillar block texture.
     * @param baseBlock The original block to obtain a texture from.
     * @param newBlock The custom block that the texture will be mapped to.
     */
    private static void generatePillarSlabFromVanilla(BlockModelGenerators blockModelGenerators, Block baseBlock, Block newBlock) {
        MultiVariant fullBlockModel = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(baseBlock));
        Identifier top = TextureMapping.getBlockTexture(baseBlock, "_top").sprite();
        Identifier side = TextureMapping.getBlockTexture(baseBlock, "_side").sprite();

        TextureMapping slabTextures = new TextureMapping().put(TextureSlot.BOTTOM, new Material(top)).put(TextureSlot.TOP, new Material(top)).put(TextureSlot.SIDE, new Material(side));
        MultiVariant blockBottom = BlockModelGenerators.plainVariant(ModelTemplates.SLAB_BOTTOM.create(newBlock, slabTextures, blockModelGenerators.modelOutput));
        MultiVariant blockTop = BlockModelGenerators.plainVariant(ModelTemplates.SLAB_TOP.create(newBlock, slabTextures, blockModelGenerators.modelOutput));

        blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createSlab(newBlock, blockBottom, blockTop, fullBlockModel));
    }

    private static void generateWoodSlabFromVanilla(BlockModelGenerators blockModelGenerators, Block logBlock, Block doubleSlabBlock, Block newBlock) {
        Identifier barkTexture = TextureMapping.getBlockTexture(logBlock).sprite();
        
        TextureMapping slabTextures = new TextureMapping().put(TextureSlot.BOTTOM, new Material(barkTexture)).put(TextureSlot.TOP, new Material(barkTexture)).put(TextureSlot.SIDE, new Material(barkTexture));
        MultiVariant blockBottom = BlockModelGenerators.plainVariant(ModelTemplates.SLAB_BOTTOM.create(newBlock, slabTextures, blockModelGenerators.modelOutput));
        MultiVariant blockTop = BlockModelGenerators.plainVariant(ModelTemplates.SLAB_TOP.create(newBlock, slabTextures, blockModelGenerators.modelOutput));
        MultiVariant fullBlockModel = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(doubleSlabBlock));

        blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createSlab(newBlock, blockBottom, blockTop, fullBlockModel));
    }

    private static void generateWoodStairsFromVanilla(BlockModelGenerators blockModelGenerators, Block logBlock, Block newBlock) {
        Identifier barkTexture = TextureMapping.getBlockTexture(logBlock).sprite();

        TextureMapping stairTextures = new TextureMapping().put(TextureSlot.BOTTOM, new Material(barkTexture)).put(TextureSlot.TOP, new Material(barkTexture)).put(TextureSlot.SIDE, new Material(barkTexture));
        MultiVariant blockStraight = BlockModelGenerators.plainVariant(ModelTemplates.STAIRS_STRAIGHT.create(newBlock, stairTextures, blockModelGenerators.modelOutput));
        MultiVariant blockInner = BlockModelGenerators.plainVariant(ModelTemplates.STAIRS_INNER.create(newBlock, stairTextures, blockModelGenerators.modelOutput));
        MultiVariant blockOuter = BlockModelGenerators.plainVariant(ModelTemplates.STAIRS_OUTER.create(newBlock, stairTextures, blockModelGenerators.modelOutput));

        blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createStairs(newBlock, blockInner, blockStraight, blockOuter));
    }

    /**
     * Creates an overhang block.
     * @param overhangBlock The block to map to.
     */
    private static void generateOverhangBlock(BlockModelGenerators blockModelGenerators, Block overhangBlock) {
        Identifier north = TextureMapping.getBlockTexture(overhangBlock, "_north").sprite();
        Identifier east = TextureMapping.getBlockTexture(overhangBlock, "_east").sprite();
        Identifier south = TextureMapping.getBlockTexture(overhangBlock, "_south").sprite();
        Identifier west = TextureMapping.getBlockTexture(overhangBlock, "_west").sprite();

        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.NORTH, new Material(north))
                .put(TextureSlot.EAST, new Material(east))
                .put(TextureSlot.SOUTH, new Material(south))
                .put(TextureSlot.WEST, new Material(west))
                .put(TextureSlot.PARTICLE, new Material(north));
        Identifier model = ModelTemplatesSD.OVERHANG_BLOCK.create(overhangBlock, mapping, blockModelGenerators.modelOutput);
        MultiVariant multiVariant = BlockModelGenerators.createRotatedVariants(BlockModelGenerators.plainModel(model));

        blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(overhangBlock, multiVariant));
    }

    @SuppressWarnings("DataFlowIssue")
    private static Identifier[] generateCauldronContentsModel(BlockModelGenerators blockModelGenerators, Identifier baseId, String nameSuffix, Identifier liquidTexture) {
        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.CONTENT, new Material(liquidTexture))
                .put(TextureSlot.TOP, new Material(Identifier.tryParse("minecraft:block/cauldron_top")))
                .put(TextureSlot.INSIDE, new Material(Identifier.tryParse("minecraft:block/cauldron_inner")))
                .put(TextureSlot.BOTTOM, new Material(Identifier.tryParse("minecraft:block/cauldron_bottom")))
                .put(TextureSlot.SIDE, new Material(Identifier.tryParse("minecraft:block/cauldron_side")))
                .put(TextureSlot.PARTICLE, new Material(Identifier.tryParse("minecraft:block/cauldron_side")));

        Identifier level1Id = Identifier.tryParse(baseId.getNamespace() + ":block/" + baseId.getPath() + nameSuffix + "_level1");
        Identifier level2Id = Identifier.tryParse(baseId.getNamespace() + ":block/" + baseId.getPath() + nameSuffix + "_level2");
        Identifier level3Id = Identifier.tryParse(baseId.getNamespace() + ":block/" + baseId.getPath() + nameSuffix + "_full");

        return new Identifier[]{
                ModelTemplates.CAULDRON_LEVEL1.create(level1Id, mapping, blockModelGenerators.modelOutput),
                ModelTemplates.CAULDRON_LEVEL2.create(level2Id, mapping, blockModelGenerators.modelOutput),
                ModelTemplates.CAULDRON_FULL.create(level3Id, mapping, blockModelGenerators.modelOutput)
        };
    }

    private static void generatePotionCauldron(BlockModelGenerators blockModelGenerators) {
        Identifier blockId = BuiltInRegistries.BLOCK.getKey(BlocksSD.POTION_CAULDRON);
        Identifier[] waterModels = generateCauldronContentsModel(blockModelGenerators, blockId, "", Identifier.tryParse("minecraft:block/water_still"));

        blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.dispatch(BlocksSD.POTION_CAULDRON)
                .with(PropertyDispatch.initial(PotionCauldronBlock.LEVEL)
                        .select(1, BlockModelGenerators.plainVariant(waterModels[0]))
                        .select(2, BlockModelGenerators.plainVariant(waterModels[0]))
                        .select(3, BlockModelGenerators.plainVariant(waterModels[1]))
                        .select(4, BlockModelGenerators.plainVariant(waterModels[1]))
                        .select(5, BlockModelGenerators.plainVariant(waterModels[2]))
                        .select(6, BlockModelGenerators.plainVariant(waterModels[2]))
                )
        );
    }

    private static void generateStewCauldron(BlockModelGenerators blockModelGenerators) {
        Identifier blockId = BuiltInRegistries.BLOCK.getKey(BlocksSD.STEW_CAULDRON);

        Identifier lightTexture = Identifier.tryParse(blockId.getNamespace() + ":block/light_stew_still");
        Identifier[] lightModels = generateCauldronContentsModel(blockModelGenerators, blockId, "_light", lightTexture);

        Identifier finishedTexture = Identifier.tryParse(blockId.getNamespace() + ":block/heavy_stew_still");
        Identifier[] finishedModels = generateCauldronContentsModel(blockModelGenerators, blockId, "_heavy", finishedTexture);

        blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.dispatch(BlocksSD.STEW_CAULDRON)
                .with(PropertyDispatch.initial(StewCauldronBlock.LEVEL, StewCauldronBlock.IS_HEAVY_STEW)
                        .select(1, false, BlockModelGenerators.plainVariant(lightModels[0]))
                        .select(2, false, BlockModelGenerators.plainVariant(lightModels[1]))
                        .select(3, false, BlockModelGenerators.plainVariant(lightModels[2]))
                        .select(1, true, BlockModelGenerators.plainVariant(finishedModels[0]))
                        .select(2, true, BlockModelGenerators.plainVariant(finishedModels[1]))
                        .select(3, true, BlockModelGenerators.plainVariant(finishedModels[2]))));
    }

    public static void generateRuntimeBlockModels(ResourceManager resourceManager) {
        for (Block block : BuiltInRegistries.BLOCK) {
            if (block.defaultBlockState().hasProperty(BlockStateProperties.SNOWLOGGED_LAYERS)) {
                Identifier blockId = BuiltInRegistries.BLOCK.getKey(block);
                SnowloggedBlockModelGenerators snowloggedGenerators = new SnowloggedBlockModelGenerators();

                switch (block) {
                    case WallBlock _ -> snowloggedGenerators.generateWallState(block, blockId);
                    case FenceBlock fence when fence == Blocks.BAMBOO_FENCE -> snowloggedGenerators.generateAltFenceState(block, blockId);
                    case FenceBlock _ -> snowloggedGenerators.generateFenceState(block, blockId);
                    case FenceGateBlock _ -> snowloggedGenerators.generateFenceGateState(block, blockId);
                    case CrossCollisionBlock _ -> snowloggedGenerators.generateCrossState(blockId, resourceManager);
                    case DoublePlantBlock _ -> snowloggedGenerators.generateDoublePlantState(block, blockId, resourceManager);
                    case SegmentableBlock _ -> snowloggedGenerators.generateSegmentableState(block, blockId, resourceManager);
                    case CropBlock _, SweetBerryBushBlock _, NetherWartBlock _, StemBlock _ -> snowloggedGenerators.generateAgeableState(block, blockId, resourceManager);
                    default -> snowloggedGenerators.generatePlantState(block, blockId, resourceManager);
                }
            }
        }
    }

    private void generateWoodFamily(BlockModelGenerators blockModelGenerators) {
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.OAK_LOG, Blocks.OAK_WOOD, BlocksSD.OAK_WOOD_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.OAK_LOG, BlocksSD.OAK_WOOD_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.SPRUCE_LOG, Blocks.SPRUCE_WOOD, BlocksSD.SPRUCE_WOOD_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.SPRUCE_LOG, BlocksSD.SPRUCE_WOOD_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.BIRCH_LOG, Blocks.BIRCH_WOOD, BlocksSD.BIRCH_WOOD_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.BIRCH_LOG, BlocksSD.BIRCH_WOOD_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.JUNGLE_LOG, Blocks.JUNGLE_WOOD, BlocksSD.JUNGLE_WOOD_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.JUNGLE_LOG, BlocksSD.JUNGLE_WOOD_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.ACACIA_LOG, Blocks.ACACIA_WOOD, BlocksSD.ACACIA_WOOD_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.ACACIA_LOG, BlocksSD.ACACIA_WOOD_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_WOOD, BlocksSD.DARK_OAK_WOOD_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.DARK_OAK_LOG, BlocksSD.DARK_OAK_WOOD_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.MANGROVE_LOG, Blocks.MANGROVE_WOOD, BlocksSD.MANGROVE_WOOD_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.MANGROVE_LOG, BlocksSD.MANGROVE_WOOD_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.CHERRY_LOG, Blocks.CHERRY_WOOD, BlocksSD.CHERRY_WOOD_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.CHERRY_LOG, BlocksSD.CHERRY_WOOD_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.PALE_OAK_LOG, Blocks.PALE_OAK_WOOD, BlocksSD.PALE_OAK_WOOD_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.PALE_OAK_LOG, BlocksSD.PALE_OAK_WOOD_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.POPLAR_LOG, Blocks.POPLAR_WOOD, BlocksSD.POPLAR_WOOD_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.POPLAR_LOG, BlocksSD.POPLAR_WOOD_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.CRIMSON_STEM, Blocks.CRIMSON_HYPHAE, BlocksSD.CRIMSON_HYPHAE_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.CRIMSON_STEM, BlocksSD.CRIMSON_HYPHAE_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.WARPED_STEM, Blocks.WARPED_HYPHAE, BlocksSD.WARPED_HYPHAE_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.WARPED_STEM, BlocksSD.WARPED_HYPHAE_STAIRS);
    }

    private void generateStrippedWoodFamily(BlockModelGenerators blockModelGenerators) {
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.STRIPPED_OAK_LOG, Blocks.STRIPPED_OAK_WOOD, BlocksSD.STRIPPED_OAK_WOOD_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.STRIPPED_OAK_LOG, BlocksSD.STRIPPED_OAK_WOOD_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.STRIPPED_SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_WOOD, BlocksSD.STRIPPED_SPRUCE_WOOD_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.STRIPPED_SPRUCE_LOG, BlocksSD.STRIPPED_SPRUCE_WOOD_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.STRIPPED_BIRCH_LOG, Blocks.STRIPPED_BIRCH_WOOD, BlocksSD.STRIPPED_BIRCH_WOOD_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.STRIPPED_BIRCH_LOG, BlocksSD.STRIPPED_BIRCH_WOOD_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.STRIPPED_JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_WOOD, BlocksSD.STRIPPED_JUNGLE_WOOD_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.STRIPPED_JUNGLE_LOG, BlocksSD.STRIPPED_JUNGLE_WOOD_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.STRIPPED_ACACIA_LOG, Blocks.STRIPPED_ACACIA_WOOD, BlocksSD.STRIPPED_ACACIA_WOOD_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.STRIPPED_ACACIA_LOG, BlocksSD.STRIPPED_ACACIA_WOOD_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.STRIPPED_DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_WOOD, BlocksSD.STRIPPED_DARK_OAK_WOOD_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.STRIPPED_DARK_OAK_LOG, BlocksSD.STRIPPED_DARK_OAK_WOOD_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.STRIPPED_MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_WOOD, BlocksSD.STRIPPED_MANGROVE_WOOD_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.STRIPPED_MANGROVE_LOG, BlocksSD.STRIPPED_MANGROVE_WOOD_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.STRIPPED_CHERRY_LOG, Blocks.STRIPPED_CHERRY_WOOD, BlocksSD.STRIPPED_CHERRY_WOOD_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.STRIPPED_CHERRY_LOG, BlocksSD.STRIPPED_CHERRY_WOOD_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.STRIPPED_PALE_OAK_LOG, Blocks.STRIPPED_PALE_OAK_WOOD, BlocksSD.STRIPPED_PALE_OAK_WOOD_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.STRIPPED_PALE_OAK_LOG, BlocksSD.STRIPPED_PALE_OAK_WOOD_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.STRIPPED_POPLAR_LOG, Blocks.STRIPPED_POPLAR_WOOD, BlocksSD.STRIPPED_POPLAR_WOOD_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.STRIPPED_POPLAR_LOG, BlocksSD.STRIPPED_POPLAR_WOOD_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.STRIPPED_WARPED_STEM, Blocks.STRIPPED_CRIMSON_HYPHAE, BlocksSD.STRIPPED_CRIMSON_HYPHAE_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.STRIPPED_WARPED_STEM, BlocksSD.STRIPPED_CRIMSON_HYPHAE_STAIRS);
        generateWoodSlabFromVanilla(blockModelGenerators, Blocks.STRIPPED_WARPED_STEM, Blocks.STRIPPED_WARPED_HYPHAE, BlocksSD.STRIPPED_WARPED_HYPHAE_SLAB);
        generateWoodStairsFromVanilla(blockModelGenerators, Blocks.STRIPPED_WARPED_STEM, BlocksSD.STRIPPED_WARPED_HYPHAE_STAIRS);
    }

    public void generateBlockModels(BlockModelGenerators blockModelGenerators) {
        blockModelGenerators.family(BlocksSD.SNOW_BRICKS).generateFor(new BlockFamily.Builder(BlocksSD.SNOW_BRICKS)
                .stairs(BlocksSD.SNOW_BRICK_STAIRS)
                .slab(BlocksSD.SNOW_BRICK_SLAB)
                .wall(BlocksSD.SNOW_BRICK_WALL)
                .getFamily());
        blockModelGenerators.family(BlocksSD.POLISHED_DRIPSTONE).generateFor(new BlockFamily.Builder(BlocksSD.POLISHED_DRIPSTONE)
                .stairs(BlocksSD.POLISHED_DRIPSTONE_STAIRS)
                .slab(BlocksSD.POLISHED_DRIPSTONE_SLAB)
                .wall(BlocksSD.POLISHED_DRIPSTONE_WALL)
                .getFamily());
        blockModelGenerators.family(BlocksSD.STONE_TILES).generateFor(new BlockFamily.Builder(BlocksSD.STONE_TILES)
                .stairs(BlocksSD.STONE_TILE_STAIRS)
                .slab(BlocksSD.STONE_TILE_SLAB)
                .wall(BlocksSD.STONE_TILE_WALL)
                .getFamily());
        blockModelGenerators.createTrivialCube(BlocksSD.CHARCOAL_BLOCK);
        blockModelGenerators.createTrivialCube(BlocksSD.CHISELED_POLISHED_DRIPSTONE);
        blockModelGenerators.createAxisAlignedPillarBlock(BlocksSD.STONE_PILLAR, TexturedModel.COLUMN);
        blockModelGenerators.createTrivialCube(BlocksSD.IRON_GRATE);
        blockModelGenerators.createPumpkinVariant(BlocksSD.SOUL_JACK_O_LANTERN, TextureMapping.column(Blocks.PUMPKIN));
        blockModelGenerators.createDoublePlant(BlocksSD.REEDS, BlockModelGenerators.PlantType.NOT_TINTED);
        blockModelGenerators.createFlowerBed(BlocksSD.PERSE_WILDFLOWERS);
        generateOverhangBlock(blockModelGenerators, BlocksSD.WARPED_OVERHANG);
        generatePillarSlabFromVanilla(blockModelGenerators, Blocks.BASALT, BlocksSD.BASALT_SLAB);
        generatePotionCauldron(blockModelGenerators);
        generateStewCauldron(blockModelGenerators);
        generateWoodFamily(blockModelGenerators);
        generateStrippedWoodFamily(blockModelGenerators);
        
    }
}
