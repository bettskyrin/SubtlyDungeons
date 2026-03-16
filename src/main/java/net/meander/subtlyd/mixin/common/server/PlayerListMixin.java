package net.meander.subtlyd.mixin.common.server;

import net.meander.subtlyd.network.HandshakePayload;
import net.meander.subtlyd.util.Util;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void setConnectionHandshake(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        ClientboundCustomPayloadPacket packet = new ClientboundCustomPayloadPacket(new HandshakePayload());

        player.connection.send(packet);
        Util.log(Component.translatable("multiplayer.startHandshake", player.getName().getString()));
    }
}
