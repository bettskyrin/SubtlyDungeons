package net.meander.subtlyd.core.particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

/**
 * @see ParticleTypes
 */
public class ParticleTypesSD {
    public static final SimpleParticleType SPORE_CLOUD = register("spore_cloud", false);
    public static final SimpleParticleType BLADE_CLASH = register("blade_clash", false);

    public static void registerServer() {
        UtilSD.LOGGER.debug("Registering server-side particle data...");}

    private static SimpleParticleType register(final String name, final boolean overrideLimiter) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, UtilSD.identifier(name), FabricParticleTypes.simple(overrideLimiter));
    }
}
