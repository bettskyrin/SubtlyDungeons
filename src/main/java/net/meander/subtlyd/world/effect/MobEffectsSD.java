package net.meander.subtlyd.world.effect;

import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class MobEffectsSD {
    public static void bootstrap() {
        MobEffects.JUMP_BOOST.value().addAttributeModifier(
                Attributes.JUMP_STRENGTH,
                Identifier.withDefaultNamespace("effect.jump_boost"),
                0.05,
                AttributeModifier.Operation.ADD_VALUE
        );
    }
}
