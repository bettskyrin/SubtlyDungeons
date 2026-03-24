package net.meander.subtlyd.mixin.common.world.entity.projectile;

import net.meander.subtlyd.sounds.SoundEventsSD;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EvokerFangs.class)
public class EvokerFangsMixin {
    @Shadow private int lifeTicks;
    private static long prevSoundTick = 0;

    /**
     * Plays evoker fang noises
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void playFangNoises(CallbackInfo ci) {
        EvokerFangs fangs = ((EvokerFangs) (Object) (this));
        Level level = fangs.level();

        if (!level.isClientSide()) {
            if (lifeTicks == 22) {
                long currentTick = level.getGameTime();

                if (currentTick - prevSoundTick > 10) {
                    fangs.playSound(SoundEventsSD.EVOKER_FANGS_APPEAR);
                    prevSoundTick = currentTick;
                }
            }
        }
    }
}
