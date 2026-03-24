package net.meander.subtlyd.mixin.common.world.entity.animal;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.AgeableWaterCreature;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Squid.class)
public abstract class SquidMixin extends AgeableWaterCreature {
    protected SquidMixin(EntityType<? extends AgeableWaterCreature> type, Level level) {
        super(type, level);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void addPredatorGoal(CallbackInfo ci) {
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Dolphin.class, 8.0F, 1.0, 1.0));
    }
}
