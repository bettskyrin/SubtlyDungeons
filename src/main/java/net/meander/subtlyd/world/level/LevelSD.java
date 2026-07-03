package net.meander.subtlyd.world.level;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.util.EventResult;
import net.meander.subtlyd.client.camera.shake.CameraShake;
import net.meander.subtlyd.core.particles.ParticleTypesSD;
import net.meander.subtlyd.world.entity.TentEntity;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.LivingEntity;

/**
 * @see net.minecraft.world.level.Level
 */
public class LevelSD {
    public static final WeightedList<ExplosionParticleInfo> DEFAULT_EXPLOSION_SPORE_PARTICLES = WeightedList.<ExplosionParticleInfo>builder()
            .add(new ExplosionParticleInfo(ParticleTypes.WARPED_SPORE, 1.0F, 0.05F))
            .add(new ExplosionParticleInfo(ParticleTypes.CRIMSON_SPORE, 1.0F, 0.05F))
            .add(new ExplosionParticleInfo(ParticleTypesSD.SPORE_CLOUD, 0.6F, 0.1F))
            .build();

    private static void registerClientTickEvents() {
        ClientTickEvents.START_LEVEL_TICK.register(_ -> {
            CameraShake.tick();
        });
    }

    private static void registerSleepEvents() {
        EntitySleepEvents.ALLOW_BED.register((livingEntity, _, _, _) -> {
            if (TentEntity.getTent(livingEntity, false) != null) {
                return EventResult.ALLOW;
            }
            return EventResult.PASS;
        });
        EntitySleepEvents.ALLOW_RESETTING_TIME.register(LivingEntity::isSleeping);
    }

    public static void registerEvents() {
        registerSleepEvents();
    }

    public static void registerClientEvents() {
        registerClientTickEvents();
    }
}
