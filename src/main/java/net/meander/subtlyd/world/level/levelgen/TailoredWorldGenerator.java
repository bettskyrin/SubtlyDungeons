package net.meander.subtlyd.world.level.levelgen;

import com.google.gson.*;
import net.meander.subtlyd.client.gui.screens.TailoredWorldGenConfig;
import net.meander.subtlyd.util.MthSD;
import net.meander.subtlyd.util.Util;
import net.minecraft.SharedConstants;
import net.minecraft.server.packs.PackType;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Dynamic Data Pack generator for the Tailored World Generation system
 */
public class TailoredWorldGenerator {
    private static final double TERRAIN_SCALER = 1.5;
    private static final double OCEAN_DEPTH_SCALER = 2.2;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void modifyWorldGeneration(Path worldDirectory) {
        try {
            final int PACK_FORMAT = SharedConstants.getCurrentVersion().packVersion(PackType.SERVER_DATA).major();
            final Path datapackRoot = worldDirectory.resolve("datapacks/subtlyd_worldgen");
            final Path densityDir = datapackRoot.resolve("data/minecraft/worldgen/density_function/overworld");
            final Path noiseDir = datapackRoot.resolve("data/minecraft/worldgen/noise_settings");

            Files.createDirectories(densityDir);
            Files.createDirectories(noiseDir);

            JsonObject packMeta = new JsonObject();
            JsonObject packData = new JsonObject();
            packData.addProperty("pack_format", PACK_FORMAT);
            packData.addProperty("description", "Subtly Dungeons Custom WorldGen");
            packData.addProperty("min_format", 84);
            packData.addProperty("max_format", PACK_FORMAT);
            packMeta.add("pack", packData);
            Files.writeString(datapackRoot.resolve("pack.mcmeta"), GSON.toJson(packMeta));

            modifyXZScale(densityDir, "continents.json", TailoredWorldGenConfig.continentScale);
            modifyXZScale(densityDir, "erosion.json", TailoredWorldGenConfig.erosionScale);
            modifyOverworldNoiseSettings(noiseDir);

            modifyOffsetSplines(densityDir);
            TailoredWorldGenConfig.saveSettingsToFile(worldDirectory);

        } catch (Exception e) {
            Util.LOGGER.error("Failed to generate dynamic datapack: {}", e.getMessage());
        }
    }

    private static void modifyXZScale(Path dir, String fileName, double sliderScale) throws Exception {
        try (InputStream fileStream = TailoredWorldGenerator.class.getResourceAsStream("/data/minecraft/worldgen/density_function/overworld/" + fileName)) {
            if (fileStream != null) {
                JsonObject json = JsonParser.parseReader(new InputStreamReader(fileStream)).getAsJsonObject();
                JsonObject targetNode = json;

                while (targetNode.has("argument")) {
                    targetNode = targetNode.getAsJsonObject("argument");
                }

                if (targetNode.has("xz_scale")) {
                    double classicScale = targetNode.get("xz_scale").getAsDouble();
                    double totalModifier = TERRAIN_SCALER * sliderScale;

                    targetNode.addProperty("xz_scale", MthSD.roundToHundredth(classicScale / totalModifier));

                    if (json == targetNode) {
                        JsonObject cache2D = new JsonObject();
                        cache2D.addProperty("type", "minecraft:cache_2d");
                        cache2D.add("argument", targetNode);

                        JsonObject flatCache = new JsonObject();
                        flatCache.addProperty("type", "minecraft:flat_cache");
                        flatCache.add("argument", cache2D);

                        json = flatCache;
                    }
                }
                Files.writeString(dir.resolve(fileName), GSON.toJson(json));
            }
        }
    }

