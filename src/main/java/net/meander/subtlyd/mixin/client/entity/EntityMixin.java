package net.meander.subtlyd.mixin.client.entity;

import net.meander.subtlyd.util.CameraShake;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("RETURN"))
    private void playSound(SoundEvent soundEvent, float f, float g, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        CameraShake.shakeScreenFromSource(soundEvent, entity.blockPosition().getCenter(), 0);
    }
}