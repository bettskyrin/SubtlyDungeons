package net.meander.subtlyd.world.block;

import net.meander.subtlyd.core.cauldron.CauldronInteractionsSD;
import net.meander.subtlyd.world.block.entity.StewCauldronBlockEntity;
import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class StewCauldronBlock extends AbstractCauldronBlock implements EntityBlock {
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_CAULDRON;
    public static final BooleanProperty IS_HEAVY_STEW = BlockStatePropertiesSD.IS_HEAVY_STEW;

    public StewCauldronBlock(Properties properties) {
        super(properties, CauldronInteractionsSD.INCOMPLETE_STEW);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 3).setValue(IS_HEAVY_STEW, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL, IS_HEAVY_STEW);
    }

    @Override
    public boolean isFull(BlockState state) {
        return state.getValue(LEVEL) == 3;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new StewCauldronBlockEntity(worldPosition, blockState);
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(final LevelReader level, final BlockPos pos, final BlockState state, final boolean includeData) {
        return new ItemStack(Items.CAULDRON);
    }
}