package com.meander.subtlyd.mixin.common.entity;

import com.meander.subtlyd.world.entity.TentEntity;
import net.minecraft.server.level.ServerLevel;
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
        if (player.level() instanceof ServerLevel && player.level().isBrightOutside()) {
            if (TentEntity.getTent(player, true) != null) {
                player.stopSleepInBed(false, true);
            }
        }
    }
}
