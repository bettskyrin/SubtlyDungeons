package com.kr1s1s.subtlyd.world.entity;

import com.kr1s1s.subtlyd.world.item.ItemsSD;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
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
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, TentEntity.getLocation(dyeColor), EntityType.Builder.of(tentFactory(supplier), MobCategory.MISC).sized(3.5F, 2.0F).noLootTable().clientTrackingRange(10).build(TentEntity.getResourceKey(dyeColor)));
    }

    private static EntityType.EntityFactory<TentEntity> tentFactory(Supplier<Item> supplier) {
        return (entityType, level) -> new TentEntity(entityType, level, supplier);
    }
}
