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
    public static double masterScale = 1.0;
    public static double continentScale = 1.0;
    public static double biomeScale = 1.0;
    public static double erosionScale = 1.0;
    private static final String dataFile = "custom_terrain_settings.json";

    public record TerrainData(double masterScale, double continentScale, double biomeScale, double erosionScale) {
        public static final Codec<TerrainData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.DOUBLE.optionalFieldOf("master_scale", 1.0).forGetter(TerrainData::masterScale),
                Codec.DOUBLE.optionalFieldOf("continent_scale", 1.0).forGetter(TerrainData::continentScale),
                Codec.DOUBLE.optionalFieldOf("biome_scale", 1.0).forGetter(TerrainData::biomeScale),
                Codec.DOUBLE.optionalFieldOf("erosion_scale", 1.0).forGetter(TerrainData::erosionScale)
        ).apply(instance, TerrainData::new));
    }

    public static void reset() {
        if (!isWorldCopy) {
            masterScale = 1.0;
            continentScale = 1.0;
            biomeScale = 1.0;
            erosionScale = 1.0;
        } else {
            isWorldCopy = false;

            if (getSettingsScreen() instanceof CustomTerrainSettingsScreen settingsScreen) {
                masterScale = settingsScreen.initialMaster;
                continentScale = settingsScreen.initialContinent;
                biomeScale = settingsScreen.initialBiome;
                erosionScale = settingsScreen.initialErosion;
            } else {
                reset();
            }
        }
    }

    private static CustomTerrainSettingsScreen getSettingsScreen() {
        Screen currentScreen = Minecraft.getInstance().gui.screen();

        return currentScreen instanceof CustomTerrainSettingsScreen screen ? screen : null;
    }

    public static void applyMasterScale(double newMasterScale) {
        masterScale = newMasterScale;
        continentScale = newMasterScale;
        biomeScale = newMasterScale;
        erosionScale = 1.0 + ((newMasterScale - 1.0) * WorldGeneratorSD.EROSION_ELASTICITY);
    }

    public static void saveSettingsToFile(Path worldRoot) {
        try {
            if (!Files.exists(worldRoot)) {
                Files.createDirectories(worldRoot);
            }

            TerrainData terrainData = new TerrainData(masterScale, continentScale, biomeScale, erosionScale);

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
                TerrainData data = TerrainData.CODEC.parse(JsonOps.INSTANCE, jsonElement).getOrThrow(msg -> new RuntimeException("Failed to decode settings: " + msg));

                isWorldCopy = true;

                masterScale = data.masterScale();
                continentScale = data.continentScale();
                biomeScale = data.biomeScale();
                erosionScale = data.erosionScale();
            }
        } catch (Exception e) {
            UtilSD.LOGGER.error("Error loading custom terrain settings from file: {}", e.getMessage());
        }
    }
}