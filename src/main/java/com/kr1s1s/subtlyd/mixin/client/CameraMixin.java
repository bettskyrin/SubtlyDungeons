package com.kr1s1s.subtlyd.mixin.client;

import com.kr1s1s.subtlyd.client.OptionsSD;
import com.kr1s1s.subtlyd.client.util.CameraShake;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
@Environment(EnvType.CLIENT)
@SuppressWarnings("unused")
public abstract class CameraMixin {
    @Shadow
    private float yRot;
    @Shadow private float xRot;
    @Shadow protected abstract void setRotation(float y, float x);

    @Inject(method = "setup", at = @At("TAIL"))
    private void setup(Level level, Entity entity, boolean bl, boolean bl2, float f, CallbackInfo ci) {
        applyCameraShake();
    }

    private void applyCameraShake() {
        if (OptionsSD.CAMERA_SHAKE.get()) {
            float intensity = CameraShake.getShakeIntensity() * 0.5F;

            if (intensity > 0) {
                float yaw = (float) (Math.sin(System.currentTimeMillis() / 30.0) * intensity);
                float pitch = (float) (Math.cos(System.currentTimeMillis() / 60.0) * intensity);
                this.setRotation((float) (this.yRot - pitch * Math.sqrt(2)), this.xRot + (yaw));
            }
        }
    }
}