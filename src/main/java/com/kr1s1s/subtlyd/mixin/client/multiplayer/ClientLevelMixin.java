package com.kr1s1s.subtlyd.mixin.client.multiplayer;

import com.kr1s1s.subtlyd.client.util.GroundShake;
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

@Mixin(ClientLevel.class)
@Environment(EnvType.CLIENT)
@SuppressWarnings("unused")
public class ClientLevelMixin {
    List<SoundEvent> powerfulSounds = List.of(SoundEvents.END_GATEWAY_SPAWN);
    List<SoundEvent> loudSounds = List.of(SoundEvents.ENDER_DRAGON_GROWL, SoundEvents.LIGHTNING_BOLT_IMPACT);

    @Inject(method = "playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V", at = @At("RETURN"))
    private void groundShake(double x, double y, double z, SoundEvent soundEvent, SoundSource soundSource, float g, float h, boolean bl, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;

        if (player != null){
            int duration = 20;
            float maxDistance = 32;
            if (powerfulSounds.contains(soundEvent)) {
                maxDistance = 128;
                float distance = (float) Math.sqrt(player.distanceToSqr(x, y, z));
                GroundShake.setShakeByDistance(duration, maxDistance, distance);
            }

            if (loudSounds.contains(soundEvent)) {
                float distance = (float) Math.sqrt(player.distanceToSqr(x, y, z));
                if (soundEvent.equals(SoundEvents.ENDER_DRAGON_GROWL)) {
                    duration = 70;
                }
                GroundShake.setShakeByDistance(duration, maxDistance, distance);
            }
        }
    }
}
