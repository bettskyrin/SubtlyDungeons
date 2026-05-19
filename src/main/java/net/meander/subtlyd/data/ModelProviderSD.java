package net.meander.subtlyd.data;

import com.mojang.math.Quadrant;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.meander.subtlyd.client.model.ModelTemplatesSD;
import net.meander.subtlyd.data.models.blockstates.SnowloggableBlocks;
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
import net.minecraft.world.level.block.state.properties.*;

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

    public static void generateCustomWalls(BlockModelGenerators blockModelGenerator) {
        generateCustomWallFromVanilla(BlocksSD.POLISHED_DRIPSTONE, BlocksSD.POLISHED_DRIPSTONE_WALL, blockModelGenerator);
        generateCustomWallFromVanilla(BlocksSD.STONE_TILES, BlocksSD.STONE_TILE_WALL, blockModelGenerator);
        generateCustomWallFromVanilla(BlocksSD.SNOW_BRICKS, BlocksSD.SNOW_BRICK_WALL, blockModelGenerator);
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

    public static void generateSnowloggedLayers(UnsafeMultiPartGenerator generator) {
        for (int i = 1; i <= SnowloggableBlocks.MAX_LAYERS; i++) {
            String modelName = (i == SnowloggableBlocks.MAX_LAYERS) ? "block/snow_block" : "block/snow_height" + (i * 2);
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
        for (Block fenceBlock : SnowloggableBlocks.FENCES) {
            UnsafeMultiPartGenerator generator = UnsafeMultiPartGenerator.multiPart(fenceBlock);

            Identifier postModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(fenceBlock) + "_post");
            Identifier sideModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(fenceBlock) + "_side");

            if (postModelId != null && sideModelId != null) {
                Quadrant quadrant;
                for (int i = 0; i < SnowloggableBlocks.MAX_LAYERS; i++) {
                    generator.with(
                            new ConditionBuilder().term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(postModelId)))
                    );

                    for (Direction direction : Direction.Plane.HORIZONTAL) {
                        switch (direction) {
                            case EAST -> quadrant = Quadrant.R90;
                            case SOUTH -> quadrant = Quadrant.R180;
                            case WEST -> quadrant = Quadrant.R270;
                            default -> quadrant = Quadrant.R0;
                        }

                        if (fenceBlock.defaultBlockState().is(Blocks.BAMBOO_FENCE)) { // Why is this the only one like this??
                            Identifier.tryParse(sideModelId + "_" + direction.name().toLowerCase());
                        }

                        generator.with(
                                new ConditionBuilder().term(FenceBlock.PROPERTY_BY_DIRECTION.get(direction), true).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                                new MultiVariant(WeightedList.of(new Variant(sideModelId).withYRot(quadrant).withUvLock(true)))
                        );
                    }
                }

                generateSnowloggedLayers(generator);
                blockModelGenerator.blockStateOutput.accept(generator);
            }
        }
    }

    public void generateSnowloggableFenceGates(BlockModelGenerators blockModelGenerator) {
        for (Block gateBlock : SnowloggableBlocks.FENCE_GATES) {
            UnsafeMultiPartGenerator generator = UnsafeMultiPartGenerator.multiPart(gateBlock);
            Identifier baseModel = ModelLocationUtils.getModelLocation(gateBlock);
            Identifier openModel = Identifier.tryParse(baseModel + "_open");
            Identifier wallModel = Identifier.tryParse(baseModel + "_wall");
            Identifier wallOpenModel = Identifier.tryParse(baseModel + "_wall_open");

            if (openModel != null && wallModel != null && wallOpenModel != null) {
                for (int i = 0; i < SnowloggableBlocks.MAX_LAYERS; i++) {
                    for (Direction direction : Direction.Plane.HORIZONTAL) {
                        Quadrant quadrant = switch (direction) {
                            case EAST -> Quadrant.R90;
                            case SOUTH -> Quadrant.R180;
                            case WEST -> Quadrant.R270;
                            default -> Quadrant.R0;
                        };

                        for (boolean isOpen : new boolean[]{true, false}) {
                            for (boolean inWall : new boolean[]{true, false}) {
                                Identifier model;

                                if (isOpen && inWall) {
                                    model = wallOpenModel;
                                } else if (isOpen) {
                                    model = openModel;
                                } else if (inWall) {
                                    model = wallModel;
                                } else {
                                    model = baseModel;
                                }

                                generator.with(new ConditionBuilder()
                                                .term(FenceGateBlock.FACING, direction)
                                                .term(FenceGateBlock.OPEN, isOpen)
                                                .term(FenceGateBlock.IN_WALL, inWall)
                                                .term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                                        new MultiVariant(WeightedList.of(
                                                new Variant(model).withYRot(quadrant).withUvLock(true)
                                        ))
                                );
                            }
                        }
                    }
                }
                generateSnowloggedLayers(generator);
                blockModelGenerator.blockStateOutput.accept(generator);
            }
        }
    }

    public void generateSnowloggableWalls(BlockModelGenerators blockModelGenerator) {
        SnowloggableBlocks.addToList();
        generateCustomWalls(blockModelGenerator);

        for (Block wallBlock : SnowloggableBlocks.WALLS) {
            UnsafeMultiPartGenerator generator = UnsafeMultiPartGenerator.multiPart(wallBlock);
            Identifier postModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(wallBlock) + "_post");
            Identifier sideModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(wallBlock) + "_side");
            Identifier sideTallModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(wallBlock) + "_side_tall");

            if (postModelId != null && sideModelId != null && sideTallModelId != null) {
                List<Identifier> directionalModels = List.of(sideModelId, sideTallModelId);

                for (int i = 0; i < SnowloggableBlocks.MAX_LAYERS; i++) {
                    generator.with(
                            new ConditionBuilder()
                                    .term(WallBlock.UP, true)
                                    .term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(postModelId)))
                    );

                    for (Direction direction : Direction.Plane.HORIZONTAL) {
                        EnumProperty<WallSide> wallSide = WallBlock.PROPERTY_BY_DIRECTION.get(direction);
                        Quadrant quadrant = switch (direction) {
                            case EAST -> Quadrant.R90;
                            case SOUTH -> Quadrant.R180;
                            case WEST -> Quadrant.R270;
                            default -> Quadrant.R0;
                        };

                        for (Identifier model : directionalModels) {
                            WallSide currentWallSide = model == sideModelId ? WallSide.LOW : WallSide.TALL;

                            generator.with(
                                    new ConditionBuilder()
                                            .term(wallSide, currentWallSide)
                                            .term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                                    new MultiVariant(WeightedList.of(new Variant(model)
                                            .withYRot(quadrant)
                                            .withUvLock(true))
                                    )
                            );
                        }
                    }
                }
                generateSnowloggedLayers(generator);
                blockModelGenerator.blockStateOutput.accept(generator);
            }
        }
    }

    public void generateSnowloggableSimpleVegetation(BlockModelGenerators blockModelGenerator) {
        for (Block block : SnowloggableBlocks.SIMPLE_VEGETATION) {
            UnsafeMultiPartGenerator generator = UnsafeMultiPartGenerator.multiPart(block);
            Identifier modelId = ModelLocationUtils.getModelLocation(block);

            for (int layer = 0; layer < SnowloggableBlocks.MAX_LAYERS; layer++) {
                generator.with(
                        new ConditionBuilder().term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, layer),
                        new MultiVariant(WeightedList.of(new Variant(modelId)))
                );
            }

            generateSnowloggedLayers(generator);
            blockModelGenerator.blockStateOutput.accept(generator);
        }
    }

    public void generateSnowloggableAgingVegetation(BlockModelGenerators blockModelGenerator) {
        for (Block block : SnowloggableBlocks.AGING_VEGETATION) {
            UnsafeMultiPartGenerator generator = UnsafeMultiPartGenerator.multiPart(block);
            Property<?> growthProperty = block.defaultBlockState().getProperties().stream()
                    .filter(p -> p.getName().equals("age") || p.getName().equals("stage"))
                    .findFirst()
                    .orElse(null);

            if (growthProperty instanceof IntegerProperty intProp) {
                int maxGrowth = intProp.getPossibleValues().stream().max(Integer::compareTo).orElse(0);

                for (int currentStage = 0; currentStage <= maxGrowth; currentStage++) {
                    Identifier modelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(block) + "_stage" + currentStage);

                    if (modelId != null) {
                        for (int layer = 0; layer < SnowloggableBlocks.MAX_LAYERS; layer++) {
                            generator.with(
                                    new ConditionBuilder()
                                            .term(intProp, currentStage)
                                            .term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, layer),
                                    new MultiVariant(WeightedList.of(new Variant(modelId)))
                            );
                        }
                    }
                }
            }
            generateSnowloggedLayers(generator);
            blockModelGenerator.blockStateOutput.accept(generator);
        }
    }

    public void generateSnowloggableTallVegetation(BlockModelGenerators blockModelGenerator) {
        for (Block block : SnowloggableBlocks.TALL_VEGETATION) {
            UnsafeMultiPartGenerator generator = UnsafeMultiPartGenerator.multiPart(block);
            Identifier bottomModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(block) + "_bottom");
            Identifier topModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(block) + "_top");

            if (bottomModelId != null && topModelId != null) {
                for (int i = 0; i < SnowloggableBlocks.MAX_LAYERS; i++) {
                    for (DoubleBlockHalf half : DoubleBlockHalf.values()) {
                        Identifier modelId = half == DoubleBlockHalf.LOWER ? bottomModelId : topModelId;

                        generator.with(
                                new ConditionBuilder()
                                        .term(DoublePlantBlock.HALF, half)
                                        .term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                                new MultiVariant(WeightedList.of(new Variant(modelId)))
                        );
                    }
                }

                generateSnowloggedLayers(generator);
                blockModelGenerator.blockStateOutput.accept(generator);
            }
        }
    }

    public void generateSnowloggableSegmentableVegetation(BlockModelGenerators blockModelGenerator) {
        for (Block block : SnowloggableBlocks.SEGMENTABLE_VEGETATION) {
            if (block instanceof SegmentableBlock segmentableBlock) {
                UnsafeMultiPartGenerator generator = UnsafeMultiPartGenerator.multiPart(block);
                List<Object> segmentCount = new ArrayList<>();

                if (block instanceof FlowerBedBlock) {
                    segmentCount.add(null);
                    segmentCount.add(new Integer[]{2, 3, 4});
                    segmentCount.add(new Integer[]{3, 4});
                } else {
                    segmentCount.add(1);
                    segmentCount.add(new Integer[]{2, 3});
                    segmentCount.add(3);
                }
                segmentCount.add(4);

                for (int i = 0; i < segmentCount.size(); i++) {
                    Identifier modelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(block) + "_" + (i + 1));

                    if (modelId != null) {
                        for (int layer = 0; layer < SnowloggableBlocks.MAX_LAYERS; layer++) {
                            for (Direction direction : Direction.Plane.HORIZONTAL) {
                                Quadrant quadrant = switch (direction) {
                                    case EAST -> Quadrant.R90;
                                    case SOUTH -> Quadrant.R180;
                                    case WEST -> Quadrant.R270;
                                    default -> Quadrant.R0;
                                };

                                if (layer == 0) { // Still generate the snow layers, but don't render them to prevent Z-Fighting. I don't want to have to write this again.
                                    switch (segmentCount.get(i)) {
                                        case Integer[] segmentValues -> generator.with(
                                                new ConditionBuilder()
                                                        .term(segmentableBlock.getSegmentAmountProperty(), segmentValues[0], segmentValues)
                                                        .term(BlockStateProperties.HORIZONTAL_FACING, direction)
                                                        .term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, layer),
                                                new MultiVariant(WeightedList.of(new Variant(modelId).withYRot(quadrant)))
                                        );
                                        case Integer segmentValue -> generator.with(
                                                new ConditionBuilder()
                                                        .term(segmentableBlock.getSegmentAmountProperty(), segmentValue)
                                                        .term(BlockStateProperties.HORIZONTAL_FACING, direction)
                                                        .term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, layer),
                                                new MultiVariant(WeightedList.of(new Variant(modelId).withYRot(quadrant)))
                                        );
                                        case null -> generator.with(
                                                new ConditionBuilder()
                                                        .term(BlockStateProperties.HORIZONTAL_FACING, direction)
                                                        .term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, layer),
                                                new MultiVariant(WeightedList.of(new Variant(modelId).withYRot(quadrant)))
                                        );
                                        default -> {
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                generateSnowloggedLayers(generator);
                blockModelGenerator.blockStateOutput.accept(generator);
            }
        }
    }

    public void generateSnowloggableCrossCollisionBlocks(BlockModelGenerators blockModelGenerator) {
        for (Block block : SnowloggableBlocks.CROSS_BLOCKS) {
            UnsafeMultiPartGenerator generator = UnsafeMultiPartGenerator.multiPart(block);
            boolean isIronBars = block == Blocks.IRON_BARS;

            Identifier sideModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(block) + "_side");
            Identifier sideAltModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(block) + "_side_alt");
            Identifier postModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(block) + (isIronBars ? "_cap" : "_noside"));
            Identifier postAltModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(block) + (isIronBars ? "_cap_alt" : "_noside_alt"));

            if (sideModelId != null && sideAltModelId != null && postModelId != null && postAltModelId != null) {
                for (int i = 0; i < SnowloggableBlocks.MAX_LAYERS; i++) {
                    for (Direction direction : Direction.Plane.HORIZONTAL) {
                        for (boolean isConnected : new boolean[]{true, false}) {
                            Identifier selectedModel;
                            Quadrant selectedRotation = Quadrant.R0;

                            if (isConnected) {
                                selectedModel = (direction == Direction.NORTH || direction == Direction.EAST) ? sideModelId : sideAltModelId;
                                selectedRotation = (direction == Direction.EAST || direction == Direction.WEST) ? Quadrant.R90 : Quadrant.R0;
                            } else {
                                switch (direction) {
                                    case EAST -> selectedModel = postAltModelId;
                                    case SOUTH -> {
                                        selectedModel = isIronBars ? postModelId : postAltModelId;
                                        selectedRotation = Quadrant.R90;
                                    }
                                    case WEST -> {
                                        selectedModel = isIronBars ? postAltModelId : postModelId;
                                        selectedRotation = isIronBars ? Quadrant.R90 : Quadrant.R270;
                                    }
                                    default -> selectedModel = postModelId;
                                }
                            }

                            generator.with(
                                    new ConditionBuilder()
                                            .term(CrossCollisionBlock.PROPERTY_BY_DIRECTION.get(direction), isConnected)
                                            .term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                                    new MultiVariant(WeightedList.of(new Variant(selectedModel)
                                            .withYRot(selectedRotation)
                                            .withUvLock(true)))
                            );
                        }
                    }
                }
                generateSnowloggedLayers(generator);
                blockModelGenerator.blockStateOutput.accept(generator);
            }
        }
    }

    private void generateSnowloggables(BlockModelGenerators blockModelGenerator) {
        generateSnowloggableSimpleVegetation(blockModelGenerator);
        generateSnowloggableAgingVegetation(blockModelGenerator);
        generateSnowloggableTallVegetation(blockModelGenerator);
        generateSnowloggableSegmentableVegetation(blockModelGenerator);
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
