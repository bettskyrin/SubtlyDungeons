package net.meander.subtlyd.mixin.common.entity;

import net.meander.subtlyd.world.entity.EntitySD;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.spider.Spider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V",
            at = @At("HEAD"),
            cancellable = true)
    private void positionRider(Entity passenger, Entity.MoveFunction moveFunction, CallbackInfo ci) {
        if ((Object) this instanceof Spider spider) {
            positionClimbingRider(spider, passenger, moveFunction, ci);
        }
    }

    /**
     * Positions a spider's jockey on the serverside, to adjust the hitbox location.
     * @param spider The mounted spider.
     * @param passenger The jockey.
     * @param moveFunction Callback interface to handle the position adjustment.
     */
    private void positionClimbingRider(Spider spider, Entity passenger, Entity.MoveFunction moveFunction, CallbackInfo ci) {
        if (spider.isClimbing()) {
            Direction wallDir = EntitySD.getNearestWall(spider);
            float yaw = (float) Math.toRadians(wallDir != null ? wallDir.toYRot() : spider.getYRot());
            double distanceFromMount = 1D;

            double offsetX = Math.sin(yaw) * distanceFromMount;
            double offsetZ = -Math.cos(yaw) * distanceFromMount;
            double offsetY = 0.05D;

            double x = spider.getX() + offsetX;
            double y = spider.getY() + offsetY;
            double z = spider.getZ() + offsetZ;

            moveFunction.accept(passenger, x, y, z);
            ci.cancel();
        }
    }

    /**
     * Prevents players from having fire rendered on them in third person.
     */
    @Inject(method = "displayFireAnimation", at = @At("RETURN"), cancellable = true)
    private void displayFireAnimation(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            if ((Object) this instanceof LivingEntity livingEntity) {
                if (livingEntity.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                    cir.setReturnValue(false);
                }
            }
        }
    }

    /**
     * Prevents mobs from having fire rendered on them.
     */
    @Inject(method = "setSharedFlagOnFire", at = @At("RETURN"))
    private void setSharedFlagOnFire(boolean value, CallbackInfo ci) {
        if (value) {
            if ((Object) this instanceof LivingEntity livingEntity) {
                if (livingEntity.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                    livingEntity.setSharedFlagOnFire(false);
                }
            }
        }
    }
}