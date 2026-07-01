package net.meander.subtlyd.world.level.levelgen;

import com.google.gson.*;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.meander.subtlyd.client.gui.screens.TailoredWorldGenSettings;
import net.meander.subtlyd.data.worldgen.placement.AquaticPlacementsSD;
import net.meander.subtlyd.data.worldgen.placement.MiscOverworldPlacementsSD;
import net.meander.subtlyd.data.worldgen.placement.VegetationPlacementsSD;
import net.meander.subtlyd.tags.BiomeTagsSD;
import net.meander.subtlyd.util.MthSD;
import net.meander.subtlyd.util.Util;
import net.minecraft.SharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.network.chat.Component;
import net.minecraft.references.BlockItemIds;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

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

    public static Path resolveRegistryPath(Path datapackRoot, ResourceKey<? extends Registry<?>> registryKey, Identifier identifier) {
        return datapackRoot.resolve("data")
                .resolve(identifier.getNamespace())
                .resolve(registryKey.identifier().getPath())
                .resolve(identifier.getPath() + ".json");
    }

    public static void modifyTreesForTailoredWorld(final Path root) throws Exception {
        modifyTrees(root, null, null);
    }

    public static void modifyTrees(final Path root, @Nullable CachedOutput cache, @Nullable List<CompletableFuture<?>> futures) throws Exception {
        final Path featurePath = root.resolve("data/minecraft/worldgen/feature");

        final int BIRCH_BASE_HEIGHT = 7;
        final int BIRCH_RAND_HEIGHT_A = 2;
        final int BIRCH_RAND_HEIGHT_B = 1;
        final int OAK_BASE_HEIGHT = 6;
        final int OAK_RAND_HEIGHT_A = 2;
        final int OAK_RAND_HEIGHT_B = 2;
        final int SUPER_BIRCH_RAND_HEIGHT_B = 6;

        List<ResourceKey<Feature>> birchTrees = getBirchTrees();
        List<ResourceKey<Feature>> oakTrees = getOakTrees();
        List<ResourceKey<Feature>> superBirchTrees = getSuperBirchTrees();
        List<List<ResourceKey<Feature>>> treeTypes = List.of(birchTrees, oakTrees, superBirchTrees);
        List<ResourceKey<Feature>> fallenTreeTypes = List.of(TreeFeatures.FALLEN_BIRCH_TREE, TreeFeatures.FALLEN_OAK_TREE, TreeFeatures.FALLEN_SUPER_BIRCH_TREE);

        for (List<ResourceKey<Feature>> treeType : treeTypes) {
            int baseHeight = -1;
            int randHeightA = -1;
            int randHeightB = -1;

            if (treeType.equals(birchTrees)) {
                baseHeight = BIRCH_BASE_HEIGHT;
                randHeightA = BIRCH_RAND_HEIGHT_A;
                randHeightB = BIRCH_RAND_HEIGHT_B;
            } else if (treeType.equals(oakTrees)) {
                baseHeight = OAK_BASE_HEIGHT;
                randHeightA = OAK_RAND_HEIGHT_A;
                randHeightB = OAK_RAND_HEIGHT_B;
            } else if (treeType.equals(superBirchTrees)) {
                baseHeight = BIRCH_BASE_HEIGHT;
                randHeightA = BIRCH_RAND_HEIGHT_A;
                randHeightB = SUPER_BIRCH_RAND_HEIGHT_B;
            }

            for (ResourceKey<Feature> feature : treeType) {
                String file = feature.identifier().getPath() + ".json";
                JsonObject storedModification = getModifiedTrunkHeight(file, baseHeight, randHeightA, randHeightB);
                storedModification = modifyTreeDecorators(feature, storedModification);

                if (cache != null && futures != null) {
                    futures.add(DataProvider.saveStable(cache, storedModification, featurePath.resolve(file)));
                } else {
                    Files.writeString(featurePath.resolve(file), GSON.toJson(storedModification));
                }
            }
        }

        for (ResourceKey<Feature> fallenTreeType : fallenTreeTypes) {
            int minLength = -1;
            int maxLength = -1;

            if (fallenTreeType.equals(TreeFeatures.FALLEN_BIRCH_TREE)) {
                minLength = BIRCH_BASE_HEIGHT;
                maxLength = BIRCH_BASE_HEIGHT + BIRCH_RAND_HEIGHT_A + BIRCH_RAND_HEIGHT_B;
            } else if (fallenTreeType.equals(TreeFeatures.FALLEN_OAK_TREE)) {
                minLength = OAK_BASE_HEIGHT;
                maxLength = OAK_BASE_HEIGHT + OAK_RAND_HEIGHT_A + OAK_RAND_HEIGHT_B;
            } if (fallenTreeType.equals(TreeFeatures.FALLEN_SUPER_BIRCH_TREE)) {
                minLength = BIRCH_BASE_HEIGHT;
                maxLength = BIRCH_BASE_HEIGHT + BIRCH_RAND_HEIGHT_A + SUPER_BIRCH_RAND_HEIGHT_B;
            }

            String file = fallenTreeType.identifier().getPath() + ".json";
            JsonObject storedModification = getModifiedFallenLogHeight(file, maxLength, minLength);

            if (cache != null && futures != null) {
                futures.add(DataProvider.saveStable(cache, storedModification, featurePath.resolve(file)));
            } else {
                Files.writeString(featurePath.resolve(file), GSON.toJson(storedModification));
            }
        }
    }

    private static JsonObject modifyTreeDecorators(ResourceKey<Feature> treeType, JsonObject storedModification) {
        if (getBirchTrees().contains(treeType)) {
            JsonObject shelfMushroom = new JsonObject();

            shelfMushroom.addProperty("type", BlockItemIds.SHELF_MUSHROOM.block().identifier().toString());
            shelfMushroom.addProperty("probability", 0.8F);

            return modifyDecorators(storedModification, shelfMushroom);
        }
        return storedModification;
    }

    private static @NonNull List<ResourceKey<Feature>> getBirchTrees() {
        final ResourceKey<Feature> birch = TreeFeatures.BIRCH;
        final ResourceKey<Feature> birchBees002 = TreeFeatures.BIRCH_BEES_002;
        final ResourceKey<Feature> birchBees0002 = TreeFeatures.BIRCH_BEES_0002;
        final ResourceKey<Feature> birchBees0002LeafLitter = TreeFeatures.BIRCH_BEES_0002_LEAF_LITTER;
        final ResourceKey<Feature> birchBees005 = TreeFeatures.BIRCH_BEES_005;

        return List.of(birch, birchBees002, birchBees0002, birchBees0002LeafLitter, birchBees005);
    }

    private static @NonNull List<ResourceKey<Feature>> getSuperBirchTrees() {
        final ResourceKey<Feature> superBirchBees = TreeFeatures.SUPER_BIRCH_BEES;
        final ResourceKey<Feature> superBirchBees0002 = TreeFeatures.SUPER_BIRCH_BEES_0002;

        return List.of(superBirchBees, superBirchBees0002);
    }

    private static @NonNull List<ResourceKey<Feature>> getOakTrees() {
        final ResourceKey<Feature> oak = TreeFeatures.OAK;
        final ResourceKey<Feature> oakBees002 = TreeFeatures.OAK_BEES_002;
        final ResourceKey<Feature> oakBeesLeafLitter = TreeFeatures.OAK_LEAF_LITTER;
        final ResourceKey<Feature> oakBees0002LeafLitter = TreeFeatures.OAK_BEES_0002_LEAF_LITTER;
        final ResourceKey<Feature> oakBees005 = TreeFeatures.OAK_BEES_005;

        return List.of(oak, oakBees002, oakBeesLeafLitter, oakBees0002LeafLitter,  oakBees005);
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

            Path continentsPath = resolveRegistryPath(outputFolder, Registries.DENSITY_FUNCTION, Identifier.withDefaultNamespace("overworld/continents"));
            Path erosionPath = resolveRegistryPath(outputFolder, Registries.DENSITY_FUNCTION, Identifier.withDefaultNamespace("overworld/erosion"));
            Path noisePath = resolveRegistryPath(outputFolder, Registries.NOISE_SETTINGS, Identifier.withDefaultNamespace("overworld"));

            futures.add(DataProvider.saveStable(cache, continents, continentsPath));
            futures.add(DataProvider.saveStable(cache, erosion, erosionPath));
            futures.add(DataProvider.saveStable(cache, biomes, noisePath));
            modifyTrees(outputFolder, cache, futures);
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
            final Path feature = packRoot.resolve("data/minecraft/worldgen/feature");

            final String continentalnessFile = "continents.json";
            final String erosionFile = "erosion.json";
            final String overworldFile = "overworld.json";
            JsonObject packMeta = buildPackMeta();

            Files.createDirectories(densityFunctions);
            Files.createDirectories(noiseSettings);
            Files.createDirectories(feature);

            Files.writeString(packRoot.resolve(PackResources.PACK_META), GSON.toJson(packMeta));

            Files.writeString(densityFunctions.resolve(continentalnessFile), GSON.toJson(getModifiedSimpleDensityFunction(continentalnessFile, TailoredWorldGenSettings.continentScale * BIOME_SCALER)));
            Files.writeString(densityFunctions.resolve(erosionFile), GSON.toJson(getModifiedSimpleDensityFunction(erosionFile, EROSION_SCALE)));
            Files.writeString(noiseSettings.resolve(overworldFile), GSON.toJson(getModifiedOverworldNoiseSettings()));
            modifyTreesForTailoredWorld(packRoot);
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

                while (targetNode.has("argument") && targetNode.get("argument").isJsonObject()) {
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

    private static JsonObject getModifiedTrunkHeight(String fileName, final int baseHeight, final int randHeightA, final int randHeightB) throws Exception {
        try (InputStream fileStream = WorldGeneratorSD.class.getResourceAsStream("/data/minecraft/worldgen/feature/" + fileName)) {
            if (fileStream != null) {
                JsonObject feature = JsonParser.parseReader(new InputStreamReader(fileStream)).getAsJsonObject();

                if (feature.has("trunk_placer")) {
                    JsonObject trunkPlacer = feature.getAsJsonObject("trunk_placer");

                    if (trunkPlacer.has("base_height")) {
                        trunkPlacer.addProperty("base_height", baseHeight);
                    }

                    if (trunkPlacer.has("height_rand_a")) {
                        trunkPlacer.addProperty("height_rand_a", randHeightA);
                    }

                    if (trunkPlacer.has("height_rand_b")) {
                        trunkPlacer.addProperty("height_rand_b", randHeightB);
                    }

                    feature.add("trunk_placer", trunkPlacer);
                    return feature;
                } else {
                    throw new IllegalStateException("Unable to resolve trunk placer at: " + fileName);
                }
            } else {
                throw new IllegalStateException("Unable to resolve " + fileName);
            }
        }
    }

    private static JsonObject getModifiedFallenLogHeight(String fileName, final int maxInclusive, final int minInclusive) throws Exception {
        try (InputStream fileStream = WorldGeneratorSD.class.getResourceAsStream("/data/minecraft/worldgen/feature/" + fileName)) {
            if (fileStream != null) {
                JsonObject feature = JsonParser.parseReader(new InputStreamReader(fileStream)).getAsJsonObject();

                if (feature.has("log_length")) {
                    JsonElement logLengthElement = feature.get("log_length");

                    if (logLengthElement.isJsonObject()) {
                        JsonObject logLength = logLengthElement.getAsJsonObject();

                        if (logLength.has("max_inclusive")) {
                            logLength.addProperty("max_inclusive", maxInclusive);
                        }
                        if (logLength.has("min_inclusive")) {
                            logLength.addProperty("min_inclusive", minInclusive);
                        }

                        feature.add("log_length", logLength);
                    } else if (logLengthElement.isJsonPrimitive()) {
                        JsonObject logLength = new JsonObject();
                        logLength.addProperty("type", "minecraft:uniform");
                        logLength.addProperty("max_inclusive", maxInclusive);
                        logLength.addProperty("min_inclusive", minInclusive);

                        feature.add("log_length", logLength);
                    }
                    return feature;
                } else {
                    throw new IllegalStateException("Unable to resolve log length at: " + fileName);
                }
            } else {
                throw new IllegalStateException("Unable to resolve " + fileName);
            }
        }
    }

    private static JsonObject modifyDecorators(JsonObject feature, JsonObject decorator) {
        JsonArray decorators = feature.has("decorators") ? feature.getAsJsonArray("decorators") : new JsonArray();

        decorators.add(decorator);
        feature.add("decorators", decorators);

        return feature;
    }

    /**
     * Adds a 2D cache to the noise router argument and modifies its value.
     * @param noiseRouter The noise router object
     * @param key The key to search for within the noise router
     */
    private static void modifyNoiseRouterArgument(JsonObject noiseRouter, String key) {
        if (noiseRouter.has(key) && noiseRouter.get(key).isJsonObject()) {
            JsonObject node = noiseRouter.getAsJsonObject(key);

            while (node.has("argument") && node.get("argument").isJsonObject()) {
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

    public static class Modifier {
        private static final int SKY_COLOR_DARK = 0x677AA1;
        private static final int FOG_COLOR_DARK = 0x8495B8;
        private static final int FOG_COLOR_SOGGY = 0xCAE8E6;

        public static void run() {
            fog();
            swamp();
            forest();
            plains();
        }

        private static void swamp() {
            final int SKY_COLOR = 0xD4E2FA;

            Identifier MANGROVE_SWAMP_ATMOSPHERE = Util.identifier("mangrove_swamp_atmosphere");
            Identifier SWAMP_ATMOSPHERE = Util.identifier("swamp_atmosphere");
            Identifier SWAMP_FROG_WEIGHT = Util.identifier("swamp_frog_weight");

            BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.SWAMP), GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacementsSD.REEDS);
            BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.SWAMP), GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacementsSD.PERSE_WILDFLOWERS_SWAMP);
            BiomeModifications.create(SWAMP_ATMOSPHERE).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.SWAMP),
                    (_, biomeModificationContext) -> {
                        biomeModificationContext.getAttributes().set(EnvironmentAttributes.SKY_COLOR, SKY_COLOR);
                        biomeModificationContext.getAttributes().set(EnvironmentAttributes.FOG_COLOR, FOG_COLOR_SOGGY);

                    });
            BiomeModifications.create(SWAMP_FROG_WEIGHT).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.SWAMP),
                    (_, biomeModificationContext) -> {
                        biomeModificationContext.getMobSpawnSettings().removeSpawnsOfEntityType(EntityTypes.FROG);
                        biomeModificationContext.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityTypes.FROG, 2, 5), 14);
                    });
            BiomeModifications.create(MANGROVE_SWAMP_ATMOSPHERE).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.MANGROVE_SWAMP),
                    (_, biomeModificationContext) ->
                            biomeModificationContext.getAttributes().set(EnvironmentAttributes.SKY_COLOR, SKY_COLOR));
        }

        private static void forest() {
            Identifier PATCH_BIRCH_GRASS = Util.identifier("patch_birch_grass");
            Identifier DARK_FOREST_ATMOSPHERE = Util.identifier("dark_forest_atmosphere");

            BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.DAPPLED_FOREST), GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacementsSD.PERSE_WILDFLOWERS_DAPPLED_FOREST);
            BiomeModifications.create(PATCH_BIRCH_GRASS).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST),
                    (_, biomeModificationContext) -> {
                        biomeModificationContext.getGenerationSettings().removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST);
                        biomeModificationContext.getGenerationSettings().removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.WILDFLOWERS_BIRCH_FOREST);
                        biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacementsSD.PATCH_GRASS_BIRCH_FOREST);
                        biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacementsSD.WILDFLOWERS_BIRCH_FOREST);
                    });
            BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.DARK_FOREST, Biomes.FOREST), GenerationStep.Decoration.LOCAL_MODIFICATIONS, MiscOverworldPlacementsSD.FOREST_ROCK_SPARSE);
            BiomeModifications.create(DARK_FOREST_ATMOSPHERE).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.DARK_FOREST),
                    (_, biomeModificationContext) -> {
                        biomeModificationContext.getAttributes().set(EnvironmentAttributes.SKY_COLOR, SKY_COLOR_DARK);
                        biomeModificationContext.getAttributes().set(EnvironmentAttributes.FOG_COLOR, FOG_COLOR_DARK);
                    });
            BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.FOREST), MobCategory.CREATURE, EntityTypes.RABBIT, 6, 3, 4);
        }

        private static void plains() {
            Identifier wildflowersMeadow =  Util.identifier("wildflowers_meadow");

            BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.PLAINS), MobCategory.CREATURE, EntityTypes.RABBIT, 10, 4, 6);
            BiomeModifications.create(wildflowersMeadow).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.MEADOW),
                    (_, biomeModificationContext) -> {
                        biomeModificationContext.getGenerationSettings().removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.WILDFLOWERS_MEADOW);
                        biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacementsSD.WILDFLOWERS_MEADOW);
                    });
        }

        private static void fog() {
            final Identifier FOG_DISTANCE = Util.identifier("fog_distance");

            BiomeModifications.create(FOG_DISTANCE)
                    .add(ModificationPhase.REPLACEMENTS, BiomeSelectors.tag(BiomeTagsSD.IS_FOGGY),
                        ((_, biomeModificationContext) -> {
                            biomeModificationContext.getAttributes().set(EnvironmentAttributes.FOG_START_DISTANCE, 16.0F);
                            biomeModificationContext.getAttributes().set(EnvironmentAttributes.FOG_END_DISTANCE, 64.0F);
                        }))
            .add(ModificationPhase.REPLACEMENTS, BiomeSelectors.tag(BiomeTagsSD.IS_VERY_FOGGY),
                    ((_, biomeModificationContext) -> {
                        biomeModificationContext.getAttributes().set(EnvironmentAttributes.FOG_START_DISTANCE, 8.0F);
                        biomeModificationContext.getAttributes().set(EnvironmentAttributes.FOG_END_DISTANCE, 32.0F);
                    }));
        }
    }
}
