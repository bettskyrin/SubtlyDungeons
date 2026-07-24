package net.meander.subtlyd.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class StewCauldronBlockEntity extends BlockEntity {
    private final NonNullList<ItemStack> ingredients = NonNullList.withSize(6, ItemStack.EMPTY);

    public StewCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesSD.STEW_CAULDRON, pos, state);
    }

    public boolean addIngredient(ItemStack stack) {
        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).isEmpty()) {
                ingredients.set(i, stack.copyWithCount(1));
                setChanged();
                return true;
            }
        }

        return false;
    }

    public NonNullList<ItemStack> getIngredients() {
        return ingredients;
    }
}