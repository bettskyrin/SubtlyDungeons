package net.meander.subtlyd.world.level.levelgen.feature.trunkplacers;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BaobabTrunkPlacer extends TrunkPlacer {
	public static final MapCodec<BaobabTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(i -> trunkPlacerParts(i).apply(i, BaobabTrunkPlacer::new));

	public BaobabTrunkPlacer(final int baseHeight, final int heightRandA, final int heightRandB) {
		super(baseHeight, heightRandA, heightRandB);
	}

	@Override
	protected TrunkPlacerType<?> type() {
		return TrunkPlacerTypeSD.BAOBAB_TRUNK_PLACER;
	}

	@Override
	public List<FoliagePlacer.FoliageAttachment> placeTrunk(final WorldGenLevel level, final BiConsumer<BlockPos, BlockState> trunkSetter, final RandomSource random, final int treeHeight, final BlockPos origin, final TreeFeature tree) {
		List<FoliagePlacer.FoliageAttachment> attachments = Lists.newArrayList();

		BlockPos below = origin.below();
		int x = origin.getX();
		int y = origin.getY();
		int z = origin.getZ();

		List<Direction> directionsToGenerateBranches = new ArrayList<>(Direction.Plane.HORIZONTAL.stream().toList());
		BlockPos highBranchPos = new BlockPos(x, y + treeHeight - 1, z);

		for (int dx = -1; dx <= 2; dx++) {
			for (int dz = -1; dz <= 2; dz++) {
				BlockPos.MutableBlockPos basePos = below.offset(dx, 0, dz).mutable();

				for (int drop = 0; drop < 5; drop++) {
					boolean wasPlaced = placeLog(level, trunkSetter, random, basePos, tree);

					if (wasPlaced) {
						basePos.move(Direction.DOWN);
					} else {
						break;
					}
				}
			}
		}

		for (int dy = 0; dy < treeHeight; dy++) {
			boolean isTopOfTrunk = (dy == (treeHeight - 1));

			for (int dx = -1; dx <= 2; dx++) {
				for (int dz = -1; dz <= 2; dz++) {
                    if (!isTopOfTrunk || (dx != -1 && dx != 2) || (dz != -1 && dz != 2)) { // Skip corners
                        BlockPos pos = new BlockPos(x + dx, y + dy, z + dz);

                        placeLog(level, trunkSetter, random, pos, tree);
                    }
                }
			}
		}

		while (!directionsToGenerateBranches.isEmpty()) {
			int index = random.nextInt(directionsToGenerateBranches.size());
			Direction direction = directionsToGenerateBranches.remove(index);

			if (random.nextBoolean()) {
				generateDiagonalBranch(level, trunkSetter, random, highBranchPos, direction, tree, attachments);
			} else {
				generateHorizontalBranch(level, trunkSetter, random, highBranchPos, direction, tree, attachments);
			}
		}

		if (random.nextFloat() < 0.2F) {
			int lowBranchY = y + 4;
			Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
			BlockPos lowBranchPos = new BlockPos(x, lowBranchY, z);

			if (random.nextFloat() < 0.3F) {
				generateDiagonalBranch(level, trunkSetter, random, lowBranchPos, direction, tree, attachments);
			} else {
				generateHorizontalBranch(level, trunkSetter, random, lowBranchPos, direction, tree, attachments);
			}
		}

		return attachments;
	}

	private void generateHorizontalBranch(final WorldGenLevel level, final BiConsumer<BlockPos, BlockState> trunkSetter, final RandomSource random, final BlockPos pos, final Direction direction, final TreeFeature tree, List<FoliagePlacer.FoliageAttachment> attachments) {
		int length = random.nextInt(3) + 4;
		int height = random.nextInt(2) + 1;
		BlockPos.MutableBlockPos mutableBlockPos = pos.mutable();
		int offset = (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) ? 2 : 1;

		mutableBlockPos.move(direction, offset);

		Function<BlockState, BlockState> axisSetter = state -> state.hasProperty(BlockStateProperties.AXIS) ? state.setValue(BlockStateProperties.AXIS, direction.getAxis()) : state;

		for (int i = 0; i < length; i++) {
			mutableBlockPos.move(direction);

			if (TreeFeature.isAirOrLeaves(level, mutableBlockPos)) {
				placeLog(level, trunkSetter, random, mutableBlockPos, tree, axisSetter);
			}
		}

		for (int i = 0; i < height; i++) {
			mutableBlockPos.move(Direction.UP);

			if (TreeFeature.isAirOrLeaves(level, mutableBlockPos)) {
				placeLog(level, trunkSetter, random, mutableBlockPos, tree);
			}
		}

		attachments.add(new FoliagePlacer.FoliageAttachment(mutableBlockPos.above(2), 0, false));
	}

	/**
	 * @see	net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer
	 */
	private void generateDiagonalBranch(final WorldGenLevel level, final BiConsumer<BlockPos, BlockState> trunkSetter, final RandomSource random, final BlockPos pos, final Direction direction, final TreeFeature tree, List<FoliagePlacer.FoliageAttachment> attachments) {
		int steps = random.nextInt(2) + 2;
		int offset = (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) ? 2 : 1;
		BlockPos.MutableBlockPos currentPos = pos.mutable();

		currentPos.move(direction, offset);

		for (int i = 0; i < steps; i++) {
			currentPos.move(Direction.UP).move(direction);

			if (TreeFeature.isAirOrLeaves(level, currentPos)) {
				placeLog(level, trunkSetter, random, currentPos, tree);
			}
		}

		attachments.add(new FoliagePlacer.FoliageAttachment(currentPos.above(2), 0, false));
	}
}