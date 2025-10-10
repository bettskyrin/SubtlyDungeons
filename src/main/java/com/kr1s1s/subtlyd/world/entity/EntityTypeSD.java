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
    public static final EntityType<TentEntity> WHITE_TENT = Registry.register(BuiltInRegistries.ENTITY_TYPE, TentEntity.getLocation(DyeColor.WHITE), EntityType.Builder.of(tentFactory(() -> ItemsSD.WHITE_TENT, DyeColor.WHITE), MobCategory.MISC).sized(3.5F, 2.0F).noLootTable().clientTrackingRange(10).build(TentEntity.getResourceKey(DyeColor.WHITE)));
    public static final EntityType<TentEntity> LIGHT_GRAY_TENT = Registry.register(BuiltInRegistries.ENTITY_TYPE, TentEntity.getLocation(DyeColor.LIGHT_GRAY), EntityType.Builder.of(tentFactory(() -> ItemsSD.LIGHT_GRAY_TENT, DyeColor.LIGHT_GRAY), MobCategory.MISC).sized(3.5F, 2.0F).noLootTable().clientTrackingRange(10).build(TentEntity.getResourceKey(DyeColor.LIGHT_GRAY)));
    public static final EntityType<TentEntity> GRAY_TENT = Registry.register(BuiltInRegistries.ENTITY_TYPE, TentEntity.getLocation(DyeColor.GRAY), EntityType.Builder.of(tentFactory(() -> ItemsSD.GRAY_TENT, DyeColor.GRAY), MobCategory.MISC).sized(3.5F, 2.0F).noLootTable().clientTrackingRange(10).build(TentEntity.getResourceKey(DyeColor.GRAY)));
    public static final EntityType<TentEntity> BLACK_TENT = Registry.register(BuiltInRegistries.ENTITY_TYPE, TentEntity.getLocation(DyeColor.BLACK), EntityType.Builder.of(tentFactory(() -> ItemsSD.BLACK_TENT, DyeColor.BLACK), MobCategory.MISC).sized(3.5F, 2.0F).noLootTable().clientTrackingRange(10).build(TentEntity.getResourceKey(DyeColor.BLACK)));
    public static final EntityType<TentEntity> BROWN_TENT = Registry.register(BuiltInRegistries.ENTITY_TYPE, TentEntity.getLocation(DyeColor.BROWN), EntityType.Builder.of(tentFactory(() -> ItemsSD.BROWN_TENT, DyeColor.BROWN), MobCategory.MISC).sized(3.5F, 2.0F).noLootTable().clientTrackingRange(10).build(TentEntity.getResourceKey(DyeColor.BROWN)));
    public static final EntityType<TentEntity> RED_TENT = Registry.register(BuiltInRegistries.ENTITY_TYPE, TentEntity.getLocation(DyeColor.RED), EntityType.Builder.of(tentFactory(() -> ItemsSD.RED_TENT, DyeColor.RED), MobCategory.MISC).sized(3.5F, 2.0F).noLootTable().clientTrackingRange(10).build(TentEntity.getResourceKey(DyeColor.RED)));
    public static final EntityType<TentEntity> ORANGE_TENT = Registry.register(BuiltInRegistries.ENTITY_TYPE, TentEntity.getLocation(DyeColor.ORANGE), EntityType.Builder.of(tentFactory(() -> ItemsSD.ORANGE_TENT, DyeColor.ORANGE), MobCategory.MISC).sized(3.5F, 2.0F).noLootTable().clientTrackingRange(10).build(TentEntity.getResourceKey(DyeColor.ORANGE)));
    public static final EntityType<TentEntity> YELLOW_TENT = Registry.register(BuiltInRegistries.ENTITY_TYPE, TentEntity.getLocation(DyeColor.YELLOW), EntityType.Builder.of(tentFactory(() -> ItemsSD.YELLOW_TENT, DyeColor.YELLOW), MobCategory.MISC).sized(3.5F, 2.0F).noLootTable().clientTrackingRange(10).build(TentEntity.getResourceKey(DyeColor.YELLOW)));
    public static final EntityType<TentEntity> LIME_TENT = Registry.register(BuiltInRegistries.ENTITY_TYPE, TentEntity.getLocation(DyeColor.LIME), EntityType.Builder.of(tentFactory(() -> ItemsSD.LIME_TENT, DyeColor.LIME), MobCategory.MISC).sized(3.5F, 2.0F).noLootTable().clientTrackingRange(10).build(TentEntity.getResourceKey(DyeColor.LIME)));
    public static final EntityType<TentEntity> GREEN_TENT = Registry.register(BuiltInRegistries.ENTITY_TYPE, TentEntity.getLocation(DyeColor.GREEN), EntityType.Builder.of(tentFactory(() -> ItemsSD.GREEN_TENT, DyeColor.GREEN), MobCategory.MISC).sized(3.5F, 2.0F).noLootTable().clientTrackingRange(10).build(TentEntity.getResourceKey(DyeColor.GREEN)));
    public static final EntityType<TentEntity> CYAN_TENT = Registry.register(BuiltInRegistries.ENTITY_TYPE, TentEntity.getLocation(DyeColor.CYAN), EntityType.Builder.of(tentFactory(() -> ItemsSD.CYAN_TENT, DyeColor.CYAN), MobCategory.MISC).sized(3.5F, 2.0F).noLootTable().clientTrackingRange(10).build(TentEntity.getResourceKey(DyeColor.CYAN)));
    public static final EntityType<TentEntity> LIGHT_BLUE_TENT = Registry.register(BuiltInRegistries.ENTITY_TYPE, TentEntity.getLocation(DyeColor.LIGHT_BLUE), EntityType.Builder.of(tentFactory(() -> ItemsSD.LIGHT_BLUE_TENT, DyeColor.LIGHT_BLUE), MobCategory.MISC).sized(3.5F, 2.0F).noLootTable().clientTrackingRange(10).build(TentEntity.getResourceKey(DyeColor.LIGHT_BLUE)));
    public static final EntityType<TentEntity> BLUE_TENT = Registry.register(BuiltInRegistries.ENTITY_TYPE, TentEntity.getLocation(DyeColor.BLUE), EntityType.Builder.of(tentFactory(() -> ItemsSD.BLUE_TENT, DyeColor.BLUE), MobCategory.MISC).sized(3.5F, 2.0F).noLootTable().clientTrackingRange(10).build(TentEntity.getResourceKey(DyeColor.BLUE)));
    public static final EntityType<TentEntity> PURPLE_TENT = Registry.register(BuiltInRegistries.ENTITY_TYPE, TentEntity.getLocation(DyeColor.PURPLE), EntityType.Builder.of(tentFactory(() -> ItemsSD.PURPLE_TENT, DyeColor.PURPLE), MobCategory.MISC).sized(3.5F, 2.0F).noLootTable().clientTrackingRange(10).build(TentEntity.getResourceKey(DyeColor.PURPLE)));
    public static final EntityType<TentEntity> MAGENTA_TENT = Registry.register(BuiltInRegistries.ENTITY_TYPE, TentEntity.getLocation(DyeColor.MAGENTA), EntityType.Builder.of(tentFactory(() -> ItemsSD.MAGENTA_TENT, DyeColor.MAGENTA), MobCategory.MISC).sized(3.5F, 2.0F).noLootTable().clientTrackingRange(10).build(TentEntity.getResourceKey(DyeColor.MAGENTA)));
    public static final EntityType<TentEntity> PINK_TENT = Registry.register(BuiltInRegistries.ENTITY_TYPE, TentEntity.getLocation(DyeColor.PINK), EntityType.Builder.of(tentFactory(() -> ItemsSD.PINK_TENT, DyeColor.PINK), MobCategory.MISC).sized(3.5F, 2.0F).noLootTable().clientTrackingRange(10).build(TentEntity.getResourceKey(DyeColor.PINK)));

    private static EntityType.EntityFactory<TentEntity> tentFactory(Supplier<Item> supplier, DyeColor color) {
        return (entityType, level) -> new TentEntity(entityType, level, supplier, color);
    }
}
