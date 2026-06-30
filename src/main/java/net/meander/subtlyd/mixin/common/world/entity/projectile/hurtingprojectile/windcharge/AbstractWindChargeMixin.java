package net.meander.subtlyd.mixin.common.world.entity.projectile.hurtingprojectile.windcharge;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractWindCharge.class)
public class AbstractWindChargeMixin {
    @Inject(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/hurtingprojectile/windcharge/AbstractWindCharge;discard()V", shift = At.Shift.BEFORE))
    private void disperseCloud(HitResult hitResult, CallbackInfo ci) {
        Entity windCharge = (Entity) (Object) this;
        ServerLevel level = (ServerLevel) windCharge.level();
        AABB searchArea = windCharge.getBoundingBox().inflate(0.8);

        for (Entity entity : level.getEntitiesOfClass(AreaEffectCloud.class, searchArea)) {
            entity.kill(level);
        }
    }
}
