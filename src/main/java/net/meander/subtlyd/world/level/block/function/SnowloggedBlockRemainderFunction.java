package net.meander.subtlyd.world.level.block.function;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SnowloggedBlockRemainderFunction implements PlayerBlockBreakEvents.After {
    @Override
    public void afterBlockBreak(Level level, Player player, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        if (level instanceof ServerLevel) {
            if (blockState.hasProperty(BlockStateProperties.SNOWLOGGED_LAYERS)) {
                int layers = blockState.getValue(BlockStateProperties.SNOWLOGGED_LAYERS);

                if (layers > 0) {
                    BlockState snowState = (layers == 8) ? Blocks.SNOW_BLOCK.defaultBlockState() : Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, layers);

                    level.setBlock(blockPos, snowState, 3);
                }
            }
        }
    }
}
