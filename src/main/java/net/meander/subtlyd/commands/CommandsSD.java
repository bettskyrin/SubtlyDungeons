package net.meander.subtlyd.commands;

import net.meander.subtlyd.network.PacketNetworking;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class CommandsSD {
    /**
     * Registers the camera shake command. Called "Camera Shake" instead of screen shake, for Bedrock parity.
     */
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(net.minecraft.commands.Commands.literal("camerashake")
                .then(net.minecraft.commands.Commands.literal("stop")
                        .then(net.minecraft.commands.Commands.argument("targets", EntityArgument.players())
                                .executes(context -> stopShake(context.getSource(), EntityArgument.getPlayers(context, "targets")))
                        )
                )

                .then(net.minecraft.commands.Commands.literal("add")
                        .then(net.minecraft.commands.Commands.argument("targets", EntityArgument.players())
                                .then(net.minecraft.commands.Commands.argument("intensity", FloatArgumentType.floatArg(0.0F, 4.0F))
                                        .then(net.minecraft.commands.Commands.argument("seconds", IntegerArgumentType.integer())
                                                .executes(ctx -> addShake(
                                                        ctx.getSource(),
                                                        EntityArgument.getPlayers(ctx, "targets"),
                                                        FloatArgumentType.getFloat(ctx, "intensity"),
                                                        IntegerArgumentType.getInteger(ctx, "seconds")
                                                ))
                                        )
                                )
                        )
                )
        );
    }

    /**
     * Begins the screen shake.
     * @param source The command source stack.
     * @param target All target that will experience the screen shake.
     * @param intensity The intensity of the screen shake.
     * @param seconds The length of time (in seconds) that the screen shake should last.
     * @return The amount of target affected.
     */
    private static int addShake(CommandSourceStack source, Collection<ServerPlayer> target, float intensity, int seconds) {
        int durationTicks = seconds * 20;
        for (ServerPlayer player : target) {
            PacketNetworking.setScreenShakePackets(player, durationTicks, intensity);
        }
        if (target.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.camerashake.success.add.single", ComponentUtils.formatList(target, ServerPlayer::getDisplayName)), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.camerashake.success.add.multiple", target.size()), true);
        }
        return target.size();
    }

    /**
     * Stops the screen shake.
     * @param source The command source stack.
     * @param target All target that will experience the stopping of the screen shake.
     * @return The amount of target affected.
     */
    private static int stopShake(CommandSourceStack source, Collection<ServerPlayer> target) {
        for (ServerPlayer player : target) {
            PacketNetworking.setScreenShakePackets(player, 0, 0);
        }
        if (target.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.camerashake.success.stop.single", ComponentUtils.formatList(target, ServerPlayer::getDisplayName)), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.camerashake.success.stop.multiple", target.size()), true);
        }
        return target.size();
    }
}
