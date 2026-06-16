package net.meander.subtlyd.world.level.levelgen;

import com.google.gson.*;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.meander.subtlyd.client.gui.screens.TailoredWorldGenSettings;
import net.meander.subtlyd.data.DataGeneratorSD;
import net.meander.subtlyd.util.MthSD;
import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.level.levelgen.feature.PlacedFeaturesSD;
import net.minecraft.SharedConstants;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WorldGeneratorSD implements DataProvider {
    private static final double BIOME_SCALER = 1.5;
    private static final double EROSION_ELASTICITY = 0.15;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final PackOutput packOutput;

    public WorldGeneratorSD(PackOutput output) {
        packOutput = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        double EROSION_SCALE = 1.0 + ((TailoredWorldGenSettings.biomeScale - 1.0) * EROSION_ELASTICITY);
        Path outputFolder = packOutput.getOutputFolder();
        List<CompletableFuture<?>> futures = new ArrayList<>();

        try {
            JsonObject continents = getModifiedSimpleDensityFunction("continents.json", TailoredWorldGenSettings.continentScale * BIOME_SCALER);
            JsonObject erosion = getModifiedSimpleDensityFunction("erosion.json", EROSION_SCALE);
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

    public static void modifyWorldGeneration(Path tempPackDir) {
        try {
            double EROSION_SCALE = 1.0 + ((TailoredWorldGenSettings.biomeScale - 1.0) * EROSION_ELASTICITY);
            final Path packRoot = tempPackDir.resolve("tailored_worldgen");
            final Path densityFunctions = packRoot.resolve("data/minecraft/worldgen/density_function/overworld");
            final Path noiseSettings = packRoot.resolve("data/minecraft/worldgen/noise_settings");

            Files.createDirectories(densityFunctions);
            Files.createDirectories(noiseSettings);

            if (!DataGeneratorSD.isDataGeneratorRunning) {
                JsonObject metaRoot = buildMcMeta();

                Files.writeString(packRoot.resolve("pack.mcmeta"), GSON.toJson(metaRoot));
            }

            Files.writeString(densityFunctions.resolve("continents.json"), GSON.toJson(getModifiedSimpleDensityFunction("continents.json", TailoredWorldGenSettings.continentScale * BIOME_SCALER)));
            Files.writeString(densityFunctions.resolve("erosion.json"), GSON.toJson(getModifiedSimpleDensityFunction("erosion.json", EROSION_SCALE)));
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
     * @param fileName The file name of the density function to modify
     * @param scaler Player custom scaling factor
     * @return The density function's file
     */
    private static JsonObject getModifiedSimpleDensityFunction(String fileName, double scaler) throws Exception {
        try (InputStream fileStream = WorldGeneratorSD.class.getResourceAsStream("/data/minecraft/worldgen/density_function/overworld/" + fileName)) {
            if (fileStream != null) {
                JsonObject densityFunction = JsonParser.parseReader(new InputStreamReader(fileStream)).getAsJsonObject();
                JsonObject targetNode = densityFunction;

                while (targetNode.has("argument")) {
                    targetNode = targetNode.getAsJsonObject("argument");
                }

                if (targetNode.has("xz_scale")) {
                    final double classicScale = targetNode.get("xz_scale").getAsDouble();
                    final double finalScaler = 1.0 / scaler;
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
                throw new IllegalStateException("Could not resolve file: " + fileName);
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
                    modifyNoiseRouterArgument(noiseRouter, "temperature");
                    modifyNoiseRouterArgument(noiseRouter, "vegetation");
                }
                return overworld;
            } else {
                throw new IllegalStateException("Unable to resolve overworld.json");
            }
        }
    }

    /**
     * Adds a 2D cache to the noise router argument and modifies its value.
     * @param noiseRouter The noise router object
     * @param key The key to search for within the noise router
     */
    private static void modifyNoiseRouterArgument(JsonObject noiseRouter, String key) {
        if (noiseRouter.has(key)) {
            JsonObject node = noiseRouter.getAsJsonObject(key);

            while (node.has("argument")) {
                node = node.getAsJsonObject("argument");
            }

            if (node.has("xz_scale")) {
                double scale = BIOME_SCALER * TailoredWorldGenSettings.biomeScale;
                final double classicScale = node.get("xz_scale").getAsDouble();
                final double finalScaler = 1.0 / scale;

                JsonObject cache2D = new JsonObject();
                JsonObject flatCache = new JsonObject();

                node.addProperty("xz_scale", MthSD.roundToTenThousandths(classicScale * finalScaler));

                cache2D.addProperty("type", "minecraft:cache_2d");
                cache2D.add("argument", node);

                flatCache.addProperty("type", "minecraft:flat_cache");
                flatCache.add("argument", cache2D);

                noiseRouter.add(key, flatCache);
            }
        }
    }

    public static void modifyBiomes() {
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.SWAMP), GenerationStep.Decoration.VEGETAL_DECORATION, PlacedFeaturesSD.REEDS);
    }

    @Override
    public String getName() {
        return "Modified Terrain Data Generator";
    }
}
