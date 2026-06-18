package net.meander.subtlyd.world.level.block.state.properties;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BlockStatePropertiesSD {
    public static final IntegerProperty SNOWLOGGED_LAYERS = IntegerProperty.create("snow_layers", 0, 8);
    public static final BooleanProperty BOTTOM_SNOWLOGGED = BooleanProperty.create("bottom_snowlogged");
}
