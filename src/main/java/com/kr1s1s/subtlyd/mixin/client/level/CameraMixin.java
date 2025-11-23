package com.kr1s1s.subtlyd.mixin.client.level;

import com.kr1s1s.subtlyd.client.OptionsSD;
import com.kr1s1s.subtlyd.client.util.ScreenShake;
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

@Environment(EnvType.CLIENT)
@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow private float yRot;
    @Shadow private float xRot;
    @Shadow protected abstract void setRotation(float y, float x);

    @Inject(method = "setup", at = @At("TAIL"))
    private void setup(Level level, Entity entity, boolean bl, boolean bl2, float f, CallbackInfo ci) {
        applyScreenShake();
    }

    private void applyScreenShake() {
        if (OptionsSD.SCREEN_SHAKE.get()) {
            float intensity = ScreenShake.getShakeIntensity() * 0.5F;

            if (intensity > 0) {
                float yaw = (float) (Math.sin(System.currentTimeMillis() / 30.0) * intensity);
                float pitch = (float) (Math.cos(System.currentTimeMillis() / 60.0) * intensity);
                this.setRotation((float) (this.yRot - pitch * Math.sqrt(2)), this.xRot + (yaw));
            }
        }
    }
}