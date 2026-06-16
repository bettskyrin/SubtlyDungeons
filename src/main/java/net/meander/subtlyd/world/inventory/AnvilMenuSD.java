package net.meander.subtlyd.world.inventory;

import net.meander.subtlyd.core.component.DataComponentsSD;
import net.meander.subtlyd.data.tags.EnchantmentTagsSD;
import net.meander.subtlyd.world.item.ItemStackSD;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class AnvilMenuSD {
    public static int getCostByEnchantability(int input, int addition) {
        int difference = (Mth.abs(input - addition));

        if (difference <= 1) {
            return 40;
        }
        return 40 + Mth.ceil(difference * 2);
    }

    public static boolean isEnchanting(final ItemStack input, final ItemStack addition) {
        boolean enchantingBook = input.has(DataComponents.STORED_ENCHANTMENTS);
        boolean usingBook = addition.has(DataComponents.STORED_ENCHANTMENTS);

        return  enchantingBook || usingBook || (input.isEnchanted() && addition.isEnchantable()) || addition.isEnchanted();
    }

    /**
     * @param input The first item in the anvil.
     * @param addition The second item in the anvil.
     * @return The magic level increase of the resulting item.
     */
    public static int getMagicLevelIncrease(ItemStack input, ItemStack addition) {
        boolean hasGlyphAffinity = EnchantmentHelper.hasTag(input, EnchantmentTagsSD.INCREASES_MAGIC_LIMIT);
        int inputLevel = input.getOrDefault(DataComponentsSD.MAGIC_LEVEL, 0);
        int additionLevel = addition.getOrDefault(DataComponentsSD.MAGIC_LEVEL, 0);
        double reduction = hasGlyphAffinity ? 4.5 : 2.5;

        return Mth.ceil((inputLevel + additionLevel) / reduction);
    }

    /**
     * @param input The first item in the anvil.
     * @param addition The second item in the anvil.
     * @return The magic level limit of the resulting item.
     */
    public static int getMagicLimit(ItemStack input, ItemStack addition) {
        boolean hasGlyphAffinity = EnchantmentHelper.hasTag(input, EnchantmentTagsSD.INCREASES_MAGIC_LIMIT);
        int magicLimit = getCostByEnchantability(ItemStackSD.getEnchantability(input), ItemStackSD.getEnchantability(addition));

        return hasGlyphAffinity ? Mth.ceil(magicLimit * 1.5F) : magicLimit;
    }
}
