package net.meander.subtlyd.data.worldgen.features;

import net.meander.subtlyd.world.block.BlocksSD;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;

/**
 * @see net.minecraft.data.worldgen.features.VegetationFeatures
 */
public class VegetationFeaturesSD {
    public static final ResourceKey<Feature> PERSE_WILDFLOWER = FeatureUtilsSD.createKey("perse_wildflower");
    public static final ResourceKey<Feature> WILDFLOWER = FeatureUtilsSD.createKey("wildflower");

    public static void bootstrap(BootstrapContext<Feature> context) {
        context.register(PERSE_WILDFLOWER, new SimpleBlockFeature(new WeightedStateProvider(VegetationFeatures.flowerBedPatchBuilder(BlocksSD.PERSE_WILDFLOWERS))));
        context.register(WILDFLOWER, new SimpleBlockFeature(new WeightedStateProvider(mixedFlowerBedPatchBuilder(Blocks.WILDFLOWERS, BlocksSD.PERSE_WILDFLOWERS))));
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