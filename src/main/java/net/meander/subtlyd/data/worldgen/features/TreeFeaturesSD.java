package net.meander.subtlyd.data.worldgen.features;

import net.meander.subtlyd.world.level.levelgen.feature.trunkplacers.BaobabTrunkPlacer;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.AcaciaFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

/**
 * @see net.minecraft.data.worldgen.features.TreeFeatures
 */
public class TreeFeaturesSD {
    public static final ResourceKey<Feature> BAOBAB = FeatureUtilsSD.createKey("baobab");

    private static TreeFeature.Builder createBaobab(final BlockStateProvider belowTrunkProvider) {
        return new TreeFeature.Builder(
                BlockStateProvider.simple(Blocks.ACACIA_LOG),
                new BaobabTrunkPlacer(9, 2, 1),
                BlockStateProvider.simple(Blocks.ACACIA_LEAVES),
                new AcaciaFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0)),
                new TwoLayersFeatureSize(1, 0, 2),
                belowTrunkProvider
        );
    }

    public static void registration(final BootstrapContext<Feature> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        BlockStateProvider belowTrunkProvider = TreeFeature.defaultPlaceBelowTreeTrunkProvider(biomes);

        context.register(BAOBAB, createBaobab(belowTrunkProvider).ignoreVines().build());
    }
}
