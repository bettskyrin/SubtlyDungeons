package net.meander.subtlyd.world.level.levelgen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.meander.subtlyd.client.gui.screens.TailoredWorldGenSettings;
import net.meander.subtlyd.data.DataGeneratorSD;
import net.meander.subtlyd.data.worldgen.placement.AquaticPlacementsSD;
import net.meander.subtlyd.data.worldgen.placement.MiscOverworldPlacementsSD;
import net.meander.subtlyd.data.worldgen.placement.VegetationPlacementsSD;
import net.meander.subtlyd.util.MthSD;
import net.meander.subtlyd.util.Util;
import net.minecraft.SharedConstants;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
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

    public static void modifyBiomes() {
        Identifier PATCH_BIRCH_GRASS = Util.identifier("patch_birch_grass");
        Identifier DARK_FOREST_ATMOSPHERE = Util.identifier("dark_forest_atmosphere");
        Identifier MANGROVE_SWAMP_ATMOSPHERE = Util.identifier("mangrove_swamp_atmosphere");
        Identifier SWAMP_ATMOSPHERE = Util.identifier("swamp_atmosphere");
        Identifier SWAMP_FROG_WEIGHT = Util.identifier("swamp_frog_weight");

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.SWAMP), GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacementsSD.REEDS);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.DAPPLED_FOREST), GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacementsSD.PERSE_WILDFLOWERS_DAPPLED_FOREST);
        BiomeModifications.create(PATCH_BIRCH_GRASS).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST),
                (_, biomeModificationContext) -> {
                    biomeModificationContext.getGenerationSettings().removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST);
                    biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacementsSD.PATCH_BIRCH_FOREST);
                });
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.DARK_FOREST, Biomes.FOREST), GenerationStep.Decoration.LOCAL_MODIFICATIONS, MiscOverworldPlacementsSD.FOREST_ROCK_SPARSE);
        BiomeModifications.create(DARK_FOREST_ATMOSPHERE).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.FOREST),
                (_, biomeModificationContext) -> {
                    biomeModificationContext.getAttributes().set(EnvironmentAttributes.SKY_COLOR, 0x677AA1);
                    biomeModificationContext.getAttributes().set(EnvironmentAttributes.FOG_COLOR, 0x8495B8);
                });
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.FOREST), MobCategory.CREATURE, EntityTypes.RABBIT, 6, 3, 4);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.PLAINS), MobCategory.CREATURE, EntityTypes.RABBIT, 10, 4, 6);
        BiomeModifications.create(MANGROVE_SWAMP_ATMOSPHERE).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.MANGROVE_SWAMP),
                (_, biomeModificationContext) ->
                        biomeModificationContext.getAttributes().set(EnvironmentAttributes.SKY_COLOR, 0xD4E2FA));
        BiomeModifications.create(SWAMP_ATMOSPHERE).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.SWAMP),
                (_, biomeModificationContext) -> {
                    biomeModificationContext.getAttributes().set(EnvironmentAttributes.SKY_COLOR, 0xD4E2FA);
                    biomeModificationContext.getAttributes().set(EnvironmentAttributes.FOG_COLOR, 0xCAE8E6);
                });
        BiomeModifications.create(SWAMP_FROG_WEIGHT).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.SWAMP),
                (_, biomeModificationContext) -> {
                    biomeModificationContext.getMobSpawnSettings().removeSpawnsOfEntityType(EntityTypes.FROG);
                    biomeModificationContext.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityTypes.FROG, 2, 5), 14);
                });

        // TODO FOGGY Biomes
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
            final String continentalnessFile = "continents.json";
            final String erosionFile = "erosion.json";
            final String overworldFile = "overworld.json";

            Files.createDirectories(densityFunctions);
            Files.createDirectories(noiseSettings);

            if (!DataGeneratorSD.isDataGeneratorRunning) {
                JsonObject metaRoot = buildPackMeta();

                Files.writeString(packRoot.resolve(PackResources.PACK_META), GSON.toJson(metaRoot));
            }

            Files.writeString(densityFunctions.resolve(continentalnessFile), GSON.toJson(getModifiedSimpleDensityFunction(continentalnessFile, TailoredWorldGenSettings.continentScale * BIOME_SCALER)));
            Files.writeString(densityFunctions.resolve(erosionFile), GSON.toJson(getModifiedSimpleDensityFunction(erosionFile, EROSION_SCALE)));
            Files.writeString(noiseSettings.resolve(overworldFile), GSON.toJson(getModifiedOverworldNoiseSettings()));
        } catch (Exception e) {
            Util.LOGGER.error("Failed to generate dynamic datapack at runtime: {}", e.getMessage());
        }
    }

    /**
     * Builds the pack.mcmeta file
     * @return pack.mcmeta as a JsonObject
     */
    private static JsonObject buildPackMeta() {
        final int packFormat = SharedConstants.getCurrentVersion().packVersion(PackType.SERVER_DATA).major();
        JsonObject metaRoot = new JsonObject();
        JsonObject packData = new JsonObject();
        JsonObject description = new JsonObject();

        packData.addProperty("pack_format", packFormat);
        description.addProperty("translate", Component.translatable("createWorld.tailored.pack").getString());
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

    @Override
    public String getName() {
        return "Modified Terrain Data Generator";
    }
}