    private static void modifyOverworldNoiseSettings(Path noiseDir) throws Exception {
        try (InputStream fileStream = TailoredWorldGenerator.class.getResourceAsStream("/data/minecraft/worldgen/noise_settings/overworld.json")) {
            if (fileStream != null) {
                JsonObject json = JsonParser.parseReader(new InputStreamReader(fileStream)).getAsJsonObject();

                if (json.has("noise_router")) {
                    JsonObject router = json.getAsJsonObject("noise_router");

                    if (router.has("temperature")) {
                        JsonObject classicTempNoise = router.getAsJsonObject("temperature");

                        if (classicTempNoise.has("xz_scale")) {
                            double vanillaScale = classicTempNoise.get("xz_scale").getAsDouble();

                            classicTempNoise.addProperty("xz_scale", MthSD.roundToHundredth(vanillaScale / (TERRAIN_SCALER * TailoredWorldGenConfig.climateScale)));

                            JsonObject cache2D = new JsonObject();
                            cache2D.addProperty("type", "minecraft:cache_2d");
                            cache2D.add("argument", classicTempNoise);

                            JsonObject flatCache = new JsonObject();
                            flatCache.addProperty("type", "minecraft:flat_cache");
                            flatCache.add("argument", cache2D);

                            router.add("temperature", flatCache);
                        }
                    }

                    if (router.has("vegetation")) {
                        JsonObject vanillaHumNoise = router.getAsJsonObject("vegetation");

                        if (vanillaHumNoise.has("xz_scale")) {
                            double classicScale = vanillaHumNoise.get("xz_scale").getAsDouble();

                            vanillaHumNoise.addProperty("xz_scale", MthSD.roundToHundredth(classicScale / (TERRAIN_SCALER * TailoredWorldGenConfig.climateScale)));

                            JsonObject cache2D = new JsonObject();
                            cache2D.addProperty("type", "minecraft:cache_2d");
                            cache2D.add("argument", vanillaHumNoise);

                            JsonObject flatCache = new JsonObject();
                            flatCache.addProperty("type", "minecraft:flat_cache");
                            flatCache.add("argument", cache2D);

                            router.add("vegetation", flatCache);
                        }
                    }
                }
                Files.writeString(noiseDir.resolve("overworld.json"), GSON.toJson(json));
            }
        }
    }

    private static void modifyOffsetSplines(Path densityDir) throws Exception {
        try (InputStream fileStream = TailoredWorldGenerator.class.getResourceAsStream("/data/minecraft/worldgen/density_function/overworld/offset.json")) {
            if (fileStream != null) {
                double totalModifier = OCEAN_DEPTH_SCALER * TailoredWorldGenConfig.oceanDepth;
                JsonObject json = JsonParser.parseReader(new InputStreamReader(fileStream)).getAsJsonObject();
                JsonArray points = findSplinePoints(json);

                if (points != null) {
                    for (JsonElement elem : points) {
                        JsonObject point = elem.getAsJsonObject();

                        if (point.has("location") && point.get("location").getAsDouble() <= 0.05) {
                            if (point.has("value") && point.get("value").isJsonPrimitive()) {
                                double classicDepth = point.get("value").getAsDouble();

                                if (classicDepth <= 0) {
                                    double newDepth = getDepth(totalModifier, classicDepth);
                                    point.addProperty("value", MthSD.roundToHundredth(newDepth));
                                }
                            }
                        }
                    }
                } else {
                    Util.LOGGER.warn("Could not resolve offset spline points");
                }
                Files.writeString(densityDir.resolve("offset.json"), GSON.toJson(json));
            }
        }
    }

    private static double getDepth(double totalModifier, double classicDepth) {
        double newDepth = classicDepth * totalModifier;

        return Math.max(newDepth, -2.5);
    }

    private static JsonArray findSplinePoints(JsonObject json) {
        if (json.has("spline") && json.getAsJsonObject("spline").has("coordinate") &&
                "minecraft:overworld/continents".equals(json.getAsJsonObject("spline").get("coordinate").getAsString())) {
            return json.getAsJsonObject("spline").getAsJsonArray("points");
        }

        for (String key : json.keySet()) {
            JsonElement element = json.get(key);
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
        return null;
    }
}
