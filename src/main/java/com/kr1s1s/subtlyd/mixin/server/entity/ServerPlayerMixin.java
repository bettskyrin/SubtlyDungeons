package com.kr1s1s.subtlyd.mixin.server.entity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    ServerPlayer player = (ServerPlayer) (Object) this;
    @Inject(method = "setRespawnPosition", at = @At("HEAD"), cancellable = true)
    public void setRespawnPosition(@Nullable ServerPlayer.RespawnConfig respawnConfig, boolean bl, CallbackInfo ci) {
        if (respawnConfig != null) {
            BlockState blockState = player.level().getBlockState(respawnConfig.respawnData().pos());
            if (!(blockState.getBlock() instanceof BedBlock)) {
                ci.cancel();
            }
        } else {
            ci.cancel();
        }
    }
}
