package net.meander.subtlyd.tags;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.alchemy.Potion;

/**
 * @see net.minecraft.tags.PotionTags
 */
public class PotionTagsSD {
    public static final TagKey<Potion> CONICAL = create("conical");
    public static final TagKey<Potion> SPHERICAL = create("spherical");
    public static final TagKey<Potion> VIAL = create("vial");

    private static TagKey<Potion> create(String name) {
        return TagKey.create(Registries.POTION, UtilSD.identifier(name));
    }
}
