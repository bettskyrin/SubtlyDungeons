package net.meander.subtlyd.mixin.common.world.food;

import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FoodData.class)
public class FoodDataMixin {
    @Shadow private float exhaustionLevel;

    @Shadow
    private int foodLevel;

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 20))
    private int removeFastHealing(int originalThreshold) {
        return Integer.MAX_VALUE;
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 80))
    private int modifyRegenerationAndStarvationRate(int original) {
        return 40;
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 18))
    private int modifyRegenerationThreshold(int originalThreshold) {
        return 7;
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;addExhaustion(F)V"))
    private void removeRegenerationSaturation(FoodData foodData, float amount) {
        exhaustionLevel += amount;

        while (exhaustionLevel > 4.0F) {
            exhaustionLevel -= 4.0F;

            foodLevel = Math.max(foodLevel - 1, 0);
        }
    }
}