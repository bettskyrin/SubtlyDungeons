package com.kr1s1s.subtlyd.world.entity;

import com.kr1s1s.subtlyd.world.item.ItemsSD;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class EntitySD {
    public static final EntityType<TentEntitySD> TENT = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            TentEntitySD.getLocation(),
            EntityType.Builder.of(tentFactory(() -> ItemsSD.WHITE_TENT), MobCategory.MISC)
                    .sized(3.5F, 2.0F).noLootTable().clientTrackingRange(10).build(TentEntitySD.TENT_ENTITY));

//    public static void registerEntities() { // TODO
//        FabricDefaultAttributeRegistry.register(EntitySD.TENT, TentEntitySD.createAttributes());
//    }

    private static EntityType.EntityFactory<TentEntitySD> tentFactory(Supplier<Item> supplier) {
        return (entityType, level) -> new TentEntitySD(entityType, level, supplier, DyeColor.WHITE);
    }
}
