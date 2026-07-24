package net.meander.subtlyd.world.level.block.state.properties;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

/**
 * @see net.minecraft.world.level.block.state.properties.BlockStateProperties
 */
@SuppressWarnings("unused")
public interface BlockStatePropertiesSD {
    IntegerProperty SNOWLOGGED_LAYERS = IntegerProperty.create("snow_layers", 0, 8);
    BooleanProperty BOTTOM_SNOWLOGGED = BooleanProperty.create("bottom_snowlogged");
    IntegerProperty POTION_LEVEL = IntegerProperty.create("level", 1, 6);
    BooleanProperty IS_HEAVY_STEW = BooleanProperty.create("is_heavy_stew");
}
