package net.meander.subtlyd.mixin.common.world.entity.ai.goal;

import net.meander.subtlyd.tags.EntityTypeTagsSD;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NearestAttackableTargetGoal.class)
public class NearestAttackableTargetGoalMixin {
    @Shadow @Final protected Class<?> targetType;

    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void checkHuntCooldowns(CallbackInfoReturnable<Boolean> cir) {
        Mob mob = ((NearestAttackableTargetGoal<?>) (Object) this).mob;
        Level level = mob.level();

        if (mob.is(EntityTypeTagsSD.CAN_BE_FULL) && Animal.class.isAssignableFrom(targetType)) {
            long nextAllowedHuntTime = ((MobSD) mob).getHuntingCooldownTicks();

            if (level.getGameTime() < nextAllowedHuntTime) {
                cir.setReturnValue(false);
            }
        }

        if (mob.is(EntityTypeTagsSD.NOCTURNAL) && level.isBrightOutside()) {
            cir.setReturnValue(false);
        }
    }
}
