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

		List<Direction> branchDirections = Lists.newArrayList(Direction.Plane.HORIZONTAL);
		List<Direction> lowBranchDirections = Lists.newArrayList(branchDirections);

		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
		for (int dx = -1; dx <= 2; dx++) {
			for (int dz = -1; dz <= 2; dz++) {
				mutablePos.setWithOffset(below, dx, 0, dz);

				for (int drop = 0; drop < 5; drop++) {
					if (placeLog(level, trunkSetter, random, mutablePos, tree)) {
						mutablePos.move(Direction.DOWN);
					} else {
						break;
					}
				}
			}
		}

		for (int dx = -2; dx <= 3; dx++) {
			for (int dz = -2; dz <= 3; dz++) {
				if (dx == -2 || dx == 3 || dz == -2 || dz == 3) {
					if (random.nextFloat() < 0.25F) {
						int rootHeight = random.nextInt(1) + 1;

						for (int ry = rootHeight; ry >= -3; ry--) {
							mutablePos.set(x + dx, y + ry, z + dz);

							boolean wasPlaced = placeLog(level, trunkSetter, random, mutablePos, tree);

							if (ry < 0 && !wasPlaced) {
								break;
							}
						}
					}
				}
			}
		}

		for (int dy = 0; dy < treeHeight; dy++) {
			for (int dx = -1; dx <= 2; dx++) {
				for (int dz = -1; dz <= 2; dz++) {
					mutablePos.set(x + dx, y + dy, z + dz);
					placeLog(level, trunkSetter, random, mutablePos, tree);
				}
			}
		}

		for (Direction direction : branchDirections) {
			int offset = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 2 : -1;
			Direction.Axis branchAxis = direction.getAxis();

			int highBranchX = branchAxis == Direction.Axis.X ? x + offset : getBranchXZOffset(random, x);
			int highBranchY = y + treeHeight - 1;
			int highBranchZ = branchAxis == Direction.Axis.Z ? z + offset : getBranchXZOffset(random, z);

			for (int branches = 1; random.nextFloat() < 0.4F && branches < 2; branches++) {
				int bonusBranchX = branchAxis == Direction.Axis.X ? x + offset : getBranchXZOffset(random, x);
				int bonusBranchZ = branchAxis == Direction.Axis.Z ? z + offset : getBranchXZOffset(random, z);

				if (bonusBranchX != highBranchX && bonusBranchZ != highBranchZ) {
					generateBranch(level, trunkSetter, random, new BlockPos(bonusBranchX, highBranchY, bonusBranchZ), direction, tree, attachments);
				} else {
					branches--;
				}
			}

			generateBranch(level, trunkSetter, random, new BlockPos(highBranchX, highBranchY, highBranchZ), direction, tree, attachments);
		}

		while (!lowBranchDirections.isEmpty()) {
			Direction direction = lowBranchDirections.removeFirst();

			if (random.nextFloat() < 0.4F) {
				int offset = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 2 : -1;
				int lowBranchY = y + random.nextInt(3) + 3;
				int lowBranchX = direction.getAxis() == Direction.Axis.X ? x + offset : getBranchXZOffset(random, x);
				int lowBranchZ = direction.getAxis() == Direction.Axis.Z ? z + offset : getBranchXZOffset(random, z);

				generateBranch(level, trunkSetter, random, new BlockPos(lowBranchX, lowBranchY, lowBranchZ), direction, tree, attachments, random.nextInt(4) + 2);
			}
		}

		return attachments;
	}

	private int getBranchXZOffset(final RandomSource random, final int origin) {
		int branchCoordinate;

		if (random.nextBoolean()) {
			branchCoordinate = origin + random.nextInt(2);
		} else {
			branchCoordinate = origin - random.nextInt(2);
		}

		return branchCoordinate;
	}

	private void generateBranch(final WorldGenLevel level, final BiConsumer<BlockPos, BlockState> trunkSetter, final RandomSource random, final BlockPos pos, final Direction direction, final TreeFeature tree, List<FoliagePlacer.FoliageAttachment> attachments) {
		generateBranch(level, trunkSetter, random, pos, direction, tree, attachments, random.nextInt(3) + 4);
	}

	private void generateBranch(final WorldGenLevel level, final BiConsumer<BlockPos, BlockState> trunkSetter, final RandomSource random, final BlockPos pos, final Direction direction, final TreeFeature tree, List<FoliagePlacer.FoliageAttachment> attachments, final int length) {
		int height = random.nextInt(2) + 1;
		BlockPos.MutableBlockPos mutableBlockPos = pos.mutable();

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
}