package com.kr1s1s.subtlyd.world.entity;

import com.kr1s1s.subtlyd.world.item.ItemsSD;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class EntitySD {
    public static final EntityType<TentEntity> WHITE_TENT = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            TentEntity.getLocation(DyeColor.WHITE.getName()),
            EntityType.Builder.of(tentFactory(() -> ItemsSD.WHITE_TENT), MobCategory.MISC)
                    .sized(3.5F, 2.0F).noLootTable().clientTrackingRange(10).build(TentEntity.WHITE_TENT_ENTITY));

    public static void registerEntities() {
        FabricDefaultAttributeRegistry.register(EntitySD.WHITE_TENT, TentEntity.createAttributes());
    }

    private static EntityType.EntityFactory<TentEntity> tentFactory(Supplier<Item> supplier) {
        return (entityType, level) -> new TentEntity(entityType, level, supplier);
    }
}
