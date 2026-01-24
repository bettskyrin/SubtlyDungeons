package com.meander.subtlyd.mixin.client.entity;

import com.meander.subtlyd.util.ScreenShake;
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
    private final Entity entity = (Entity) (Object) this;

    @Inject(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("RETURN"))
    private void playSound(SoundEvent soundEvent, float f, float g, CallbackInfo ci) {
        ScreenShake.shakeScreenFromSource(soundEvent, entity.blockPosition().getCenter(), 0);
    }
}