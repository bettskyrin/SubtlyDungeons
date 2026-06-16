package net.meander.subtlyd.client.data.model;

import com.google.gson.JsonObject;
import com.mojang.math.Quadrant;
import net.meander.subtlyd.data.models.blockstates.SnowloggableBlocks;
import net.meander.subtlyd.data.models.blockstates.UnsafeMultiPartGenerator;
import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.ConditionBuilder;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;

import java.util.ArrayList;
import java.util.List;

public class SnowloggedBlockModelProvider {
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

    /**
     * Generates a block model JSON file that enables ambient occulsion for snowlogged foliage.
     * @param block The vanilla foliage block.
     * @param level The snowlogged level of the block model
     */
    private static Identifier generateAmbientOcclusionModel(Block block, int level, BlockModelGenerators blockModelGenerator) {
        Identifier baseModelId = ModelLocationUtils.getModelLocation(block);
        String blockName = BuiltInRegistries.BLOCK.getKey(block).getPath();

        Identifier aoModelId = Identifier.tryParse("subtlyd:block/snowlogged_" + blockName + level);

        if (aoModelId != null) {
            blockModelGenerator.modelOutput.accept(aoModelId, () -> {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("parent", baseModelId.toString());
                jsonObject.addProperty("ambientocclusion", true);

                return jsonObject;
            });
        }

        return aoModelId;
    }

    public static void generateSnowloggableFences(BlockModelGenerators blockModelGenerator) {
        for (Block fenceBlock : SnowloggableBlocks.FENCES) {
            UnsafeMultiPartGenerator generator = UnsafeMultiPartGenerator.multiPart(fenceBlock);

            Identifier postModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(fenceBlock) + "_post");
            Identifier sideModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(fenceBlock) + "_side");
            Identifier finalSideModelId = sideModelId;

            if (postModelId != null && sideModelId != null) {
                Quadrant quadrant;
                for (int i = 0; i < SnowloggableBlocks.MAX_LAYERS; i++) {
                    generator.with(
                            new ConditionBuilder().term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                            new MultiVariant(WeightedList.of(new Variant(postModelId)))
                    );

                    for (Direction direction : Direction.Plane.HORIZONTAL) {
                        if (fenceBlock.defaultBlockState().is(Blocks.BAMBOO_FENCE)) { // These are weird for some reason.
                            finalSideModelId = Identifier.tryParse(sideModelId + "_" + direction.name().toLowerCase());
                            quadrant = Quadrant.R0;
                        } else {
                            switch (direction) {
                                case EAST -> quadrant = Quadrant.R90;
                                case SOUTH -> quadrant = Quadrant.R180;
                                case WEST -> quadrant = Quadrant.R270;
                                default -> quadrant = Quadrant.R0;
                            }
                        }

                        if (finalSideModelId != null) {
                            generator.with(
                                    new ConditionBuilder().term(FenceBlock.PROPERTY_BY_DIRECTION.get(direction), true).term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                                    new MultiVariant(WeightedList.of(new Variant(finalSideModelId).withYRot(quadrant).withUvLock(true)))
                            );
                        }
                    }
                }

                generateSnowloggedLayers(generator);
                blockModelGenerator.blockStateOutput.accept(generator);
            }
        }
    }

    public static void generateSnowloggableFenceGates(BlockModelGenerators blockModelGenerator) {
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

    public static void generateSnowloggableWalls(BlockModelGenerators blockModelGenerator) {
        SnowloggableBlocks.addToList();
        ModelProviderSD.generateCustomWalls(blockModelGenerator);

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

    public static void generateSnowloggableSimpleVegetation(BlockModelGenerators blockModelGenerator) {
        for (Block block : SnowloggableBlocks.SIMPLE_VEGETATION) {
            UnsafeMultiPartGenerator generator = UnsafeMultiPartGenerator.multiPart(block);
            Identifier baseModelId = ModelLocationUtils.getModelLocation(block);

            for (int layer = 0; layer < SnowloggableBlocks.MAX_LAYERS; layer++) {
                Identifier finalModelId = (layer == 0) ? baseModelId : generateAmbientOcclusionModel(block, layer, blockModelGenerator);

                generator.with(
                        new ConditionBuilder().term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, layer),
                        new MultiVariant(WeightedList.of(new Variant(finalModelId)))
                );
            }
            generateSnowloggedLayers(generator);
            blockModelGenerator.blockStateOutput.accept(generator);
        }
    }

    public static void generateSnowloggableAgingVegetation(BlockModelGenerators blockModelGenerator) {
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

    public static void generateSnowloggableTallVegetation(BlockModelGenerators blockModelGenerator) {
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

    public static void generateSnowloggableSegmentableVegetation(BlockModelGenerators blockModelGenerator) {
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
                                        default -> {}
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

    public static void generateSnowloggableCrossCollisionBlocks(BlockModelGenerators blockModelGenerator) {
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
}
