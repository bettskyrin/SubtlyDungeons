package net.meander.subtlyd.world.level.storage.loot;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.concurrent.CompletableFuture;

public class BlockLootSD extends FabricBlockLootSubProvider {
    public BlockLootSD(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override public void generate() {
        LootItemCondition.Builder isLitCampfire = LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.CAMPFIRE)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CampfireBlock.LIT, true));
        LootItemCondition.Builder isLitSoulCampfire = LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SOUL_CAMPFIRE)
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CampfireBlock.LIT, true));

        LootTable.Builder campfireBuilder = LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(AlternativesEntry.alternatives(
                                LootItem.lootTableItem(Items.CAMPFIRE)
                                        .when(hasSilkTouch())
                                        .when(isLitCampfire),
                                LootItem.lootTableItem(ItemsSD.UNLIT_CAMPFIRE)
                                        .when(hasSilkTouch()),
                                LootItem.lootTableItem(Items.CHARCOAL)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))
                        ))
                );

        LootTable.Builder soulCampfireBuilder = LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(AlternativesEntry.alternatives(
                                LootItem.lootTableItem(Items.SOUL_CAMPFIRE)
                                        .when(hasSilkTouch())
                                        .when(isLitSoulCampfire),
                                LootItem.lootTableItem(ItemsSD.UNLIT_SOUL_CAMPFIRE)
                                        .when(hasSilkTouch()),
                                LootItem.lootTableItem(Items.SOUL_SOIL)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                        ))
                );

        dropSelf(BlocksSD.SNOW_BRICKS);
        dropSelf(BlocksSD.SNOW_BRICK_STAIRS);
        dropSelf(BlocksSD.SNOW_BRICK_SLAB);
        dropSelf(BlocksSD.SNOW_BRICK_WALL);
        dropSelf(BlocksSD.CHARCOAL_BLOCK);
        dropSelf(BlocksSD.IRON_GRATE);
        dropSelf(BlocksSD.CHISELED_POLISHED_DRIPSTONE);
        dropSelf(BlocksSD.POLISHED_DRIPSTONE);
        dropSelf(BlocksSD.POLISHED_DRIPSTONE_STAIRS);
        dropSelf(BlocksSD.POLISHED_DRIPSTONE_SLAB);
        dropSelf(BlocksSD.POLISHED_DRIPSTONE_WALL);
        dropSelf(BlocksSD.STONE_TILES);
        dropSelf(BlocksSD.STONE_TILE_STAIRS);
        dropSelf(BlocksSD.STONE_TILE_SLAB);
        dropSelf(BlocksSD.STONE_TILE_WALL);
        dropSelf(BlocksSD.STONE_PILLAR);
        add(BlocksSD.WARPED_OVERHANG, this::createShearsOrSilkTouchOnlyDrop);
        add(BlocksSD.REEDS, this::createShearsOrSilkTouchOnlyDrop);
        dropSelf(BlocksSD.BASALT_SLAB);
        dropSelf(BlocksSD.SOUL_JACK_O_LANTERN);
        add(Blocks.CAMPFIRE, campfireBuilder);
        add(Blocks.SOUL_CAMPFIRE, soulCampfireBuilder);
        dropSelf(BlocksSD.PERSE_WILDFLOWERS);
    }
}
