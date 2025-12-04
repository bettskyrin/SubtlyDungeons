package com.kr1s1s.subtlyd.mixin.client.entity;

import com.kr1s1s.subtlyd.client.entity.player.PlayerSD;
import com.kr1s1s.subtlyd.world.entity.TentEntity;
import com.kr1s1s.subtlyd.world.entity.ServerPlayerSD;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {
    @SuppressWarnings("DataFlowIssue")
    private final Player player = (Player) (Object) this;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        tickTentSleep();
    }

    private void tickTentSleep() {
        if (TentEntity.inTent(player)) {
            if (player.isSleeping()) {
                PlayerSD.sleepCounter++;
                if (PlayerSD.sleepCounter > 100) {
                    PlayerSD.sleepCounter = 100;
                }

                if (!player.level().isClientSide() && player.level().isBrightOutside()) {
                    ServerPlayerSD.stopSleepInTent(true, (ServerPlayer) player);
                }
            } else if (PlayerSD.sleepCounter > 0) {
                PlayerSD.sleepCounter++;
                if (PlayerSD.sleepCounter >= 110) {
                    PlayerSD.sleepCounter = 0;
                }
            }
        }
    }
}
