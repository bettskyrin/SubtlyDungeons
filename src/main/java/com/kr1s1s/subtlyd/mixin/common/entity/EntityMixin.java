package com.kr1s1s.subtlyd.mixin.common.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.spider.Spider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V",
            at = @At("HEAD"), cancellable = true)
    private void positionClimbingRider(Entity passenger, Entity.MoveFunction moveFunction, CallbackInfo ci) {
        if ((Object) this instanceof Spider spider) {
            if (spider.isClimbing()) {
                float yaw = (float) Math.toRadians(spider.yBodyRot);
                double distance = 1D;
                double offsetX = Math.sin(yaw) * distance;
                double offsetY = 0.05D;
                double offsetZ = -Math.cos(yaw) * distance;

                double x = spider.getX() + offsetX;
                double y = spider.getY() + offsetY;
                double z = spider.getZ() + offsetZ;

                moveFunction.accept(passenger, x, y, z);
                ci.cancel();
            }
        }
    }
}
