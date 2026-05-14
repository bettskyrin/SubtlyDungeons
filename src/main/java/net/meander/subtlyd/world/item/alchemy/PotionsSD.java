package net.meander.subtlyd.world.item.alchemy;

import net.fabricmc.fabric.api.registry.FabricPotionBrewingBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

public class PotionsSD {
    public static final Holder<Potion> DECAY = register(PotionIdsSD.DECAY,
            new Potion("decay", new MobEffectInstance(MobEffects.WITHER, 800, 1))
    );

    public static void registration() {
        FabricPotionBrewingBuilder.BUILD.register(builder -> {
            builder.addMix(Potions.AWKWARD, Items.WITHER_SKELETON_SKULL, DECAY);
        });
        MobEffects.JUMP_BOOST.value().addAttributeModifier(
                Attributes.JUMP_STRENGTH,
                Identifier.withDefaultNamespace("effect.jump_boost"),
                0.05,
                AttributeModifier.Operation.ADD_VALUE
        );
    }

    private static Holder<Potion> register(final ResourceKey<Potion> key, final Potion potion) {
        return Registry.registerForHolder(BuiltInRegistries.POTION, key, potion);
    }
}
