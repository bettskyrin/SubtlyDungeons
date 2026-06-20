package net.meander.subtlyd.data.worldgen.features;

import net.meander.subtlyd.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class MiscOverworldFeaturesSD {
    public static final ResourceKey<ConfiguredFeature<?, ?>> MUD_PATCH = ResourceKey.create(Registries.CONFIGURED_FEATURE, Util.identifier("mud_patch"));

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        context.register(MUD_PATCH, new ConfiguredFeature<>(Feature.DISK, new DiskConfiguration(BlockStateProvider.simple(Blocks.MUD), BlockPredicate.matchesTag(BlockTags.DIRT), UniformInt.of(5, 8), 1)));
    }
}
