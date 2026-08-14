package net.meander.subtlyd.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.meander.subtlyd.client.camera.shake.CameraShake;
import net.meander.subtlyd.network.protocol.CameraShakePacketPayload;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class PacketNetworking {
    public static void registerPayloadTypes() {
        UtilSD.LOGGER.debug("Registering payload types...");
        PayloadTypeRegistry.clientboundPlay().register(CameraShakePacketPayload.ID, CameraShakePacketPayload.CODEC);
    }

    public static void registerPayloadHandlers() {
        UtilSD.LOGGER.debug("Registering payload handlers...");
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

    private static void sendPackets(ServerPlayer player, CustomPacketPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }

    public static void sendCameraShakePackets(ServerPlayer player, int durationTicks, float intensity) {
        sendPackets(player, new CameraShakePacketPayload(durationTicks, intensity));
    }
}