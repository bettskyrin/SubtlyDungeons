package net.meander.subtlyd.world.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Map;

public class ItemStackSD {
    public static Map<Item, Integer> enchantabilityMap = Map.ofEntries(
            Map.entry(Items.ENCHANTED_BOOK, 1),
            Map.entry(Items.ELYTRA, 9),
            Map.entry(Items.WOLF_ARMOR, 9),
            Map.entry(Items.DIAMOND_HORSE_ARMOR, 10),
            Map.entry(Items.DIAMOND_NAUTILUS_ARMOR, 10),
            Map.entry(Items.COPPER_HORSE_ARMOR, 8),
            Map.entry(Items.COPPER_NAUTILUS_ARMOR, 8),
            Map.entry(Items.IRON_HORSE_ARMOR, 9),
            Map.entry(Items.IRON_NAUTILUS_ARMOR, 9),
            Map.entry(Items.LEATHER_HORSE_ARMOR, 15),
            Map.entry(Items.NETHERITE_HORSE_ARMOR, 15),
            Map.entry(Items.NETHERITE_NAUTILUS_ARMOR, 15),
            Map.entry(Items.GOLDEN_HORSE_ARMOR, 25),
            Map.entry(Items.GOLDEN_NAUTILUS_ARMOR, 25)
    );

    public static int getEnchantabilityFromMap(Item item) {
        if (enchantabilityMap.containsKey(item)) {
            return enchantabilityMap.get(item);
        }
        return 1;
    }

    @SuppressWarnings("DataFlowIssue")
    public static int getEnchantability(ItemStack itemStack) {
        if (itemStack.getComponents().get(DataComponents.ENCHANTABLE) != null) {
            return itemStack.getComponents().get(DataComponents.ENCHANTABLE).value();
        } else if (enchantabilityMap.containsKey(itemStack.getItem())) {
            return getEnchantabilityFromMap(itemStack.getItem());
        }
        return 0;
    }
}
