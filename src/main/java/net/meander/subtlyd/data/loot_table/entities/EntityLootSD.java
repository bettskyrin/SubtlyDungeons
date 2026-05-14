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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class EntityLootSD {
    public static void register() {
        LootTableEvents.MODIFY.register((resourceKey, tableBuilder, _, provider) -> {
            HolderLookup.RegistryLookup<Biome> biomeLookup = provider.lookupOrThrow(Registries.BIOME);
            HolderLookup.RegistryLookup<Structure> structureLookup = provider.lookupOrThrow(Registries.STRUCTURE);
            EntityPredicate onFirePredicate = EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true)).build();

            try {
                if (resourceKey.equals(EntityTypes.SQUID.getDefaultLootTable().orElseThrow())
                        || resourceKey.equals(EntityTypes.GLOW_SQUID.getDefaultLootTable().orElseThrow())) {
                    LootPool.Builder squidPool = LootPool.lootPool()
                            .add(LootItem.lootTableItem(ItemsSD.CALAMARI)
                                    .apply(SmeltItemFunction.smelted()
                                            .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, onFirePredicate))))
                            .setRolls(ConstantValue.exactly(1.0F));
                    tableBuilder.withPool(squidPool).build();
                } else if (resourceKey.equals(EntityTypes.WITCH.getDefaultLootTable().orElseThrow())) {
                    LootPool.Builder witchPool = LootPool.lootPool()
                            .add(LootItem.lootTableItem(ItemsSD.COVEN_ELIXIR)
                                    .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setStructures(HolderSet.direct(
                                            structureLookup.getOrThrow(BuiltinStructures.SWAMP_HUT)))))
                                    .setWeight(18))
                            .add(LootItem.lootTableItem(ItemsSD.COVEN_ELIXIR)
                                    .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(
                                            biomeLookup.getOrThrow(BiomeTags.HAS_SWAMP_HUT))))
                                    .setWeight(1))
                            .add(EmptyLootItem.emptyItem()
                                    .setWeight(19))
                            .setRolls(ConstantValue.exactly(1.0F));
                    tableBuilder.withPool(witchPool).build();
                } else if (resourceKey.equals(EntityTypes.COW.getDefaultLootTable().orElseThrow())
                        || resourceKey.equals(EntityTypes.MOOSHROOM.getDefaultLootTable().orElseThrow())
                        || resourceKey.equals(EntityTypes.HORSE.getDefaultLootTable().orElseThrow())
                        || resourceKey.equals(EntityTypes.LLAMA.getDefaultLootTable().orElseThrow())
                        || resourceKey.equals(EntityTypes.MULE.getDefaultLootTable().orElseThrow())
                        || resourceKey.equals(EntityTypes.DONKEY.getDefaultLootTable().orElseThrow())
                        || resourceKey.equals(EntityTypes.TRADER_LLAMA.getDefaultLootTable().orElseThrow())) {
                    LootPool.Builder bonusLeatherPool = LootPool.lootPool()
                            .add(LootItem.lootTableItem(Items.LEATHER)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                    .build());
                    tableBuilder.withPool(bonusLeatherPool).build();
                }
            } catch (Exception e) {
                Util.LOGGER.error("Failed to register entity loot table {}", e.getMessage());
            }
        });
    }
}