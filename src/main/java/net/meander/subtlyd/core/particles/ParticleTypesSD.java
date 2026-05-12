package net.meander.subtlyd.core.particles;

import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.meander.subtlyd.client.particle.SporeCloudParticle;
import net.meander.subtlyd.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ParticleTypesSD {
    public static final SimpleParticleType SPORE_CLOUD = register("spore_cloud", false);

    public static void registration() {
        ParticleProviderRegistry.getInstance().register(SPORE_CLOUD, SporeCloudParticle.Provider::new);
    }

    private static SimpleParticleType register(final String name, final boolean overrideLimiter) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, Util.identifier(name), FabricParticleTypes.simple(overrideLimiter));
    }

    public static void generatePotionParticles(Level level, BlockPos pos, int color, boolean generateMultiple) {
        ParticleStatus particleStatus = Minecraft.getInstance().options.particles().get();
        BlockState stateAbove = level.getBlockState(pos.above());

        if (particleStatus != ParticleStatus.MINIMAL && !stateAbove.canOcclude()) {
            RandomSource random = RandomSource.create();
            int multiplier, particleCount = 1;

            if (generateMultiple) {
                multiplier = particleStatus == ParticleStatus.DECREASED ? 1 : 2;
                particleCount = random.nextInt(3 * multiplier, 5 * multiplier);
            } else {
                if (particleStatus == ParticleStatus.DECREASED && random.nextInt(10) % 5 != 0)
                    return;
                else if (random.nextInt(10) % 3 != 0)
                    return;
            }

            for (int i = 1; i <= particleCount; i++) {
                Minecraft.getInstance().particleEngine.createParticle(
                        ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, color),
                        pos.getX() + 0.45 + random.nextDouble() * 0.2,
                        pos.getY() + 1.0,
                        pos.getZ() + 0.45 + random.nextDouble() * 0.2,
                        0.7,
                        1.3,
                        0.7
                );
            }
        }
    }
}
