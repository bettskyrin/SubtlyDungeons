package com.kr1s1s.subtlyd.mixin.client.entity;

import com.kr1s1s.subtlyd.client.util.ScreenShake;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(Entity.class)
public class EntityMixin {
    @SuppressWarnings("DataFlowIssue")
    private final Entity entity = (Entity) (Object) this;

    @Inject(method = "playSound(Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("RETURN"))
    private void playSound(SoundEvent soundEvent, float f, float g, CallbackInfo ci) {
        shakeScreenByEvent(soundEvent);
    }

    private void shakeScreenByEvent(SoundEvent soundEvent) {
        List<SoundEvent> powerfulSounds = List.of(SoundEvents.WARDEN_ROAR, SoundEvents.WARDEN_SONIC_BOOM);
        List<SoundEvent> loudSounds = List.of(SoundEvents.RAVAGER_ROAR, SoundEvents.WARDEN_EMERGE, SoundEvents.WARDEN_DIG, SoundEvents.ENDER_DRAGON_AMBIENT);

        int duration = 25;
        int maxDistance = 16;
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            float distance = player.distanceTo(entity);
            if (powerfulSounds.contains(soundEvent)) {
                maxDistance = 32;
                ScreenShake.setShakeByDistance(duration, maxDistance, distance);
            }

            if (loudSounds.contains(soundEvent)) {
                if (soundEvent.equals(SoundEvents.WARDEN_EMERGE)) {
                    maxDistance = 32;
                    duration = 110;
                }
                ScreenShake.setShakeByDistance(duration, maxDistance, distance);
            }
        }
    }
}
