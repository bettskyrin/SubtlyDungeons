package net.meander.subtlyd.world.entity;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.function.Supplier;

public class EntityTypeSD {
    /**
     * As of this comment, only Wolf Variants are actual Data Components. Once they're Data Components, we can implement this better.
     * My suggestion would be to create a new Data Tag type and give each variant the proper tag
     */
    public static Map<?, Identifier> variantMap = Map.ofEntries(
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
            case Pig p -> variant = p.getVariant();
            case Cow c -> variant = c.getVariant();
            case Chicken c -> variant = c.getVariant();
            case Frog f -> variant = f.getVariant();
            case Rabbit r -> variant = r.getVariant();
            case Wolf w -> {
                Holder<WolfVariant> holder = w.get(DataComponents.WOLF_VARIANT);
                variant = holder != null ? holder.unwrapKey().orElse(null) : null;
            }
            case Fox f -> variant = f.getVariant();
            case null, default -> {
                return null;
            }
        }

        if (variantMap.containsKey(variant)) {
            return variantMap.get(variant);
        }
        return null;
    }

    public static <T extends Entity> EntityType<T> register(Item item, EntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, BuiltInRegistries.ITEM.getKey(item), builder.build(ResourceKey.create(Registries.ENTITY_TYPE, BuiltInRegistries.ITEM.getKey(item))));
    }

    public static <T extends Entity> EntityType<T> register(Identifier key, EntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(ResourceKey.create(Registries.ENTITY_TYPE, key)));
    }

    public static EntityType.EntityFactory<TentEntity> tentFactory(Supplier<Item> supplier) {
        return (entityType, level) -> new TentEntity(entityType, level, supplier);
    }
}
