package net.meander.subtlyd.client.gui.screens;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.meander.subtlyd.util.Util;

import java.nio.file.Files;
import java.nio.file.Path;

public class TailoredWorldGenSettings {
    private static boolean isWorldCopy = false;
    public static boolean shouldAlterSettings = false;
    public static double masterScale = 1.0;
    public static double continentScale = 1.0;
    public static double biomeScale = 1.0;

    public static void reset() {
        if (!isWorldCopy) {
            masterScale = 1.0;
            continentScale = 1.0;
            biomeScale = 1.0;
        } else {
            isWorldCopy = false;
        }
    }

    public static void applyMasterScale(double newMasterScale) {
        masterScale = newMasterScale;
        continentScale = newMasterScale;
        biomeScale = newMasterScale;
    }

    public static void saveSettingsToFile(Path worldRoot) {
        try {
            JsonObject settings = new JsonObject();
            settings.addProperty("masterScale", masterScale);
            settings.addProperty("continentScale", continentScale);
            settings.addProperty("climateScale", biomeScale);
            Files.writeString(worldRoot.resolve("tailored_worldgen_settings.json"), settings.toString());
        } catch (Exception e) {
            Util.LOGGER.error("Error saving tailored world generation settings to file: {}", e.getMessage());
        }
    }

    public static void loadSettingsFromFile(Path oldWorldRoot) {
        try {
            Path file = oldWorldRoot.resolve("tailored_worldgen_settings.json");

            if (Files.exists(file)) {
                JsonObject json = JsonParser.parseString(Files.readString(file)).getAsJsonObject();
                masterScale = json.has("masterScale") ? json.get("masterScale").getAsDouble() : 1.0;
                continentScale = json.has("continentScale") ? json.get("continentScale").getAsDouble() : 1.0;
                biomeScale = json.has("climateScale") ? json.get("climateScale").getAsDouble() : 1.0;

                isWorldCopy = true;
                shouldAlterSettings = true;
            }
        } catch (Exception e) {
            Util.LOGGER.error("Error loading tailored world generation settings from file: {}", e.getMessage());
        }
    }
}
