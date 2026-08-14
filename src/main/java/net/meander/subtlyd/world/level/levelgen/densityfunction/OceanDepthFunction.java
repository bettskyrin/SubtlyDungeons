package net.meander.subtlyd.world.level.levelgen.densityfunction;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.meander.subtlyd.client.gui.screens.CustomTerrainSettings;
import net.minecraft.data.worldgen.TerrainProvider;
import net.minecraft.util.Interval;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.NoiseRouterData;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;

public record OceanDepthFunction(DensityFunction delegate, DensityFunction continents) implements DensityFunction {
    public static final MapCodec<OceanDepthFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    DensityFunction.CODEC.fieldOf("delegate").forGetter(OceanDepthFunction::delegate),
                    DensityFunction.CODEC.fieldOf("continents").forGetter(OceanDepthFunction::continents)
            ).apply(instance, OceanDepthFunction::new)
    );

    @Override
    public float compute(FunctionContext context) {
        float baseDensity = delegate.compute(context);
        float continentValue = continents.compute(context);

        if (continentValue < TerrainProvider.BEACH_CONTINENTALNESS && baseDensity < NoiseRouterData.GLOBAL_OFFSET) {
            float transition = Mth.clamp((continentValue - TerrainProvider.BEACH_CONTINENTALNESS) / (TerrainProvider.DEEP_OCEAN_CONTINENTALNESS - TerrainProvider.BEACH_CONTINENTALNESS), 0.0F, 1.0F);

            float maxScalar = (float) (1.5 * CustomTerrainSettings.oceanDepthScale);
            float currentScalar = Mth.lerp(transition, 1.0F, maxScalar);

            float isolatedDepth = baseDensity - NoiseRouterData.GLOBAL_OFFSET;
            float scaledDepth = isolatedDepth * currentScalar;

            return NoiseRouterData.GLOBAL_OFFSET + scaledDepth;
        }

        return baseDensity;
    }

    @Override
    public void fillArray(float[] output, ContextProvider contextProvider) {
        delegate.fillArray(output, contextProvider);

        float[] continentValues = new float[output.length];
        continents.fillArray(continentValues, contextProvider);

        for (int i = 0; i < output.length; i++) {
            float baseDensity = output[i];
            float continentValue = continentValues[i];

            if (continentValue < TerrainProvider.BEACH_CONTINENTALNESS && baseDensity < NoiseRouterData.GLOBAL_OFFSET) {
                float transition = Mth.clamp((continentValue - TerrainProvider.BEACH_CONTINENTALNESS) / (TerrainProvider.DEEP_OCEAN_CONTINENTALNESS - TerrainProvider.BEACH_CONTINENTALNESS), 0.0F, 1.0F);

                float maxScalar = (float) (1.5 * CustomTerrainSettings.oceanDepthScale);
                float currentScalar = Mth.lerp(transition, 1.0F, maxScalar);

                float isolatedDepth = baseDensity - NoiseRouterData.GLOBAL_OFFSET;
                float scaledDepth = isolatedDepth * currentScalar;

                output[i] = NoiseRouterData.GLOBAL_OFFSET + scaledDepth;
            }
        }
    }

    @Override
    public DensityFunction mapChildren(Visitor visitor) {
        return new OceanDepthFunction(delegate.mapAll(visitor), continents.mapAll(visitor));
    }

    @Override
    public Interval range() {
        return delegate.range();
    }

    @Override
    public @Axes int domainAxes() {
        return delegate.domainAxes() | continents.domainAxes();
    }

    @Override
    public MapCodec<? extends DensityFunction> codec() {
        return CODEC;
    }
}