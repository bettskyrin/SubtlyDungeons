package net.meander.subtlyd.client.data.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.meander.subtlyd.server.packs.VirtualResourceRegistry;
import net.meander.subtlyd.util.UtilSD;
import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

/**
 * Dynamically creates snowlogged blockstates and models at runtime for compatability.
 */
public class SnowloggedBlockModelProvider extends ModelProviderSD {
    public SnowloggedBlockModelProvider(FabricPackOutput output) {
        super(output);
    }

    /**
     * @param block The base block
     * @param blockId The base block's identifier
     * @param resourceManager The resource manager to determine if a "snowy" texture variant exists
     */
    protected static void generatePlantState(Block block, Identifier blockId, ResourceManager resourceManager) {
        String modelPath = ModelLocationUtils.getModelLocation(block).toString();
        String snowModel;
        
        Identifier stateId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "blockstates/" + blockId.getPath() + ".json");

        JsonObject snowCondition = createMaxLayerCondition(7);
        JsonObject root = new JsonObject();
        JsonArray multipart = new JsonArray();
        
        if (hasSnowyVariant(blockId, resourceManager)) {
            snowModel = generateSnowyModel(modelPath, "cross");
        } else {
            snowModel = generateAoModel(modelPath);
        }

        multipart.add(createConditionPart(BlockStatePropertiesSD.SNOWLOGGED_LAYERS.getName(), "0", modelPath, 0, false));
        snowCondition.getAsJsonArray("OR").remove(0);
        multipart.add(createPart(snowCondition, createApply(snowModel, 0, false)));

        addSnowLayers(multipart);
        
