package com.kr1s1s.subtlyd.mixin.client.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @SuppressWarnings("DataFlowIssue") ItemStack itemStack = (ItemStack) (Object) this;

    /**
     * Modifies the rarity level of items
     */
    @Inject(method = "getRarity", at = @At("HEAD"), cancellable = true)
    public void getRarity(CallbackInfoReturnable<Rarity> cir) {
        List<Item> uncommon = List.of(Items.NETHERITE_AXE,
                Items.NETHERITE_HOE,
                Items.NETHERITE_PICKAXE,
                Items.NETHERITE_SHOVEL,
                Items.NETHERITE_SPEAR,
                Items.NETHERITE_SWORD,
                Items.NETHERITE_HELMET,
                Items.NETHERITE_CHESTPLATE,
                Items.NETHERITE_LEGGINGS,
                Items.NETHERITE_BOOTS,
                Items.NETHERITE_HORSE_ARMOR,
                Items.NETHERITE_NAUTILUS_ARMOR,
                Items.OMINOUS_TRIAL_KEY,
                Items.LINGERING_POTION,
                Items.TIPPED_ARROW);
        List<Item> rare = List.of(Items.WITHER_ROSE);
        if (uncommon.contains(itemStack.getItem())) {
            cir.setReturnValue(Rarity.UNCOMMON);
        } else if (rare.contains(itemStack.getItem())) {
            cir.setReturnValue(Rarity.RARE);
        }
    }
}
