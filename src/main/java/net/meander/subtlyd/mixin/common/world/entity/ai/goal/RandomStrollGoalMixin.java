package net.meander.subtlyd.mixin.common.world.entity.ai.goal;

import net.meander.subtlyd.world.entity.ai.goal.ShelteredRandomStrollGoal;
import net.meander.subtlyd.world.entity.ai.util.GoalUtilsSD;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RandomStrollGoal.class)
public abstract class RandomStrollGoalMixin extends Goal {
    @Shadow @Final protected PathfinderMob mob;

    @SuppressWarnings("ConstantValue")
    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void maintainShelter(CallbackInfoReturnable<Boolean> cir) {
        if (!((Object) this instanceof ShelteredRandomStrollGoal) && GoalUtilsSD.isMobSheltering(mob, mob.blockPosition())) {
            cir.setReturnValue(false);
        }
    }
}