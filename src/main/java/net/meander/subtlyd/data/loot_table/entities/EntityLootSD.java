package net.meander.subtlyd.data.loot_table.entities;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.advancements.predicates.entity.EntityFlagsPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class EntityLootSD {
    public static void register() {
        LootTableEvents.MODIFY.register((resourceKey, tableBuilder, _, _) -> {
            EntityPredicate onFirePredicate = EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true)).build();

            try {
                if (resourceKey.equals(EntityTypes.SQUID.getDefaultLootTable().orElseThrow()) || resourceKey.equals(EntityTypes.GLOW_SQUID.getDefaultLootTable().orElseThrow())) {
                    LootPool.Builder poolBuilder = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(ItemsSD.CALAMARI)
                                    .apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, onFirePredicate))));
                    tableBuilder.withPool(poolBuilder).build();
                }
            } catch (Exception e) {
                Util.LOGGER.error("Failed to register entity loot table {}", e.getMessage());
            }
        });
    }
}