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
    public static void generateSnowloggables(BlockModelGenerators blockModelGenerator) {
        generateSnowloggableSimpleVegetation(blockModelGenerator);
        generateSnowloggableAgingVegetation(blockModelGenerator);
        generateSnowloggableTallVegetation(blockModelGenerator);
        generateSnowloggableSegmentableVegetation(blockModelGenerator);
        generateSnowloggableFences(blockModelGenerator);
        generateSnowloggableCrossCollisionBlocks(blockModelGenerator);
        generateSnowloggableWalls(blockModelGenerator);
        generateSnowloggableFenceGates(blockModelGenerator);
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

    private static void writeModelJson(Identifier modelId, String parentModel, String textureSlot, Identifier textureId, BlockModelGenerators generators) {
        generators.modelOutput.accept(modelId, () -> {
            JsonObject json = new JsonObject();
            json.addProperty("parent", parentModel);
            json.addProperty("ambientocclusion", true);

            if (textureSlot != null && textureId != null) {
                JsonObject textures = new JsonObject();
                textures.addProperty(textureSlot, textureId.toString());
                json.add("textures", textures);
            }
            return json;
        });
    }

    /**
     * Generates a block model JSON file that enables ambient occulsion for snowlogged foliage.
     * @param block The vanilla foliage block.
     */
    private static Identifier generateAmbientOcclusionModel(Block block, BlockModelGenerators generators) {
        Identifier baseModelId = ModelLocationUtils.getModelLocation(block);
        Identifier aoModelId = Identifier.tryParse("subtlyd:block/snowlogged_" + BuiltInRegistries.BLOCK.getKey(block).getPath());

        if (aoModelId != null) {
            writeModelJson(aoModelId, baseModelId.toString(), null, null, generators);
        }
        return aoModelId;
    }

    /**
     * Generates a new model file for blocks with a snowy texture variant.
     * @param baseModelId The base block's model ID
     * @param textureSlot The slot in the parent model to apply the snowy texture to
     */
    private static Identifier generateSnowyModelVariant(Identifier baseModelId, String textureSlot, BlockModelGenerators generators) {
        Identifier snowyModelId = Identifier.tryParse(baseModelId + "_snowy");
        Identifier snowyTexId = Identifier.tryParse(baseModelId.getNamespace() + ":" + baseModelId.getPath() + "_snowy");

        if (snowyModelId != null && snowyTexId != null) {
            String parent = textureSlot.equals("crop") ? "minecraft:block/crop" : "minecraft:block/cross";
            writeModelJson(snowyModelId, parent, textureSlot, snowyTexId, generators);
        }
        return snowyModelId;
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
                        if (fenceBlock.defaultBlockState().is(Blocks.BAMBOO_FENCE)) {
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
            boolean hasSnowyVariant = SnowloggableBlocks.SNOWY_BLOCKS.contains(block);

            Identifier snowyModelId = hasSnowyVariant ? generateSnowyModelVariant(baseModelId, "cross", blockModelGenerator) : null;
            Identifier aoModelId = !hasSnowyVariant ? generateAmbientOcclusionModel(block, blockModelGenerator) : null;

            for (int layer = 0; layer < SnowloggableBlocks.MAX_LAYERS; layer++) {
                Identifier finalModelId = baseModelId;

                if (layer > 0) {
                    finalModelId = hasSnowyVariant ? snowyModelId : aoModelId;
                }

                if (finalModelId != null) {
                    generator.with(
                            new ConditionBuilder().term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, layer),
                            new MultiVariant(WeightedList.of(new Variant(finalModelId)))
                    );
                }
            }
            generateSnowloggedLayers(generator);
            blockModelGenerator.blockStateOutput.accept(generator);
        }
    }

    public static void generateSnowloggableAgingVegetation(BlockModelGenerators blockModelGenerator) {
        for (Block block : SnowloggableBlocks.AGING_VEGETATION) {
            boolean hasSnowyVariant = SnowloggableBlocks.SNOWY_BLOCKS.contains(block);
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
                        Identifier snowyModelId = hasSnowyVariant ? generateSnowyModelVariant(modelId, "crop", blockModelGenerator) : null;

                        for (int layer = 0; layer < SnowloggableBlocks.MAX_LAYERS; layer++) {
                            Identifier finalModelId = (layer > 0 && hasSnowyVariant) ? snowyModelId : modelId;

                            if (finalModelId != null) {
                                generator.with(
                                        new ConditionBuilder()
                                                .term(intProp, currentStage)
                                                .term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, layer),
                                        new MultiVariant(WeightedList.of(new Variant(finalModelId)))
                                );
                            }
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
            boolean hasSnowyVariant = SnowloggableBlocks.SNOWY_BLOCKS.contains(block);
            UnsafeMultiPartGenerator generator = UnsafeMultiPartGenerator.multiPart(block);
            Identifier bottomModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(block) + "_bottom");
            Identifier topModelId = Identifier.tryParse(ModelLocationUtils.getModelLocation(block) + "_top");

            if (bottomModelId != null && topModelId != null) {
                Identifier snowyBottomModelId = hasSnowyVariant ? generateSnowyModelVariant(bottomModelId, "cross", blockModelGenerator) : null;
                Identifier snowyTopModelId = hasSnowyVariant ? generateSnowyModelVariant(topModelId, "cross", blockModelGenerator) : null;

                for (int i = 0; i < SnowloggableBlocks.MAX_LAYERS; i++) {
                    Identifier lowerModel = (i > 0 && hasSnowyVariant) ? snowyBottomModelId : bottomModelId;

                    if (lowerModel != null) {
                        generator.with(
                                new ConditionBuilder()
                                        .term(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
                                        .term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                                new MultiVariant(WeightedList.of(new Variant(lowerModel)))
                        );
                    }

                    if (i > 0) {
                        Identifier upperModel = hasSnowyVariant ? snowyTopModelId : topModelId;
                        if (upperModel != null) {
                            generator.with(
                                    new ConditionBuilder()
                                            .term(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
                                            .term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, i),
                                    new MultiVariant(WeightedList.of(new Variant(upperModel)))
                            );
                        }
                    } else {
                        for (boolean isBottomSnowlogged : new boolean[]{false, true}) {
                            Identifier upperModel = (isBottomSnowlogged && hasSnowyVariant) ? snowyTopModelId : topModelId;
                            if (upperModel != null) {
                                generator.with(
                                        new ConditionBuilder()
                                                .term(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
                                                .term(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, 0)
                                                .term(BlockStatePropertiesSD.BOTTOM_SNOWLOGGED, isBottomSnowlogged),
                                        new MultiVariant(WeightedList.of(new Variant(upperModel)))
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

                                if (layer == 0) {
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