        root.add("multipart", multipart);
        VirtualResourceRegistry.registerResource(stateId, root.toString());
    }

    protected static void generateDoublePlantState(Block block, Identifier blockId, ResourceManager resourceManager) {
        if (block != Blocks.PITCHER_CROP && block != Blocks.PITCHER_PLANT) { // Pitcher plants have top and bottom models that are also aging
            String modelPath = ModelLocationUtils.getModelLocation(block).toString();
            String bottomModel = modelPath + "_bottom";
            String topModel = modelPath + "_top";
            String bottomSnowloggedModel;
            String topSnowloggedModel;
            
            Identifier stateId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "blockstates/" + blockId.getPath() + ".json");
            Identifier bottomId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), blockId.getPath() + "_bottom");
            Identifier topId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), blockId.getPath() + "_top");

            JsonObject visibleLayers = createMaxLayerCondition(7);
            JsonObject root = new JsonObject();
            JsonObject bottomDefaultCondition = new JsonObject(); // Conditions for when the blocks are visibly snowlogged or not
            JsonObject topDefaultCondition = new JsonObject();
            JsonObject topSnowyCondition = new JsonObject(); // When the bottom is snowlogged but top should not be
            JsonObject topSnowloggedCondition = visibleLayers.deepCopy();
            JsonObject bottomSnowloggedCondition = visibleLayers.deepCopy();
            JsonArray multipart = new JsonArray();

            if (hasSnowyVariant(bottomId, resourceManager)) {
                bottomSnowloggedModel = generateSnowyModel(bottomModel, "cross");
            } else {
                bottomSnowloggedModel = generateAoModel(bottomModel);
            }

            if (hasSnowyVariant(topId, resourceManager)) {
                topSnowloggedModel = generateSnowyModel(topModel, "cross");
            } else {
                topSnowloggedModel = generateAoModel(topModel);
            }

            addProperty(bottomDefaultCondition, "half", "lower");
            addProperty(bottomDefaultCondition, BlockStatePropertiesSD.SNOWLOGGED_LAYERS.getName(), "0");
            multipart.add(createPart(bottomDefaultCondition, createApply(bottomModel, 0, false)));

            bottomSnowloggedCondition.getAsJsonArray("OR").remove(0);
            addProperty(bottomSnowloggedCondition, "half", "lower");
            multipart.add(createPart(bottomSnowloggedCondition, createApply(bottomSnowloggedModel, 0, false)));

            addProperty(topDefaultCondition, "half", "upper");
            addProperty(topDefaultCondition, BlockStatePropertiesSD.SNOWLOGGED_LAYERS.getName(), "0");
            addProperty(topDefaultCondition, BlockStatePropertiesSD.BOTTOM_SNOWLOGGED.getName(), "false");
            multipart.add(createPart(topDefaultCondition, createApply(topModel, 0, false)));

            topSnowloggedCondition.getAsJsonArray("OR").remove(0);
            addProperty(topSnowloggedCondition, "half", "upper");
            multipart.add(createPart(topSnowloggedCondition, createApply(topSnowloggedModel, 0, false)));

            addProperty(topSnowyCondition, "half", "upper");
            addProperty(topSnowyCondition, BlockStatePropertiesSD.SNOWLOGGED_LAYERS.getName(), "0");
            addProperty(topSnowyCondition, BlockStatePropertiesSD.BOTTOM_SNOWLOGGED.getName(), "true");
            multipart.add(createPart(topSnowyCondition, createApply(topSnowloggedModel, 0, false)));

            addSnowLayers(multipart);

            root.add("multipart", multipart);
            VirtualResourceRegistry.registerResource(stateId, root.toString());
        }
    }

    protected static void generateAgeableState(Block block, Identifier blockId, ResourceManager resourceManager) {
        Identifier stateId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "blockstates/" + blockId.getPath() + ".json");

        JsonObject visibleLayers = createMaxLayerCondition(7);
        JsonObject root = new JsonObject();
        JsonArray multipart = new JsonArray();

        Property<?> ageProperty = null;

        for (Property<?> property : block.defaultBlockState().getProperties()) {
            if (property.getName().equals("age")) {
                ageProperty = property;
                break;
            }
        }

        if (ageProperty != null) {
            for (Comparable<?> value : ageProperty.getPossibleValues()) {
                int age = (Integer) value;
                int modelAge = getAge(block, age);

                String stageModel = blockId.getNamespace() + ":block/" + blockId.getPath() + "_stage" + modelAge;
                String textureSlot = (block instanceof CropBlock) ? "crop" : "cross";
                String snowloggedModel;

                Identifier stageId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), blockId.getPath() + "_stage" + modelAge);

                JsonObject ageCondition = visibleLayers.deepCopy();
                JsonObject defaultCondition = new JsonObject();

                if (hasSnowyVariant(stageId, resourceManager)) {
                    snowloggedModel = generateSnowyModel(stageModel, textureSlot);
                } else {
                    snowloggedModel = generateAoModel(stageModel);
                }
                
                addProperty(defaultCondition, ageProperty.getName(), String.valueOf(age));
                addProperty(defaultCondition, BlockStatePropertiesSD.SNOWLOGGED_LAYERS.getName(), "0");
                multipart.add(createPart(defaultCondition, createApply(stageModel, 0, false)));

                ageCondition.getAsJsonArray("OR").remove(0);
                addProperty(ageCondition, ageProperty.getName(), String.valueOf(age));
                multipart.add(createPart(ageCondition, createApply(snowloggedModel, 0, false)));
            }
        }
        addSnowLayers(multipart);
        
        root.add("multipart", multipart);
        VirtualResourceRegistry.registerResource(stateId, root.toString());
    }

    private static int getAge(Block block, int age) {
        int modelAge = age;

        if (block == Blocks.NETHER_WART && age == 3) {
            modelAge = 2;
        } else if (block == Blocks.CARROTS || block == Blocks.POTATOES) {
            if (age >= 4 && age <= 6) {
                modelAge = 2;
            } else if (age == 7) {
                modelAge = 3;
            } else if (age >= 2) {
                modelAge = 1;
            } else {
                modelAge = 0;
            }
        }
        return modelAge;
    }

    protected static void generateSegmentableState(Block block, Identifier blockId, ResourceManager resourceManager) {
        Identifier stateId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "blockstates/" + blockId.getPath() + ".json");

        JsonObject visibleLayers = createMaxLayerCondition(2);
        JsonObject root = new JsonObject();
        JsonArray multipart = new JsonArray();

        Property<?> amountProperty = null;

        for (Property<?> property : block.defaultBlockState().getProperties()) {
            if (property.getName().equals("flower_amount") || property.getName().equals("amount") || property.getName().equals("segment_amount")) {
                amountProperty = property;
                break;
            }
        }

        if (amountProperty != null) {
            boolean isHorizontal = block.defaultBlockState().hasProperty(BlockStateProperties.HORIZONTAL_FACING);

            int minAmount = Integer.MAX_VALUE;
            int maxAmount = Integer.MIN_VALUE;

            for (Comparable<?> value : amountProperty.getPossibleValues()) {
                int val = (Integer) value;
                minAmount = Math.min(minAmount, val);
                maxAmount = Math.max(maxAmount, val);
            }

            for (Comparable<?> value : amountProperty.getPossibleValues()) {
                int amount = (Integer) value;
                String amountModel = blockId.getNamespace() + ":block/" + blockId.getPath() + "_" + amount;
                Identifier amountPropertyId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), blockId.getPath() + "_" + amount);
                String snowloggedModel;

                if (hasSnowyVariant(amountPropertyId, resourceManager)) {
                    snowloggedModel = generateSnowyModel(amountModel, "cross");
                } else {
                    snowloggedModel = generateAoModel(amountModel);
                }

                StringBuilder additiveConditionBuilder = new StringBuilder();
                for (int i = amount; i <= maxAmount; i++) {
                    if (!additiveConditionBuilder.isEmpty()) {
                        additiveConditionBuilder.append("|");
                    }
                    additiveConditionBuilder.append(i);
                }

                boolean isTautology = (amount == minAmount); // Console likes to yell if you don't ommit the values in a tautological case
                String additiveCondition = additiveConditionBuilder.toString();

                if (isHorizontal) {
                    for (Direction direction : Direction.Plane.HORIZONTAL) {
                        int yRot = (int) Mth.wrapDegrees(direction.toYRot() + 180);

                        JsonObject defaultCondition = new JsonObject();
                        JsonObject snowloggedCondition = visibleLayers.deepCopy();

                        if (!isTautology) {
                            addProperty(defaultCondition, amountProperty.getName(), additiveCondition);
                        }
                        addProperty(defaultCondition, "facing", direction.getSerializedName());
                        addProperty(defaultCondition, BlockStatePropertiesSD.SNOWLOGGED_LAYERS.getName(), "0");
                        multipart.add(createPart(defaultCondition, createApply(amountModel, yRot, false)));

                        snowloggedCondition.getAsJsonArray("OR").remove(0);
                        if (!isTautology) {
                            addProperty(snowloggedCondition, amountProperty.getName(), additiveCondition);
                        }
                        addProperty(snowloggedCondition, "facing", direction.getSerializedName());
                        multipart.add(createPart(snowloggedCondition, createApply(snowloggedModel, yRot, false)));
                    }
                } else {
                    JsonObject amountCondition = visibleLayers.deepCopy();
                    JsonObject defaultCondition = new JsonObject();

                    if (!isTautology) {
                        addProperty(defaultCondition, amountProperty.getName(), additiveCondition);
                    }

                    addProperty(defaultCondition, BlockStatePropertiesSD.SNOWLOGGED_LAYERS.getName(), "0");
                    multipart.add(createPart(defaultCondition, createApply(amountModel, 0, false)));

                    amountCondition.getAsJsonArray("OR").remove(0);

                    if (!isTautology) {
                        addProperty(amountCondition, amountProperty.getName(), additiveCondition);
                    }
                    multipart.add(createPart(amountCondition, createApply(snowloggedModel, 0, false)));
                }
            }
        }
        addSnowLayers(multipart);

        root.add("multipart", multipart);
        VirtualResourceRegistry.registerResource(stateId, root.toString());
    }

    protected static void generateFenceState(Block block, Identifier blockId) {
        Identifier stateId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "blockstates/" + blockId.getPath() + ".json");
        String modelPath = ModelLocationUtils.getModelLocation(block).toString();
        String postModel = modelPath + "_post";
        String sideModel = modelPath + "_side";

        JsonObject visibleLayers = createMaxLayerCondition(7);
        JsonObject root = new JsonObject();
        JsonArray multipart = new JsonArray();

        multipart.add(createPart(visibleLayers.deepCopy(), createApply(postModel, 0, false)));

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            int yRot = (int) Mth.wrapDegrees(direction.toYRot() + 180);

            JsonObject directionCondition = visibleLayers.deepCopy();

            addProperty(directionCondition, direction.getSerializedName(), "true");
            multipart.add(createPart(directionCondition, createApply(sideModel, yRot, true)));
        }

        addSnowLayers(multipart);
        root.add("multipart", multipart);
        VirtualResourceRegistry.registerResource(stateId, root.toString());
    }

    protected static void generateAltFenceState(Block block, Identifier blockId) {
        String modelPath = ModelLocationUtils.getModelLocation(block).toString();
        String postModel = modelPath + "_post";

        Identifier stateId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "blockstates/" + blockId.getPath() + ".json");

        JsonObject visibleLayers = createMaxLayerCondition(7);
        JsonObject root = new JsonObject();
        JsonArray multipart = new JsonArray();

        multipart.add(createPart(visibleLayers.deepCopy(), createApply(postModel, 0, false)));

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            String sideModel = modelPath + "_side_" + direction.getSerializedName();

            JsonObject directionCondition = visibleLayers.deepCopy();

            addProperty(directionCondition, direction.getSerializedName(), "true");
            multipart.add(createPart(directionCondition, createApply(sideModel, 0, false)));
        }

        addSnowLayers(multipart);

        root.add("multipart", multipart);
        VirtualResourceRegistry.registerResource(stateId, root.toString());
    }

    protected static void generateFenceGateState(Block block, Identifier blockId) {
        String modelPath = ModelLocationUtils.getModelLocation(block).toString();
        String openModel = modelPath + "_open";
        String wallModel = modelPath + "_wall";
        String wallOpenModel = modelPath + "_wall_open";
        
        Identifier stateId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "blockstates/" + blockId.getPath() + ".json");

        JsonObject visibleLayers = createMaxLayerCondition(7);
        JsonObject root = new JsonObject();
        JsonArray multipart = new JsonArray();

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            int yRot = (int) Mth.wrapDegrees(direction.toYRot() + 180);

            for (boolean isOpen : new boolean[]{true, false}) {
                for (boolean inWall : new boolean[]{true, false}) {
                    String model = modelPath;

                    if (isOpen && inWall) {
                        model = wallOpenModel;
                    } else if (isOpen) {
                        model = openModel;
                    } else if (inWall) {
                        model = wallModel;
                    }

                    JsonObject placementCondition = visibleLayers.deepCopy();
                    addProperty(placementCondition, "facing", direction.getSerializedName());
                    addProperty(placementCondition, "open", String.valueOf(isOpen));
                    addProperty(placementCondition, "in_wall", String.valueOf(inWall));

                    multipart.add(createPart(placementCondition, createApply(model, yRot, true)));
                }
            }
        }

        addSnowLayers(multipart);
        root.add("multipart", multipart);
        VirtualResourceRegistry.registerResource(stateId, root.toString());
    }

    protected static void generateWallState(Block block, Identifier blockId) {
        String modelPath = ModelLocationUtils.getModelLocation(block).toString();
        String postModel = modelPath + "_post";
        String sideModel = modelPath + "_side";
        String sideTallModel = modelPath + "_side_tall";

        Identifier stateId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "blockstates/" + blockId.getPath() + ".json");

        JsonObject visibleLayers = createMaxLayerCondition(7);
        JsonObject postCondition = visibleLayers.deepCopy();
        JsonObject root = new JsonObject();
        JsonArray multipart = new JsonArray();
        
        addProperty(postCondition, "up", "true");
        multipart.add(createPart(postCondition, createApply(postModel, 0, false)));

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            int yRot = (int) Mth.wrapDegrees(direction.toYRot() + 180);

            JsonObject lowCondition = visibleLayers.deepCopy();
            JsonObject tallCondition = visibleLayers.deepCopy();

            addProperty(lowCondition, direction.getSerializedName(), "low");
            multipart.add(createPart(lowCondition, createApply(sideModel, yRot, true)));

            addProperty(tallCondition, direction.getSerializedName(), "tall");
            multipart.add(createPart(tallCondition, createApply(sideTallModel, yRot, true)));
        }
        addSnowLayers(multipart);

        root.add("multipart", multipart);
        VirtualResourceRegistry.registerResource(stateId, root.toString());
    }

    protected static void generateCrossState(Identifier blockId, ResourceManager resourceManager) {
        String basePath = blockId.getPath();
        String unwaxedPath;

        boolean isWaxed = basePath.startsWith("waxed_");

        if (isWaxed) {
            unwaxedPath = basePath.substring(6);
        } else {
            unwaxedPath = basePath;
        }

        Identifier sideId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "models/block/" + unwaxedPath + "_side.json");

        if (resourceManager.getResource(sideId).isPresent()) {
            Identifier stateId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "blockstates/" + basePath + ".json");
            Identifier capId = Identifier.fromNamespaceAndPath(blockId.getNamespace(), "models/block/" + unwaxedPath + "_cap.json");

            boolean usesCap = resourceManager.getResource(capId).isPresent();

            JsonObject visibleLayers = createMaxLayerCondition(7);
            JsonObject root = new JsonObject();
            JsonArray multipart = new JsonArray();

            String modelNamespace = blockId.getNamespace();
            String modelPath = modelNamespace + ":block/" + unwaxedPath;
            String sideModel = modelPath + "_side";
            String sideAltModel = modelPath + "_side_alt";
            String postModel = modelPath + (usesCap ? "_cap" : "_noside");
            String postAltModel = modelPath + (usesCap ? "_cap_alt" : "_noside_alt");

            for (Direction direction : Direction.Plane.HORIZONTAL) {
                for (boolean isConnected : new boolean[]{true, false}) {
                    int selectedRotation = 0;
                    String selectedModel;
                    JsonObject directionCondition = visibleLayers.deepCopy();

                    if (isConnected) {
                        selectedModel = (direction == Direction.NORTH || direction == Direction.EAST) ? sideModel : sideAltModel;
                        selectedRotation = (direction == Direction.EAST || direction == Direction.WEST) ? 90 : 0;
                    } else {
                        switch (direction) {
                            case EAST -> selectedModel = postAltModel;
                            case SOUTH -> {
                                selectedModel = usesCap ? postModel : postAltModel;
                                selectedRotation = 90;
                            }
                            case WEST -> {
                                selectedModel = usesCap ? postAltModel : postModel;
                                selectedRotation = usesCap ? 90 : 270;
                            }
                            default -> selectedModel = postModel;
                        }
                    }

                    addProperty(directionCondition, direction.getSerializedName(), String.valueOf(isConnected));
                    multipart.add(createPart(directionCondition, createApply(selectedModel, selectedRotation, true)));
                }
            }
            addUnculledSnowLayers(multipart);

            root.add("multipart", multipart);
            VirtualResourceRegistry.registerResource(stateId, root.toString());
        }
    }

    private static String generateSnowyModel(String targetModelPath, String textureSlot) {
        String namespace = targetModelPath.split(":")[0];
        String snowloggedModelName = targetModelPath.substring(targetModelPath.lastIndexOf('/') + 1) + "_snowy";
        String parent = textureSlot.equals("crop") ? "minecraft:block/crop" : "minecraft:block/cross";

        Identifier modelPath = Identifier.fromNamespaceAndPath(namespace, "models/block/" + snowloggedModelName + ".json");

        JsonObject root = new JsonObject();
        JsonObject textures = new JsonObject();

        root.addProperty("parent", parent);
        root.addProperty("ambientocclusion", true);
        textures.addProperty(textureSlot, namespace + ":block/" + snowloggedModelName);
        root.add("textures", textures);

        VirtualResourceRegistry.registerResource(modelPath, root.toString());
        return namespace + ":block/" + snowloggedModelName;
    }

    /**
     * Provides ambient occlusion to fix snow layer lighting.
     */
    private static String generateAoModel(String targetModelPath) {
        String modelName = "snowlogged_" + targetModelPath.substring(targetModelPath.lastIndexOf('/') + 1);
        Identifier modelPath = UtilSD.identifier("models/block/" + modelName + ".json");

        JsonObject json = new JsonObject();

        json.addProperty("parent", targetModelPath);
        json.addProperty("ambientocclusion", true);

        VirtualResourceRegistry.registerResource(modelPath, json.toString());
        return "subtlyd:block/" + modelName;
    }

    private static void addUnculledSnowLayers(JsonArray multipart) {
        String layers = BlockStatePropertiesSD.SNOWLOGGED_LAYERS.getName();
        String namespace = "subtlyd";

        for (int index = 1; index <= 8; index++) {
            JsonObject layerCondition = new JsonObject();
            String modelName;
            String model;

            layerCondition.addProperty(layers, String.valueOf(index));

            if (index == 8) {
                modelName = "snow_block_unculled";
            } else {
                modelName = "snow_height" + (index * 2) + "_unculled";
            }

            model = namespace + ":block/" + modelName;

            generateUnculledSnowModel(index, modelName);
            multipart.add(createPart(layerCondition, createApply(model, 0, false)));
        }
    }

    private static void generateUnculledSnowModel(int layer, String modelName) {
        int height;

        Identifier modelId = Identifier.fromNamespaceAndPath("subtlyd", "models/block/" + modelName + ".json");

        JsonObject root = new JsonObject();
        JsonObject textures = new JsonObject();
        JsonArray elements = new JsonArray();

        JsonObject element = new JsonObject();
        JsonObject faces = new JsonObject();
        JsonArray from = new JsonArray();
        JsonArray to = new JsonArray();

        if (layer == 8) {
            height = 16;
        } else {
            height = layer * 2;
        }

        textures.addProperty("particle", "minecraft:block/snow");
        textures.addProperty("texture", "minecraft:block/snow");
        root.add("textures", textures);

        from.add(0);
        from.add(0);
        from.add(0);

        to.add(16);
        to.add(height);
        to.add(16);

        element.add("from", from);
        element.add("to", to);

        for (Direction direction : Direction.values()) {
            JsonObject face = new JsonObject();
            JsonArray uv = new JsonArray();

            if (Direction.Plane.VERTICAL.test(direction)) {
                uv.add(0);
                uv.add(0);
                uv.add(16);
                uv.add(16);
            } else {
                uv.add(0);
                uv.add(16 - height);
                uv.add(16);
                uv.add(16);
            }

            face.add("uv", uv);
            face.addProperty("texture", "#texture");

            if (direction == Direction.DOWN) {
                face.addProperty("cullface", direction.getSerializedName());
            }

            faces.add(direction.getSerializedName(), face);
        }

        element.add("faces", faces);
        elements.add(element);
        root.add("elements", elements);

        VirtualResourceRegistry.registerResource(modelId, root.toString());
    }

    private static void addSnowLayers(JsonArray multipart) {
        String layers = BlockStatePropertiesSD.SNOWLOGGED_LAYERS.getName();
        String snowBlock = BuiltInRegistries.BLOCK.getKey(Blocks.SNOW).getNamespace();

        for (int i = 1; i <= 8; i++) {
            JsonObject layerCondition = new JsonObject();
            layerCondition.addProperty(layers, String.valueOf(i));

            String model;

            if (i == 8) {
                model = "minecraft:block/snow_block";
            } else {
                model = snowBlock + ":block/snow_height" + (i * 2);
            }

            multipart.add(createPart(layerCondition, createApply(model, 0, false)));
        }
    }

    private static boolean hasSnowyVariant(Identifier modelId, ResourceManager resourceManager) {
        Identifier path = Identifier.fromNamespaceAndPath(modelId.getNamespace(), "textures/block/" + modelId.getPath() + "_snowy.png");

        if (resourceManager.getResource(path).isPresent()) {
            return true;
        } else {
            String fallbackPath = modelId.getPath().replace("_top", "").replace("_bottom", "");
            Identifier fallback = Identifier.fromNamespaceAndPath(modelId.getNamespace(), "textures/block/" + fallbackPath + "_snowy.png");

            return resourceManager.getResource(fallback).isPresent();
        }
    }

    /**
     * Creates a condition where a block stops being rendered after reaching a specified layer size
     * @param maxLayer The layer in which the texture should stop being rendered at
     * @return The condition as a JSON object
     */
    private static JsonObject createMaxLayerCondition(int maxLayer) {
        JsonObject maxLayerCondition = new JsonObject();
        JsonArray orArray = new JsonArray();

        String layers = BlockStatePropertiesSD.SNOWLOGGED_LAYERS.getName();

        for (int i = 0; i <= maxLayer; i++) {
            JsonObject condition = new JsonObject();
            condition.addProperty(layers, String.valueOf(i));
            orArray.add(condition);
        }

        maxLayerCondition.add("OR", orArray);
        return maxLayerCondition;
    }

    private static JsonObject createConditionPart(String stateKey, String stateValue, String model, int yRot, boolean uvlock) {
        JsonObject condition = new JsonObject();

        condition.addProperty(stateKey, stateValue);
        return createPart(condition, createApply(model, yRot, uvlock));
    }

    private static JsonObject createPart(JsonObject condition, JsonObject apply) {
        JsonObject part = new JsonObject();

        if (condition != null && !condition.isEmpty()) {
            part.add("when", condition);
        }
        part.add("apply", apply);

        return part;
    }

    private static JsonObject createApply(String model, int yRot, boolean uvlock) {
        JsonObject apply = new JsonObject();

        apply.addProperty("model", model);
        if (yRot != 0) {
            apply.addProperty("y", yRot);
        }

        if (uvlock) {
            apply.addProperty("uvlock", true);
        }
        return apply;
    }

    /**
     * A more safe addProperty method
     * @param condition The condition for the property
     * @param key The property key
     * @param value The value of the property
     */
    private static void addProperty(JsonObject condition, String key, String value) {
        if (condition.has("OR")) {
            for (JsonElement element : condition.getAsJsonArray("OR")) {
                element.getAsJsonObject().addProperty(key, value);
            }
        } else {
            condition.addProperty(key, value);
        }
    }
}