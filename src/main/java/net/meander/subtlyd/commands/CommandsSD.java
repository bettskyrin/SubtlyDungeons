package net.meander.subtlyd.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;

/**
 * @see Commands
 */
public class CommandsSD {
    public static void registration() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, _, _) -> CameraShakeCommand.register(dispatcher)));
    }

    public static void initClient() {
        CommandMacroManager.load();
    }
}
