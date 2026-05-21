package net.meander.subtlyd.mixin.client.level;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.meander.subtlyd.client.camera.shake.CameraShake;
import net.meander.subtlyd.client.OptionsSD;
import net.meander.subtlyd.world.entity.TentEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow private @Nullable Level level;

    @Shadow protected abstract void setRotation(float yRot, float xRot);
    @Shadow protected abstract void setPosition(double x, double y, double z);
    @Shadow public abstract @Nullable Entity entity();
    @Shadow public abstract float xRot();
    @Shadow public abstract float yRot();

    @Inject(method = "update", at = @At("TAIL"))
    private void setup(DeltaTracker deltaTracker, CallbackInfo ci) {
        if (level != null && entity() != null) {
            applyScreenShake();
            setCampingPlayerCamera(entity(), xRot());
        }
    }

    /**
     * Determines if screen shake is enabled and if so, handles the math for placing the camera to create the screen shake effect.
     */
    private void applyScreenShake() {
        if (OptionsSD.CAMERA_SHAKE.get()) {
            float intensity = CameraShake.getShakeIntensity() * 0.5F;

            if (intensity > 0.0F) {
                float yaw = Mth.sin(Util.getMillis() / 30.0) * intensity;
                float pitch = Mth.cos(Util.getMillis() / 60.0) * intensity;
                this.setRotation(yRot() - pitch * Mth.sqrt(2), xRot() + (yaw));
            }
        }
    }

    /**
     * Locks the camera to the player's head while sleeping in a tent
     * @param entity The sleeping entity.
     * @param f The entity head rotation angle
     */
    private void setCampingPlayerCamera(Entity entity, float f) {
        if (entity instanceof LivingEntity livingEntity) {
            if (TentEntity.getTent(livingEntity, true) != null) {
                this.setRotation(livingEntity.getViewYRot(f), -90F);
                this.setPosition(livingEntity.getX(), livingEntity.getY() + 0.2, livingEntity.getZ());
            }
        }
    }
}