package net.meander.subtlyd.mixin.client.level;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.meander.subtlyd.client.OptionInstanceSD;
import net.meander.subtlyd.camera.CameraShake;
import net.meander.subtlyd.world.entity.TentEntity;
import net.minecraft.client.Camera;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
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
    @Shadow protected abstract void setPosition(double x, double y, double z);

    @Shadow
    public abstract @Nullable Entity entity();

    @Shadow
    public abstract float xRot();

    @Inject(method = "setPosition(Lnet/minecraft/world/phys/Vec3;)V", at = @At("TAIL"))
    private void setup(Vec3 position, CallbackInfo ci) {
        applyScreenShake();
        setCampingPlayerCamera(this.entity(), this.xRot());
    }

    /**
     * Determines if screen shake is enabled and if so, handles the math for placing the camera to create the screen shake effect.
     */
    private void applyScreenShake() {
        if (OptionInstanceSD.SCREEN_SHAKE.get()) {
            float intensity = CameraShake.getShakeIntensity() * 0.5F;
            if (intensity > 0.0F) {
                float yaw = (float) (Math.sin(Util.getMillis() / 30.0) * intensity);
                float pitch = (float) (Math.cos(Util.getMillis() / 60.0) * intensity);
                this.setRotation((float) (this.yRot - pitch * Math.sqrt(2)), this.xRot + (yaw));
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