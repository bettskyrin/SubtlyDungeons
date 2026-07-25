package net.meander.subtlyd.world.level;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.util.EventResult;
import net.meander.subtlyd.client.camera.shake.CameraShake;
import net.meander.subtlyd.core.particles.ParticleTypesSD;
import net.meander.subtlyd.world.entity.Tent;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.LivingEntity;

/**
 * @see net.minecraft.world.level.Level
 */
public interface LevelSD {
    default Identifier getClimateAsTemperatureVariant(BlockPos pos) {
        Level level = (Level) this;

        if (level.precipitationAt(pos) == Biome.Precipitation.SNOW || level.getBiome(pos).value().coldEnoughToSnow(pos, level.getSeaLevel())) {
            return TemperatureVariants.COLD;
        } else if (level.getBiome(pos).value().getBaseTemperature() >= 2.0F) {
            if (level.isDarkOutside() || level.isWaterAt(pos) || level.isRainingAt(pos)) {
                return TemperatureVariants.TEMPERATE;
            }

            return TemperatureVariants.WARM;
        } else {
            return TemperatureVariants.TEMPERATE;
        }
    }

    private static void registerClientTickEvents() {
        ClientTickEvents.START_LEVEL_TICK.register(_ -> {
            CameraShake.tick();
        });
    }

    private static void registerSleepEvents() {
        EntitySleepEvents.ALLOW_BED.register((livingEntity, _, _, _) -> {
            if (Tent.getTent(livingEntity, false) != null) {
                return EventResult.ALLOW;
            }
            return EventResult.PASS;
        });

        EntitySleepEvents.ALLOW_RESETTING_TIME.register(LivingEntity::isSleeping);
    }

    static void registerEvents() {
        registerSleepEvents();
    }

    static void registerClientEvents() {
        registerClientTickEvents();
    }
}
