package net.meander.subtlyd.mixin.common.world.entity.projectile.hurtingprojectile;

import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WitherSkull.class)
public class WitherSkullMixin {
    @Inject(method = "isOnFire", at = @At("RETURN"), cancellable = true)
    private void isOnFire(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
