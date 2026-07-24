package net.meander.subtlyd.world.entity;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.TemperatureVariants;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.chicken.ChickenVariants;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.cow.CowVariants;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.FrogVariants;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.pig.PigVariants;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.animal.wolf.WolfVariants;

import java.util.Map;

/**
 * @see EntityType
 */
public class EntityTypeSD {
    /**
     * Would be better if Data Driven, though currently Rabbit Variants are not yet ResourceKeys like the rest of the variants.
     */
    public static Map<?, Identifier> tempVariantMap = Map.ofEntries(
            Map.entry((Object) PigVariants.WARM, TemperatureVariants.WARM),
            Map.entry((Object) CowVariants.WARM, TemperatureVariants.WARM),
            Map.entry((Object) ChickenVariants.WARM, TemperatureVariants.WARM),
            Map.entry((Object) FrogVariants.WARM, TemperatureVariants.WARM),
            Map.entry((Object) Rabbit.Variant.GOLD, TemperatureVariants.WARM),
            Map.entry((Object) WolfVariants.STRIPED, TemperatureVariants.WARM),
            Map.entry((Object) WolfVariants.SPOTTED, TemperatureVariants.WARM),
            Map.entry((Object) WolfVariants.RUSTY, TemperatureVariants.WARM),

            Map.entry((Object) PigVariants.TEMPERATE, TemperatureVariants.TEMPERATE),
            Map.entry((Object) CowVariants.TEMPERATE, TemperatureVariants.TEMPERATE),
            Map.entry((Object) ChickenVariants.TEMPERATE, TemperatureVariants.TEMPERATE),
            Map.entry((Object) FrogVariants.TEMPERATE, TemperatureVariants.TEMPERATE),
            Map.entry((Object) Rabbit.Variant.BROWN, TemperatureVariants.TEMPERATE),
            Map.entry((Object) Rabbit.Variant.SALT, TemperatureVariants.TEMPERATE),
            Map.entry((Object) Rabbit.Variant.BLACK, TemperatureVariants.TEMPERATE),
            Map.entry((Object) WolfVariants.WOODS, TemperatureVariants.TEMPERATE),

            Map.entry((Object) PigVariants.COLD, TemperatureVariants.COLD),
            Map.entry((Object) CowVariants.COLD, TemperatureVariants.COLD),
            Map.entry((Object) ChickenVariants.COLD, TemperatureVariants.COLD),
            Map.entry((Object) FrogVariants.COLD, TemperatureVariants.COLD),
            Map.entry((Object) Rabbit.Variant.WHITE_SPLOTCHED, TemperatureVariants.COLD),
            Map.entry((Object) Rabbit.Variant.WHITE, TemperatureVariants.COLD),
            Map.entry((Object) WolfVariants.BLACK, TemperatureVariants.COLD),
            Map.entry((Object) WolfVariants.CHESTNUT, TemperatureVariants.COLD),
            Map.entry((Object) WolfVariants.PALE, TemperatureVariants.COLD),
            Map.entry((Object) WolfVariants.ASHEN, TemperatureVariants.COLD),
            Map.entry((Object) WolfVariants.SNOWY, TemperatureVariants.COLD),
            Map.entry((Object) Fox.Variant.SNOW, TemperatureVariants.COLD),
            Map.entry((Object) Fox.Variant.RED, TemperatureVariants.COLD)
    );

    /**
     * Determines whether a mob variant is considered "warm", "temperate", or "cold"
     * @param mob The mob to test
     * @return What temperature variant a mob is.
     */
    public static Identifier getTemperatureVariantType(Mob mob) {
        Object variant;

        switch (mob) {
            case Pig pig -> variant = pig.getVariant();
            case Cow cow -> variant = cow.getVariant();
            case Chicken chicken -> variant = chicken.getVariant();
            case Frog frog -> variant = frog.getVariant();
            case Rabbit rabbit -> variant = rabbit.getVariant();
            case Wolf wolf -> {
                Holder<WolfVariant> holder = wolf.get(DataComponents.WOLF_VARIANT);
                variant = holder != null ? holder.unwrapKey().orElse(null) : null;
            }
            case Fox fox -> variant = fox.getVariant();
            case null, default -> {
                return null;
            }
        }

        if (tempVariantMap.containsKey(variant)) {
            return tempVariantMap.get(variant);
        }

        return null;
    }
}
