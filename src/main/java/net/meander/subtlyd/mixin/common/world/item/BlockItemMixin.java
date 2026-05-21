package net.meander.subtlyd.mixin.common.world.item;

import net.meander.subtlyd.world.food.FoodsSD;
import net.meander.subtlyd.world.item.component.ConsumablesSD;
import net.meander.subtlyd.world.level.block.SimpleSnowloggedBlock;
import net.meander.subtlyd.world.level.block.state.properties.BlockStatePropertiesSD;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    /**
     * Adds food components to block items.
     */
    @Inject(method = "<init>", at = @At("HEAD"))
    private static void addFoodProperties(final Block block, final Item.Properties properties, CallbackInfo ci) {
        if (block == Blocks.BROWN_MUSHROOM) {
            properties.food(FoodsSD.BROWN_MUSHROOM);
            properties.component(DataComponents.CONSUMABLE, Consumables.DEFAULT_FOOD);
        } else if (block == Blocks.RED_MUSHROOM) {
            properties.food(FoodsSD.RED_MUSHROOM);
            properties.component(DataComponents.CONSUMABLE, ConsumablesSD.RED_MUSHROOM);
        }
    }

    @Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
    private void acceptSnowLayers(BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
        BlockState placementState = cir.getReturnValue();

        if (placementState != null && SimpleSnowloggedBlock.isSnowloggable(placementState.getBlock())) {
            BlockPos pos = context.getClickedPos();
            BlockState worldState = context.getLevel().getBlockState(pos);

            if (worldState.is(Blocks.SNOW)) {
                int layers = worldState.getValue(SnowLayerBlock.LAYERS);

                cir.setReturnValue(placementState.setValue(BlockStatePropertiesSD.SNOWLOGGED_LAYERS, layers));
            }
        }
    }
}