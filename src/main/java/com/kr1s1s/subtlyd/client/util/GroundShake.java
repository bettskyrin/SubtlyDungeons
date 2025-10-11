package com.kr1s1s.subtlyd.client.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

import java.util.Objects;


public class GroundShake {
    private static int totalDuration = 0;
    private static int remainingDuration = 0;
    private static float intensity = 0.0F;
    private static final Minecraft minecraft = Minecraft.getInstance();

    public static void setShakeByDistance(int durationTicks, float maxDistance, float distance) {
        setShakeByDistance(durationTicks, maxDistance, distance, 4);
    }

    public static void setShakeByDistance(int durationTicks, float maxDistance, float distance, float modifier) {
        if (distance < Math.pow(maxDistance, 2)) {
            shake(durationTicks, ((maxDistance - distance) / maxDistance) * modifier);
        }
    }

    public static void shake(int durationTicks, float magnitude) {
        durationTicks += 10;
        if (remainingDuration <= 0 || magnitude > intensity) {
            totalDuration = durationTicks;
            remainingDuration = durationTicks;
            intensity = magnitude;
        }
    }

    public static void tick() {
        if (remainingDuration > 0) {
            remainingDuration--;
            getShake();
        } else {
            intensity = 0.0F;
        }
    }

    public static float getShake() {
        float progress = (remainingDuration) / (float) totalDuration;
        Entity entity = minecraft.getCameraEntity();

        if (remainingDuration <= 0 || !Objects.requireNonNull(entity).onGround() || intensity <= 0) {
            return 0.0F;
        }

        return intensity * progress * progress;
    }
}
