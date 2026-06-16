package net.meander.subtlyd.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.meander.subtlyd.client.camera.shake.CameraShake;
import net.meander.subtlyd.network.protocol.CameraShakePacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class PacketNetworking {
    /**
     * Registers common packets.
     */
    public static void registerCommon() {
        PayloadTypeRegistry.clientboundPlay().register(CameraShakePacketPayload.ID, CameraShakePacketPayload.CODEC);
    }

    /**
     * Registers client receivers.
     */
    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(CameraShakePacketPayload.ID, ((payload, context) -> context.client().execute(
            () -> {
                if (payload.durationTicks() <= 0) {
                    CameraShake.stop();
                } else {
                    CameraShake.setShake(payload.durationTicks(), payload.intensity());
                }
            }
        )));
    }

    /**
     * Sends packets.
     * @param player The server player.
     * @param payload The custom packet payload.
     */
    private static void sendPackets(ServerPlayer player, CustomPacketPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }

    /**
     * Allows for the creation of screen shake packets.
     * @param player The server player.
     * @param durationTicks The time (in ticks) that the screen shake should last.
     * @param intensity The intensity of the screen shake effect.
     */
    public static void setScreenShakePackets(ServerPlayer player, int durationTicks, float intensity) {
        sendPackets(player, new CameraShakePacketPayload(durationTicks, intensity));
    }
}