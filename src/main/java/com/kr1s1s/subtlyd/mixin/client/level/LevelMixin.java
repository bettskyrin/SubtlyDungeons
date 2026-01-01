package com.kr1s1s.subtlyd.mixin.client.level;

import com.kr1s1s.subtlyd.util.ScreenShake;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
public class LevelMixin {
    @Inject(method = "playSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V",
    at = @At("RETURN"))
    private void playSound(Entity except, double x, double y, double z,
                                 SoundEvent sound, SoundSource source, float volume, float pitch, CallbackInfo ci) {
        ScreenShake.shakeScreenFromSource(sound, new Vec3(x, y, z), 0);
    }
}
