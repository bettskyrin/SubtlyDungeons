package net.meander.subtlyd.data.worldgen.features;

import net.meander.subtlyd.world.block.BlocksSD;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.RandomSelectorFeature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

/**
 * @see net.minecraft.data.worldgen.features.VegetationFeatures
 */
public class VegetationFeaturesSD {
    public static final ResourceKey<Feature> PERSE_WILDFLOWER = FeatureUtilsSD.createKey("perse_wildflower");
    public static final ResourceKey<Feature> WILDFLOWER = FeatureUtilsSD.createKey("wildflower");
    public static final ResourceKey<Feature> DARK_FOREST_VEGETATION = FeatureUtilsSD.createKey("dark_forest_vegetation");

    @SuppressWarnings("deprecation")
    public static void bootstrap(BootstrapContext<Feature> context) {
        HolderGetter<Feature> configuredFeatures = context.lookup(Registries.FEATURE);
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        Holder<Feature> hugeBrownMushroom = configuredFeatures.getOrThrow(TreeFeatures.HUGE_BROWN_MUSHROOM);
        Holder<Feature> hugeRedMushroom = configuredFeatures.getOrThrow(TreeFeatures.HUGE_RED_MUSHROOM);
        Holder<PlacedFeature> oakLeafLitter = placedFeatures.getOrThrow(TreePlacements.OAK_LEAF_LITTER);
        Holder<PlacedFeature> fancyOakLeafLitter = placedFeatures.getOrThrow(TreePlacements.FANCY_OAK_LEAF_LITTER);
        Holder<PlacedFeature> fallenOak = placedFeatures.getOrThrow(TreePlacements.FALLEN_OAK_TREE);
        Holder<PlacedFeature> darkOakLeafLitter = placedFeatures.getOrThrow(TreePlacements.DARK_OAK_LEAF_LITTER);

        context.register(PERSE_WILDFLOWER, new SimpleBlockFeature(new WeightedStateProvider(VegetationFeatures.flowerBedPatchBuilder(BlocksSD.PERSE_WILDFLOWERS))));
        context.register(WILDFLOWER, new SimpleBlockFeature(new WeightedStateProvider(mixedFlowerBedPatchBuilder(Blocks.WILDFLOWERS, BlocksSD.PERSE_WILDFLOWERS))));
        context.register(
                DARK_FOREST_VEGETATION,
                new RandomSelectorFeature(
                        List.of(
                                new WeightedPlacedFeature(PlacementUtils.inlinePlaced(hugeBrownMushroom), 0.00625F),
                                new WeightedPlacedFeature(PlacementUtils.inlinePlaced(hugeRedMushroom), 0.00625F),
                                new WeightedPlacedFeature(oakLeafLitter, 0.025F),
                                new WeightedPlacedFeature(fallenOak, 0.00625F),
                                new WeightedPlacedFeature(fancyOakLeafLitter, 0.025F)
                        ),
                        darkOakLeafLitter
                )
        );
    }

    private static WeightedList.Builder<BlockState> mixedFlowerBedPatchBuilder(final Block... flowerBedBlocks) {
        return mixedSegmentedBlockPatchBuilder(1, 4, FlowerBedBlock.AMOUNT, FlowerBedBlock.FACING, flowerBedBlocks);
    }

    private static WeightedList.Builder<BlockState> mixedSegmentedBlockPatchBuilder(final int minState, final int maxState, final IntegerProperty amountProperty, final EnumProperty<Direction> directionProperty, final Block... blocks) {
        WeightedList.Builder<BlockState> segmentedBlockBuild = WeightedList.builder();

        for (Block block : blocks) {
            for (int amount = minState; amount <= maxState; amount++) {
                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    segmentedBlockBuild.add(block.defaultBlockState().setValue(amountProperty, amount).setValue(directionProperty, direction), 1);
                }
            }
        }
        return segmentedBlockBuild;
    }
}