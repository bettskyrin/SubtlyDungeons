package net.meander.subtlyd.client.gui.screens;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.meander.subtlyd.data.worldgen.WorldGeneratorSD;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import java.nio.file.Files;
import java.nio.file.Path;

public class CustomTerrainSettings {
    public static boolean isWorldCopy = false;
    public static double masterScale = CustomTerrainSettingsScreen.initialMaster;
    public static double continentScale = CustomTerrainSettingsScreen.initialContinent;
    public static double biomeScale = CustomTerrainSettingsScreen.initialBiome;
    public static double erosionScale = CustomTerrainSettingsScreen.initialErosion;
    public static double oceanDepthScale = CustomTerrainSettingsScreen.initialOceanDepth;
    private static final String dataFile = "custom_terrain_settings.json";

    public record TerrainData(double masterScale, double continentScale, double biomeScale, double erosionScale, double oceanDepthScale) {
        public static final Codec<TerrainData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.DOUBLE.optionalFieldOf("master_scale", CustomTerrainSettingsScreen.initialMaster).forGetter(TerrainData::masterScale),
                Codec.DOUBLE.optionalFieldOf("continent_scale", CustomTerrainSettingsScreen.initialContinent).forGetter(TerrainData::continentScale),
                Codec.DOUBLE.optionalFieldOf("biome_scale", CustomTerrainSettingsScreen.initialBiome).forGetter(TerrainData::biomeScale),
                Codec.DOUBLE.optionalFieldOf("erosion_scale", CustomTerrainSettingsScreen.initialErosion).forGetter(TerrainData::erosionScale),
                Codec.DOUBLE.optionalFieldOf("ocean_depth_scale", CustomTerrainSettingsScreen.initialOceanDepth).forGetter(TerrainData::oceanDepthScale)
        ).apply(instance, TerrainData::new));
    }

    public static void reset() {
        if (!isWorldCopy) {
            masterScale = 1.0;
            continentScale = 1.0;
            biomeScale = 1.0;
            erosionScale = 1.0;
            oceanDepthScale = 1.5;
        } else {
            isWorldCopy = false;

            masterScale = CustomTerrainSettingsScreen.initialMaster;
            continentScale = CustomTerrainSettingsScreen.initialContinent;
            biomeScale = CustomTerrainSettingsScreen.initialBiome;
            erosionScale = CustomTerrainSettingsScreen.initialErosion;
            oceanDepthScale = CustomTerrainSettingsScreen.initialOceanDepth;
        }
    }

    private static CustomTerrainSettingsScreen getSettingsScreen() {
        Screen currentScreen = Minecraft.getInstance().gui.screen();

        return currentScreen instanceof CustomTerrainSettingsScreen screen ? screen : null;
    }

    private static double getSoftScale(double initialScale, double newScale) {
        return initialScale + (newScale - 1) * WorldGeneratorSD.SOFT_SCALAR;
    }

    public static void applyMasterScale(double newMasterScale) {
        if (getSettingsScreen() != null) {
            masterScale = newMasterScale;
            continentScale = newMasterScale;
            biomeScale = newMasterScale;
            erosionScale = getSoftScale(CustomTerrainSettingsScreen.initialErosion, newMasterScale);
            oceanDepthScale = getSoftScale(CustomTerrainSettingsScreen.initialOceanDepth, newMasterScale);
        }
    }

    public static void saveSettingsToFile(Path worldRoot) {
        try {
            if (!Files.exists(worldRoot)) {
                Files.createDirectories(worldRoot);
            }

            TerrainData terrainData = new TerrainData(masterScale, continentScale, biomeScale, erosionScale, oceanDepthScale);

            JsonElement encodedData = TerrainData.CODEC.encodeStart(JsonOps.INSTANCE, terrainData).getOrThrow(e -> new RuntimeException("Failed to encode settings: " + e));

            Files.writeString(worldRoot.resolve(dataFile), encodedData.toString());
        } catch (Exception e) {
            UtilSD.LOGGER.error("Error saving custom terrain settings to file: {}", e.getMessage());
        }
    }

    public static void loadSettingsFromFile(Path oldWorldRoot) {
        try {
            Path file = oldWorldRoot.resolve(dataFile);

            if (Files.exists(file)) {
                JsonElement jsonElement = JsonParser.parseString(Files.readString(file));
                TerrainData data = TerrainData.CODEC.parse(JsonOps.INSTANCE, jsonElement).getOrThrow(e -> new RuntimeException("Failed to decode settings: " + e));

                isWorldCopy = true;

                masterScale = data.masterScale();
                continentScale = data.continentScale();
                biomeScale = data.biomeScale();
                erosionScale = data.erosionScale();
                oceanDepthScale = data.oceanDepthScale();
            }
        } catch (Exception e) {
            UtilSD.LOGGER.error("Error loading custom terrain settings from file: {}", e.getMessage());
        }
    }
}