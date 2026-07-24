package net.meander.subtlyd.data.worldgen;

import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.meander.subtlyd.client.gui.screens.TailoredWorldGenSettings;
import net.meander.subtlyd.data.worldgen.features.FeatureUtilsSD;
import net.meander.subtlyd.data.worldgen.placement.AquaticPlacementsSD;
import net.meander.subtlyd.data.worldgen.placement.MiscOverworldPlacementsSD;
import net.meander.subtlyd.data.worldgen.placement.VegetationPlacementsSD;
import net.meander.subtlyd.tags.BiomeTagsSD;
import net.meander.subtlyd.util.MthSD;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.SharedConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackFormat;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.util.InclusiveRange;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.feature.FallenTreeFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.treedecorators.ShelfMushroomDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WorldGeneratorSD implements DataProvider {
    private static final double BIOME_SCALER = 1.5;
    public static final double EROSION_ELASTICITY = 0.15;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final PackOutput packOutput;
    private final CompletableFuture<HolderLookup.Provider> completableFuture;

    public WorldGeneratorSD(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        packOutput = output;
        completableFuture = future;
    }

    private static Path resolveResourcePath(Path datapackRoot, ResourceKey<? extends Registry<?>> registryKey, Identifier identifier) {
        return datapackRoot.resolve("data").resolve(identifier.getNamespace()).resolve(registryKey.identifier().getPath()).resolve(identifier.getPath() + ".json");
    }

    private static String resolveResourcePath(ResourceKey<? extends Registry<?>> registryKey, Identifier identifier) {
        return "/data" + "/" + identifier.getNamespace() + "/" +  registryKey.identifier().getPath() + "/" + identifier.getPath() + ".json";
    }

    private static String resolveRegistryPath(ResourceKey<? extends Registry<?>> registryKey) {
        return "/data" + "/" + registryKey.identifier().getNamespace() + "/" + registryKey.identifier().getPath();
    }

    private static void modifyTrees(final Path root, RegistryOps<JsonElement> ops) throws Exception {
        modifyTrees(root, ops, null, null);
    }

    private static void modifyTrees(final Path root, RegistryOps<JsonElement> ops, CachedOutput cache, List<CompletableFuture<?>> futures) throws Exception {
        final int birchBaseHeight = 7;
        final int birchRandHeightA = 2;
        final int birchRandHeightB = 1;
        final int oakBaseHeight = 6;
        final int oakRandHeightA = 2;
        final int oakRandHeightB = 2;
        final int superBirchRandHeightB = 6;
        final Path featurePath = Path.of(resolveRegistryPath(Registries.FEATURE));

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
                baseHeight = birchBaseHeight;
                randHeightA = birchRandHeightA;
                randHeightB = birchRandHeightB;
            } else if (treeType.equals(oakTrees)) {
                baseHeight = oakBaseHeight;
                randHeightA = oakRandHeightA;
                randHeightB = oakRandHeightB;
            } else if (treeType.equals(superBirchTrees)) {
                baseHeight = birchBaseHeight;
                randHeightA = birchRandHeightA;
                randHeightB = superBirchRandHeightB;
            }

            for (ResourceKey<Feature> feature : treeType) {
                JsonObject storedModification = getModifiedStraightTrunkTreeFeature(ops, feature, baseHeight, randHeightA, randHeightB);
                Path outputPath = resolveResourcePath(root, Registries.FEATURE, feature.identifier());

                if (cache != null && futures != null) {
                    futures.add(DataProvider.saveStable(cache, storedModification, outputPath));
                } else {
                    Files.createDirectories(outputPath.getParent());
                    Files.writeString(outputPath, GSON.toJson(storedModification));
                }
            }
        }

        for (ResourceKey<Feature> fallenTreeType : fallenTreeTypes) {
            int minLength = -1;
            int maxLength = -1;

            if (fallenTreeType.equals(TreeFeatures.FALLEN_BIRCH_TREE)) {
                minLength = birchBaseHeight;
                maxLength = birchBaseHeight + birchRandHeightA + birchRandHeightB;
            } else if (fallenTreeType.equals(TreeFeatures.FALLEN_OAK_TREE)) {
                minLength = oakBaseHeight;
                maxLength = oakBaseHeight + oakRandHeightA + oakRandHeightB;
            } if (fallenTreeType.equals(TreeFeatures.FALLEN_SUPER_BIRCH_TREE)) {
                minLength = birchBaseHeight;
                maxLength = birchBaseHeight + birchRandHeightA + superBirchRandHeightB;
            }

            JsonObject storedModification = getModifiedFallenTreeFeature(ops, fallenTreeType.identifier(), maxLength, minLength);
            Path outputPath = resolveResourcePath(root, Registries.FEATURE, fallenTreeType.identifier());

            if (cache != null && futures != null) {
                futures.add(DataProvider.saveStable(cache, storedModification, outputPath));
            } else {
                Files.writeString(featurePath.resolve(fallenTreeType.identifier().getPath()), GSON.toJson(storedModification));
            }
        }
    }

    private static List<ResourceKey<Feature>> getBirchTrees() {
        final ResourceKey<Feature> birch = TreeFeatures.BIRCH;
        final ResourceKey<Feature> birchBees002 = TreeFeatures.BIRCH_BEES_002;
        final ResourceKey<Feature> birchBees0002 = TreeFeatures.BIRCH_BEES_0002;
        final ResourceKey<Feature> birchBees0002LeafLitter = TreeFeatures.BIRCH_BEES_0002_LEAF_LITTER;
        final ResourceKey<Feature> birchBees005 = TreeFeatures.BIRCH_BEES_005;

        return List.of(birch, birchBees002, birchBees0002, birchBees0002LeafLitter, birchBees005);
    }

    private static List<ResourceKey<Feature>> getSuperBirchTrees() {
        final ResourceKey<Feature> superBirchBees = TreeFeatures.SUPER_BIRCH_BEES;
        final ResourceKey<Feature> superBirchBees0002 = TreeFeatures.SUPER_BIRCH_BEES_0002;

        return List.of(superBirchBees, superBirchBees0002);
    }

    private static List<ResourceKey<Feature>> getOakTrees() {
        final ResourceKey<Feature> oak = TreeFeatures.OAK;
        final ResourceKey<Feature> oakBees002 = TreeFeatures.OAK_BEES_002;
        final ResourceKey<Feature> oakBeesLeafLitter = TreeFeatures.OAK_LEAF_LITTER;
        final ResourceKey<Feature> oakBees0002LeafLitter = TreeFeatures.OAK_BEES_0002_LEAF_LITTER;
        final ResourceKey<Feature> oakBees005 = TreeFeatures.OAK_BEES_005;

        return List.of(oak, oakBees002, oakBeesLeafLitter, oakBees0002LeafLitter,  oakBees005);
    }

    private static JsonObject buildPackMeta() {
        int packFormat = SharedConstants.getCurrentVersion().packVersion(PackType.SERVER_DATA).major();
        PackMetadataSection metadata = new PackMetadataSection(Component.translatable("createWorld.tailored.pack"), new InclusiveRange<>(PackFormat.of(packFormat)));
        JsonElement encodedMeta = PackMetadataSection.SERVER_TYPE.codec().encodeStart(JsonOps.INSTANCE, metadata).getOrThrow(IllegalStateException::new);
        JsonObject root = new JsonObject();

        root.add("pack", encodedMeta);
        return root;
    }

    private static DensityFunction scaleDensityNode(DensityFunction node, double scaler) {
        if (node instanceof DensityFunctions.ShiftedNoise shiftedNoise) {
            double newScale = MthSD.roundToTenThousandths(shiftedNoise.xzScale() / scaler);

            return DensityFunctions.shiftedNoise2d(
                    shiftedNoise.shiftX(),
                    shiftedNoise.shiftZ(),
                    newScale,
                    shiftedNoise.noise().noiseData()
            );
        } else if (node instanceof DensityFunctions.Marker(DensityFunctions.Marker.Type type, DensityFunction wrapped)) {
            DensityFunction modifiedInner = scaleDensityNode(wrapped, scaler);

            return new DensityFunctions.Marker(type, modifiedInner);
        }
        return node;
    }

    private static JsonObject getModifiedSimpleDensityFunction(RegistryOps<JsonElement> ops, ResourceKey<DensityFunction> densityFunctionKey, double customScaler) throws Exception {
        try (InputStream fileStream = WorldGeneratorSD.class.getResourceAsStream(resolveResourcePath(Registries.DENSITY_FUNCTION, densityFunctionKey.identifier()))) {
            if (fileStream != null) {
                JsonElement rootElement = JsonParser.parseReader(new InputStreamReader(fileStream));
                DensityFunction original = DensityFunction.CODEC.parse(ops, rootElement).getOrThrow(IllegalStateException::new);
                DensityFunction modified = scaleDensityNode(original, customScaler);
                JsonElement newDensityFunction = DensityFunction.CODEC.encodeStart(ops, modified).getOrThrow(IllegalStateException::new);

                return newDensityFunction.getAsJsonObject();
            } else {
                throw new IllegalStateException("Could not resolve file: " + densityFunctionKey.identifier());
            }
        }
    }

    private static List<TreeDecorator> getModifiedTrunkDecorator(ResourceKey<Feature> featureKey, List<TreeDecorator> decorators) {
        List<TreeDecorator> modifiedDecorators = new ArrayList<>(decorators);

        if (getSuperBirchTrees().contains(featureKey)) {
            modifiedDecorators.add(new ShelfMushroomDecorator(0.8F));
        }

        return modifiedDecorators;
    }

    private static TreeFeature.Builder getTreeFeatureBuilder(TreeFeature original, StraightTrunkPlacer modifiedTrunkPlacer) {
        TreeFeature.Builder builder = new TreeFeature.Builder(
                original.trunkProvider(),
                modifiedTrunkPlacer,
                original.foliageProvider(),
                original.foliagePlacer(),
                original.rootPlacer(),
                original.minimumSize(),
                original.belowTrunkProvider()
        );

        if (original.ignoreVines()) {
            builder.ignoreVines();
        }
        return builder;
    }

    private static JsonObject getModifiedStraightTrunkTreeFeature(RegistryOps<JsonElement> ops, ResourceKey<Feature> featureKey, final int baseHeight, final int randHeightA, final int randHeightB) throws Exception {
        try (InputStream fileStream = WorldGeneratorSD.class.getResourceAsStream(resolveResourcePath(Registries.FEATURE, featureKey.identifier()))) {
            if (fileStream != null) {
                JsonElement rootElement = JsonParser.parseReader(new InputStreamReader(fileStream));
                Feature parsedFeature = Feature.DIRECT_CODEC.parse(ops, rootElement).getOrThrow(IllegalStateException::new);

                if (parsedFeature instanceof TreeFeature original) {
                    StraightTrunkPlacer modifiedTrunkPlacer = new StraightTrunkPlacer(baseHeight, randHeightA, randHeightB);
                    List<TreeDecorator> modifiedDecorators = getModifiedTrunkDecorator(featureKey, original.decorators());
                    TreeFeature.Builder builder = getTreeFeatureBuilder(original, modifiedTrunkPlacer);

                    builder.decorators(modifiedDecorators);

                    Feature modified = builder.build();
                    JsonElement newFeature = Feature.DIRECT_CODEC.encodeStart(ops, modified).getOrThrow(IllegalStateException::new);

                    return newFeature.getAsJsonObject();
                } else {
                    throw new IllegalStateException("Parsed feature is not a TreeFeature: " + featureKey.identifier());
                }
            } else {
                throw new IllegalStateException("Unable to resolve " + featureKey.identifier());
            }
        }
    }

    private static JsonObject getModifiedFallenTreeFeature(RegistryOps<JsonElement> ops, Identifier featureId, final int maxInclusive, final int minInclusive) throws Exception {
        try (InputStream fileStream = WorldGeneratorSD.class.getResourceAsStream(resolveResourcePath(Registries.FEATURE, featureId))) {
            if (fileStream != null) {
                JsonElement rootElement = JsonParser.parseReader(new InputStreamReader(fileStream));
                Feature parsedFeature = Feature.DIRECT_CODEC.parse(ops, rootElement).getOrThrow(IllegalStateException::new);

                if (parsedFeature instanceof FallenTreeFeature original) {
                    FallenTreeFeature modified = new FallenTreeFeature(
                            original.trunkProvider(),
                            UniformInt.of(minInclusive, maxInclusive),
                            original.stumpDecorators(),
                            original.logDecorators()
                    );

                    JsonElement newFallenTreeFeatureElement = Feature.DIRECT_CODEC.encodeStart(ops, modified).getOrThrow(IllegalStateException::new);

                    return newFallenTreeFeatureElement.getAsJsonObject();
                } else {
                    throw new IllegalStateException("Parsed feature is not a FallenTreeFeature: " + featureId);
                }
            } else {
                throw new IllegalStateException("Unable to resolve " + featureId);
            }
        }
    }

    public static void customizeWorldGeneration(Path tempPackDir, HolderLookup.Provider provider) {
        try {
            RegistryOps<JsonElement> ops = provider.createSerializationContext(JsonOps.INSTANCE);
            double EROSION_SCALER = 1.0 + ((TailoredWorldGenSettings.biomeScale - 1.0) * EROSION_ELASTICITY);
            final Path packRoot = tempPackDir.resolve("tailored_worldgen");
            final Path continentsPath = resolveResourcePath(packRoot, Registries.DENSITY_FUNCTION, NoiseRouterData.OVERWORLD_FUNCTIONS.continents().identifier());
            final Path erosionPath = resolveResourcePath(packRoot, Registries.DENSITY_FUNCTION, NoiseRouterData.OVERWORLD_FUNCTIONS.erosion().identifier());
            final Path temperaturePath = resolveResourcePath(packRoot, Registries.DENSITY_FUNCTION, NoiseRouterData.OVERWORLD_FUNCTIONS.temperature().identifier());
            final Path vegetationPath = resolveResourcePath(packRoot, Registries.DENSITY_FUNCTION, NoiseRouterData.OVERWORLD_FUNCTIONS.vegetation().identifier());

            JsonObject packMeta = buildPackMeta();

            Files.createDirectories(packRoot);
            Files.createDirectories(continentsPath.getParent());
            Files.createDirectories(erosionPath.getParent());
            Files.createDirectories(temperaturePath.getParent());
            Files.createDirectories(vegetationPath.getParent());

            Files.writeString(packRoot.resolve(PackResources.PACK_META), GSON.toJson(packMeta));
            Files.writeString(continentsPath, GSON.toJson(getModifiedSimpleDensityFunction(ops, NoiseRouterData.OVERWORLD_FUNCTIONS.continents(),  TailoredWorldGenSettings.continentScale * BIOME_SCALER)));
            Files.writeString(erosionPath, GSON.toJson(getModifiedSimpleDensityFunction(ops, NoiseRouterData.OVERWORLD_FUNCTIONS.erosion(), TailoredWorldGenSettings.erosionScale * EROSION_SCALER)));
            Files.writeString(temperaturePath, GSON.toJson(getModifiedSimpleDensityFunction(ops, NoiseRouterData.OVERWORLD_FUNCTIONS.temperature(), TailoredWorldGenSettings.biomeScale * BIOME_SCALER)));
            Files.writeString(vegetationPath, GSON.toJson(getModifiedSimpleDensityFunction(ops, NoiseRouterData.OVERWORLD_FUNCTIONS.vegetation(), TailoredWorldGenSettings.biomeScale * BIOME_SCALER)));

            modifyTrees(packRoot, ops);
        } catch (Exception e) {
            UtilSD.LOGGER.error("Failed to generate dynamic datapack at runtime: {}", e.getMessage());
        }
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        double EROSION_SCALER = 1.0 + ((TailoredWorldGenSettings.biomeScale - 1.0) * EROSION_ELASTICITY);
        Path outputFolder = packOutput.getOutputFolder();
        List<CompletableFuture<?>> futures = new ArrayList<>();

        try {
            HolderLookup.Provider provider = completableFuture.join();
            RegistryOps<JsonElement> ops = provider.createSerializationContext(JsonOps.INSTANCE);

            JsonObject continents = getModifiedSimpleDensityFunction(ops, NoiseRouterData.OVERWORLD_FUNCTIONS.continents(), TailoredWorldGenSettings.continentScale * BIOME_SCALER);
            JsonObject erosion = getModifiedSimpleDensityFunction(ops, NoiseRouterData.OVERWORLD_FUNCTIONS.erosion(), TailoredWorldGenSettings.erosionScale * EROSION_SCALER);
            JsonObject temperature = getModifiedSimpleDensityFunction(ops, NoiseRouterData.OVERWORLD_FUNCTIONS.temperature(), TailoredWorldGenSettings.biomeScale * BIOME_SCALER);
            JsonObject vegetation = getModifiedSimpleDensityFunction(ops, NoiseRouterData.OVERWORLD_FUNCTIONS.vegetation(), TailoredWorldGenSettings.biomeScale * BIOME_SCALER);

            Path continentsPath = resolveResourcePath(outputFolder, Registries.DENSITY_FUNCTION, NoiseRouterData.OVERWORLD_FUNCTIONS.continents().identifier());
            Path erosionPath = resolveResourcePath(outputFolder, Registries.DENSITY_FUNCTION, NoiseRouterData.OVERWORLD_FUNCTIONS.erosion().identifier());
            Path temperaturePath = resolveResourcePath(outputFolder, Registries.DENSITY_FUNCTION, NoiseRouterData.OVERWORLD_FUNCTIONS.temperature().identifier());
            Path vegetationPath = resolveResourcePath(outputFolder, Registries.DENSITY_FUNCTION, NoiseRouterData.OVERWORLD_FUNCTIONS.vegetation().identifier());

            futures.add(DataProvider.saveStable(cache, continents, continentsPath));
            futures.add(DataProvider.saveStable(cache, erosion, erosionPath));
            futures.add(DataProvider.saveStable(cache, temperature, temperaturePath));
            futures.add(DataProvider.saveStable(cache, vegetation, vegetationPath));
            modifyTrees(outputFolder, ops, cache, futures);
        } catch (Exception e) {
            UtilSD.LOGGER.error("Failed to execute datagen tasks: {}", e.getMessage());
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    @Override
    public String getName() {
        return "World Generation";
    }

    public static class BiomeModifier {
        private static final int SKY_COLOR_DARK = 0x677AA1;
        private static final int FOG_COLOR_DARK = 0x8495B8;
        private static final int FOG_COLOR_SOGGY = 0xCAE8E6;

        public static void run() {
            FeatureUtilsSD.registration();
            modifyFog();
            modifySwampLike();
            modifyForestLike();
            modifyPlainsLike();
            modifySavannaLike();
        }

        private static void modifySwampLike() {
            final int SKY_COLOR = 0xD4E2FA;
            final Identifier mangroveSwampAtmosphere = UtilSD.identifier("mangrove_swamp_atmosphere");
            final Identifier swampAtmosphere = UtilSD.identifier("swamp_atmosphere");
            final Identifier swampFrogWeight = UtilSD.identifier("swamp_frog_weight");

            BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.SWAMP), GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacementsSD.REEDS);
            BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.SWAMP), GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacementsSD.PERSE_WILDFLOWERS_SWAMP);
            BiomeModifications.create(swampAtmosphere).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.SWAMP),
                    (_, biomeModificationContext) -> {
                        biomeModificationContext.getAttributes().set(EnvironmentAttributes.SKY_COLOR, SKY_COLOR);
                        biomeModificationContext.getAttributes().set(EnvironmentAttributes.FOG_COLOR, FOG_COLOR_SOGGY);

                    });
            BiomeModifications.create(swampFrogWeight).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.SWAMP),
                    (_, biomeModificationContext) -> {
                        biomeModificationContext.getMobSpawnSettings().removeSpawnsOfEntityType(EntityTypes.FROG);
                        biomeModificationContext.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityTypes.FROG, new UniformInt(2, 5)), 14);
                    });
            BiomeModifications.create(mangroveSwampAtmosphere).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.MANGROVE_SWAMP),
                    (_, biomeModificationContext) -> biomeModificationContext.getAttributes().set(EnvironmentAttributes.SKY_COLOR, SKY_COLOR));
        }

        private static void modifyForestLike() {
            final Identifier patchBirchGrass = UtilSD.identifier("patch_birch_grass");
            final Identifier darkForestAtmosphere = UtilSD.identifier("dark_forest_atmosphere");

            BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.DAPPLED_FOREST), GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacementsSD.PERSE_WILDFLOWERS_DAPPLED_FOREST);
            BiomeModifications.create(patchBirchGrass)
                    .add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST), (_, biomeModificationContext) -> {
                        biomeModificationContext.getGenerationSettings().removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST);
                        biomeModificationContext.getGenerationSettings().removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.WILDFLOWERS_BIRCH_FOREST);
                        biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacementsSD.PATCH_GRASS_BIRCH_FOREST);
                        biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacementsSD.WILDFLOWERS_BIRCH_FOREST);
                    });
            BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.DARK_FOREST, Biomes.FOREST), GenerationStep.Decoration.LOCAL_MODIFICATIONS, MiscOverworldPlacementsSD.FOREST_ROCK_SPARSE);
            BiomeModifications.create(darkForestAtmosphere).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.DARK_FOREST),
                    (_, biomeModificationContext) -> {
                        biomeModificationContext.getAttributes().set(EnvironmentAttributes.SKY_COLOR, SKY_COLOR_DARK);
                        biomeModificationContext.getAttributes().set(EnvironmentAttributes.FOG_COLOR, FOG_COLOR_DARK);
                    });
            BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.FOREST), MobCategory.CREATURE, EntityTypes.RABBIT, 6, 3, 4);
        }

        private static void modifyPlainsLike() {
            final Identifier wildflowersMeadow =  UtilSD.identifier("wildflowers_meadow");

            BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.PLAINS), MobCategory.CREATURE, EntityTypes.RABBIT, 10, 4, 6);
            BiomeModifications.create(wildflowersMeadow).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.MEADOW),
                    (_, biomeModificationContext) -> {
                        biomeModificationContext.getGenerationSettings().removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.WILDFLOWERS_MEADOW);
                        biomeModificationContext.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacementsSD.WILDFLOWERS_MEADOW);
                    });
        }

        private static void modifySavannaLike() {
            BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA), GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacementsSD.BAOBAB);
        }

        private static void modifyFog() {
            final Identifier fogDistance = UtilSD.identifier("fog_distance");

            BiomeModifications.create(fogDistance).add(ModificationPhase.REPLACEMENTS, BiomeSelectors.tag(BiomeTagsSD.IS_FOGGY),
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
