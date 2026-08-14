package net.meander.subtlyd.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.meander.subtlyd.world.level.block.BlocksSD;
import net.meander.subtlyd.world.level.block.ReedsBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;

public record ReedsFeature(float probability) implements Feature{
    public static final MapCodec<ReedsFeature> CODEC = RecordCodecBuilder.mapCodec((i) -> i.group(Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(ReedsFeature::probability)).apply(i, ReedsFeature::new));

    public MapCodec<ReedsFeature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(final WorldGenLevel level, final ChunkGenerator chunkGenerator, final RandomSource random, final BlockPos origin) {
        boolean placedAny = false;
        int x = random.nextInt(8) - random.nextInt(8);
        int z = random.nextInt(8) - random.nextInt(8);
        int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, origin.getX() + x, origin.getZ() + z);
        BlockPos reedsPos = new BlockPos(origin.getX() + x, y, origin.getZ() + z);

        if (level.getBlockState(reedsPos).is(Blocks.WATER)) {
            BlockState state = BlocksSD.REEDS.defaultBlockState();
            if (state.canSurvive(level, reedsPos)) {
                BlockState upperState = state.setValue(ReedsBlock.HALF, DoubleBlockHalf.UPPER);
                BlockPos blockPos3 = reedsPos.above();

                if (level.getBlockState(blockPos3).is(Blocks.AIR)) {
                    level.setBlock(reedsPos, state, 2);
                    level.setBlock(blockPos3, upperState, 2);
                }

                placedAny = true;
            }
        }
        return placedAny;
    }
}
