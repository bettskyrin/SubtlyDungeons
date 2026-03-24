package net.meander.subtlyd.world.entity;

import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
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
import net.minecraft.world.entity.animal.wolf.WolfVariants;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class EntityTypeSD {
    public static final EntityType<TentEntity> WHITE_TENT = registerTent(DyeColor.WHITE, () -> ItemsSD.WHITE_TENT);
    public static final EntityType<TentEntity> LIGHT_GRAY_TENT = registerTent(DyeColor.LIGHT_GRAY, () -> ItemsSD.LIGHT_GRAY_TENT);
    public static final EntityType<TentEntity> GRAY_TENT = registerTent(DyeColor.GRAY, () -> ItemsSD.GRAY_TENT);
    public static final EntityType<TentEntity> BLACK_TENT = registerTent(DyeColor.BLACK, () -> ItemsSD.BLACK_TENT);
    public static final EntityType<TentEntity> BROWN_TENT = registerTent(DyeColor.BROWN, () -> ItemsSD.BROWN_TENT);
    public static final EntityType<TentEntity> RED_TENT = registerTent(DyeColor.RED, () -> ItemsSD.RED_TENT);
    public static final EntityType<TentEntity> ORANGE_TENT = registerTent(DyeColor.ORANGE, () -> ItemsSD.ORANGE_TENT);
    public static final EntityType<TentEntity> YELLOW_TENT = registerTent(DyeColor.YELLOW, () -> ItemsSD.YELLOW_TENT);
    public static final EntityType<TentEntity> LIME_TENT = registerTent(DyeColor.LIME, () -> ItemsSD.LIME_TENT);
    public static final EntityType<TentEntity> GREEN_TENT = registerTent(DyeColor.GREEN, () -> ItemsSD.GREEN_TENT);
    public static final EntityType<TentEntity> CYAN_TENT = registerTent(DyeColor.CYAN, () -> ItemsSD.CYAN_TENT);
    public static final EntityType<TentEntity> LIGHT_BLUE_TENT = registerTent(DyeColor.LIGHT_BLUE, () -> ItemsSD.LIGHT_BLUE_TENT);
    public static final EntityType<TentEntity> BLUE_TENT = registerTent(DyeColor.BLUE, () -> ItemsSD.BLUE_TENT);
    public static final EntityType<TentEntity> PURPLE_TENT = registerTent(DyeColor.PURPLE, () -> ItemsSD.PURPLE_TENT);
    public static final EntityType<TentEntity> MAGENTA_TENT = registerTent(DyeColor.MAGENTA, () -> ItemsSD.MAGENTA_TENT);
    public static final EntityType<TentEntity> PINK_TENT = registerTent(DyeColor.PINK, () -> ItemsSD.PINK_TENT);

    private static EntityType<TentEntity> registerTent(DyeColor dyeColor, Supplier<Item> supplier) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, TentEntity.getLocation(dyeColor), EntityType.Builder.of(tentFactory(supplier), MobCategory.MISC).sized(3.5F, 1.8F).noLootTable().clientTrackingRange(10).build(TentEntity.getResourceKey(dyeColor)));
    }

    private static EntityType.EntityFactory<TentEntity> tentFactory(Supplier<Item> supplier) {
        return (entityType, level) -> new TentEntity(entityType, level, supplier);
    }

    /**
     * This monstrosity determines whether a mob variant is considered "warm", "temperate", or "cold"
     * @param mob The mob to test
     * @return What temperature variant a mob is.
     */
    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
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
            if (variableMob.get(DataComponents.WOLF_VARIANT) == WolfVariants.STRIPED || variableMob.get(DataComponents.WOLF_VARIANT) == WolfVariants.SPOTTED || variableMob.get(DataComponents.WOLF_VARIANT) == WolfVariants.RUSTY) {
                return TemperatureVariants.WARM;
            }else if (variableMob.get(DataComponents.WOLF_VARIANT) == WolfVariants.WOODS) {
                return TemperatureVariants.TEMPERATE;
            } else if (variableMob.get(DataComponents.WOLF_VARIANT) == WolfVariants.BLACK || variableMob.get(DataComponents.WOLF_VARIANT) == WolfVariants.CHESTNUT || variableMob.get(DataComponents.WOLF_VARIANT) == WolfVariants.PALE || variableMob.get(DataComponents.WOLF_VARIANT) == WolfVariants.ASHEN || variableMob.get(DataComponents.WOLF_VARIANT) == WolfVariants.SNOWY) {
                return TemperatureVariants.COLD;
            }
        } else if (mob instanceof Fox variableMob) {
            if (variableMob.getVariant() == Fox.Variant.SNOW || variableMob.getVariant() == Fox.Variant.RED) {
                return TemperatureVariants.COLD;
            }
        }
        return null;
    }
}
