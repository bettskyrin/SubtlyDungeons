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
    public static final double TERRAIN_SCALER = 2.0;
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
            JsonObject erosion = getModifiedSimpleDensityFunction("erosion.json", TailoredWorldGenSettings.biomeScale);
            JsonObject biomes = getModifiedOverworldNoiseSettings();

            Path continentsPath = outputFolder.resolve("data/minecraft/worldgen/density_function/overworld/continents.json");
            Path erosionPath = outputFolder.resolve("data/minecraft/worldgen/density_function/overworld/erosion.json");
            Path noisePath = outputFolder.resolve("data/minecraft/worldgen/noise_settings/overworld.json");

            futures.add(DataProvider.saveStable(cache, continents, continentsPath));
            futures.add(DataProvider.saveStable(cache, erosion, erosionPath));
            futures.add(DataProvider.saveStable(cache, biomes, noisePath));
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
            Files.writeString(densityFunctions.resolve("erosion.json"), GSON.toJson(getModifiedSimpleDensityFunction("erosion.json", TailoredWorldGenSettings.biomeScale)));
            Files.writeString(noiseSettings.resolve("overworld.json"), GSON.toJson(getModifiedOverworldNoiseSettings()));
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

    @Override
    public String getName() {
        return "Subtly Dungeons Modified Terrain Data Generator";
    }
}
