package com.kr1s1s.subtlyd.mixin.server.entity;

import com.kr1s1s.subtlyd.world.entity.ServerPlayerSD;
import com.kr1s1s.subtlyd.world.entity.TentEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.kr1s1s.subtlyd.client.entity.player.PlayerSD.sleepCounter;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    ServerPlayer player = (ServerPlayer) (Object) this;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (TentEntity.inTent(player)) {
            if (player.isSleeping()) {
                sleepCounter++;
                if (sleepCounter > 100) {
                    sleepCounter = 100;
                }

                if (!player.level().isClientSide() && player.level().isBrightOutside()) {
                    ServerPlayerSD.stopSleepInTent(true, player);
                }
            } else if (sleepCounter > 0) {
                sleepCounter++;
                if (sleepCounter >= 110) {
                    sleepCounter = 0;
                }
            }
        }
    }

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
