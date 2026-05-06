package net.meander.subtlyd.world.item.enchantment;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;

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

    public static int getEnchantmentCost(ItemStack itemStack) {
        int value = 0;

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemStack.getEnchantments().entrySet()) {
            if (entry.getKey().unwrapKey().isPresent()) {
                Enchantment enchantment = entry.getKey().value();
                value += enchantment.getAnvilCost();
            }
        }

        return value;
    }
}
