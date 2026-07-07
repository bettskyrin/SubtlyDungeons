package net.meander.subtlyd.commands;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.meander.subtlyd.util.Util;
import net.minecraft.client.Minecraft;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandMacroManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("command_macros.json");
    public static List<String> macros = new ArrayList<>(Collections.nCopies(10, ""));

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                List<String> loaded = GSON.fromJson(reader, new TypeToken<List<String>>(){}.getType());

                if (loaded != null && loaded.size() == 10) {
                    macros = loaded;
                }
            } catch (Exception e) {
                Util.LOGGER.error("Error while loading command macros", e);
            }
        }
    }

    public static void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(macros, writer);

            for (String command : macros) {
                Util.LOGGER.info("Saved command macro: {}", command);
            }
        } catch (Exception e) {
            Util.LOGGER.error("Error while saving command macros", e);
        }
    }

    public static void execute(int index) {
        if (index >= 0 && index < 10) {
            String command = macros.get(index);

            if (command != null && !command.trim().isEmpty()) {
                Minecraft minecraft = Minecraft.getInstance();

                if (minecraft.player != null && minecraft.getConnection() != null) {
                    if (!command.startsWith("/")) {
                        command = "/" + command;
                    }

                    minecraft.getConnection().sendCommand(command.substring(1));
                }
            }
        }
    }
}