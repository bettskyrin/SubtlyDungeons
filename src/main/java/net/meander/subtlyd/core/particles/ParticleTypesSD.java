package net.meander.subtlyd.core.particles;

import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.meander.subtlyd.client.particle.SporeCloudParticle;
import net.meander.subtlyd.util.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

public class ParticleTypesSD {
    public static final SimpleParticleType SPORE_CLOUD = register("spore_cloud", false);

    public static void registration() {
        ParticleProviderRegistry.getInstance().register(SPORE_CLOUD, SporeCloudParticle.Provider::new);
    }

    private static SimpleParticleType register(final String name, final boolean overrideLimiter) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, Util.identifier(name), FabricParticleTypes.simple(overrideLimiter));
    }
}
