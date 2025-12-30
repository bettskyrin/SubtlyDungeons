package com.kr1s1s.subtlyd.mixin.client.level;

import com.kr1s1s.subtlyd.util.ScreenShake;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Inject(method = "playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V", at = @At("RETURN"))
    private void playLocalSound(double x, double y, double z, SoundEvent soundEvent, SoundSource soundSource, float g, float h, boolean bl, CallbackInfo ci) {
        shakeScreenByEvent(x, y, z, soundEvent);
    }

    /**
     * Determines the duration and intensity of a screen shake event
     * @param x Sound source x-coordinate
     * @param y Sound source y-coordinate
     * @param z Sound source z-coordinate
     * @param soundEvent Triggering sound event
     */
    private void shakeScreenByEvent(double x, double y, double z, SoundEvent soundEvent) {
        List<SoundEvent> powerfulSounds = List.of(SoundEvents.END_GATEWAY_SPAWN);
        List<SoundEvent> loudSounds = List.of(SoundEvents.ENDER_DRAGON_GROWL, SoundEvents.LIGHTNING_BOLT_IMPACT);
        List<SoundEvent> explosions = List.of(SoundEvents.DRAGON_FIREBALL_EXPLODE);
        Player player = Minecraft.getInstance().player;

        if (player != null) {
            int duration = 20;
            float maxDistance = 32;
            float distance = (float) Math.sqrt(player.distanceToSqr(x, y, z));

            if (powerfulSounds.contains(soundEvent)) {
                maxDistance = 128;
                ScreenShake.setShakeByDistance(duration, maxDistance, distance);
            }

            if (loudSounds.contains(soundEvent)) {
                if (soundEvent.equals(SoundEvents.ENDER_DRAGON_GROWL)) {
                    duration = 70;
                }
                ScreenShake.setShakeByDistance(duration, maxDistance, distance);
            }

            if (explosions.contains(soundEvent)) {
                if (soundEvent.equals(SoundEvents.DRAGON_FIREBALL_EXPLODE)) {
                    ScreenShake.setShakeByDistanceAndPower(15, maxDistance, distance, 3);
                }
            }
        }
    }
}
