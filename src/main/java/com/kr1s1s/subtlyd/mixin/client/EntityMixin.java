package com.kr1s1s.subtlyd.mixin.client;

import com.kr1s1s.subtlyd.client.util.GroundShake;
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

@Mixin(Entity.class)
@Environment(EnvType.CLIENT)
public class EntityMixin {
    Entity entity = (Entity) (Object) this;
    List<SoundEvent> powerfulSounds = List.of(SoundEvents.WARDEN_ROAR, SoundEvents.WARDEN_SONIC_BOOM);
    List<SoundEvent> loudSounds = List.of(SoundEvents.RAVAGER_ROAR, SoundEvents.WARDEN_EMERGE, SoundEvents.ENDER_DRAGON_AMBIENT);

    @Inject(method = "playSound", at = @At("RETURN"))
    private void groundShake(SoundEvent soundEvent, float f, float g, CallbackInfo ci) {
        int duration = 25;
        int maxDistance = 16;
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            float distance = player.distanceTo(entity);
            if (powerfulSounds.contains(soundEvent)) {
                maxDistance = 32;
                GroundShake.setShakeByDistance(duration, maxDistance, distance);
            }

            if (loudSounds.contains(soundEvent)) {
                if (soundEvent.equals(SoundEvents.WARDEN_EMERGE)) {
                    duration = 110;
                }
                GroundShake.setShakeByDistance(duration, maxDistance, distance);
            }
        }
    }
}
