package net.meander.subtlyd.tags;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

/**
 * @see net.minecraft.tags.DamageTypeTags
 */
public class DamageTypeTagsSD {
    public static final TagKey<DamageType> IS_OCCULT = create("is_occult");
    public static final TagKey<DamageType> CAUSES_FLOCK_PANIC = create("causes_flock_panic");

    private static TagKey<DamageType> create(String string) {
        return TagKey.create(Registries.DAMAGE_TYPE, UtilSD.identifier(string));
    }
}
