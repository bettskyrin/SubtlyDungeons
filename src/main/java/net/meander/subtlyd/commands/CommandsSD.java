package net.meander.subtlyd.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.commands.Commands;

/**
 * @see Commands
 */
public class CommandsSD {
    public static void registration() {
        UtilSD.LOGGER.debug("Registering commands...");
        CommandRegistrationCallback.EVENT.register(((dispatcher, _, _) -> CameraShakeCommand.register(dispatcher)));
    }

    public static void load() {
        UtilSD.LOGGER.debug("Loading client command data...");
        CommandMacroManager.load();
    }
}
