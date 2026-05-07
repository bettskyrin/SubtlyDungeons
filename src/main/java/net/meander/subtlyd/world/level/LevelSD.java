package net.meander.subtlyd.world.level;

import net.meander.subtlyd.core.particles.ParticleTypesSD;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.random.WeightedList;

public class LevelSD {
    public static final WeightedList<ExplosionParticleInfo> DEFAULT_EXPLOSION_SPORE_PARTICLES = WeightedList.<ExplosionParticleInfo>builder()
            .add(new ExplosionParticleInfo(ParticleTypes.WARPED_SPORE, 1.0F, 0.05F))
            .add(new ExplosionParticleInfo(ParticleTypes.CRIMSON_SPORE, 1.0F, 0.05F))
            .add(new ExplosionParticleInfo(ParticleTypesSD.SPORE_CLOUD, 0.6F, 0.1F))
            .build();
}
