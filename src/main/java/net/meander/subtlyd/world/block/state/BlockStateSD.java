package net.meander.subtlyd.world.block.state;

import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

public class BlockStateSD extends BlockState {
    public BlockStateSD(Block owner, Property<?>[] propertyKeys, Comparable<?>[] propertyValues) {
        super(owner, propertyKeys, propertyValues);
    }

    public static boolean canBeSnowlogged(BlockState state) {
        if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
            if (state.getValue(BlockStateProperties.WATERLOGGED)) {
                return false;
            }
        }

        if (!state.getFluidState().isEmpty()) {
            return false;
        }

        return state.hasProperty(BlockStatePropertiesSD.SNOWLOGGED_LAYERS);
    }
}
