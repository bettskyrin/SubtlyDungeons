package net.meander.subtlyd.mixin.common.world.entity.projectile;

import net.meander.subtlyd.sounds.SoundEventsSD;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.EvokerFangs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EvokerFangs.class)
public class EvokerFangsMixin {
    @Shadow private int lifeTicks;
    private static long ticksSinceLastSound = 0;

    @Inject(method = "tick", at = @At("TAIL"))
    private void playFangNoises(CallbackInfo ci) {
        EvokerFangs fangs = ((EvokerFangs) (Object) (this));

        if (fangs.level() instanceof ServerLevel level) {
            if (lifeTicks == 22) {
                long currentTick = level.getGameTime();

                if (currentTick - ticksSinceLastSound > 10) {
                    fangs.playSound(SoundEventsSD.EVOKER_FANGS_APPEAR);

                    ticksSinceLastSound = currentTick;
                }
            }
        }
    }
}
