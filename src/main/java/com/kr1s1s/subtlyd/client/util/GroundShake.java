package com.kr1s1s.subtlyd.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;


public class GroundShake {
    private static int totalDuration = 0;
    private static int remainingDuration = 0;
    private static float intensity = 0.0F;
    private static final Minecraft minecraft = Minecraft.getInstance();
    private static final RandomSource random = minecraft.level.getRandom();

    public static void shakeByDistance(int durationTicks, float maxDistance, float distance) {
        shakeByDistance(durationTicks, maxDistance, distance, 1.5F);
    }

    public static void shakeByDistance(int durationTicks, float maxDistance, float distance, float modifier) {
        if (distance < Math.pow(maxDistance, 2)) {
            shake(durationTicks, ((maxDistance - distance) / maxDistance) * modifier);
        }
    }

    public static void shake(int durationTicks, float magnitude) {
        if (remainingDuration <= 0 || magnitude > intensity) {
            totalDuration = durationTicks;
            remainingDuration = durationTicks;
            intensity = magnitude;
        }
    }

    public static void tick() {
        if (remainingDuration > 0) {
            remainingDuration--;
            applyShake();
        } else {
            intensity = 0.0F;
        }
    }

    public static void applyShake() {
        if (remainingDuration <= 0) {
            return;
        }
        float tickDelta = minecraft.getDeltaTracker().getGameTimeDeltaTicks();
        float progress = (remainingDuration - tickDelta) / (float) totalDuration;
        float currentIntensity = 1.5F * intensity * progress * progress;
        Entity entity = minecraft.getCameraEntity();
        Vec3 movement = entity.getKnownMovement();

        if (currentIntensity > 0) {
            float offsetX = (random.nextFloat() - 0.5F) * (currentIntensity / 10F);
            float offsetY = (random.nextFloat() - 0.5F) * (currentIntensity / 10F);
            float offsetZ = (random.nextFloat() - 0.5F) * (currentIntensity / 10F);

            if (entity.onGround()) {
                entity.setDeltaMovement(movement.x() + offsetX, movement.y() + offsetY, movement.z() + offsetZ);
                entity.setDeltaMovement(movement.x() + (offsetY / 2), movement.y() + offsetZ, movement.z() + (offsetX / 2));
            }
        }
    }
}
