package net.meander.subtlyd.data;

import com.mojang.math.Quadrant;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.meander.subtlyd.client.model.ModelTemplatesSD;
import net.meander.subtlyd.data.models.blockstates.UnsafeMultiPartGenerator;
import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.block.PotionCauldronBlock;
import net.meander.subtlyd.world.item.ItemsSD;
import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.ConditionBuilder;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.WallSide;

import java.util.ArrayList;
import java.util.List;

public class ModelProviderSD extends FabricModelProvider {
    public ModelProviderSD(FabricPackOutput output) {
        super(output);
    }

    /**
     * Builds a slab model from a vanilla pillar block texture.
     * @param vanillaBlock The original block to obtain a texture from.
     * @param newBlock The custom block that the texture will be mapped to.
     */
    private static void generatePillarSlabFromVanilla(Block vanillaBlock, Block newBlock, BlockModelGenerators blockModelGenerator) {
        MultiVariant fullBlockModel = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(vanillaBlock));
        Identifier top = TextureMapping.getBlockTexture(vanillaBlock, "_top").sprite();
        Identifier side = TextureMapping.getBlockTexture(vanillaBlock, "_side").sprite();

        TextureMapping slabTextures = new TextureMapping()
                .put(TextureSlot.BOTTOM, new Material(top))
                .put(TextureSlot.TOP, new Material(top))
                .put(TextureSlot.SIDE, new Material(side));

        MultiVariant blockBottom = BlockModelGenerators.plainVariant(ModelTemplates.SLAB_BOTTOM.create(newBlock, slabTextures, blockModelGenerator.modelOutput));
        MultiVariant blockTop = BlockModelGenerators.plainVariant(ModelTemplates.SLAB_TOP.create(newBlock, slabTextures, blockModelGenerator.modelOutput));

