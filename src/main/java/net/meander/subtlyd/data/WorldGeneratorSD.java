package net.meander.subtlyd.data;

import com.google.gson.*;
import net.meander.subtlyd.client.gui.screens.TailoredWorldGenSettings;
import net.meander.subtlyd.util.MthSD;
import net.meander.subtlyd.util.Util;
import net.minecraft.SharedConstants;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WorldGeneratorSD implements DataProvider {
    public static final double TERRAIN_SCALER = 2.5;
    //public static final double OCEAN_DEPTH_SCALER = 1.0;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final PackOutput packOutput;

    public WorldGeneratorSD(PackOutput output) {
        packOutput = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Path outputFolder = packOutput.getOutputFolder();
        List<CompletableFuture<?>> futures = new ArrayList<>();

        try {
            JsonObject continents = getModifiedSimpleDensityFunction("continents.json", TailoredWorldGenSettings.continentScale);
            JsonObject biomes = getModifiedOverworldNoiseSettings();
            //JsonObject ocean = getModifiedOceanOffsetSplines();

            Path continentsPath = outputFolder.resolve("data/minecraft/worldgen/density_function/overworld/continents.json");
            Path noisePath = outputFolder.resolve("data/minecraft/worldgen/noise_settings/overworld.json");
            //Path offsetPath = outputFolder.resolve("data/minecraft/worldgen/density_function/overworld/offset.json");

            futures.add(DataProvider.saveStable(cache, continents, continentsPath));
            futures.add(DataProvider.saveStable(cache, biomes, noisePath));
            //futures.add(DataProvider.saveStable(cache, ocean, offsetPath));
        } catch (Exception e) {
            Util.LOGGER.error("Failed to execute datagen tasks: {}", e.getMessage());
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    public static void modifyWorldGeneration(Path rootFolder) {
        try {
            final Path densityFunctions = rootFolder.resolve("data/minecraft/worldgen/density_function/overworld");
            final Path noiseSettings = rootFolder.resolve("data/minecraft/worldgen/noise_settings");

            Files.createDirectories(rootFolder);
            Files.createDirectories(densityFunctions);
            Files.createDirectories(noiseSettings);

            if (!DataGeneratorSD.isDataGeneratorRunning) {
                JsonObject metaRoot = buildMcMeta();

                Files.writeString(rootFolder.resolve("pack.mcmeta"), GSON.toJson(metaRoot));
            }

            Files.writeString(densityFunctions.resolve("continents.json"), GSON.toJson(getModifiedSimpleDensityFunction("continents.json", TailoredWorldGenSettings.continentScale)));
            Files.writeString(noiseSettings.resolve("overworld.json"), GSON.toJson(getModifiedOverworldNoiseSettings()));
            //Files.writeString(densityFunctions.resolve("offset.json"), GSON.toJson(getModifiedOceanOffsetSplines()));
        } catch (Exception e) {
            Util.LOGGER.error("Failed to generate dynamic datapack at runtime: {}", e.getMessage());
        }
    }

    /**
     * Builds the pack.mcmeta file
     * @return pack.mcmeta as a JsonObject
     */
    private static JsonObject buildMcMeta() {
        final int packFormat = SharedConstants.getCurrentVersion().packVersion(PackType.SERVER_DATA).major();
        JsonObject metaRoot = new JsonObject();
        JsonObject packData = new JsonObject();
        JsonObject description = new JsonObject();

        packData.addProperty("pack_format", packFormat);
        description.addProperty("translate", Component.translatable("createWorld.customize.tailored.pack").getString());
        packData.addProperty("min_format", packFormat);
        packData.addProperty("max_format", packFormat);
        packData.add("description", description);
        metaRoot.add("pack", packData);

        return metaRoot;
    }

    /**
     * Modifies a simple overworld density function's scale on the X and Z axes.
     * @param densityFunctionName The name of the density function to modify
     * @param customScaler Player custom scaling factor
     * @return The density function's file
     */
    private static JsonObject getModifiedSimpleDensityFunction(String densityFunctionName, double customScaler) throws Exception {
        try (InputStream fileStream = WorldGeneratorSD.class.getResourceAsStream("/data/minecraft/worldgen/density_function/overworld/" + densityFunctionName)) {
            if (fileStream != null) {
                JsonObject densityFunction = JsonParser.parseReader(new InputStreamReader(fileStream)).getAsJsonObject();
                JsonObject targetNode = densityFunction;

                while (targetNode.has("argument")) {
                    targetNode = targetNode.getAsJsonObject("argument");
                }

                if (targetNode.has("xz_scale")) {
                    final double classicScale = targetNode.get("xz_scale").getAsDouble();
                    final double finalScaler = (1 / TERRAIN_SCALER) * customScaler;
                    JsonObject cache2D = new JsonObject();
                    JsonObject flatCache = new JsonObject();

                    targetNode.addProperty("xz_scale", MthSD.roundToTenThousandths(classicScale * finalScaler));

                    cache2D.addProperty("type", "minecraft:cache_2d");
                    cache2D.add("argument", targetNode);

                    flatCache.addProperty("type", "minecraft:flat_cache");
                    flatCache.add("argument", cache2D);

                    return flatCache;
                }
                return densityFunction;
            } else {
                throw new IllegalStateException("Could not resolve file: " + densityFunctionName);
            }
        }
    }

    /**
     * Stretches climate zones by modifying Overworld noise settings. Increases biome size.
     * @return The overworld.json file
     */
    private static JsonObject getModifiedOverworldNoiseSettings() throws Exception {
        try (InputStream fileStream = WorldGeneratorSD.class.getResourceAsStream("/data/minecraft/worldgen/noise_settings/overworld.json")) {
            if (fileStream != null) {
                JsonObject overworld = JsonParser.parseReader(new InputStreamReader(fileStream)).getAsJsonObject();

                if (overworld.has("noise_router")) {
                    JsonObject noiseRouter = overworld.getAsJsonObject("noise_router");

                    if (noiseRouter.has("temperature")) {
                        JsonObject temperature = noiseRouter.getAsJsonObject("temperature");

                        while (temperature.has("argument")) {
                            temperature = temperature.getAsJsonObject("argument");
                        }

                        if (temperature.has("xz_scale")) {
                            final double classicScale = temperature.get("xz_scale").getAsDouble();
                            final double finalScaler = (1 / TERRAIN_SCALER) * TailoredWorldGenSettings.biomeScale;
                            JsonObject cache2D = new JsonObject();
                            JsonObject flatCache = new JsonObject();

                            temperature.addProperty("xz_scale", MthSD.roundToTenThousandths(classicScale * finalScaler));

                            cache2D.addProperty("type", "minecraft:cache_2d");
                            cache2D.add("argument", temperature);

                            flatCache.addProperty("type", "minecraft:flat_cache");
                            flatCache.add("argument", cache2D);

                            noiseRouter.add("temperature", flatCache);
                        }
                    }

                    if (noiseRouter.has("vegetation")) {
                        JsonObject humidity = noiseRouter.getAsJsonObject("vegetation");

                        while (humidity.has("argument")) {
                            humidity = humidity.getAsJsonObject("argument");
                        }

                        if (humidity.has("xz_scale")) {
                            final double classicScale = humidity.get("xz_scale").getAsDouble();
                            final double finalScaler = (1 / TERRAIN_SCALER) * TailoredWorldGenSettings.biomeScale;
                            JsonObject cache2D = new JsonObject();
                            JsonObject flatCache = new JsonObject();

                            humidity.addProperty("xz_scale", MthSD.roundToTenThousandths(classicScale * finalScaler));

                            cache2D.addProperty("type", "minecraft:cache_2d");
                            cache2D.add("argument", humidity);

                            flatCache.addProperty("type", "minecraft:flat_cache");
                            flatCache.add("argument", cache2D);

                            noiseRouter.add("vegetation", flatCache);
                        }
                    }
                }
                return overworld;
            } else {
                throw new IllegalStateException("Unable to resolve overworld.json");
            }
        }
    }

//    /**
//     * @return The modified ocean depth created by editing offset.json spline point values.
//     */
//    private static JsonObject getModifiedOceanOffsetSplines() throws Exception {
//        try (InputStream fileStream = WorldGeneratorSD.class.getResourceAsStream("/data/minecraft/worldgen/density_function/overworld/offset.json")) {
//            if (fileStream != null) {
//                final double finalScaler = OCEAN_DEPTH_SCALER * TailoredWorldGenSettings.oceanDepth;
//                JsonObject offset = JsonParser.parseReader(new InputStreamReader(fileStream)).getAsJsonObject();
//                JsonObject splineParent = getSplineParent(offset);
//
//                if (splineParent != null && splineParent.has("points")) {
//                    JsonArray oldPoints = splineParent.getAsJsonArray("points");
//                    JsonArray newPoints = new JsonArray();
//
//                    for (JsonElement splinePoint : oldPoints) {
//                        JsonObject point = splinePoint.getAsJsonObject();
//
//                        if (point.has("location")) {
//                            double location = point.get("location").getAsDouble();
//
//                            if (location == -0.16) {
//                                newPoints.add(point);
//                                continue;
//                            } else if (location == -0.18) {
//                                double classicDepth = point.get("value").getAsDouble(); // Vanilla is -0.12
//                                double targetDeepDepth = (classicDepth - 0.1) * Math.max(finalScaler, -2.5);
//                                double shelfDepth = classicDepth + (targetDeepDepth - classicDepth) * 0.2;
//
//                                point.addProperty("value", MthSD.roundToTenThousandths(shelfDepth));
//                                newPoints.add(point);
//
//                                JsonObject slopePoint = new JsonObject();
//                                double midDepth = classicDepth + (targetDeepDepth - classicDepth) * 0.6;
//
//                                slopePoint.addProperty("derivative", 0.0);
//                                slopePoint.addProperty("location", -0.31);
//                                slopePoint.addProperty("value", MthSD.roundToTenThousandths(midDepth));
//                                newPoints.add(slopePoint);
//                                continue;
//                            } else if (location <= -0.44 && point.has("value") && point.get("value").isJsonPrimitive()) {
//                                double classicDepth = point.get("value").getAsDouble();
//
//                                double modifiedDepth = (classicDepth - 0.1) * Math.max(finalScaler, -2.5);
//                                point.addProperty("value", MthSD.roundToTenThousandths(modifiedDepth));
//                                newPoints.add(point);
//                                continue;
//                            }
//                        }
//                        newPoints.add(point);
//                    }
//                    splineParent.add("points", newPoints);
//
//                } else {
//                    Util.LOGGER.warn("Could not resolve offset.json spline parent");
//                }
//                return offset;
//            } else {
//                throw new IllegalStateException("Could not resolve offset.json");
//            }
//        }
//    }
//
//    /**
//     * Recursively searches a file for spline points.
//     * @param jsonFile The JSON file to search
//     * @return Spline points within a JSON file as a JSONArray
//     */
//    private static JsonObject getSplineParent(JsonObject jsonFile) {
//        if (jsonFile.has("spline") && jsonFile.getAsJsonObject("spline").has("coordinate") &&
//                "minecraft:overworld/continents".equals(jsonFile.getAsJsonObject("spline").get("coordinate").getAsString())) {
//            return jsonFile.getAsJsonObject("spline");
//        } else {
//            for (String key : jsonFile.keySet()) {
//                JsonElement element = jsonFile.get(key);
//
//                if (element.isJsonObject()) {
//                    JsonObject foundParent = getSplineParent(element.getAsJsonObject());
//
//                    if (foundParent != null) {
//                        return foundParent;
//                    }
//                } else if (element.isJsonArray()) {
//                    for (JsonElement arrayElem : element.getAsJsonArray()) {
//                        if (arrayElem.isJsonObject()) {
//                            JsonObject foundParent = getSplineParent(arrayElem.getAsJsonObject());
//
//                            if (foundParent != null) {
//                                return foundParent;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return null;
//    }

    @Override
    public String getName() {
        return "Subtly Dungeons Modified Terrain Data Generator";
    }
}
