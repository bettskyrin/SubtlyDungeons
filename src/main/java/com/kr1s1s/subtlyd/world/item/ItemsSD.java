package com.kr1s1s.subtlyd.world.item;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.world.block.BlocksSD;
import com.kr1s1s.subtlyd.world.entity.EntityTypeSD;
import com.kr1s1s.subtlyd.world.entity.TentEntity;
import com.kr1s1s.subtlyd.world.food.FoodsSD;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static net.minecraft.world.item.Items.*;

public class ItemsSD {
    public static HashSet<Item> FLAT_ITEMS  = new HashSet<>();
    public static HashSet<Block> CUBE_ALL  = new HashSet<>();
    public static HashSet<Block> COLUMN  = new HashSet<>();
    public static HashSet<HashSet<Item>> ITEMS_SET  = new HashSet<>();
    public static HashSet<HashSet<Block>> BLOCKS_SET  = new HashSet<>();

    public static List<Item> TENT_ITEM_LIST = new ArrayList<>();

    public static final Item APPLE_PIE = registerItemSD("apple_pie", Item::new, new Item.Properties().food(FoodsSD.APPLE_PIE));
    public static final Item CALAMARI = registerItemSD("calamari", Item::new, new Item.Properties().food(FoodsSD.CALAMARI));
    public static final Item COOKED_CALAMARI = registerItemSD("cooked_calamari", Item::new, new Item.Properties().food(FoodsSD.COOKED_CALAMARI));
    public static final Item POTTAGE = registerItemSD("pottage", Item::new, new Item.Properties().food(FoodsSD.POTTAGE).stacksTo(1));
    public static final Item WHITE_TENT = registerTentItem("white", EntityTypeSD.WHITE_TENT);
    public static final Item LIGHT_GRAY_TENT = registerTentItem("light_gray", EntityTypeSD.LIGHT_GRAY_TENT);
    public static final Item GRAY_TENT = registerTentItem("gray", EntityTypeSD.GRAY_TENT);
    public static final Item BLACK_TENT = registerTentItem("black", EntityTypeSD.BLACK_TENT);
    public static final Item BROWN_TENT = registerTentItem("brown", EntityTypeSD.BROWN_TENT);
    public static final Item RED_TENT = registerTentItem("red", EntityTypeSD.RED_TENT);
    public static final Item ORANGE_TENT = registerTentItem("orange", EntityTypeSD.ORANGE_TENT);
    public static final Item YELLOW_TENT = registerTentItem("yellow", EntityTypeSD.YELLOW_TENT);
    public static final Item LIME_TENT = registerTentItem("lime", EntityTypeSD.LIME_TENT);
    public static final Item GREEN_TENT = registerTentItem("green", EntityTypeSD.GREEN_TENT);
    public static final Item CYAN_TENT = registerTentItem("cyan", EntityTypeSD.CYAN_TENT);
    public static final Item LIGHT_BLUE_TENT = registerTentItem("light_blue", EntityTypeSD.LIGHT_BLUE_TENT);
    public static final Item BLUE_TENT = registerTentItem("blue", EntityTypeSD.BLUE_TENT);
    public static final Item PURPLE_TENT = registerTentItem("purple", EntityTypeSD.PURPLE_TENT);
    public static final Item MAGENTA_TENT = registerTentItem("magenta", EntityTypeSD.MAGENTA_TENT);
    public static final Item PINK_TENT = registerTentItem("pink", EntityTypeSD.PINK_TENT);
    public static final Item UNLIT_CAMPFIRE = registerBlockSD(Blocks.CAMPFIRE, (properties -> properties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY).component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(CampfireBlock.LIT, false))), "unlit_campfire");
    public static final Item SNOW_BRICKS = registerBlockSD(BlocksSD.SNOW_BRICKS);
    public static final Item SNOW_BRICK_STAIRS = registerBlockSD(BlocksSD.SNOW_BRICK_STAIRS);
    public static final Item SNOW_BRICK_SLAB = registerBlockSD(BlocksSD.SNOW_BRICK_SLAB);
    public static final Item SNOW_BRICK_WALL = registerBlockSD(BlocksSD.SNOW_BRICK_WALL);
    public static final Item CHARCOAL_BLOCK = registerBlockSD(BlocksSD.CHARCOAL_BLOCK);
    public static final Item IRON_GRATE = registerBlockSD(BlocksSD.IRON_GRATE);
    public static final Item CHISELED_POLISHED_DRIPSTONE = registerBlockSD(BlocksSD.CHISELED_POLISHED_DRIPSTONE);
    public static final Item POLISHED_DRIPSTONE = registerBlockSD(BlocksSD.POLISHED_DRIPSTONE);
    public static final Item POLISHED_DRIPSTONE_SLAB = registerBlockSD(BlocksSD.POLISHED_DRIPSTONE_SLAB);
    public static final Item POLISHED_DRIPSTONE_STAIRS = registerBlockSD(BlocksSD.POLISHED_DRIPSTONE_STAIRS);
    public static final Item POLISHED_DRIPSTONE_WALL = registerBlockSD(BlocksSD.POLISHED_DRIPSTONE_WALL);
    public static final Item STONE_PILLAR = registerBlockSD(BlocksSD.STONE_PILLAR);
    public static final Item STONE_TILES = registerBlockSD(BlocksSD.STONE_TILES);
    public static final Item STONE_TILE_STAIRS = registerBlockSD(BlocksSD.STONE_TILE_STAIRS);
    public static final Item STONE_TILE_SLAB = registerBlockSD(BlocksSD.STONE_TILE_SLAB);
    public static final Item STONE_TILE_WALL = registerBlockSD(BlocksSD.STONE_TILE_WALL);

    public static List<Item> WOOL_ITEM_LIST = List.of(WHITE_WOOL, LIGHT_GRAY_WOOL, GRAY_WOOL, BLACK_WOOL, BROWN_WOOL, RED_WOOL, ORANGE_WOOL, YELLOW_WOOL, LIME_WOOL, GREEN_WOOL, CYAN_WOOL, LIGHT_BLUE_WOOL, BLUE_WOOL, PURPLE_WOOL, MAGENTA_WOOL, PINK_WOOL);
    public static List<Item> DYE_ITEM_LIST = List.of(WHITE_DYE, LIGHT_GRAY_DYE, GRAY_DYE, BLACK_DYE, BROWN_DYE, RED_DYE, ORANGE_DYE, YELLOW_DYE, LIME_DYE, GREEN_DYE, CYAN_DYE, LIGHT_BLUE_DYE, BLUE_DYE, PURPLE_DYE, MAGENTA_DYE, PINK_DYE);
    public static List<Item> SNOW_BRICK_LIST = List.of(SNOW_BRICKS, SNOW_BRICK_STAIRS, SNOW_BRICK_SLAB, SNOW_BRICK_WALL);
    public static List<Item> POLISHED_DRIPSTONE_LIST = List.of(POLISHED_DRIPSTONE, POLISHED_DRIPSTONE_STAIRS, POLISHED_DRIPSTONE_SLAB, POLISHED_DRIPSTONE_WALL);
    public static List<Item> STONE_TILE_LIST = List.of(STONE_TILES, STONE_TILE_STAIRS, STONE_TILE_SLAB, STONE_TILE_WALL);

    private static ResourceKey<Item> resourceKey(String name) {
        return ResourceKey.create(Registries.ITEM, SubtlyDungeons.resourceLocation(name));
    }

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            for (Item item : SNOW_BRICK_LIST) {
                entries.addBefore(SANDSTONE, item);
            }
            entries.addBefore(COAL_BLOCK, CHARCOAL_BLOCK);
            entries.addAfter(IRON_BLOCK, IRON_GRATE);

            for (Item item : STONE_TILE_LIST) {
                entries.addBefore(GRANITE, item);
            }

            for (Item item : POLISHED_DRIPSTONE_LIST) {
                entries.addBefore(GRANITE, item);
            }

            entries.addAfter(STONE_SLAB, STONE_PILLAR);
            entries.addBefore(POLISHED_DRIPSTONE, CHISELED_POLISHED_DRIPSTONE);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COLORED_BLOCKS).register(entries -> {
            for (Item item : TENT_ITEM_LIST.reversed()) {
                entries.addAfter(PINK_BED, item);
            }
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
            for (Item item : TENT_ITEM_LIST.reversed()) {
                entries.addAfter(PINK_BED, item);
            }
            entries.addAfter(CAMPFIRE, UNLIT_CAMPFIRE);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> {
            entries.addAfter(PUMPKIN_PIE, APPLE_PIE);
            entries.addBefore(COD, CALAMARI);
            entries.addAfter(CALAMARI, COOKED_CALAMARI);
            entries.addAfter(RABBIT_STEW, POTTAGE);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            for (Item item : SNOW_BRICK_LIST) {
                entries.addBefore(SANDSTONE, item);
            }
            entries.addBefore(COAL_BLOCK, CHARCOAL_BLOCK);
        });

        CompostingChanceRegistry.INSTANCE.add(APPLE_PIE, 1.0F);
    }

    @NotNull private static Item registerTentItem(String string, EntityType<TentEntity> entityType) {
        Item item = registerItemSD(string + "_tent", properties -> new TentItem(entityType, properties), new Item.Properties().stacksTo(1));
        TENT_ITEM_LIST.add(item);
        return item;
    }

    private static Item registerItemSD(String string, Function<Item.Properties, Item> function, Item.Properties properties) {
        return registerItemSD(resourceKey(string), function, properties, ModelTemplates.FLAT_ITEM);
    }

    private static Item registerItemSD(ResourceKey<Item> resourceKey, Function<Item.Properties, Item> function, Item.Properties properties, ModelTemplate modelTemplate) {
        Item item = registerItem(resourceKey, function, properties);
        switch (modelTemplate) {
            default:
                FLAT_ITEMS.add(item);
        }
        return item;
    }

    private static Item registerBlockSD(Block block) {
        return registerBlockSD(block, BlockItem::new);
    }

    public static Item registerBlockSD(Block block, BiFunction<Block, Item.Properties, Item> biFunction) {
        return registerBlockSD(block, biFunction, new Item.Properties());
    }

    public static Item registerBlockSD(Block block, BiFunction<Block, Item.Properties, Item> biFunction, Item.Properties properties) {
        return registerItem(
                blockIdToItemIdSD(BuiltInRegistries.BLOCK.getResourceKey(block).get()), propertiesx -> (Item)biFunction.apply(block, propertiesx), properties.useBlockDescriptionPrefix()
        );
    }

    private static Item registerBlockSD(Block block, UnaryOperator<Item.Properties> unaryOperator, String location) {
        return registerBlockSD(block, unaryOperator, location, ModelTemplates.CUBE_ALL);
    }

    private static Item registerBlockSD(Block block, UnaryOperator<Item.Properties> unaryOperator, String location, ModelTemplate modelTemplate) {
        return registerBlockSD(block, ((blockx, properties) -> new BlockItem(blockx, unaryOperator.apply(properties))), location, modelTemplate);
    }

    private static Item registerBlockSD(Block block, BiFunction<Block, Item.Properties, Item> biFunction, String location, ModelTemplate modelTemplate) {
        return registerBlockSD(block, biFunction, new Item.Properties(), location, modelTemplate);
    }

    private static Item registerBlockSD(Block block, BiFunction<Block, Item.Properties, Item> biFunction, Item.Properties properties, String location, ModelTemplate modelTemplate) {
        Item item = registerItem(blockIdToItemIdSD(location), propertiesx -> biFunction.apply(block, propertiesx), properties.useBlockDescriptionPrefix());

        switch (modelTemplate) {
            default:
                CUBE_ALL.add(block);
        }

        return item;
    }

    private static ResourceKey<Item> blockIdToItemIdSD(ResourceKey<Block> resourceKey) {
        return ResourceKey.create(Registries.ITEM, resourceKey.location());
    }

    private static ResourceKey<Item> blockIdToItemIdSD(String location) {
        return ResourceKey.create(Registries.ITEM, SubtlyDungeons.resourceLocation(location));
    }
}
