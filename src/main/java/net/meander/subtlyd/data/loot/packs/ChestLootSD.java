package net.meander.subtlyd.data.loot.packs;

import net.meander.subtlyd.world.item.ItemsSD;
import net.meander.subtlyd.world.level.storage.loot.functions.EnchantNonHumanoidArmorFunction;
import net.meander.subtlyd.world.level.storage.loot.functions.SetDaggerFunction;
import net.minecraft.core.Holder;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SequenceFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @see net.minecraft.data.loot.packs.VanillaChestLoot
 */
public class ChestLootSD {
    public static void register(WritableRegistry<LootTable> registry) {
        modify(registry, BuiltInLootTables.ANCIENT_CITY, ChestLootSD::ancientCity);
        modify(registry, BuiltInLootTables.NETHER_BRIDGE, ChestLootSD::netherBridge);
        modify(registry, BuiltInLootTables.RUINED_PORTAL, ChestLootSD::ruinedPortal);
        modify(registry, BuiltInLootTables.STRONGHOLD_CORRIDOR, ChestLootSD::strongholdCorridor);
        modify(registry, BuiltInLootTables.END_CITY_TREASURE, ChestLootSD::endCityTreasure);
        modify(registry, BuiltInLootTables.BASTION_BRIDGE, ChestLootSD::bastionBridge);
        modify(registry, BuiltInLootTables.BASTION_OTHER, ChestLootSD::bastionOther);
        modify(registry, BuiltInLootTables.BASTION_TREASURE, ChestLootSD::bastionTreasure);
        modify(registry, BuiltInLootTables.BURIED_TREASURE, ChestLootSD::buriedTreasure);
        modify(registry, BuiltInLootTables.PILLAGER_OUTPOST, ChestLootSD::pillagerOutpost);
        modify(registry, BuiltInLootTables.VILLAGE_PLAINS_HOUSE, ChestLootSD::villagePlainsHouse);
        modify(registry, BuiltInLootTables.VILLAGE_FISHER, ChestLootSD::villageFisher);
        modify(registry, BuiltInLootTables.VILLAGE_WEAPONSMITH, ChestLootSD::villageWeaponsmith);
        modify(registry, BuiltInLootTables.LEATHERWORKER_GIFT, ChestLootSD::leatherworkerGift);
        modify(registry, BuiltInLootTables.VILLAGE_TANNERY, ChestLootSD::villageTannery);
        modify(registry, BuiltInLootTables.WOODLAND_MANSION, ChestLootSD::woodlandMansion);
    }

    private static void modify(WritableRegistry<LootTable> registry, ResourceKey<LootTable> key, Consumer<LootTable> modifier) {
        registry.get(key).ifPresent(lootTableReference -> modifier.accept(lootTableReference.value()));
    }

    private static void applyFunction(LootTable table, LootItemFunction.Builder... builders) {
        List<Holder<LootItemFunction>> functionList = new ArrayList<>();

        table.modifier.ifPresent(functionList::add);

        for (LootItemFunction.Builder builder : builders) {
            functionList.add(Holder.direct(builder.build()));
        }

        if (functionList.size() == 1) {
            table.modifier = Optional.of(functionList.getFirst());
        } else if (functionList.size() > 1) {
            SequenceFunction sequence = SequenceFunction.of(functionList);
            table.modifier = Optional.of(Holder.direct(sequence));
        }
    }

    private static void ancientCity(LootTable table) {
        applyFunction(table, EnchantNonHumanoidArmorFunction.builder());
    }

    private static void netherBridge(LootTable table) {
        applyFunction(table, EnchantNonHumanoidArmorFunction.builder(), SetDaggerFunction.setDagger());
    }

    private static void ruinedPortal(LootTable table) {
        applyFunction(table, SetDaggerFunction.setDagger());
    }

    private static void strongholdCorridor(LootTable table) {
        applyFunction(table, EnchantNonHumanoidArmorFunction.builder(), SetDaggerFunction.setDagger());
    }

    private static void endCityTreasure(LootTable table) {
        applyFunction(table, EnchantNonHumanoidArmorFunction.builder(), SetDaggerFunction.setDagger());
    }

    private static void bastionBridge(LootTable table) {
        applyFunction(table, SetDaggerFunction.setDagger());
    }

    private static void bastionOther(LootTable table) {
        applyFunction(table, SetDaggerFunction.setDagger());
    }

    private static void bastionTreasure(LootTable table) {
        applyFunction(table, SetDaggerFunction.setDagger());
    }

    private static void buriedTreasure(LootTable table) {
        applyFunction(table, SetDaggerFunction.setDagger());
    }

    private static void pillagerOutpost(LootTable table) {
        List<LootPool> newPools = new ArrayList<>(table.pools);

        newPools.add(LootPool.lootPool()
                .add(LootItem.lootTableItem(ItemsSD.IRON_DAGGER).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .add(LootItem.lootTableItem(ItemsSD.HEAVY_SHIELD).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .add(LootItem.lootTableItem(ItemsSD.QUIVER).setWeight(2).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .build());

        table.pools = newPools;
    }

    private static void villagePlainsHouse(LootTable table) {
        List<LootPool> newPools = new ArrayList<>(table.pools);

        newPools.add(LootPool.lootPool()
                .add(LootItem.lootTableItem(ItemsSD.APPLE_PIE).setWeight(97))
                .add(EmptyLootItem.emptyItem().setWeight(903))
                .setRolls(ConstantValue.exactly(1))
                .build());

        table.pools = newPools;
    }

    private static void villageFisher(LootTable table) {
        List<LootPool> newPools = new ArrayList<>(table.pools);

        newPools.add(LootPool.lootPool()
                .add(LootItem.lootTableItem(ItemsSD.CALAMARI).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                .build());

        table.pools = newPools;
    }

    private static void villageWeaponsmith(LootTable table) {
        List<LootPool> newPools = new ArrayList<>(table.pools);

        newPools.add(LootPool.lootPool()
                .add(LootItem.lootTableItem(ItemsSD.IRON_DAGGER).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .add(LootItem.lootTableItem(ItemsSD.COPPER_DAGGER).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .add(LootItem.lootTableItem(ItemsSD.HEAVY_SHIELD).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .build());

        table.pools = newPools;
    }

    private static void leatherworkerGift(LootTable table) {
        List<LootPool> newPools = new ArrayList<>(table.pools);

        newPools.add(LootPool.lootPool()
                .add(LootItem.lootTableItem(ItemsSD.QUIVER).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .add(LootItem.lootTableItem(Items.BUNDLE).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .build());

        table.pools = newPools;
    }

    private static void villageTannery(LootTable table) {
        List<LootPool> newPools = new ArrayList<>(table.pools);

        newPools.add(LootPool.lootPool()
                .add(LootItem.lootTableItem(ItemsSD.QUIVER).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                .build());

        table.pools = newPools;
    }

    private static void woodlandMansion(LootTable table) {
        List<LootPool> newPools = new ArrayList<>(table.pools);

        newPools.add(LootPool.lootPool()
                .add(LootItem.lootTableItem(ItemsSD.HEAVY_SHIELD).setWeight(1))
                .add(EmptyLootItem.emptyItem().setWeight(9))
                .setRolls(ConstantValue.exactly(1))
                .build());

        table.pools = newPools;
    }
}
