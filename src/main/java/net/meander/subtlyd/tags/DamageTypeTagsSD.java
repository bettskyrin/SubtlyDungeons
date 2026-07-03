package net.meander.subtlyd.tags;

import net.meander.subtlyd.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

/**
 * @see net.minecraft.tags.DamageTypeTags
 */
public class DamageTypeTagsSD {
    public static final TagKey<DamageType> CAN_BREAK_TENT = bind("can_break_tents");
    public static final TagKey<DamageType> ALWAYS_KILLS_TENT = bind("always_kills_tent");
    public static final TagKey<DamageType> BURNS_TENTS = bind("burns_tents");
    public static final TagKey<DamageType> IGNITES_TENTS = bind("ignites_tents");
    public static final TagKey<DamageType> IS_OCCULT = bind("is_occult");

    private static TagKey<DamageType> bind(String string) {
        return TagKey.create(Registries.DAMAGE_TYPE, Util.identifier(string));
    }
}
