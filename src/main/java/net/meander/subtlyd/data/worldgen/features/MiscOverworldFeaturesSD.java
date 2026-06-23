package net.meander.subtlyd.data.worldgen.features;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.DiskFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

/**
 * @see net.minecraft.data.worldgen.features.MiscOverworldFeatures
 */
public class MiscOverworldFeaturesSD {
    public static final ResourceKey<Feature> MUD_PATCH = FeatureUtilsSD.createKey("mud_patch");

    public static void bootstrap(BootstrapContext<Feature> context) {
        context.register(MUD_PATCH, new DiskFeature(BlockStateProvider.simple(Blocks.MUD), BlockPredicate.matchesTag(BlockTags.DIRT), UniformInt.of(5, 8), 1));
    }
}
