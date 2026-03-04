package net.meander.subtlyd.util;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;


public class CameraShake {
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
            Triple.of(SoundEvents.MACE_SMASH_AIR, 8, 10),
            Triple.of(SoundEvents.MACE_SMASH_GROUND, 8, 10),
            Triple.of(SoundEvents.MACE_SMASH_GROUND_HEAVY, 10, 15)
    );

    /**
     * Ticks the camera shake event and decreases the remaining duration.
     */
    public static void tick() {
        if (remainingDuration > 0.0F) {
            remainingDuration--;
        } else {
            intensity = 0.0F;
        }
    }

    /**
     * @return The intensity value of the camera shake.
     */
    public static float getShakeIntensity() {
        float progress = remainingDuration / (float) totalDuration;

        if (remainingDuration <= 0 || intensity <= 0) {
            return 0.0F;
        }
        return intensity * Mth.square(progress);
    }

    /**
     * Determines the remaining duration and intensity of the camera shake.
     * @param durationTicks The duration of the camera shake in ticks.
     * @param magnitude The intensity of the camera shake.
     */
    public static void setShake(int durationTicks, float magnitude) {
        durationTicks += 10;
        if (remainingDuration <= 0 || magnitude > intensity) {
            totalDuration = durationTicks;
            remainingDuration = durationTicks;
            intensity = magnitude;
        }
    }

    /**
     * Sets a camera shake with intensity that varies by distance and magnitude.
     * @param durationTicks The duration of the camera shake in ticks.
     * @param maxDistance The maximum distance from the source of the camera shake that the shake can go in effect.
     * @param distance The distance of the player from the camera shake source.
     * @param modifier The magnitude modifier.
     */
    public static void setShake(int durationTicks, float maxDistance, float distance, float modifier) {
        if (distance <= maxDistance) {
            setShake(durationTicks, ((maxDistance - distance) / maxDistance) * modifier);
        }
    }

    /**
     * Shakes the screen based on the distance from a source.
     * @param soundEvent The sound event causing the camera shake.
     * @param source The source of the camera shake event.
     */
    public static void shakeScreenFromSource(SoundEvent soundEvent, Vec3 source, float modifier) {
        float maxDistance;
        int duration;
        Vec3 sourcePos = new Vec3(source.x, source.y, source.z);
        Entity player = minecraft.getCameraEntity();

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

    /**
     * Stops the camera shake effect
     */
    public static void stop() {
        remainingDuration = 0;
        intensity = 0.0F;
    }
}