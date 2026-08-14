package net.meander.subtlyd.world.effect;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * @see MobEffects
 */
public class MobEffectsSD {
    public static void init() {
        modifyAttributes();
    }

    private static void modifyAttributes() {
        UtilSD.LOGGER.debug("Modifying mob effects...");
        MobEffects.JUMP_BOOST.value().addAttributeModifier(
                Attributes.JUMP_STRENGTH,
                Identifier.withDefaultNamespace("effect.jump_boost"),
                0.05,
                AttributeModifier.Operation.ADD_VALUE
        );
    }
}
