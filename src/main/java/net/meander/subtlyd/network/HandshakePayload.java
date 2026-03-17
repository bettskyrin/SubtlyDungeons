package net.meander.subtlyd.network;

import net.meander.subtlyd.util.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record HandshakePayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<HandshakePayload> TYPE = new  CustomPacketPayload.Type<>(Util.identifier("handshake"));
    public static final StreamCodec<FriendlyByteBuf, HandshakePayload> CODEC = StreamCodec.unit(new HandshakePayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
