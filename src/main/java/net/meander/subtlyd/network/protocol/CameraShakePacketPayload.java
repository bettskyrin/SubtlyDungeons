package net.meander.subtlyd.network.protocol;

import net.meander.subtlyd.util.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;

public record CameraShakePacketPayload(int durationTicks, float intensity) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CameraShakePacketPayload> ID = new  CustomPacketPayload.Type<>(Util.identifier("camera_shake"));

    public static final StreamCodec<FriendlyByteBuf, CameraShakePacketPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, CameraShakePacketPayload::durationTicks,
            ByteBufCodecs.FLOAT, CameraShakePacketPayload::intensity,
            CameraShakePacketPayload::new
    );

    @Override @NonNull
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