        blockModelGenerator.blockStateOutput.accept(BlockModelGenerators.createSlab(newBlock, blockBottom, blockTop, fullBlockModel));
    }

    /**
     * Separate from the rest of the block family generation to allow for snowlogging.
     * @param vanillaBlock The original block to obtain a texture from.
     * @param wallBlock The custom wall that the texture will be mapped to.
     */
    private static void generateCustomWallFromVanilla(Block vanillaBlock, Block wallBlock, BlockModelGenerators blockModelGenerator) {
        if (BuiltInRegistries.BLOCK.getKey(wallBlock).toString().contains("subtlyd:")) {
            TextureMapping mapping = new TextureMapping()
                    .put(TextureSlot.WALL, TextureMapping.getBlockTexture(vanillaBlock))
                    .put(TextureSlot.TEXTURE, TextureMapping.getBlockTexture(vanillaBlock))
                    .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(vanillaBlock));

            ModelTemplates.WALL_POST.create(wallBlock, mapping, blockModelGenerator.modelOutput);
            ModelTemplates.WALL_LOW_SIDE.create(wallBlock, mapping, blockModelGenerator.modelOutput);
            ModelTemplates.WALL_TALL_SIDE.create(wallBlock, mapping, blockModelGenerator.modelOutput);
            ModelTemplates.WALL_INVENTORY.create(wallBlock, mapping, blockModelGenerator.modelOutput);
        }
    }

    /**
     * Creates an overhang block.
     * @param overhangBlock The block to map to.
     */
    private static void generateOverhangBlock(Block overhangBlock, BlockModelGenerators blockModelGenerator) {
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
        Identifier model = ModelTemplatesSD.OVERHANG_BLOCK.create(overhangBlock, mapping, blockModelGenerator.modelOutput);
        MultiVariant multiVariant = BlockModelGenerators.createRotatedVariants(BlockModelGenerators.plainModel(model));

        blockModelGenerator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(overhangBlock, multiVariant));
    }

    /**
     * Creates a textured cauldron block
     */
    @SuppressWarnings("DataFlowIssue")
    private static void generateFilledCauldron(Block cauldronBlock, BlockModelGenerators blockModelGenerator) {
        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.CONTENT, new Material(Identifier.tryParse("minecraft:block/water_still")))
                .put(TextureSlot.TOP, new Material(Identifier.tryParse("minecraft:block/cauldron_top")))
                .put(TextureSlot.INSIDE, new Material(Identifier.tryParse("minecraft:block/cauldron_inner")))
                .put(TextureSlot.BOTTOM, new Material(Identifier.tryParse("minecraft:block/cauldron_bottom")))
                .put(TextureSlot.SIDE, new Material(Identifier.tryParse("minecraft:block/cauldron_side")))
                .put(TextureSlot.PARTICLE, new Material(Identifier.tryParse("minecraft:block/cauldron_side")));
        Identifier blockId = BuiltInRegistries.BLOCK.getKey(cauldronBlock);

        Identifier level1Id = Identifier.tryParse(blockId.getNamespace() + ":block/" + blockId.getPath() + "_level1");
        Identifier level2Id = Identifier.tryParse(blockId.getNamespace() + ":block/" + blockId.getPath() + "_level2");
        Identifier level3Id = Identifier.tryParse(blockId.getNamespace() + ":block/" + blockId.getPath() + "_full");

        Identifier level1 = ModelTemplates.CAULDRON_LEVEL1.create(level1Id, mapping, blockModelGenerator.modelOutput);
        Identifier level2 = ModelTemplates.CAULDRON_LEVEL2.create(level2Id, mapping, blockModelGenerator.modelOutput);
        Identifier level3 = ModelTemplates.CAULDRON_FULL.create(level3Id, mapping, blockModelGenerator.modelOutput);

        blockModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(cauldronBlock)
                .with(PropertyDispatch.initial(PotionCauldronBlock.POTION_LEVEL)
                        .select(1, BlockModelGenerators.plainVariant(level1))
                        .select(2, BlockModelGenerators.plainVariant(level1))
                        .select(3, BlockModelGenerators.plainVariant(level2))
                        .select(4, BlockModelGenerators.plainVariant(level2))
                        .select(5, BlockModelGenerators.plainVariant(level3))
                        .select(6, BlockModelGenerators.plainVariant(level3))
                )
        );
    }

    public static void generateSnowLayers(UnsafeMultiPartGenerator generator) {
        int maxLevels = BlockStatePropertiesSD.SNOWLOGGED_LAYERS.getPossibleValues().getLast();

        for (int i = 1; i <= maxLevels; i++) {
            String modelName = (i == maxLevels) ? "block/snow_block" : "block/snow_height" + (i * 2);
            Identifier snowModelId = Identifier.withDefaultNamespace(modelName);

            generator.with(
                    new ConditionBuilder().term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                    new MultiVariant(WeightedList.of(
                            new Variant(snowModelId)
                    ))
            );
        }
    }

    public void generateSnowloggableFences(BlockModelGenerators blockModelGenerator) {
        List<Block> fences = List.of(Blocks.OAK_FENCE, Blocks.SPRUCE_FENCE, Blocks.BIRCH_FENCE, Blocks.JUNGLE_FENCE,
                Blocks.ACACIA_FENCE, Blocks.DARK_OAK_FENCE, Blocks.MANGROVE_FENCE, Blocks.CHERRY_FENCE, Blocks.BAMBOO_FENCE,
                Blocks.CRIMSON_FENCE, Blocks.WARPED_FENCE, Blocks.NETHER_BRICK_FENCE, Blocks.PALE_OAK_FENCE
        );

        for (Block fenceBlock : fences) {
            UnsafeMultiPartGenerator generator = UnsafeMultiPartGenerator.multiPart(fenceBlock);

            Identifier postModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(fenceBlock) + "_post");
            Identifier sideModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(fenceBlock) + "_side");

            if (postModelId != null && sideModelId != null) {
                for (int i = 0; i < BlockStatePropertiesSD.SNOWLOGGED_LAYERS.getPossibleValues().getLast(); i++) {
                    generator.with(
                            new ConditionBuilder().term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(postModelId)))
                    );

                    generator.with(
                            new ConditionBuilder().term(FenceBlock.NORTH, true).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(sideModelId).withUvLock(true)))
                    );

                    generator.with(
                            new ConditionBuilder().term(FenceBlock.EAST, true).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(sideModelId).withYRot(Quadrant.R90).withUvLock(true)))
                    );

                    generator.with(
                            new ConditionBuilder().term(FenceBlock.SOUTH, true).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(sideModelId).withYRot(Quadrant.R180).withUvLock(true)))
                    );

                    generator.with(
                            new ConditionBuilder().term(FenceBlock.WEST, true).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(sideModelId).withYRot(Quadrant.R270).withUvLock(true)))
                    );
                }

                generateSnowLayers(generator);
                blockModelGenerator.blockStateOutput.accept(generator);
            }
        }
    }

    public void generateSnowloggableFenceGates(BlockModelGenerators blockModelGenerator) {
        List<Block> fenceGates = List.of(Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE,
                Blocks.ACACIA_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE, Blocks.MANGROVE_FENCE_GATE, Blocks.CHERRY_FENCE_GATE, Blocks.BAMBOO_FENCE_GATE,
                Blocks.CRIMSON_FENCE_GATE, Blocks.WARPED_FENCE_GATE, Blocks.PALE_OAK_FENCE_GATE
        );

        for (Block gateBlock : fenceGates) {
            UnsafeMultiPartGenerator generator = UnsafeMultiPartGenerator.multiPart(gateBlock);

            Identifier baseModel = ModelLocationUtils.getModelLocation(gateBlock);
            Identifier openModel = Identifier.tryParse(baseModel + "_open");
            Identifier wallModel = Identifier.tryParse(baseModel + "_wall");
            Identifier wallOpenModel = Identifier.tryParse(baseModel + "_wall_open");

            if (openModel != null && wallModel != null && wallOpenModel != null) {
                generator.with(new ConditionBuilder().term(FenceGateBlock.FACING, Direction.NORTH).term(FenceGateBlock.OPEN, false).term(FenceGateBlock.IN_WALL, false), new MultiVariant(WeightedList.of(new Variant(baseModel).withUvLock(true))));
                generator.with(new ConditionBuilder().term(FenceGateBlock.FACING, Direction.NORTH).term(FenceGateBlock.OPEN, true).term(FenceGateBlock.IN_WALL, false), new MultiVariant(WeightedList.of(new Variant(openModel).withUvLock(true))));
                generator.with(new ConditionBuilder().term(FenceGateBlock.FACING, Direction.NORTH).term(FenceGateBlock.OPEN, false).term(FenceGateBlock.IN_WALL, true), new MultiVariant(WeightedList.of(new Variant(wallModel).withUvLock(true))));
                generator.with(new ConditionBuilder().term(FenceGateBlock.FACING, Direction.NORTH).term(FenceGateBlock.OPEN, true).term(FenceGateBlock.IN_WALL, true), new MultiVariant(WeightedList.of(new Variant(wallOpenModel).withUvLock(true))));

                generator.with(new ConditionBuilder().term(FenceGateBlock.FACING, Direction.EAST).term(FenceGateBlock.OPEN, false).term(FenceGateBlock.IN_WALL, false), new MultiVariant(WeightedList.of(new Variant(baseModel).withYRot(Quadrant.R90).withUvLock(true))));
                generator.with(new ConditionBuilder().term(FenceGateBlock.FACING, Direction.EAST).term(FenceGateBlock.OPEN, true).term(FenceGateBlock.IN_WALL, false), new MultiVariant(WeightedList.of(new Variant(openModel).withYRot(Quadrant.R90).withUvLock(true))));
                generator.with(new ConditionBuilder().term(FenceGateBlock.FACING, Direction.EAST).term(FenceGateBlock.OPEN, false).term(FenceGateBlock.IN_WALL, true), new MultiVariant(WeightedList.of(new Variant(wallModel).withYRot(Quadrant.R90).withUvLock(true))));
                generator.with(new ConditionBuilder().term(FenceGateBlock.FACING, Direction.EAST).term(FenceGateBlock.OPEN, true).term(FenceGateBlock.IN_WALL, true), new MultiVariant(WeightedList.of(new Variant(wallOpenModel).withYRot(Quadrant.R90).withUvLock(true))));

                generator.with(new ConditionBuilder().term(FenceGateBlock.FACING, Direction.SOUTH).term(FenceGateBlock.OPEN, false).term(FenceGateBlock.IN_WALL, false), new MultiVariant(WeightedList.of(new Variant(baseModel).withYRot(Quadrant.R180).withUvLock(true))));
                generator.with(new ConditionBuilder().term(FenceGateBlock.FACING, Direction.SOUTH).term(FenceGateBlock.OPEN, true).term(FenceGateBlock.IN_WALL, false), new MultiVariant(WeightedList.of(new Variant(openModel).withYRot(Quadrant.R180).withUvLock(true))));
                generator.with(new ConditionBuilder().term(FenceGateBlock.FACING, Direction.SOUTH).term(FenceGateBlock.OPEN, false).term(FenceGateBlock.IN_WALL, true), new MultiVariant(WeightedList.of(new Variant(wallModel).withYRot(Quadrant.R180).withUvLock(true))));
                generator.with(new ConditionBuilder().term(FenceGateBlock.FACING, Direction.SOUTH).term(FenceGateBlock.OPEN, true).term(FenceGateBlock.IN_WALL, true), new MultiVariant(WeightedList.of(new Variant(wallOpenModel).withYRot(Quadrant.R180).withUvLock(true))));

                generator.with(new ConditionBuilder().term(FenceGateBlock.FACING, Direction.WEST).term(FenceGateBlock.OPEN, false).term(FenceGateBlock.IN_WALL, false), new MultiVariant(WeightedList.of(new Variant(baseModel).withYRot(Quadrant.R270).withUvLock(true))));
                generator.with(new ConditionBuilder().term(FenceGateBlock.FACING, Direction.WEST).term(FenceGateBlock.OPEN, true).term(FenceGateBlock.IN_WALL, false), new MultiVariant(WeightedList.of(new Variant(openModel).withYRot(Quadrant.R270).withUvLock(true))));
                generator.with(new ConditionBuilder().term(FenceGateBlock.FACING, Direction.WEST).term(FenceGateBlock.OPEN, false).term(FenceGateBlock.IN_WALL, true), new MultiVariant(WeightedList.of(new Variant(wallModel).withYRot(Quadrant.R270).withUvLock(true))));
                generator.with(new ConditionBuilder().term(FenceGateBlock.FACING, Direction.WEST).term(FenceGateBlock.OPEN, true).term(FenceGateBlock.IN_WALL, true), new MultiVariant(WeightedList.of(new Variant(wallOpenModel).withYRot(Quadrant.R270).withUvLock(true))));

                generateSnowLayers(generator);
                blockModelGenerator.blockStateOutput.accept(generator);
            }
        }
    }

    public void generateSnowloggableWalls(BlockModelGenerators blockModelGenerator) {
        List<Block> walls = List.of(
                Blocks.COBBLESTONE_WALL, Blocks.MOSSY_COBBLESTONE_WALL, Blocks.STONE_BRICK_WALL, Blocks.MOSSY_STONE_BRICK_WALL,
                Blocks.GRANITE_WALL, Blocks.DIORITE_WALL, Blocks.ANDESITE_WALL, Blocks.COBBLED_DEEPSLATE_WALL, Blocks.POLISHED_DEEPSLATE_WALL,
                Blocks.DEEPSLATE_BRICK_WALL, Blocks.DEEPSLATE_TILE_WALL, Blocks.BRICK_WALL, Blocks.PRISMARINE_WALL, Blocks.RED_SANDSTONE_WALL,
                Blocks.SANDSTONE_WALL, Blocks.END_STONE_BRICK_WALL, Blocks.BLACKSTONE_WALL, Blocks.POLISHED_BLACKSTONE_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_WALL,
                Blocks.CINNABAR_WALL, Blocks.CINNABAR_BRICK_WALL, Blocks.POLISHED_CINNABAR_WALL, Blocks.SULFUR_WALL, Blocks.SULFUR_BRICK_WALL,
                Blocks.POLISHED_SULFUR_WALL, Blocks.RESIN_BRICK_WALL, BlocksSD.POLISHED_DRIPSTONE_WALL, BlocksSD.STONE_TILE_WALL, BlocksSD.SNOW_BRICK_WALL
        );

        generateCustomWallFromVanilla(BlocksSD.POLISHED_DRIPSTONE, BlocksSD.POLISHED_DRIPSTONE_WALL, blockModelGenerator);
        generateCustomWallFromVanilla(BlocksSD.STONE_TILES, BlocksSD.STONE_TILE_WALL, blockModelGenerator);
        generateCustomWallFromVanilla(BlocksSD.SNOW_BRICKS, BlocksSD.SNOW_BRICK_WALL, blockModelGenerator);

        for (Block wallBlock : walls) {
            UnsafeMultiPartGenerator generator = UnsafeMultiPartGenerator.multiPart(wallBlock);

            Identifier postModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(wallBlock) + "_post");
            Identifier sideModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(wallBlock) + "_side");
            Identifier sideTallModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(wallBlock) + "_side_tall");

            if (postModelId != null && sideModelId != null && sideTallModelId != null) {
                for (int i = 0; i < BlockStatePropertiesSD.SNOWLOGGED_LAYERS.getPossibleValues().getLast(); i++) {
                    generator.with(
                            new ConditionBuilder().term(WallBlock.UP, true).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(postModelId)))
                    );

                    generator.with(
                            new ConditionBuilder().term(WallBlock.NORTH, WallSide.LOW).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(sideModelId).withUvLock(true)))
                    );
                    generator.with(
                            new ConditionBuilder().term(WallBlock.NORTH, WallSide.TALL).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(sideTallModelId).withUvLock(true)))
                    );

                    generator.with(
                            new ConditionBuilder().term(WallBlock.EAST, WallSide.LOW).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(sideModelId).withYRot(Quadrant.R90).withUvLock(true)))
                    );
                    generator.with(
                            new ConditionBuilder().term(WallBlock.EAST, WallSide.TALL).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(sideTallModelId).withYRot(Quadrant.R90).withUvLock(true)))
                    );

                    generator.with(
                            new ConditionBuilder().term(WallBlock.SOUTH, WallSide.LOW).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(sideModelId).withYRot(Quadrant.R180).withUvLock(true)))
                    );
                    generator.with(
                            new ConditionBuilder().term(WallBlock.SOUTH, WallSide.TALL).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(sideTallModelId).withYRot(Quadrant.R180).withUvLock(true)))
                    );

                    generator.with(
                            new ConditionBuilder().term(WallBlock.WEST, WallSide.LOW).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(sideModelId).withYRot(Quadrant.R270).withUvLock(true)))
                    );
                    generator.with(
                            new ConditionBuilder().term(WallBlock.WEST, WallSide.TALL).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(sideTallModelId).withYRot(Quadrant.R270).withUvLock(true)))
                    );
                }
                generateSnowLayers(generator);
                blockModelGenerator.blockStateOutput.accept(generator);
            }
        }
    }

    public void generateSnowloggableSimpleVegetation(BlockModelGenerators blockModelGenerator) {
        List<Block> snowLoggableVegetation = List.of(Blocks.SHORT_DRY_GRASS, Blocks.TALL_DRY_GRASS, Blocks.SHORT_GRASS,
                Blocks.BUSH, Blocks.FIREFLY_BUSH, Blocks.FERN, Blocks.DANDELION, Blocks.POPPY, Blocks.CORNFLOWER, Blocks.ALLIUM,
                Blocks.AZURE_BLUET, Blocks.BLUE_ORCHID, Blocks.GOLDEN_DANDELION, Blocks.ORANGE_TULIP, Blocks.PINK_TULIP, Blocks.RED_TULIP,
                Blocks.WHITE_TULIP, Blocks.OXEYE_DAISY, Blocks.LILY_OF_THE_VALLEY, Blocks.WITHER_ROSE, Blocks.CLOSED_EYEBLOSSOM,
                Blocks.OPEN_EYEBLOSSOM, Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.CRIMSON_FUNGUS, Blocks.WARPED_FUNGUS, Blocks.CRIMSON_ROOTS,
                Blocks.WARPED_ROOTS, Blocks.NETHER_SPROUTS
        );

        for (Block block : snowLoggableVegetation) {
            UnsafeMultiPartGenerator generator = UnsafeMultiPartGenerator.multiPart(block);
            Identifier modelId = ModelLocationUtils.getModelLocation(block);

            generator.with(new MultiVariant(WeightedList.of(new Variant(modelId))));
            generateSnowLayers(generator);
            blockModelGenerator.blockStateOutput.accept(generator);
        }
    }

    public void generateSnowloggableAgingVegetation(BlockModelGenerators blockModelGenerator) {
        List<Block> snowLoggableVegetation = List.of(Blocks.SWEET_BERRY_BUSH, Blocks.TORCHFLOWER_CROP, Blocks.ACACIA_SAPLING,
                Blocks.BIRCH_SAPLING, Blocks.CHERRY_SAPLING, Blocks.DARK_OAK_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.OAK_SAPLING,
                Blocks.PALE_OAK_SAPLING, Blocks.SPRUCE_SAPLING);

        for (Block block : snowLoggableVegetation) {
            int maxAge = -1;

            if (block instanceof CropBlock crop) {
                maxAge = crop.getMaxAge();
            } else if (block instanceof SweetBerryBushBlock) {
                maxAge = SweetBerryBushBlock.MAX_AGE;
            }

            UnsafeMultiPartGenerator generator = UnsafeMultiPartGenerator.multiPart(block);

            for (int i = 0; i <= maxAge; i++) {
                Identifier modelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(block) + "_stage" + i);

                if (modelId != null) {
                    generator.with(new MultiVariant(WeightedList.of(new Variant(modelId))));
                }
            }
            generateSnowLayers(generator);
            blockModelGenerator.blockStateOutput.accept(generator);
        }
    }

    public void generateSnowloggableDoubleVegetation(BlockModelGenerators blockModelGenerator) {
        List<Block> doubleVegetation = List.of(Blocks.TALL_GRASS, Blocks.LARGE_FERN);

        for (Block block : doubleVegetation) {
            UnsafeMultiPartGenerator generator = UnsafeMultiPartGenerator.multiPart(block);

            Identifier bottomModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(block) + "_bottom");
            Identifier topModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(block) + "_top");

            if (bottomModelId != null && topModelId != null) {
                generator.with(
                        new ConditionBuilder().term(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER),
                        new MultiVariant(WeightedList.of(new Variant(bottomModelId)))
                );

                generator.with(
                        new ConditionBuilder().term(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER),
                        new MultiVariant(WeightedList.of(new Variant(topModelId)))
                );

                generateSnowLayers(generator);
                blockModelGenerator.blockStateOutput.accept(generator);
            }
        }
    }

    public void generateSnowloggableCrossCollisionBlocks(BlockModelGenerators blockModelGenerator) {
        List<Block> crossBlocks = new ArrayList<>(List.of(Blocks.IRON_BARS, Blocks.GLASS_PANE));
        crossBlocks.addAll(Blocks.STAINED_GLASS_PANE.asList());

        for (Block block : crossBlocks) {
            UnsafeMultiPartGenerator generator = UnsafeMultiPartGenerator.multiPart(block);

            boolean areIronBars = block == Blocks.IRON_BARS;

            Identifier sideModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(block) + "_side");
            Identifier sideAltModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(block) + "_side_alt");

            Identifier falseModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(block) + (areIronBars ? "_cap" : "_noside"));
            Identifier falseAltModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(block) + (areIronBars ? "_cap_alt" : "_noside_alt"));

            if (sideModelId != null && sideAltModelId != null && falseModelId != null && falseAltModelId != null) {
                for (int i = 0; i < BlockStatePropertiesSD.SNOWLOGGED_LAYERS.getPossibleValues().getLast(); i++) {
                    generator.with(
                            new ConditionBuilder().term(CrossCollisionBlock.NORTH, true).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(sideModelId).withUvLock(true)))
                    );
                    generator.with(
                            new ConditionBuilder().term(CrossCollisionBlock.NORTH, false).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(falseModelId).withUvLock(true)))
                    );

                    generator.with(
                            new ConditionBuilder().term(CrossCollisionBlock.EAST, true).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(sideModelId).withYRot(Quadrant.R90).withUvLock(true)))
                    );
                    generator.with(
                            new ConditionBuilder().term(CrossCollisionBlock.EAST, false).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(falseAltModelId).withUvLock(true)))
                    );

                    generator.with(
                            new ConditionBuilder().term(CrossCollisionBlock.SOUTH, true).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(sideAltModelId).withUvLock(true)))
                    );
                    generator.with(
                            new ConditionBuilder().term(CrossCollisionBlock.SOUTH, false).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(areIronBars ? falseModelId : falseAltModelId).withYRot(Quadrant.R90).withUvLock(true)))
                    );

                    generator.with(
                            new ConditionBuilder().term(CrossCollisionBlock.WEST, true).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(sideAltModelId).withYRot(Quadrant.R90).withUvLock(true)))
                    );
                    generator.with(
                            new ConditionBuilder().term(CrossCollisionBlock.WEST, false).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(areIronBars ? falseAltModelId : falseModelId).withYRot(areIronBars ? Quadrant.R90 : Quadrant.R270).withUvLock(true)))
                    );
                }
                generateSnowLayers(generator);
                blockModelGenerator.blockStateOutput.accept(generator);
            }
        }
    }

    private void generateSnowloggables(BlockModelGenerators blockModelGenerator) {
        generateSnowloggableSimpleVegetation(blockModelGenerator);
        generateSnowloggableAgingVegetation(blockModelGenerator);
        generateSnowloggableDoubleVegetation(blockModelGenerator);
        generateSnowloggableFences(blockModelGenerator);
        generateSnowloggableCrossCollisionBlocks(blockModelGenerator);
        generateSnowloggableWalls(blockModelGenerator);
        generateSnowloggableFenceGates(blockModelGenerator);
    }

    private void generatePotionArchetypes(ItemModelGenerators itemModelGenerator) {
        Identifier conicalBottle = Util.identifier("item/potion/conical_overlay");
        Identifier sphericalBottle = Util.identifier("item/potion/spherical_overlay");
        Identifier vialBottle = Util.identifier("item/potion/vial_overlay");

        ModelTemplates.FLAT_ITEM.create(conicalBottle, TextureMapping.layer0(new Material(conicalBottle)), itemModelGenerator.modelOutput);
        ModelTemplates.FLAT_ITEM.create(sphericalBottle, TextureMapping.layer0(new Material(sphericalBottle)), itemModelGenerator.modelOutput);
        ModelTemplates.FLAT_ITEM.create(vialBottle, TextureMapping.layer0(new Material(vialBottle)), itemModelGenerator.modelOutput);
    }

    private void generateInventoryItemFromBlock(Block block, ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(block, "_inventory")));
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerator) {
        blockModelGenerator.family(BlocksSD.SNOW_BRICKS).generateFor(new BlockFamily.Builder(BlocksSD.SNOW_BRICKS)
                .stairs(BlocksSD.SNOW_BRICK_STAIRS)
                .slab(BlocksSD.SNOW_BRICK_SLAB)
                .getFamily());
        blockModelGenerator.family(BlocksSD.POLISHED_DRIPSTONE).generateFor(new BlockFamily.Builder(BlocksSD.POLISHED_DRIPSTONE)
                .stairs(BlocksSD.POLISHED_DRIPSTONE_STAIRS)
                .slab(BlocksSD.POLISHED_DRIPSTONE_SLAB)
                .getFamily());
        blockModelGenerator.family(BlocksSD.STONE_TILES).generateFor(new BlockFamily.Builder(BlocksSD.STONE_TILES)
                .stairs(BlocksSD.STONE_TILE_STAIRS)
                .slab(BlocksSD.STONE_TILE_SLAB)
                .getFamily());
        blockModelGenerator.createTrivialCube(BlocksSD.CHARCOAL_BLOCK);
        blockModelGenerator.createTrivialCube(BlocksSD.CHISELED_POLISHED_DRIPSTONE);
        blockModelGenerator.createAxisAlignedPillarBlock(BlocksSD.STONE_PILLAR, TexturedModel.COLUMN);
        blockModelGenerator.createTrivialCube(BlocksSD.IRON_GRATE);
        blockModelGenerator.createDoublePlant(BlocksSD.REEDS, BlockModelGenerators.PlantType.NOT_TINTED);
        generateOverhangBlock(BlocksSD.WARPED_OVERHANG, blockModelGenerator);
        generatePillarSlabFromVanilla(Blocks.BASALT, BlocksSD.BASALT_SLAB, blockModelGenerator);
        blockModelGenerator.createPumpkinVariant(BlocksSD.SOUL_JACK_O_LANTERN, TextureMapping.column(Blocks.PUMPKIN));
        generateFilledCauldron(BlocksSD.POTION_CAULDRON, blockModelGenerator);
        generateSnowloggables(blockModelGenerator);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        ItemsSD.TENT.forEach(item -> itemModelGenerator.generateFlatItem(item, ModelTemplates.FLAT_ITEM));
        itemModelGenerator.generateFlatItem(ItemsSD.APPLE_PIE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.CALAMARI, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.COOKED_CALAMARI, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.UNLIT_CAMPFIRE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.UNLIT_SOUL_CAMPFIRE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.POTTAGE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.REEDS, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.WARPED_OVERHANG, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.BLAST_FUNGUS, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.COVEN_ELIXIR, ModelTemplates.FLAT_ITEM);
        generatePotionArchetypes(itemModelGenerator);
        generateInventoryItemFromBlock(BlocksSD.POLISHED_DRIPSTONE_WALL, itemModelGenerator);
        generateInventoryItemFromBlock(BlocksSD.STONE_TILE_WALL, itemModelGenerator);
        generateInventoryItemFromBlock(BlocksSD.SNOW_BRICK_WALL, itemModelGenerator);}
}
