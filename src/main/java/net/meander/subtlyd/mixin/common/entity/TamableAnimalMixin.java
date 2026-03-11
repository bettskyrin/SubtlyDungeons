package net.meander.subtlyd.mixin.common.entity;

import net.meander.subtlyd.world.entity.TamableAnimalSD;
import net.minecraft.world.entity.TamableAnimal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(TamableAnimal.class)
public class TamableAnimalMixin {
    /**
     * Increases pet follow radius to 20 blocks.
     */
    @ModifyConstant(method = "shouldTryTeleportToOwner", constant = @Constant(doubleValue = 144.0))
    private double increaseFollowRange(double originalDistance) {
        return TamableAnimalSD.MAX_FOLLOW_DISTANCE_SQR;
    }
}
