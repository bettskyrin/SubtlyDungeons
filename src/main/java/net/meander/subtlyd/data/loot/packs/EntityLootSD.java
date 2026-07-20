package net.meander.subtlyd.data.loot.packs;

import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.advancements.predicates.entity.EntityFlagsPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @see net.minecraft.data.loot.packs.VanillaEntityLoot
 */
public class EntityLootSD {
    private static final EntityPredicate onFirePredicate = EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true)).build();

    private static ResourceKey<LootTable> getLootTable(EntityType<?> entityType) {
        return entityType.getDefaultLootTable().orElseThrow();
    }

    public static void register(WritableRegistry<LootTable> registry, HolderLookup.Provider provider) {
        try {
            modify(registry, getLootTable(EntityTypes.SQUID), EntityLootSD::calamari);
            modify(registry, getLootTable(EntityTypes.GLOW_SQUID), EntityLootSD::calamari);
            modify(registry, getLootTable(EntityTypes.WITCH), table -> covenElixir(table, provider));
            modify(registry, getLootTable(EntityTypes.COW), EntityLootSD::bonusLeather);
            modify(registry, getLootTable(EntityTypes.MOOSHROOM), EntityLootSD::bonusLeather);
            modify(registry, getLootTable(EntityTypes.HORSE), EntityLootSD::bonusLeather);
            modify(registry, getLootTable(EntityTypes.LLAMA), EntityLootSD::bonusLeather);
            modify(registry, getLootTable(EntityTypes.MULE), EntityLootSD::bonusLeather);
            modify(registry, getLootTable(EntityTypes.DONKEY), EntityLootSD::bonusLeather);
            modify(registry, getLootTable(EntityTypes.TRADER_LLAMA), EntityLootSD::bonusLeather);

        } catch (Exception e) {
            Util.LOGGER.error("Failed to register entity loot table {}", e.getMessage());
        }
    }

    private static void modify(WritableRegistry<LootTable> registry, ResourceKey<LootTable> key, Consumer<LootTable> modifier) {
        registry.get(key).ifPresent(lootTableReference -> modifier.accept(lootTableReference.value()));
    }

    private static void calamari(LootTable table) {
        List<LootPool> newPools = new ArrayList<>(table.pools);

        newPools.add(LootPool.lootPool()
                .add(LootItem.lootTableItem(ItemsSD.CALAMARI)
                        .apply(SmeltItemFunction.smelted()
                                .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, onFirePredicate))))
                .setRolls(ConstantValue.exactly(1.0F))
                .build());

        table.pools = newPools;
    }

    private static void bonusLeather(LootTable table) {
        List<LootPool> newPools = new ArrayList<>(table.pools);

        newPools.add(LootPool.lootPool()
                .add(LootItem.lootTableItem(Items.LEATHER)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                .build());

        table.pools = newPools;
    }

    private static void covenElixir(LootTable table, HolderLookup.Provider provider) {
        HolderLookup.RegistryLookup<Biome> biomeLookup = provider.lookupOrThrow(Registries.BIOME);
        HolderLookup.RegistryLookup<Structure> structureLookup = provider.lookupOrThrow(Registries.STRUCTURE);
        List<LootPool> newPools = new ArrayList<>(table.pools);

        newPools.add(LootPool.lootPool()
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
                .setRolls(ConstantValue.exactly(1.0F))
                .build());

        table.pools = newPools;
    }
}