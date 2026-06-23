package net.meander.subtlyd.mixin.common.world.entity.projectile;

import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Projectile.class)
public class ProjectileMomentumMixin {
    @ModifyArg(method = "shootFromRotation", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;"), index = 1)
    private double removeVerticalMomentum(double originalY) {
        return 0.0D;
    }
}