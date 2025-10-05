package com.kr1s1s.subtlyd.mixin.util;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.client.util.GroundShake;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
@SuppressWarnings("unused")
public abstract class CameraMixin {
    @Shadow
    private float yRot;
    @Shadow private float xRot;
    @Shadow protected abstract void setRotation(float y, float x);

    @Inject(method = "setup", at = @At("TAIL"))
    private void applyCameraShake(BlockGetter blockGetter, Entity entity, boolean bl, boolean bl2, float f, CallbackInfo ci) {
        float intensity = GroundShake.getShake();

        if (intensity > 0) {
            float yaw = (float) (Math.sin(System.currentTimeMillis() / 30.0) * intensity * 0.5F);
            float pitch = (float) (Math.cos(System.currentTimeMillis() / 60.0) * intensity * 0.5F);
            this.setRotation((float) (this.yRot - pitch * Math.sqrt(2)), this.xRot + (yaw));
        }
    }
}