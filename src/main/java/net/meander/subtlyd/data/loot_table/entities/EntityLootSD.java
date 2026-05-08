package net.meander.subtlyd.data.loot_table.entities;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.advancements.predicates.entity.EntityFlagsPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class EntityLootSD {
    public static void register() {
        LootTableEvents.MODIFY.register((resourceKey, tableBuilder, _, provider) -> {
            HolderLookup.RegistryLookup<Biome> biomeLookup = provider.lookupOrThrow(Registries.BIOME);
            HolderLookup.RegistryLookup<Structure> structureLookup = provider.lookupOrThrow(Registries.STRUCTURE);
            EntityPredicate onFirePredicate = EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true)).build();

            try {
                if (resourceKey.equals(EntityTypes.SQUID.getDefaultLootTable().orElseThrow()) || resourceKey.equals(EntityTypes.GLOW_SQUID.getDefaultLootTable().orElseThrow())) {
                    LootPool.Builder poolBuilder = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(ItemsSD.CALAMARI)
                                    .apply(SmeltItemFunction.smelted().when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, onFirePredicate))));
                    tableBuilder.withPool(poolBuilder).build();
                } else if (resourceKey.equals(EntityTypes.WITCH.getDefaultLootTable().orElseThrow())) {
                    LootPool.Builder poolBuilder = LootPool.lootPool()
                            .add(LootItem.lootTableItem(ItemsSD.ELIXIR).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setStructures(HolderSet.direct(
                                    structureLookup.getOrThrow(BuiltinStructures.SWAMP_HUT)
                            )))).setWeight(18))
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(ItemsSD.ELIXIR).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(
                                            biomeLookup.getOrThrow(BiomeTags.HAS_SWAMP_HUT)
                            ))).setWeight(1))
                            .add(EmptyLootItem.emptyItem().setWeight(19));
                    tableBuilder.withPool(poolBuilder).build();
                }
            } catch (Exception e) {
                Util.LOGGER.error("Failed to register entity loot table {}", e.getMessage());
            }
        });
    }
}