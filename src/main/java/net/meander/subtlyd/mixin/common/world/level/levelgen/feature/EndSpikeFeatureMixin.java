package net.meander.subtlyd.mixin.common.world.level.levelgen.feature;

import net.meander.subtlyd.world.level.block.BlocksSD;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.EndSpikeFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(EndSpikeFeature.class)
public abstract class EndSpikeFeatureMixin {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Shadow @Final private Optional<BlockPos> crystalBeamTarget;
    @Shadow @Final private boolean crystalInvulnerable;

    @Inject(method = "placeSpike", at = @At("HEAD"), cancellable = true)
    private void modifySpike(ServerLevelAccessor level, RandomSource random, EndSpikeFeature.EndSpike spike, CallbackInfo ci) {
        int baseRadius = spike.getRadius();
        int topY = spike.getHeight();
        int centerX = spike.getCenterX();
        int centerZ = spike.getCenterZ();

        for (int y = topY; y >= level.getMinY(); y--) {
            float progressDown = Mth.clamp((topY - Math.max(y, 60.0F)) / (topY - 60.0F), 0.0F, 1.0F);
            float currentRadius = Mth.lerp(progressDown, 1.0F, baseRadius * 1.5F);
            int ceilRadius = Mth.ceil(currentRadius);

            for (int x = -ceilRadius; x <= ceilRadius; x++) {
                for (int z = -ceilRadius; z <= ceilRadius; z++) {
                    float distSqr = Mth.square(x) + Mth.square(z);

                    if (distSqr <= Mth.square(currentRadius)) {
                        boolean isEdge = distSqr > Mth.square(currentRadius - 1.5F);

                        if (!isEdge || random.nextFloat() <= 0.85F) {
                            BlockPos pos = new BlockPos(centerX + x, y, centerZ + z);
                            BlockState currentState = level.getBlockState(pos);

                            if (currentState.isAir() || currentState.is(Blocks.END_STONE)) {
                                level.setBlock(pos, Blocks.OBSIDIAN.defaultBlockState(), 2);
                            }
                        }
                    }
                }
            }
        }

        for (BlockPos pos : BlockPos.betweenClosed(new BlockPos(centerX - baseRadius, topY, centerZ - baseRadius), new BlockPos(centerX + baseRadius, topY + 10, centerZ + baseRadius))) {
            if (pos.distToLowCornerSqr(centerX, pos.getY(), centerZ) <= baseRadius * baseRadius + 1 && pos.getY() > topY) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            }
        }

        if (spike.isGuarded()) {
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    boolean isWall = Mth.abs(dx) == 2 || Mth.abs(dz) == 2;

                    for (int dy = 1; dy <= 4; dy++) {
                        pos.set(centerX + dx, topY + dy, centerZ + dz);

                        if (isWall) {
                            boolean isFloorOrCeiling = (dy == 1 || dy == 4);
                            boolean xEdge = dx == -2 || dx == 2 || isFloorOrCeiling;
                            boolean zEdge = dz == -2 || dz == 2 || isFloorOrCeiling;

                            BlockState state = Blocks.IRON_BARS.defaultBlockState()
                                    .setValue(IronBarsBlock.NORTH, xEdge && dz != -2)
                                    .setValue(IronBarsBlock.SOUTH, xEdge && dz != 2)
                                    .setValue(IronBarsBlock.WEST, zEdge && dx != -2)
                                    .setValue(IronBarsBlock.EAST, zEdge && dx != 2);

                            level.setBlock(pos, state, 2);
                        } else {
                            if (dy == 1 && (dx != 0 || dz != 0)) {
                                level.setBlock(pos, BlocksSD.IRON_GRATE.defaultBlockState(), 2);
                            } else if (dy == 4) {
                                level.setBlock(pos, BlocksSD.IRON_GRATE.defaultBlockState(), 2);
                            }
                        }
                    }
                }
            }
        }

        EndCrystal endCrystal = EntityTypes.END_CRYSTAL.create(level.getLevel(), EntitySpawnReason.STRUCTURE);

        if (endCrystal != null) {
            endCrystal.setBeamTarget(crystalBeamTarget.orElse(null));
            endCrystal.setInvulnerable(crystalInvulnerable);
            endCrystal.snapTo(centerX + 0.5, topY + 2, centerZ + 0.5, random.nextFloat() * 360.0F, 0.0F);
            level.addFreshEntity(endCrystal);

            BlockPos crystalPos = endCrystal.blockPosition();

            level.setBlock(crystalPos.below(), Blocks.BEDROCK.defaultBlockState(), 2);
            level.setBlock(crystalPos, FireBlock.getState(level, crystalPos), 2);
        }

        ci.cancel();
    }
}
