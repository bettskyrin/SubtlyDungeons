package net.meander.subtlyd.world.level;

import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.random.WeightedList;

public class LevelSD {
    public static final WeightedList<ExplosionParticleInfo> DEFAULT_EXPLOSION_SPORE_PARTICLES = WeightedList.<ExplosionParticleInfo>builder()
            .add(new ExplosionParticleInfo(ParticleTypes.WARPED_SPORE, 0.5F, 1.0F))
            .add(new ExplosionParticleInfo(ParticleTypes.CRIMSON_SPORE, 1.0F, 1.0F))
            .add(new ExplosionParticleInfo(ParticleTypes.CRIMSON_SPORE, 2.0F, 0.5F))
            .add(new ExplosionParticleInfo(ParticleTypes.WARPED_SPORE, 2.0F, 0.5F))
            .build();
}
