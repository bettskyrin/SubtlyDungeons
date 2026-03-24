package net.meander.subtlyd.mixin.common.world.entity.boss;

import net.minecraft.world.entity.boss.wither.WitherBoss;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(WitherBoss.class)
public class WitherBossMixin {
    /**
     * Determines that the maximum amount of health a wither can have naturally, is 600 health points.
     * @param health The orignal amount of health.
     * @return 600 health points
     */
    @ModifyConstant(method = "createAttributes", constant = @Constant(doubleValue = 300.0))
    private static double updatedHealth(double health) {
        return 600.0;
    }
}
