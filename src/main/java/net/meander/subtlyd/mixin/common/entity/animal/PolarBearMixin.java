package net.meander.subtlyd.mixin.common.entity.animal;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.fish.Cod;
import net.minecraft.world.entity.animal.fish.Salmon;
import net.minecraft.world.entity.animal.polarbear.PolarBear;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PolarBear.class)
public abstract class PolarBearMixin extends Animal {
    protected PolarBearMixin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void addHuntingGoal(CallbackInfo ci) {
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Salmon.class, true, true) {

            @Override
            public boolean canUse() {
                return this.mob.isInWater() && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return this.mob.isInWater() && super.canContinueToUse();
            }
        });

        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Cod.class, true, true) {

            @Override
            public boolean canUse() {
                return this.mob.isInWater() && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return this.mob.isInWater() && super.canContinueToUse();
            }
        });
    }
}
