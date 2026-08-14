package net.meander.subtlyd.tags;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * @see ItemTags
 */
public class ItemTagsSD {
    public static final TagKey<Item> TENTS = bind("tents");
    public static final TagKey<Item> LIQUID_CONSUMABLES = bind("liquid_consumables");
    public static final TagKey<Item> NON_HUMANOID_ARMOR = bind("non_humanoid_armor");
    public static final TagKey<Item> HAS_MAGIC_LIMIT = bind("has_magic_limit");
    public static final TagKey<Item> STEW_INGREDIENT = bind("stew_ingredient");
    public static final TagKey<Item> DAGGERS = bind("daggers");
    public static final TagKey<Item> CAN_PARRY_SWORDS = bind("can_parry_swords");
    public static final TagKey<Item> CAN_PARRY_DAGGERS = bind("can_parry_daggers");
    public static final TagKey<Item> SWEEPING_WEAPON = bind("sweeping_weapon");
    public static final TagKey<Item> QUIVERS = bind("quivers");
    public static final TagKey<Item> SHIELDS = bind("shields");

    private static TagKey<Item> bind(String string) {
        return TagKey.create(Registries.ITEM, UtilSD.identifier(string));
    }

    /**
     * Can be used to get a list of items by their item tag. Cannot be used within data generator classes.
     * @param tag The specified tag to search.
     * @return A list of items with the specified items tag.
     */
    public static List<Item> getItems(TagKey<Item> tag) {
        Iterable<Holder<Item>> holders = BuiltInRegistries.ITEM.getTagOrEmpty(tag);
        List<Item> items = new ArrayList<>(List.of());

        for  (Holder<Item> holder : holders) {
            items.add(holder.value());
        }
        return items;
    }
}
