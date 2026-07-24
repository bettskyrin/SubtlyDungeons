package net.meander.subtlyd.world.item.enchantment;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;

/**
 * @see net.minecraft.world.item.enchantment.EnchantmentHelper
 */
public class EnchantmentHelperSD {
    /**
     * @param input The first slot's item. This is the item being "repaired."
     * @param addition The second slot's item.
     * @param enchantment The enchantment to search for.
     * @return A boolean relating to whether an enchantment is being used.
     */
    public static boolean checkEnchantment(ItemStack input, ItemStack addition, ResourceKey<Enchantment> enchantment) {
        List<ItemStack> inputs = List.of(input, addition);

        for (ItemStack inputStack : inputs) {
            for (Object2IntMap.Entry<Holder<Enchantment>> enchantmentEntry : inputStack.getEnchantments().entrySet()) {
                if (enchantmentEntry.getKey().unwrapKey().isPresent()) {
                    if (enchantmentEntry.getKey().unwrapKey().get() == enchantment) {
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

    public static int getEnchantmentCost(ItemStack itemStack) {
        int cost = 0;

        for (Object2IntMap.Entry<Holder<Enchantment>> enchantmentEntry : itemStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, itemStack.getEnchantments()).entrySet()) {
            if (enchantmentEntry.getKey().unwrapKey().isPresent()) {
                Enchantment enchantment = enchantmentEntry.getKey().value();
                cost += enchantment.getAnvilCost();
            }
        }

        return cost;
    }
}
