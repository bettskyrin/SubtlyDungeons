package net.meander.subtlyd.world.item.component;

import net.meander.subtlyd.world.item.consume_effects.RemoveNegativeStatusEffectsConsumeEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

/**
 * @see Consumables
 */
public class ConsumablesSD {
    public static final Consumable RED_MUSHROOM = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.POISON, 60, 0))).build();
    public static final Consumable COVEN_ELIXIR = Consumables.defaultDrink().onConsume(RemoveNegativeStatusEffectsConsumeEffect.INSTANCE).build();
}
