package com.kr1s1s.subtlyd.mixin.entity;

import com.kr1s1s.subtlyd.client.util.GroundShake;
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
public class EntityMixin {
    Entity entity = (Entity) (Object) this;
    List<SoundEvent> powerfulSounds = List.of(SoundEvents.WARDEN_ROAR, SoundEvents.WARDEN_SONIC_BOOM);
    List<SoundEvent> loudSounds = List.of(SoundEvents.RAVAGER_ROAR, SoundEvents.WARDEN_EMERGE);

    @Inject(method = "playSound", at = @At("RETURN"))
    private void groundShake(SoundEvent soundEvent, float f, float g, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            if (powerfulSounds.contains(soundEvent)) {
                int maxDistance = 128;
                float distance = (float) player.distanceToSqr(entity);
                GroundShake.shakeByDistance(20, maxDistance, distance, 8);
            }

            if (loudSounds.contains(soundEvent)) {
                int maxDistance = 32;
                float distance = (float) player.distanceToSqr(entity);
                GroundShake.shakeByDistance(20, maxDistance, distance, 4);
            }
        }
    }


}
