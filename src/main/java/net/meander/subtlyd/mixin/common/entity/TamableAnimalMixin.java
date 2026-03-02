package net.meander.subtlyd.mixin.common.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TamableAnimal.class)
public class TamableAnimalMixin {
    TamableAnimal tamableAnimal = (TamableAnimal) (Object) this;

    /**
     * Increases pet follow radius to 20 blocks.
     */
    @Inject(method = "shouldTryTeleportToOwner", at = @At("RETURN"), cancellable = true)
    private void shouldTryTeleportToOwner(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity owner = tamableAnimal.getOwner();
        cir.setReturnValue(owner != null && tamableAnimal.distanceToSqr(tamableAnimal.getOwner()) >= 400.0);
    }
}
