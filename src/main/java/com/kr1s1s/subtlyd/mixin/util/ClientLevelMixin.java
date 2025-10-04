package com.kr1s1s.subtlyd.mixin.util;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.client.util.GroundShake;
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

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    List<SoundEvent> powerfulSounds = List.of(SoundEvents.ENDER_DRAGON_GROWL);
    List<SoundEvent> loudSounds = List.of(SoundEvents.END_PORTAL_SPAWN, SoundEvents.END_GATEWAY_SPAWN);

    @Inject(method = "playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V", at = @At("RETURN"))
    private void groundShake(double x, double y, double z, SoundEvent soundEvent, SoundSource soundSource, float g, float h, boolean bl, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            if (powerfulSounds.contains(soundEvent)) {
                float maxDistance = 256;
                float distance = (float) Math.sqrt(player.distanceToSqr(x, y, z));
                GroundShake.shakeByDistance(40, maxDistance, distance, 2);
            }

            if (loudSounds.contains(soundEvent)) {
                float maxDistance = 32;
                float distance = (float) Math.sqrt(player.distanceToSqr(x, y, z));
                GroundShake.shakeByDistance(10, maxDistance, distance);

            }
        }
    }
}
