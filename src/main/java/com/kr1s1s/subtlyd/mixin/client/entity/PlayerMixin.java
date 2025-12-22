package com.kr1s1s.subtlyd.mixin.client.entity;

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
    private final Player player = (Player) (Object) this;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        tickTentSleep();
    }

    /**
     * Wakes up the player once it's daytime
     */
    private void tickTentSleep() {
        if (TentEntity.inTent(player) && player.isSleeping()) {
            if (!player.level().isClientSide() && player.level().isBrightOutside()) {
                ServerPlayerSD.stopSleepInTent(true, (ServerPlayer) player);
            }
        }
    }
}
