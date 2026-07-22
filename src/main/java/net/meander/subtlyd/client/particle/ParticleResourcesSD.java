package net.meander.subtlyd.client.particle;

import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.meander.subtlyd.core.particles.ParticleTypesSD;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @see net.minecraft.client.particle.ParticleResources
 */
public class ParticleResourcesSD {
    public static void registerProviders() {
        ParticleProviderRegistry.getInstance().register(ParticleTypesSD.SPORE_CLOUD, SporeCloudParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(ParticleTypesSD.BLADE_CLASH, BladeClashParticle.Provider::new);
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
                if ((particleStatus == ParticleStatus.DECREASED && random.nextInt(10) % 5 != 0) || (random.nextInt(10) % 3 != 0)) {
                    return;
                }
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
