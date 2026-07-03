package net.meander.subtlyd.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.meander.subtlyd.network.PacketNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class CameraShakeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("camerashake")
                .then(Commands.literal("stop")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(context -> stopCameraShake(context.getSource(), EntityArgument.getPlayers(context, "targets")))))
                .then(Commands.literal("add")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("intensity", FloatArgumentType.floatArg(0.0F, 4.0F))
                                        .then(Commands.argument("seconds", IntegerArgumentType.integer())
                                                .executes(context -> addCameraShake(
                                                        context.getSource(),
                                                        EntityArgument.getPlayers(context, "targets"),
                                                        FloatArgumentType.getFloat(context, "intensity"),
                                                        IntegerArgumentType.getInteger(context, "seconds")
                                                ))))))
        );
    }

    /**
     * @param source The command source stack
     * @param targets All targets that will experience the screen shake
     * @param intensity The intensity of the screen shake.
     * @param seconds The length of time (in seconds) that the screen shake should last
     * @return The amount of targets affected.
     */
    private static int addCameraShake(CommandSourceStack source, Collection<ServerPlayer> targets, float intensity, int seconds) {
        int durationTicks = seconds * 20;

        for (ServerPlayer player : targets) {
            PacketNetworking.setScreenShakePackets(player, durationTicks, intensity);
        }

        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.camerashake.success.add.single", ComponentUtils.formatList(targets, ServerPlayer::getDisplayName)), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.camerashake.success.add.multiple", targets.size()), true);
        }
        return targets.size();
    }

    /**
     * @param source The command source stack
     * @param targets All targets to affect
     * @return The amount of targets affected.
     */
    private static int stopCameraShake(CommandSourceStack source, Collection<ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            PacketNetworking.setScreenShakePackets(player, 0, 0);
        }

        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.camerashake.success.stop.single", ComponentUtils.formatList(targets, ServerPlayer::getDisplayName)), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.camerashake.success.stop.multiple", targets.size()), true);
        }
        return targets.size();
    }
}
