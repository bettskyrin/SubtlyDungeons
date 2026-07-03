package net.meander.subtlyd.tags;

import net.meander.subtlyd.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * @see net.minecraft.tags.EnchantmentTags
 */
public class EnchantmentTagsSD {
    public static TagKey<Enchantment> INCREASES_MAGIC_LIMIT = create("increases_magic_limit");
    public static TagKey<Enchantment> REPAIRS_EQUIPMENT = create("repairs_equipment");

    private static TagKey<Enchantment> create(final String name) {
        return TagKey.create(Registries.ENCHANTMENT, Util.identifier(name));
    }
}
