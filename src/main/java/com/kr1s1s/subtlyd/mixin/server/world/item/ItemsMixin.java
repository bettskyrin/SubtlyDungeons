package com.kr1s1s.subtlyd.mixin.server.world.item;

import com.kr1s1s.subtlyd.world.food.FoodsSD;
import com.kr1s1s.subtlyd.world.item.component.ConsumablesSD;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumables;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Items.class)
public class ItemsMixin {
    @Shadow @Final public static Item BROWN_MUSHROOM;
    @Shadow @Final public static Item RED_MUSHROOM;

    /**
     * Modifies item components.
     */
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyItemComponents(CallbackInfo ci) {
        DataComponentMap brownMushroom = DataComponentMap.builder()
                .addAll(BROWN_MUSHROOM.components())
                .set(DataComponents.FOOD, FoodsSD.BROWN_MUSHROOM)
                .set(DataComponents.CONSUMABLE, Consumables.DEFAULT_FOOD)
                .build();
        ((ItemAccessor) BROWN_MUSHROOM).setComponents(brownMushroom);

        DataComponentMap redMushroom = DataComponentMap.builder()
                .addAll(RED_MUSHROOM.components())
                .set(DataComponents.FOOD, FoodsSD.RED_MUSHROOM)
                .set(DataComponents.CONSUMABLE, ConsumablesSD.RED_MUSHROOM)
                .build();
        ((ItemAccessor) RED_MUSHROOM).setComponents(redMushroom);
    }
}