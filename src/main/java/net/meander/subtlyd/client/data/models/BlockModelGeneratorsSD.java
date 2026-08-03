package net.meander.subtlyd.client.data.models;

import net.meander.subtlyd.client.data.models.model.ModelTemplatesSD;
import net.meander.subtlyd.data.BlockFamiliesSD;
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
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Map;

/**
 * @see BlockModelGenerators
 */
public class BlockModelGeneratorsSD {
    private final BlockModelGenerators blockModelGenerators;

    public BlockModelGeneratorsSD(BlockModelGenerators blockModelGenerators) {
        this.blockModelGenerators = blockModelGenerators;
    }

    /**
     * Builds a slab model from a vanilla pillar block texture.
     * @param baseBlock The original block to obtain a texture from.
     * @param newBlock The custom block that the texture will be mapped to.
     */
    private void generatePillarSlabFromVanilla(Block baseBlock, Block newBlock) {
        MultiVariant fullBlockModel = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(baseBlock));
        Identifier top = TextureMapping.getBlockTexture(baseBlock, "_top").sprite();
        Identifier side = TextureMapping.getBlockTexture(baseBlock, "_side").sprite();

        TextureMapping slabTextures = new TextureMapping().put(TextureSlot.BOTTOM, new Material(top)).put(TextureSlot.TOP, new Material(top)).put(TextureSlot.SIDE, new Material(side));
        MultiVariant blockBottom = BlockModelGenerators.plainVariant(ModelTemplates.SLAB_BOTTOM.create(newBlock, slabTextures, blockModelGenerators.modelOutput));
        MultiVariant blockTop = BlockModelGenerators.plainVariant(ModelTemplates.SLAB_TOP.create(newBlock, slabTextures, blockModelGenerators.modelOutput));

        blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createSlab(newBlock, blockBottom, blockTop, fullBlockModel));
    }

    private void generateSlabFromVanilla(Block baseBlock, Block newBlock) {
        Identifier faceTexture = TextureMapping.getBlockTexture(baseBlock).sprite();
        
        TextureMapping slabTextures = new TextureMapping().put(TextureSlot.BOTTOM, new Material(faceTexture)).put(TextureSlot.TOP, new Material(faceTexture)).put(TextureSlot.SIDE, new Material(faceTexture));
        MultiVariant blockBottom = BlockModelGenerators.plainVariant(ModelTemplates.SLAB_BOTTOM.create(newBlock, slabTextures, blockModelGenerators.modelOutput));
        MultiVariant blockTop = BlockModelGenerators.plainVariant(ModelTemplates.SLAB_TOP.create(newBlock, slabTextures, blockModelGenerators.modelOutput));
        MultiVariant fullBlockModel = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(baseBlock));

        blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createSlab(newBlock, blockBottom, blockTop, fullBlockModel));
    }

    private void generateStairsFromVanilla(Block baseBlock, Block newBlock) {
        Identifier faceTexture = TextureMapping.getBlockTexture(baseBlock).sprite();

        TextureMapping stairTextures = new TextureMapping().put(TextureSlot.BOTTOM, new Material(faceTexture)).put(TextureSlot.TOP, new Material(faceTexture)).put(TextureSlot.SIDE, new Material(faceTexture));
        MultiVariant blockStraight = BlockModelGenerators.plainVariant(ModelTemplates.STAIRS_STRAIGHT.create(newBlock, stairTextures, blockModelGenerators.modelOutput));
        MultiVariant blockInner = BlockModelGenerators.plainVariant(ModelTemplates.STAIRS_INNER.create(newBlock, stairTextures, blockModelGenerators.modelOutput));
        MultiVariant blockOuter = BlockModelGenerators.plainVariant(ModelTemplates.STAIRS_OUTER.create(newBlock, stairTextures, blockModelGenerators.modelOutput));

        blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createStairs(newBlock, blockInner, blockStraight, blockOuter));
    }

    /**
     * Creates an overhang block.
     * @param overhangBlock The block to map to.
     */
    private void generateOverhangBlock(Block overhangBlock) {
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
    private Identifier[] generateCauldronContentsModel(Identifier baseId, String nameSuffix, Identifier liquidTexture) {
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

    private void generatePotionCauldron() {
        Identifier blockId = BuiltInRegistries.BLOCK.getKey(BlocksSD.POTION_CAULDRON);
        Identifier[] waterModels = generateCauldronContentsModel(blockId, "", Identifier.tryParse("minecraft:block/water_still"));

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

    private void generateStewCauldron() {
        Identifier blockId = BuiltInRegistries.BLOCK.getKey(BlocksSD.STEW_CAULDRON);

        Identifier lightTexture = Identifier.tryParse(blockId.getNamespace() + ":block/light_stew_still");
        Identifier[] lightModels = generateCauldronContentsModel(blockId, "_light", lightTexture);

        Identifier finishedTexture = Identifier.tryParse(blockId.getNamespace() + ":block/heavy_stew_still");
        Identifier[] finishedModels = generateCauldronContentsModel(blockId, "_heavy", finishedTexture);

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

    public void woodFamily(BlockFamily blockFamily, BlockFamily baseBlockFamily, BlockFamily.Variant variantType) {
        Map<BlockFamily.Variant, Block> variants = blockFamily.getVariants();
        Map<BlockFamily.Variant, Block> baseVariants = baseBlockFamily.getVariants();
        Block baseBlock = baseVariants.get(variantType);

        for (BlockFamily.Variant variant : variants.keySet()) {
            switch (variant) {
                case STAIRS -> generateStairsFromVanilla(baseBlock, variants.get(variant)); // Oak Wood Stairs
                case SLAB -> generateSlabFromVanilla(baseBlock, variants.get(variant));
            }
        }
    }

    private void generateWoodFamily() {
        woodFamily(BlockFamiliesSD.OAK_WOOD, BlockFamilies.OAK_PLANKS, BlockFamily.Variant.LOG);
        woodFamily(BlockFamiliesSD.STRIPPED_OAK_WOOD, BlockFamilies.OAK_PLANKS, BlockFamily.Variant.STRIPPED_LOG);
        woodFamily(BlockFamiliesSD.BIRCH_WOOD, BlockFamilies.BIRCH_PLANKS, BlockFamily.Variant.LOG);
        woodFamily(BlockFamiliesSD.STRIPPED_BIRCH_WOOD, BlockFamilies.BIRCH_PLANKS, BlockFamily.Variant.STRIPPED_LOG);
        woodFamily(BlockFamiliesSD.SPRUCE_WOOD, BlockFamilies.SPRUCE_PLANKS, BlockFamily.Variant.LOG);
        woodFamily(BlockFamiliesSD.STRIPPED_SPRUCE_WOOD, BlockFamilies.SPRUCE_PLANKS, BlockFamily.Variant.STRIPPED_LOG);
        woodFamily(BlockFamiliesSD.JUNGLE_WOOD, BlockFamilies.JUNGLE_PLANKS, BlockFamily.Variant.LOG);
        woodFamily(BlockFamiliesSD.STRIPPED_JUNGLE_WOOD, BlockFamilies.JUNGLE_PLANKS, BlockFamily.Variant.STRIPPED_LOG);
        woodFamily(BlockFamiliesSD.ACACIA_WOOD, BlockFamilies.ACACIA_PLANKS, BlockFamily.Variant.LOG);
        woodFamily(BlockFamiliesSD.STRIPPED_ACACIA_WOOD, BlockFamilies.ACACIA_PLANKS, BlockFamily.Variant.STRIPPED_LOG);
        woodFamily(BlockFamiliesSD.DARK_OAK_WOOD, BlockFamilies.DARK_OAK_PLANKS, BlockFamily.Variant.LOG);
        woodFamily(BlockFamiliesSD.STRIPPED_DARK_OAK_WOOD, BlockFamilies.DARK_OAK_PLANKS, BlockFamily.Variant.STRIPPED_LOG);
        woodFamily(BlockFamiliesSD.MANGROVE_WOOD, BlockFamilies.MANGROVE_PLANKS, BlockFamily.Variant.LOG);
        woodFamily(BlockFamiliesSD.STRIPPED_MANGROVE_WOOD, BlockFamilies.MANGROVE_PLANKS, BlockFamily.Variant.STRIPPED_LOG);
        woodFamily(BlockFamiliesSD.CHERRY_WOOD, BlockFamilies.CHERRY_PLANKS, BlockFamily.Variant.LOG);
        woodFamily(BlockFamiliesSD.STRIPPED_CHERRY_WOOD, BlockFamilies.CHERRY_PLANKS, BlockFamily.Variant.STRIPPED_LOG);
        woodFamily(BlockFamiliesSD.PALE_OAK_WOOD, BlockFamilies.PALE_OAK_PLANKS, BlockFamily.Variant.LOG);
        woodFamily(BlockFamiliesSD.STRIPPED_PALE_OAK_WOOD, BlockFamilies.PALE_OAK_PLANKS, BlockFamily.Variant.STRIPPED_LOG);
        woodFamily(BlockFamiliesSD.POPLAR_WOOD, BlockFamilies.POPLAR_PLANKS, BlockFamily.Variant.LOG);
        woodFamily(BlockFamiliesSD.STRIPPED_POPLAR_WOOD, BlockFamilies.POPLAR_PLANKS, BlockFamily.Variant.STRIPPED_LOG);
        woodFamily(BlockFamiliesSD.CRIMSON_HYPHAE, BlockFamilies.CRIMSON_PLANKS, BlockFamily.Variant.LOG);
        woodFamily(BlockFamiliesSD.STRIPPED_CRIMSON_HYPHAE, BlockFamilies.CRIMSON_PLANKS, BlockFamily.Variant.STRIPPED_LOG);
        woodFamily(BlockFamiliesSD.WARPED_HYPHAE, BlockFamilies.WARPED_PLANKS, BlockFamily.Variant.LOG);
        woodFamily(BlockFamiliesSD.STRIPPED_WARPED_HYPHAE, BlockFamilies.WARPED_PLANKS, BlockFamily.Variant.STRIPPED_LOG);
    }

    public void generateBlockModels() {
        blockModelGenerators.family(BlocksSD.SNOW_BRICKS).generateFor(BlockFamiliesSD.SNOW_BRICKS);
        blockModelGenerators.family(BlocksSD.POLISHED_DRIPSTONE).generateFor(BlockFamiliesSD.POLISHED_DRIPSTONE);
        blockModelGenerators.family(BlocksSD.STONE_TILES).generateFor(BlockFamiliesSD.STONE_TILES);
        blockModelGenerators.createTrivialCube(BlocksSD.CHARCOAL_BLOCK);
        blockModelGenerators.createAxisAlignedPillarBlock(BlocksSD.STONE_PILLAR, TexturedModel.COLUMN);
        blockModelGenerators.createTrivialCube(BlocksSD.IRON_GRATE);
        blockModelGenerators.createPumpkinVariant(BlocksSD.SOUL_JACK_O_LANTERN, TextureMapping.column(Blocks.PUMPKIN));
        blockModelGenerators.createDoublePlant(BlocksSD.REEDS, BlockModelGenerators.PlantType.NOT_TINTED);
        blockModelGenerators.createFlowerBed(BlocksSD.PERSE_WILDFLOWERS);
        generateOverhangBlock(BlocksSD.WARPED_OVERHANG);
        generatePillarSlabFromVanilla(Blocks.BASALT, BlocksSD.BASALT_SLAB);
        generatePotionCauldron();
        generateStewCauldron();
        generateWoodFamily();
    }
}
