package net.meander.subtlyd.world.entity;

import net.meander.subtlyd.references.EntityTypeIdsSD;
import net.meander.subtlyd.world.item.ItemsSD;
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
import net.minecraft.world.entity.MobCategory;
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
import net.minecraft.world.level.block.ColorCollection;

import java.util.function.Supplier;

public class EntityTypeSD {
    public static final ColorCollection<EntityType<TentEntity>> TENT = ColorCollection.zipMap(ColorCollection.VALUES,
            EntityTypeIdsSD.TENT,
            (color, key) -> Registry.register(
                    BuiltInRegistries.ENTITY_TYPE,
                    key,
                    EntityType.Builder.of(EntityTypeSD.tentFactory(() -> ItemsSD.TENT.pick(color)), MobCategory.MISC)
                        .sized(3.5F, 1.8F)
                        .noLootTable()
                        .clientTrackingRange(10)
                        .build(key))
    );
    public static final EntityType<BlastFungusEntity> BLAST_FUNGUS = register(ItemsSD.BLAST_FUNGUS, EntityType.Builder.<BlastFungusEntity>of(BlastFungusEntity::new, MobCategory.MISC).noLootTable().sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));

    public static <T extends Entity> EntityType<T> register(Item item, EntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, BuiltInRegistries.ITEM.getKey(item), builder.build(ResourceKey.create(Registries.ENTITY_TYPE, BuiltInRegistries.ITEM.getKey(item))));
    }

    public static <T extends Entity> EntityType<T> register(Identifier key, EntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(ResourceKey.create(Registries.ENTITY_TYPE, key)));
    }

    public static EntityType.EntityFactory<TentEntity> tentFactory(Supplier<Item> supplier) {
        return (entityType, level) -> new TentEntity(entityType, level, supplier);
    }

    /**
     * This monstrosity determines whether a mob variant is considered "warm", "temperate", or "cold"
     * @param mob The mob to test
     * @return What temperature variant a mob is.
     */
    public static Identifier getTemperatureVariantType(Mob mob) {
        if (mob instanceof Pig variableMob) {
            if (variableMob.getVariant().is(PigVariants.WARM)) {
                return TemperatureVariants.WARM;
            } else if (variableMob.getVariant().is(PigVariants.TEMPERATE)) {
                return TemperatureVariants.TEMPERATE;
            } else if (variableMob.getVariant().is(PigVariants.COLD)) {
                return TemperatureVariants.COLD;
            }
        } else if (mob instanceof Cow variableMob) {
            if (variableMob.getVariant().is(CowVariants.WARM)) {
                return TemperatureVariants.WARM;
            } else if (variableMob.getVariant().is(CowVariants.TEMPERATE)) {
                return TemperatureVariants.TEMPERATE;
            } else if (variableMob.getVariant().is(CowVariants.COLD)) {
                return TemperatureVariants.COLD;
            }
        } else if (mob instanceof Chicken variableMob) {
            if (variableMob.getVariant().is(ChickenVariants.WARM)) {
                return TemperatureVariants.WARM;
            } else if (variableMob.getVariant().is(ChickenVariants.TEMPERATE)) {
                return TemperatureVariants.TEMPERATE;
            } else if (variableMob.getVariant().is(ChickenVariants.COLD)) {
                return TemperatureVariants.COLD;
            }
        } else if (mob instanceof Frog variableMob) {
            if (variableMob.getVariant().is(FrogVariants.WARM)) {
                return TemperatureVariants.WARM;
            } else if (variableMob.getVariant().is(FrogVariants.TEMPERATE)) {
                return TemperatureVariants.TEMPERATE;
            } else if (variableMob.getVariant().is(FrogVariants.COLD)) {
                variableMob.getVariant().is(TemperatureVariants.WARM);
                return TemperatureVariants.COLD;
            }
        } else if (mob instanceof Rabbit variableMob) {
            if (variableMob.getVariant() == Rabbit.Variant.GOLD) {
                return TemperatureVariants.WARM;
            } else if (variableMob.getVariant() == Rabbit.Variant.BROWN || variableMob.getVariant() == Rabbit.Variant.SALT ||  variableMob.getVariant() == Rabbit.Variant.BLACK) {
                return TemperatureVariants.TEMPERATE;
            } else if (variableMob.getVariant() == Rabbit.Variant.WHITE_SPLOTCHED ||  variableMob.getVariant() == Rabbit.Variant.WHITE ||  variableMob.getVariant() == Rabbit.Variant.EVIL) {
                return TemperatureVariants.COLD;
            }
        } else if (mob instanceof Wolf variableMob) {
            Holder<WolfVariant> variant = variableMob.get(DataComponents.WOLF_VARIANT);

            if (variant != null) {
                if (variant.is(WolfVariants.STRIPED) || variant.is(WolfVariants.SPOTTED) || variant.is(WolfVariants.RUSTY)) {
                    return TemperatureVariants.WARM;
                } else if (variant.is(WolfVariants.WOODS)) {
                    return TemperatureVariants.TEMPERATE;
                } else if (variant.is(WolfVariants.BLACK) || variant.is(WolfVariants.CHESTNUT) || variant.is(WolfVariants.PALE) || variant.is(WolfVariants.ASHEN) || variant.is(WolfVariants.SNOWY)) {
                    return TemperatureVariants.COLD;
                }
            }
        } else if (mob instanceof Fox variableMob) {
            if (variableMob.getVariant() == Fox.Variant.SNOW || variableMob.getVariant() == Fox.Variant.RED) {
                return TemperatureVariants.COLD;
            }
        }
        return null;
    }
}
