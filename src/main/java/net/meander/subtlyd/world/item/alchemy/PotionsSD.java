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

public class PotionsSD { // TODO Formatting and Names
    public static final Holder.Reference<Potion> BLAND_BREW = register(new Potion("bland_brew")); // Water -> Vines

    public static final Holder.Reference<Potion> SUAVE_BREW = register(new Potion("suave_brew")); // Bland Brew -> Sweet Berry

    public static final Holder.Reference<Potion> SWEET_BREW = register(new Potion("sweet_brew", // Vines + Sweet Berries + Glow Berries
            new MobEffectInstance(MobEffects.RESISTANCE, 900)));




    public static final Holder.Reference<Potion> HARSH_BREW = register(new Potion("harsh_brew")); // Ingredient + Nothing

    public static final Holder.Reference<Potion> THIN_BREW = register(new Potion("thin_brew")); // Flower + Wrong Ingredient

    public static final Holder.Reference<Potion> ACRID_BREW = register(new Potion("acrid_brew", // Anything + Glowstone
            new MobEffectInstance(MobEffects.WEAKNESS, 900), new MobEffectInstance(MobEffects.BLINDNESS, 900)));

    public static final Holder.Reference<Potion> STINKY_BREW = register(new Potion("stinky_brew", // Flower + Right Ingredient 1 + Wrong Ingredient 2
            new MobEffectInstance(MobEffects.HUNGER, 900), new MobEffectInstance(MobEffects.SLOWNESS, 900)));

    public static final Holder.Reference<Potion> SPARKLING_BREW = register(new Potion("sparkling_brew", // Any Potion + Redstone OR Eyeblossom + Nothing
            new MobEffectInstance(MobEffects.NAUSEA, 400)));

    public static final Holder.Reference<Potion> MURKY_BREW = register(new Potion("murky_brew", // Anything + Wither Rose
            new MobEffectInstance(MobEffects.WITHER, 40)));

    public static final Holder.Reference<Potion> RANK_BREW = register(new Potion("rank_brew", // Anything + Wither Rose
            new MobEffectInstance(MobEffects.WITHER, 40)));

    public static final Holder.Reference<Potion> BITTER_BREW = register(new Potion("bitter_brew", // Oxeye Daisy + Pink Petals + Brown Mushroom
            new MobEffectInstance(MobEffects.REGENERATION, 200)));

    public static final Holder.Reference<Potion> MILKY_BREW = register(new Potion("milky_brew", // Poppy + Spider Eye + Sunflower
            new MobEffectInstance(MobEffects.NIGHT_VISION, 900), new MobEffectInstance(MobEffects.NAUSEA, 40)));

    public static final Holder.Reference<Potion> BUBBLY_BREW = register(new Potion("bubbly_brew", // Cornflower + Feather + Wildflowers
            new MobEffectInstance(MobEffects.JUMP_BOOST, 900), new MobEffectInstance(MobEffects.SLOW_FALLING, 900)));

    public static final Holder.Reference<Potion> DECAY = register(new Potion("decay",
            new MobEffectInstance(MobEffects.WITHER, 800, 1)));

    private static Holder.Reference<Potion> register(final Potion potion) {
        return Registry.registerForHolder(BuiltInRegistries.POTION, Util.identifier(potion.name()), potion);
    }

    public static void registration() {
        FabricPotionBrewingBuilder.BUILD.register(builder -> {
            builder.addMix(Potions.AWKWARD, Items.WITHER_SKELETON_SKULL, DECAY);
        });
    }
}
