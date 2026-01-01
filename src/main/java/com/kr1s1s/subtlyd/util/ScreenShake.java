package com.kr1s1s.subtlyd.util;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;


public class ScreenShake {
    private static int totalDuration = 0;
    private static int remainingDuration = 0;
    private static float intensity = 0.0F;
    private static final Minecraft minecraft = Minecraft.getInstance();
    /**
     * List of sound events, their maxDistance value, and duration value.
     */
    private static final List<Triple<SoundEvent, Integer, Integer>> shakeEvents = List.of(
            Triple.of(SoundEvents.WARDEN_ROAR, 32, 50),
            Triple.of(SoundEvents.WARDEN_SONIC_BOOM, 64, 25),
            Triple.of(SoundEvents.WARDEN_EMERGE, 128, 110),
            Triple.of(SoundEvents.WARDEN_DIG, 128, 25),
            Triple.of(SoundEvents.RAVAGER_ROAR, 32, 25),
            Triple.of(SoundEvents.ENDER_DRAGON_AMBIENT, 64, 25),
            Triple.of(SoundEvents.WEATHER_END_FLASH, 64, 25),
            Triple.of(SoundEvents.END_GATEWAY_SPAWN, 512, 25),
            Triple.of(SoundEvents.LIGHTNING_BOLT_IMPACT, 32, 20),
            Triple.of(SoundEvents.DRAGON_FIREBALL_EXPLODE, 32, 25),
            Triple.of(SoundEvents.GENERIC_EXPLODE.value(), 16, 20),
            Triple.of(SoundEvents.MACE_SMASH_AIR, 8, 20),
            Triple.of(SoundEvents.MACE_SMASH_GROUND, 8, 20),
            Triple.of(SoundEvents.MACE_SMASH_GROUND_HEAVY, 10, 20)
    );

    /**
     * Ticks the screen shake event and decreases the remaining duration.
     */
    public static void tick() {
        if (remainingDuration > 0.0F) {
            remainingDuration--;
        } else {
            intensity = 0.0F;
        }
    }

    /**
     * @return The intensity value of the screen shake.
     */
    public static float getShakeIntensity() {
        float progress = remainingDuration / (float) totalDuration;

        if (remainingDuration <= 0 || intensity <= 0) {
            return 0.0F;
        }
        return intensity * Mth.square(progress);
    }

    /**
     * Determines the remaining duration and intensity of the screen shake.
     * @param durationTicks The duration of the screen shake in ticks.
     * @param magnitude The intensity of the screen shake.
     */
    private static void setShake(int durationTicks, float magnitude) {
        durationTicks += 10;
        if (remainingDuration <= 0 || magnitude > intensity) {
            totalDuration = durationTicks;
            remainingDuration = durationTicks;
            intensity = magnitude;
        }
    }

    /**
     * Sets a screen shake with intensity that varies by distance and magnitude.
     * @param durationTicks The duration of the screen shake in ticks.
     * @param maxDistance The maximum distance from the source of the screen shake that the shake can go in effect.
     * @param distance The distance of the player from the screen shake source.
     * @param modifier The magnitude modifier.
     */
    public static void setShake(int durationTicks, float maxDistance, float distance, float modifier) {
        if (distance <= maxDistance) {
            setShake(durationTicks, ((maxDistance - distance) / maxDistance) * modifier);
        }
    }

    /**
     * Shakes the screen based on the distance from a source.
     * @param soundEvent The sound event causing the screen shake.
     * @param source The source of the screen shake event.
     */
    public static void shakeScreenFromSource(SoundEvent soundEvent, Vec3 source, float modifier) {
        float maxDistance;
        int duration;
        Vec3 sourcePos = new Vec3(source.x, source.y, source.z);
        Player player = (Player) minecraft.getCameraEntity();

        if (player != null && !player.isSpectator() && player.level().isClientSide()) {
            float distance = (float) Math.sqrt(player.distanceToSqr(sourcePos));

            for (Triple<SoundEvent, Integer, Integer> triple : shakeEvents) {
                 if (triple.getLeft() == soundEvent) {
                     if (modifier == 0.0F) {
                         maxDistance = triple.getMiddle();
                         duration = triple.getRight();
                         modifier = 4.0F;
                     } else {
                         maxDistance = (int) (16 * (modifier / 3));
                         duration = 15;
                     }
                     setShake(duration, maxDistance, distance, modifier);
                }
            }
        }
    }
}