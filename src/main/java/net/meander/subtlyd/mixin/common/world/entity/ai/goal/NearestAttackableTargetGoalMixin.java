package net.meander.subtlyd.mixin.common.world.entity.ai.goal;

import net.meander.subtlyd.util.data.tags.EntityTypeTagsSD;
import net.meander.subtlyd.world.entity.MobSD;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NearestAttackableTargetGoal.class)
public class NearestAttackableTargetGoalMixin {
    @Shadow @Final protected Class<?> targetType;
    @Shadow @Nullable protected LivingEntity target;

    /**
     * Determines when hunting cooldowns should be used.
     */
    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void checkHuntCooldowns(CallbackInfoReturnable<Boolean> cir) {
        Mob mob = ((TargetGoalAccessor) this).subtlyDungeons$getMob();

        if (mob.is(EntityTypeTagsSD.CAN_BE_FULL) && Animal.class.isAssignableFrom(targetType)) {
            long nextAllowedHuntTime = ((MobSD) mob).subtlyDungeons$getHuntingCooldownTicks();

            if (mob.level().getGameTime() < nextAllowedHuntTime) {
                cir.setReturnValue(false);
            }
        }

        if (mob.is(EntityTypeTagsSD.NOCTURNAL) && mob.level().isBrightOutside()) {
            cir.setReturnValue(false);
        }
    }
}
