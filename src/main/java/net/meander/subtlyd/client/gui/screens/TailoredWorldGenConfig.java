package net.meander.subtlyd.client.gui.screens;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.meander.subtlyd.util.Util;

import java.nio.file.Files;
import java.nio.file.Path;

public class TailoredWorldGenConfig {
    private static boolean isWorldCopy = false;

    public static double masterScale = 1.0;
    public static double continentScale = 1.0;
    public static double erosionScale = 1.0;
    public static double climateScale = 1.0;
    public static double oceanDepth = 1.0;

    public static void reset() {
        if (!isWorldCopy) {
            masterScale = 1.0;
            continentScale = 1.0;
            erosionScale = 1.0;
            climateScale = 1.0;
            oceanDepth = 1.0;
        } else {
            isWorldCopy = false;
        }
    }

    public static void applyMasterScale(double newMasterScale) {
        masterScale = newMasterScale;
        continentScale = newMasterScale;
        erosionScale = newMasterScale;
        climateScale = newMasterScale;
        oceanDepth = newMasterScale;
    }

    public static void saveSettingsToFile(Path worldRoot) {
        try {
            JsonObject settings = new JsonObject();
            settings.addProperty("masterScale", masterScale);
            settings.addProperty("continentScale", continentScale);
            settings.addProperty("erosionScale", erosionScale);
            settings.addProperty("climateScale", climateScale);
            settings.addProperty("oceanDepth", oceanDepth);
            Files.writeString(worldRoot.resolve("subtlyd_worldgen_settings.json"), settings.toString());
        } catch (Exception e) {
            Util.LOGGER.error("Error saving custom world generation settings to file: {}", e.getMessage());
        }
    }

    public static void loadSettingsFromFile(Path oldWorldRoot) {
        try {
            Path file = oldWorldRoot.resolve("subtlyd_worldgen_settings.json");
            if (Files.exists(file)) {
                JsonObject json = JsonParser.parseString(Files.readString(file)).getAsJsonObject();
                masterScale = json.has("masterScale") ? json.get("masterScale").getAsDouble() : 1.0;
                continentScale = json.has("continentScale") ? json.get("continentScale").getAsDouble() : 1.0;
                erosionScale = json.has("erosionScale") ? json.get("erosionScale").getAsDouble() : 1.0;
                climateScale = json.has("climateScale") ? json.get("climateScale").getAsDouble() : 1.0;
                oceanDepth = json.has("oceanDepth") ? json.get("oceanDepth").getAsDouble() : 1.0;

                isWorldCopy = true;
            }
        } catch (Exception e) {
            Util.LOGGER.error("Error loading custom world generation settings from file: {}", e.getMessage());
        }
    }
}
