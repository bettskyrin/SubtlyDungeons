package net.meander.subtlyd.world.level.block.function;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class CropExperienceFunction implements PlayerBlockBreakEvents.After {
    @Override
    public void afterBlockBreak(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity) {
        if (level instanceof ServerLevel serverLevel) {
            if (serverLevel.getGameRules().get(GameRules.BLOCK_DROPS)) {
                if (blockState.is(BlockTags.CROPS)) {
                    CropBlock crop = (CropBlock) blockState.getBlock();

                    if (crop.isMaxAge(blockState)) {
                        ExperienceOrb.award(serverLevel, Vec3.atCenterOf(blockPos), UniformInt.of(0, 2).sample(level.getRandom()));
                    }
                }
            }
        }
    }
}
