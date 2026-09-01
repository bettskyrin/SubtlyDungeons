package net.meander.subtlyd.data.loot.packs;

import net.meander.subtlyd.world.level.storage.loot.LootTablesSD;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

import java.util.function.BiConsumer;

public class BlockEntityLoot {
    public static void register(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> writer) {
        LootTable.Builder cauldronLoot = LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(Items.POTION).setWeight(25).apply(SetPotionFunction.setPotion(Potions.HEALING)))
                        .add(LootItem.lootTableItem(Items.POTION).setWeight(25).apply(SetPotionFunction.setPotion(Potions.POISON)))
                        .add(LootItem.lootTableItem(Items.POTION).setWeight(15).apply(SetPotionFunction.setPotion(Potions.SWIFTNESS)))
                        .add(LootItem.lootTableItem(Items.POTION).setWeight(10).apply(SetPotionFunction.setPotion(Potions.SLOWNESS)))
                        .add(LootItem.lootTableItem(Items.POTION).setWeight(10).apply(SetPotionFunction.setPotion(Potions.WEAKNESS)))
                        .add(LootItem.lootTableItem(Items.POTION).setWeight(10).apply(SetPotionFunction.setPotion(Potions.WATER_BREATHING)))
                        .add(LootItem.lootTableItem(Items.POTION).setWeight(5).apply(SetPotionFunction.setPotion(Potions.FIRE_RESISTANCE)))
                );

        writer.accept(LootTablesSD.SWAMP_HUT_CAULDRON, cauldronLoot);
    }
}
