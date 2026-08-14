package net.meander.subtlyd.world.item.alchemy;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

/**
 * @see Potions
 */
public class PotionsSD {
    public static final Holder<Potion> DECAY = Potions.register(PotionIdsSD.DECAY, new Potion("decay", new MobEffectInstance(MobEffects.WITHER, 800, 1)));
}
