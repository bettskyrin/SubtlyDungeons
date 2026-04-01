package net.meander.subtlyd.world.item.alchemy;

import net.fabricmc.fabric.api.registry.FabricPotionBrewingBuilder;
import net.meander.subtlyd.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

public class PotionsSD {
    public static final Holder.Reference<Potion> DECAY = register(new Potion("decay",
            new MobEffectInstance(MobEffects.WITHER, 800, 1))
    );

    public static void registration() {
        FabricPotionBrewingBuilder.BUILD.register(builder -> {
            builder.addMix(Potions.AWKWARD, Items.WITHER_SKELETON_SKULL, DECAY);
        });
    }

    private static Holder.Reference<Potion> register(final Potion potion) {
        return Registry.registerForHolder(BuiltInRegistries.POTION, Util.identifier(potion.name()), potion);
    }
}
