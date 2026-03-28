package net.meander.subtlyd.mixin.client.multiplayer;

import net.meander.subtlyd.camera.CameraShake;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    /**
     * Handles camera shake for explosions.
     */
    @Inject(method = "handleExplosion", at = @At("TAIL"))
    private void handleExplosionShake(ClientboundExplodePacket packet, CallbackInfo ci) {
        CameraShake.shakeScreenFromSource(packet.explosionSound().value(), packet.center(), packet.radius());
    }
}
