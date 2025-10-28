package com.kr1s1s.subtlyd.world.item;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.world.block.BlocksSD;
import com.kr1s1s.subtlyd.world.entity.EntityTypeSD;
import com.kr1s1s.subtlyd.world.entity.TentEntity;
import com.kr1s1s.subtlyd.world.food.FoodsSD;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.core.component.DataComponents;
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

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

import static net.minecraft.world.item.Items.*;

public class ItemsSD {
    public static final Item APPLE_PIE = registerItem(resourceKey("apple_pie"), Item::new, new Item.Properties().food(FoodsSD.APPLE_PIE));
    public static final Item CALAMARI = registerItem(resourceKey("calamari"), Item::new, new Item.Properties().food(FoodsSD.CALAMARI));
    public static final Item COOKED_CALAMARI = registerItem(resourceKey("cooked_calamari"), Item::new, new Item.Properties().food(FoodsSD.COOKED_CALAMARI));
    public static final Item POTTAGE = registerItem(resourceKey("pottage"), Item::new, new Item.Properties().food(FoodsSD.POTTAGE).stacksTo(1));

    public static final Item WHITE_TENT = registerTent("white", EntityTypeSD.WHITE_TENT);
    public static final Item LIGHT_GRAY_TENT = registerTent("light_gray", EntityTypeSD.LIGHT_GRAY_TENT);
    public static final Item GRAY_TENT = registerTent("gray", EntityTypeSD.GRAY_TENT);
    public static final Item BLACK_TENT = registerTent("black", EntityTypeSD.BLACK_TENT);
    public static final Item BROWN_TENT = registerTent("brown", EntityTypeSD.BROWN_TENT);
    public static final Item RED_TENT = registerTent("red", EntityTypeSD.RED_TENT);
    public static final Item ORANGE_TENT = registerTent("orange", EntityTypeSD.ORANGE_TENT);
    public static final Item YELLOW_TENT = registerTent("yellow", EntityTypeSD.YELLOW_TENT);
    public static final Item LIME_TENT = registerTent("lime", EntityTypeSD.LIME_TENT);
    public static final Item GREEN_TENT = registerTent("green", EntityTypeSD.GREEN_TENT);
    public static final Item CYAN_TENT = registerTent("cyan", EntityTypeSD.CYAN_TENT);
    public static final Item LIGHT_BLUE_TENT = registerTent("light_blue", EntityTypeSD.LIGHT_BLUE_TENT);
    public static final Item BLUE_TENT = registerTent("blue", EntityTypeSD.BLUE_TENT);
    public static final Item PURPLE_TENT = registerTent("purple", EntityTypeSD.PURPLE_TENT);
    public static final Item MAGENTA_TENT = registerTent("magenta", EntityTypeSD.MAGENTA_TENT);
    public static final Item PINK_TENT = registerTent("pink", EntityTypeSD.PINK_TENT);
    public static final Item UNLIT_CAMPFIRE = registerBlockSD(Blocks.CAMPFIRE, (properties -> properties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY).component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(CampfireBlock.LIT, false))), "unlit_campfire");
    public static final Item SNOW_BRICKS = registerBlock(BlocksSD.SNOW_BRICKS);
    public static final Item SNOW_BRICK_STAIRS = registerBlock(BlocksSD.SNOW_BRICK_STAIRS);
    public static final Item SNOW_BRICK_SLAB = registerBlock(BlocksSD.SNOW_BRICK_SLAB);

    public static List<Item> TENT_ITEM_LIST = List.of(WHITE_TENT, LIGHT_GRAY_TENT, GRAY_TENT, BLACK_TENT, BROWN_TENT, RED_TENT, ORANGE_TENT, YELLOW_TENT, LIME_TENT, GREEN_TENT, CYAN_TENT, LIGHT_BLUE_TENT, BLUE_TENT, PURPLE_TENT, MAGENTA_TENT, PINK_TENT);
    public static List<Item> WOOL_ITEM_LIST = List.of(WHITE_WOOL, LIGHT_GRAY_WOOL, GRAY_WOOL, BLACK_WOOL, BROWN_WOOL, RED_WOOL, ORANGE_WOOL, YELLOW_WOOL, LIME_WOOL, GREEN_WOOL, CYAN_WOOL, LIGHT_BLUE_WOOL, BLUE_WOOL, PURPLE_WOOL, MAGENTA_WOOL, PINK_WOOL);
    public static List<Item> DYE_ITEM_LIST = List.of(WHITE_DYE, LIGHT_GRAY_DYE, GRAY_DYE, BLACK_DYE, BROWN_DYE, RED_DYE, ORANGE_DYE, YELLOW_DYE, LIME_DYE, GREEN_DYE, CYAN_DYE, LIGHT_BLUE_DYE, BLUE_DYE, PURPLE_DYE, MAGENTA_DYE, PINK_DYE);
    public static List<Item> SNOW_BRICK_LIST = List.of(SNOW_BRICKS, SNOW_BRICK_STAIRS, SNOW_BRICK_SLAB);

    private static ResourceKey<Item> resourceKey(String name) {
        return ResourceKey.create(Registries.ITEM, SubtlyDungeons.resourceLocation(name));
    }

    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            for (Item item : SNOW_BRICK_LIST) {
                entries.addBefore(SANDSTONE, item);
            }
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

        CompostingChanceRegistry.INSTANCE.add(APPLE_PIE, 1.0F);
    }

    private static ResourceKey<Item> blockIdToItemIdSD(String location) {
        return ResourceKey.create(Registries.ITEM, SubtlyDungeons.resourceLocation(location));
    }

    @NotNull
    private static Item registerTent(String string, EntityType<TentEntity> entityType) {
        return registerItem(resourceKey(string + "_tent"), properties -> new TentItem(entityType, properties), new Item.Properties().stacksTo(1));
    }

    private static Item registerBlockSD(Block block, UnaryOperator<Item.Properties> unaryOperator, String location) {
        return registerBlockSD(
                block, ((blockx, properties) -> new BlockItem(blockx, unaryOperator.apply(properties))), location
        );
    }

    private static Item registerBlockSD(Block block, BiFunction<Block, Item.Properties, Item> biFunction, String location) {
        return registerBlockSD(block, biFunction, new Item.Properties(), location);
    }

    private static Item registerBlockSD(Block block, BiFunction<Block, Item.Properties, Item> biFunction, Item.Properties properties, String location) {
        return registerItem(
                blockIdToItemIdSD(location), propertiesx -> biFunction.apply(block, propertiesx), properties.useBlockDescriptionPrefix()
        );
    }
}
