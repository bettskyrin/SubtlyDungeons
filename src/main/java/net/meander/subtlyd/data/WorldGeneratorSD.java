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
    private static final double TERRAIN_SCALER = 1.5;
    private static final double OCEAN_DEPTH_SCALER = 2.25;
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
            JsonObject erosion = getModifiedSimpleDensityFunction("erosion.json", TailoredWorldGenSettings.erosionScale);
            JsonObject climate = getModifiedOverworldNoiseSettings();
            JsonObject ocean = getModifiedOceanOffsetSplines();

            Path continentsPath = outputFolder.resolve("data/minecraft/worldgen/density_function/overworld/continents.json");
            Path erosionPath = outputFolder.resolve("data/minecraft/worldgen/density_function/overworld/erosion.json");
            Path noisePath = outputFolder.resolve("data/minecraft/worldgen/noise_settings/overworld.json");
            Path offsetPath = outputFolder.resolve("data/minecraft/worldgen/density_function/overworld/offset.json");

            futures.add(DataProvider.saveStable(cache, continents, continentsPath));
            futures.add(DataProvider.saveStable(cache, erosion, erosionPath));
            futures.add(DataProvider.saveStable(cache, climate, noisePath));
            futures.add(DataProvider.saveStable(cache, ocean, offsetPath));
        } catch (Exception e) {
            Util.LOGGER.error("Failed to execute datagen tasks: {}", e.getMessage());
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    public static void modifyWorldGeneration(Path rootFolder) {
        try {
            final Path densityFunctions = rootFolder.resolve("data/minecraft/worldgen/density_function/overworld");
            final Path noiseSettings = rootFolder.resolve("data/minecraft/worldgen/noise_settings");

            if (!DataGeneratorSD.isDataGeneratorRunning) {
                JsonObject metaRoot = buildMcMeta();
                Files.writeString(rootFolder.resolve("pack.mcmeta"), GSON.toJson(metaRoot));
            }

            Files.createDirectories(densityFunctions);
            Files.createDirectories(noiseSettings);

            Files.writeString(densityFunctions.resolve("continents.json"), GSON.toJson(getModifiedSimpleDensityFunction("continents.json", TailoredWorldGenSettings.continentScale)));
            Files.writeString(densityFunctions.resolve("erosion.json"), GSON.toJson(getModifiedSimpleDensityFunction("erosion.json", TailoredWorldGenSettings.erosionScale)));
            Files.writeString(noiseSettings.resolve("overworld.json"), GSON.toJson(getModifiedOverworldNoiseSettings()));
            Files.writeString(densityFunctions.resolve("offset.json"), GSON.toJson(getModifiedOceanOffsetSplines()));
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
                    double finalScaler = TERRAIN_SCALER * customScaler;

                    targetNode.addProperty("xz_scale", MthSD.roundToTenThousandths(classicScale / finalScaler));

                    if (densityFunction == targetNode) {
                        JsonObject cache2D = new JsonObject();
                        JsonObject flatCache = new JsonObject();

                        cache2D.addProperty("type", "minecraft:cache_2d");
                        cache2D.add("argument", targetNode);

                        flatCache.addProperty("type", "minecraft:flat_cache");
                        flatCache.add("argument", cache2D);

                        densityFunction = flatCache;
                    }
                }
                return densityFunction;
            } else {
                throw new IllegalStateException("Unable to resolve file: " + densityFunctionName);
            }
        }
    }

    /**
     * Stretches climate zones by modifying Overworld noise settings.
     * @return The overworld.json file
     */
    private static JsonObject getModifiedOverworldNoiseSettings() throws Exception {
        try (InputStream fileStream = WorldGeneratorSD.class.getResourceAsStream("/data/minecraft/worldgen/noise_settings/overworld.json")) {
            if (fileStream == null) throw new IllegalStateException("Missing vanilla overworld.json");

            JsonObject overworld = JsonParser.parseReader(new InputStreamReader(fileStream)).getAsJsonObject();

            if (overworld.has("noise_router")) {
                JsonObject noiseRouter = overworld.getAsJsonObject("noise_router");

                if (noiseRouter.has("temperature")) {
                    JsonObject temperature = noiseRouter.getAsJsonObject("temperature");

                    if (temperature.has("xz_scale")) {
                        final double classicScale = temperature.get("xz_scale").getAsDouble();
                        double finalScaler = TERRAIN_SCALER * TailoredWorldGenSettings.climateScale;

                        temperature.addProperty("xz_scale", MthSD.roundToTenThousandths(classicScale / finalScaler));

                        JsonObject cache2D = new JsonObject();
                        JsonObject flatCache = new JsonObject();

                        cache2D.addProperty("type", "minecraft:cache_2d");
                        cache2D.add("argument", temperature);

                        flatCache.addProperty("type", "minecraft:flat_cache");
                        flatCache.add("argument", cache2D);

                        noiseRouter.add("temperature", flatCache);
                    }
                }

                if (noiseRouter.has("vegetation")) {
                    JsonObject humidity = noiseRouter.getAsJsonObject("vegetation");

                    if (humidity.has("xz_scale")) {
                        double classicScale = humidity.get("xz_scale").getAsDouble();

                        humidity.addProperty("xz_scale", MthSD.roundToTenThousandths(classicScale / (TERRAIN_SCALER * TailoredWorldGenSettings.climateScale)));

                        JsonObject cache2D = new JsonObject();
                        JsonObject flatCache = new JsonObject();

                        cache2D.addProperty("type", "minecraft:cache_2d");
                        cache2D.add("argument", humidity);

                        flatCache.addProperty("type", "minecraft:flat_cache");
                        flatCache.add("argument", cache2D);

                        noiseRouter.add("vegetation", flatCache);
                    }
                }
            }
            return overworld;
        }
    }

    /**
     * @return The modified ocean depth created by editing offset.json spline point values.
     */
    private static JsonObject getModifiedOceanOffsetSplines() throws Exception {
        try (InputStream fileStream = WorldGeneratorSD.class.getResourceAsStream("/data/minecraft/worldgen/density_function/overworld/offset.json")) {
            if (fileStream != null) {
                double finalScaler = OCEAN_DEPTH_SCALER * TailoredWorldGenSettings.oceanDepth;
                JsonObject offset = JsonParser.parseReader(new InputStreamReader(fileStream)).getAsJsonObject();
                JsonArray splinePoints = findSplinePoints(offset);

                if (splinePoints != null) {
                    for (JsonElement splinePoint : splinePoints) {
                        JsonObject point = splinePoint.getAsJsonObject();

                        if (point.has("location") && point.get("location").getAsDouble() <= 0.05) {
                            if (point.has("value") && point.get("value").isJsonPrimitive()) {
                                double classicDepth = point.get("value").getAsDouble();

                                if (classicDepth <= 0) {
                                    double newDepth = Math.max(classicDepth * finalScaler, -2.5);
                                    point.addProperty("value", MthSD.roundToTenThousandths(newDepth));
                                }
                            }
                        }
                    }
                } else {
                    Util.LOGGER.warn("Could not resolve offset spline points");
                }
                return offset;
            } else {
                throw new IllegalStateException("Could not resolve offset.json");
            }
        }
    }


    /**
     * Recursively searches a file for spline points.
     * @param jsonFile The JSON file to search
     * @return Spline points within a JSON file as a JSONArray
     */
    private static JsonArray findSplinePoints(JsonObject jsonFile) {
        if (jsonFile.has("spline") && jsonFile.getAsJsonObject("spline").has("coordinate") &&
                "minecraft:overworld/continents".equals(jsonFile.getAsJsonObject("spline").get("coordinate").getAsString())) {
            return jsonFile.getAsJsonObject("spline").getAsJsonArray("points");
        } else {
            for (String key : jsonFile.keySet()) {
                JsonElement element = jsonFile.get(key);

                if (element.isJsonObject()) {
                    JsonArray foundPoints = findSplinePoints(element.getAsJsonObject());

                    if (foundPoints != null) {
                        return foundPoints;
                    }
                } else if (element.isJsonArray()) {
                    for (JsonElement arrayElem : element.getAsJsonArray()) {
                        if (arrayElem.isJsonObject()) {
                            JsonArray foundPoints = findSplinePoints(arrayElem.getAsJsonObject());

                            if (foundPoints != null) {
                                return foundPoints;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return "Subtly Dungeons Modified Terrain Data Generator";
    }
}
