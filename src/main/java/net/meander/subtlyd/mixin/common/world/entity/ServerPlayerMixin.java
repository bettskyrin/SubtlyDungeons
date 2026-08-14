package net.meander.subtlyd.mixin.common.world.entity;

import net.meander.subtlyd.server.level.ServerPlayerSD;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin implements ServerPlayerSD {
    @Redirect(method = "updatePlayerAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isCrouching()Z"))
    private boolean setCrawlingToDiscrete(ServerPlayer player) {
        return player.isCrouching() || player.isVisuallyCrawling();
    }
}
