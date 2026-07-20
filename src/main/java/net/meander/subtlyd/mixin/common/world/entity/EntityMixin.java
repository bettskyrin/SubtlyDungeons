package net.meander.subtlyd.mixin.common.world.entity;

import net.meander.subtlyd.tags.EntityTypeTagsSD;
import net.meander.subtlyd.world.entity.EntitySD;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    public boolean isClimbing(Entity entity) {
        return EntitySD.getNearestWall(entity) != null;
    }

    @Inject(method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V", at = @At("HEAD"), cancellable = true)
    private void positionRider(Entity passenger, Entity.MoveFunction moveFunction, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;

        if (entity.is(EntityTypeTagsSD.SCANSORIAL)) {
            positionClimbingJockey(entity, passenger, moveFunction, ci);
        }
    }

    /**
     * Positions a spider's jockey on the serverside, to adjust the hitbox location.
     * @param vehicle The mounted spider.
     * @param passenger The jockey.
     * @param moveFunction Callback interface to handle the position adjustment.
     */
    private void positionClimbingJockey(Entity vehicle, Entity passenger, Entity.MoveFunction moveFunction, CallbackInfo ci) {
        if (isClimbing(vehicle)) {
            Direction wallDir = EntitySD.getNearestWall(vehicle);
            float yaw = Mth.DEG_TO_RAD * (wallDir != null ? wallDir.toYRot() : vehicle.getYRot());
            double distanceFromMount = 1.0;

            double offsetX = Mth.sin(yaw) * distanceFromMount;
            double offsetZ = -Mth.cos(yaw) * distanceFromMount;
            double offsetY = 1.0;

            double x = vehicle.getX() + offsetX;
            double y = vehicle.getY() + offsetY;
            double z = vehicle.getZ() + offsetZ;

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