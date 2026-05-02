package net.meander.subtlyd.world.inventory;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

public class AnvilMenuSD {
    public static int getCostByEnchantibility(int input, int addition) {
        int difference = Math.abs(input - addition);

        if (difference <= 1) {
            return 40;
        }
        return 40 + Mth.ceil(difference * 2.4);
    }

    public static boolean isEnchanting(final ItemStack input, final ItemStack addition) {
        boolean enchantingBook = input.has(DataComponents.STORED_ENCHANTMENTS);
        boolean usingBook = addition.has(DataComponents.STORED_ENCHANTMENTS);

        return  enchantingBook || usingBook || (input.isEnchanted() && addition.isEnchantable()) || addition.isEnchanted();
    }

    /**
     * @param inputs A list of anvil inputs.
     * @return A boolean relating to whether Mending is being used.
     */
    public static boolean checkMending(ItemStack input,  ItemStack addition) {
        List<ItemStack> inputs = List.of(input, addition);

        for (ItemStack inputStack : inputs) {
            for (Object2IntMap.Entry<Holder<Enchantment>> entry : inputStack.getEnchantments().entrySet()) {
                if (entry.getKey().unwrapKey().isPresent()) {
                    if (entry.getKey().unwrapKey().get() == Enchantments.MENDING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
