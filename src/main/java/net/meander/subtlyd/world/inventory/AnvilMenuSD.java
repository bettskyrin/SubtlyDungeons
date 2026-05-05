package net.meander.subtlyd.world.inventory;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.meander.subtlyd.core.component.DataComponentsSD;
import net.meander.subtlyd.data.tags.EnchantmentTagsSD;
import net.meander.subtlyd.world.item.ItemStackSD;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.List;

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
     * @param input The first slot's item. This is the item being "repaired."
     * @param addition The second slot's item.
     * @param enchantment The enchantment to search for.
     * @return A boolean relating to whether an enchantment is being used.
     */
    public static boolean checkEnchantment(ItemStack input, ItemStack addition, ResourceKey<Enchantment> enchantment) {
        List<ItemStack> inputs = List.of(input, addition);

        for (ItemStack inputStack : inputs) {
            for (Object2IntMap.Entry<Holder<Enchantment>> entry : inputStack.getEnchantments().entrySet()) {
                if (entry.getKey().unwrapKey().isPresent()) {
                    if (entry.getKey().unwrapKey().get() == enchantment) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean checkEnchantment(ItemStack input, ResourceKey<Enchantment> enchantment) {
        return checkEnchantment(input, ItemStack.EMPTY, enchantment);
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
