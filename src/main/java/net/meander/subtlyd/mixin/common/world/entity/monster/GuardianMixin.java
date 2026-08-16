package net.meander.subtlyd.mixin.common.world.entity.monster;

import net.meander.subtlyd.sounds.SoundEventsSD;
import net.minecraft.world.entity.monster.Guardian;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class GuardianMixin {
    @Mixin(targets = "net.minecraft.world.entity.monster.Guardian$GuardianAttackGoal")
    public static class GuardianAttackGoalMixin {
        @Shadow @Final private Guardian guardian;
        @Shadow @Final private boolean elder;

        @Inject(
                method = "tick",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/world/entity/LivingEntity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"
                )
        )
        private void playBeamSound(CallbackInfo ci) {
            guardian.level().playSound(
                    null,
                    guardian.getX(),
                    guardian.getY(),
                    guardian.getZ(),
                    elder ? SoundEventsSD.ELDER_GUARDIAN_BEAM : SoundEventsSD.GUARDIAN_BEAM,
                    guardian.getSoundSource(),
                    1.0F,
                    1.0F
            );
        }
    }
}