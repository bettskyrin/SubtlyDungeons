package net.meander.subtlyd.mixin.client.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.meander.subtlyd.client.camera.shake.CameraShake;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("RETURN"))
    private void playSound(SoundEvent sound, float volume, float pitch, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;

        CameraShake.shakeScreenFromSource(sound, Vec3.atCenterOf(entity.blockPosition()));
    }
}