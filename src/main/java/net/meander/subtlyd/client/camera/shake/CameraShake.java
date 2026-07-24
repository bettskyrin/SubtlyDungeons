package net.meander.subtlyd.client.camera.shake;

import net.meander.subtlyd.core.registries.RegistriesSD;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class CameraShake {
    private static int totalDuration = 0;
    private static int remainingDuration = 0;
    private static float intensity = 0.0F;
    private static final Minecraft minecraft = Minecraft.getInstance();

    public static void tick() {
        if (remainingDuration > 0.0F) {
            remainingDuration--;
        } else {
            intensity = 0.0F;
        }
    }


    public static float getShakeIntensity() {
        if (remainingDuration <= 0 || intensity <= 0) {
            return 0.0F;
        } else {
            float progress = remainingDuration / (float) totalDuration;

            return intensity * Mth.square(progress);
        }
    }

    /**
     * Determines the remaining duration and intensity of the camera shake.
     * @param durationTicks The duration of the camera shake in ticks.
     * @param magnitude The intensity of the camera shake.
     */
    public static void setShake(int durationTicks, float magnitude) {
        durationTicks += 10;

        if (remainingDuration <= 0 || magnitude > intensity || durationTicks > remainingDuration) {
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
     * @param modifier The magnitude intensity.
     */
    public static void setShake(int durationTicks, float maxDistance, float distance, float modifier) {
        if (maxDistance <= 0.0F) {
            maxDistance = 1.0F;
        }

        if (distance <= maxDistance) {
            setShake(durationTicks, ((maxDistance - distance) / maxDistance) * modifier);
        }
    }

    public static void shakeScreenFromSource(final SoundEvent soundEvent, final Vec3 source) {
        shakeScreenFromSource(soundEvent, source, 4.0F);
    }

    /**
     * Shakes the screen based on the distance from a source.
     * @param soundEvent The sound event causing the camera shake.
     * @param source The source of the camera shake event.
     */
    public static void shakeScreenFromSource(final SoundEvent soundEvent, final Vec3 source, float modifier) {
        Entity player = minecraft.getCameraEntity();

        if (player != null && !player.isSpectator() && player.level().isClientSide()) {
            Vec3 sourcePos = new Vec3(source.x, source.y, source.z);
            float distance = (float) Math.sqrt(player.distanceToSqr(sourcePos));

            try {
                Registry<CameraShakeEvent> registry = player.level().registryAccess().lookupOrThrow(RegistriesSD.CAMERA_SHAKE_EVENT);

                for (CameraShakeEvent event : registry) {
                    if (event.soundEvent().equals(soundEvent.location())) {
                        int duration = event.durationTicks();
                        int maxDistance = event.range();

                        if (modifier == 4.0F) {
                            modifier = event.intensity();
                        } else {
                            maxDistance = ((int) (16.0F * (modifier / 3.0F)));
                            duration = 15;
                        }

                        setShake(duration, maxDistance, distance, modifier);
                        break;
                    }
                }
            } catch (Exception e) {
                UtilSD.LOGGER.error("Failed to load shake event: {}", e.getMessage());
            }
        }
    }

    public static void stop() {
        remainingDuration = 0;
        intensity = 0.0F;
    }
}