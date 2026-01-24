package net.meander.subtlyd.world.item.component;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class ConsumablesSD {
    public static final Consumable RED_MUSHROOM = Consumable.builder()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.POISON, 60, 0)))
            .build();
}
