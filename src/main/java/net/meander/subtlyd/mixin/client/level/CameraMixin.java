package net.meander.subtlyd.mixin.client.level;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.meander.subtlyd.client.OptionsSD;
import net.meander.subtlyd.client.camera.shake.CameraShake;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow protected abstract void setRotation(float yRot, float xRot);
    @Shadow public abstract @Nullable Entity entity();
    @Shadow public abstract float xRot();
    @Shadow public abstract float yRot();

    @Inject(method = "update", at = @At("TAIL"))
    private void setup(DeltaTracker deltaTracker, CallbackInfo ci) {
        applyCameraShake();
    }

    /**
     * Determines if camera shake is enabled and if so, handles the math for placing the camera to create the camera shake effect.
     */
    private void applyCameraShake() {
        if (OptionsSD.cameraShake().get()) {
            float intensity = CameraShake.getShakeIntensity() * 0.5F;

            if (intensity > 0.0F) {
                float yaw = Mth.sin(Util.getMillis() / 30.0) * intensity;
                float pitch = Mth.cos(Util.getMillis() / 60.0) * intensity;

                setRotation(yRot() - pitch * Mth.sqrt(2), xRot() + (yaw));
            }
        }
    }
}