package net.meander.subtlyd.world.inventory;

import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

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

        return (input.isEnchanted() ^ (addition.isEnchanted() || usingBook)) && !enchantingBook && addition.isEnchantable();
    }
}
