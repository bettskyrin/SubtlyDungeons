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

		for (int dx = -1; dx <= 2; dx++) {
			for (int dz = -1; dz <= 2; dz++) {
				BlockPos.MutableBlockPos basePos = below.offset(dx, 0, dz).mutable();

				for (int drop = 0; drop < 5; drop++) {
					boolean placed = placeLog(level, trunkSetter, random, basePos, tree);
					if (placed) {
						basePos.move(Direction.DOWN);
					} else {
						break;
					}
				}
			}
		}

		for (int dy = 0; dy < treeHeight; dy++) {
			boolean isTop = (dy == treeHeight - 1);
			for (int dx = -1; dx <= 2; dx++) {
				for (int dz = -1; dz <= 2; dz++) {
					if (isTop && (dx == -1 || dx == 2) && (dz == -1 || dz == 2)) { // Skip corners
						continue;
					}

					BlockPos pos = new BlockPos(x + dx, y + dy, z + dz);

					placeLog(level, trunkSetter, random, pos, tree);
				}
			}
		}

		int topBranchY = y + treeHeight - 1;
		int targetBranches = random.nextInt(3) + 2;
		List<Direction> availableDirs = new ArrayList<>(Direction.Plane.HORIZONTAL.stream().toList());

		for (int i = 0; i < targetBranches; i++) {
			int index = random.nextInt(availableDirs.size());
			Direction dir = availableDirs.remove(index);

			generateBranch(level, trunkSetter, random, new BlockPos(x, topBranchY, z), dir, tree, attachments);
		}

		if (random.nextFloat() < 0.1F) {
			int lowBranchY = y + 4;
			Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
			generateBranch(level, trunkSetter, random, new BlockPos(x, lowBranchY, z), dir, tree, attachments);
		}

		return attachments;
	}

	private void generateBranch(WorldGenLevel level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, BlockPos startPos, Direction dir, TreeFeature tree, List<FoliagePlacer.FoliageAttachment> attachments) {
		int length = random.nextInt(3) + 3;
		int height = random.nextInt(3) + 1;
		BlockPos.MutableBlockPos currentPos = startPos.mutable();

		int offset = (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE) ? 2 : 1;
		currentPos.move(dir, offset);

		Function<BlockState, BlockState> axisSetter = state -> state.hasProperty(BlockStateProperties.AXIS) ? state.setValue(BlockStateProperties.AXIS, dir.getAxis()) : state;

		for (int i = 0; i < height; i++) {
			currentPos.move(Direction.UP);
			if (TreeFeature.isAirOrLeaves(level, currentPos)) {
				placeLog(level, trunkSetter, random, currentPos, tree);
			}
		}

		for (int i = 0; i < length; i++) {
			currentPos.move(dir);
			if (TreeFeature.isAirOrLeaves(level, currentPos)) {
				placeLog(level, trunkSetter, random, currentPos, tree, axisSetter);
			}
		}

		attachments.add(new FoliagePlacer.FoliageAttachment(currentPos.above(), 0, false));
	}
}