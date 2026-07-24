package net.meander.subtlyd.world.entity.ai.attributes;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

/**
 * @see net.minecraft.world.entity.ai.attributes.Attributes
 */
public class AttributesSD {
    public static final Holder<Attribute> SHIELD_STRENGTH = register("shield_strength", new RangedAttribute("attribute.name.generic.shield_strength", 5.0, 0.0, 1024.0).setSyncable(true));

    public static void registration() {}

    private static Holder<Attribute> register(final String id, final Attribute attribute) {
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, UtilSD.identifier(id), attribute);
    }
}