package net.meander.subtlyd.mixin.common.world.entity;

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
    /**
     * Prevents setting respawn position if it's not a bed (i.e. a tent).
     */
    @Inject(method = "setRespawnPosition", at = @At("HEAD"), cancellable = true)
    private void preventTentRespawnSetting(@Nullable ServerPlayer.RespawnConfig respawnConfig, boolean showMessage, CallbackInfo ci) {
        if (respawnConfig != null) {
            final ServerPlayer player = (ServerPlayer) (Object) this;
            BlockState blockState = player.level().getBlockState(respawnConfig.respawnData().pos());
            if (!(blockState.getBlock() instanceof BedBlock)) {
                ci.cancel();
            }
        } else {
            ci.cancel();
        }
    }
}
