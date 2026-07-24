package net.meander.subtlyd.world.inventory;

import net.meander.subtlyd.core.component.DataComponentsSD;
import net.meander.subtlyd.tags.EnchantmentTagsSD;
import net.meander.subtlyd.world.item.enchantment.EnchantmentHelperSD;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
        boolean isEnchantingBook = input.has(DataComponents.STORED_ENCHANTMENTS);
        boolean isUsingBook = addition.has(DataComponents.STORED_ENCHANTMENTS);

        return isEnchantingBook || isUsingBook || (input.isEnchanted() && addition.isEnchantable()) || addition.isEnchanted(); // Is the player attempting to enchant an item
    }

    /**
     * @param input The first item in the anvil.
     * @param addition The second item in the anvil.
     * @return The magic level increase of the resulting item.
     */
    public static int getMagicLevelIncrease(ItemStack input, ItemStack addition) {
        boolean hasGlyphAffinity = EnchantmentHelper.hasTag(input, EnchantmentTagsSD.INCREASES_MAGIC_LIMIT);
        double reduction = hasGlyphAffinity ? 4.5 : 2.5;
        int inputLevel = input.getOrDefault(DataComponentsSD.MAGIC_LEVEL, 0);
        int additionLevel = addition.getOrDefault(DataComponentsSD.MAGIC_LEVEL, 0);

        if (input.is(Items.ENCHANTED_BOOK)) {
            inputLevel = EnchantmentHelperSD.getEnchantmentCost(input);
        }

        if (addition.is(Items.ENCHANTED_BOOK)) {
            additionLevel = EnchantmentHelperSD.getEnchantmentCost(addition);
        }

        return Mth.ceil((inputLevel + additionLevel) / reduction);
    }

    /**
     * @param input The first item in the anvil.
     * @param addition The second item in the anvil.
     * @return The magic level limit of the resulting item.
     */
    public static int getMagicLimit(ItemStack input, ItemStack addition) {
        boolean hasGlyphAffinity = EnchantmentHelper.hasTag(input, EnchantmentTagsSD.INCREASES_MAGIC_LIMIT);
        int magicLimit = getCostByEnchantability(input.getEnchantability(), addition.getEnchantability());

        return hasGlyphAffinity ? Mth.ceil(magicLimit * 1.5F) : magicLimit;
    }
}
