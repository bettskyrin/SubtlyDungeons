package com.kr1s1s.subtlyd.world.item;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.world.entity.EntityTypeSD;
import com.kr1s1s.subtlyd.world.food.FoodsSD;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

import java.util.List;

import static net.minecraft.world.item.Items.*;

public class ItemsSD {
    public static Item WHITE_TENT = registerItem(resourceKey("white_tent"), properties -> new TentItem(EntityTypeSD.WHITE_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item LIGHT_GRAY_TENT = registerItem(resourceKey("light_gray_tent"), properties -> new TentItem(EntityTypeSD.LIGHT_GRAY_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item GRAY_TENT = registerItem(resourceKey("gray_tent"), properties -> new TentItem(EntityTypeSD.GRAY_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item BLACK_TENT = registerItem(resourceKey("black_tent"), properties -> new TentItem(EntityTypeSD.BLACK_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item BROWN_TENT = registerItem(resourceKey("brown_tent"), properties -> new TentItem(EntityTypeSD.BROWN_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item RED_TENT = registerItem(resourceKey("red_tent"), properties -> new TentItem(EntityTypeSD.RED_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item ORANGE_TENT = registerItem(resourceKey("orange_tent"), properties -> new TentItem(EntityTypeSD.ORANGE_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item YELLOW_TENT = registerItem(resourceKey("yellow_tent"), properties -> new TentItem(EntityTypeSD.YELLOW_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item LIME_TENT = registerItem(resourceKey("lime_tent"), properties -> new TentItem(EntityTypeSD.LIME_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item GREEN_TENT = registerItem(resourceKey("green_tent"), properties -> new TentItem(EntityTypeSD.GREEN_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item CYAN_TENT = registerItem(resourceKey("cyan_tent"), properties -> new TentItem(EntityTypeSD.CYAN_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item LIGHT_BLUE_TENT = registerItem(resourceKey("light_blue_tent"), properties -> new TentItem(EntityTypeSD.LIGHT_BLUE_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item BLUE_TENT = registerItem(resourceKey("blue_tent"), properties -> new TentItem(EntityTypeSD.BLUE_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item PURPLE_TENT = registerItem(resourceKey("purple_tent"), properties -> new TentItem(EntityTypeSD.PURPLE_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item MAGENTA_TENT = registerItem(resourceKey("magenta_tent"), properties -> new TentItem(EntityTypeSD.MAGENTA_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item PINK_TENT = registerItem(resourceKey("pink_tent"), properties -> new TentItem(EntityTypeSD.PINK_TENT, properties), new Item.Properties().stacksTo(1));
    public static final Item APPLE_PIE = registerItem(resourceKey("apple_pie"), Item::new, new Item.Properties().food(FoodsSD.APPLE_PIE));
    public static final Item CALAMARI = registerItem(resourceKey("calamari"), Item::new, new Item.Properties().food(FoodsSD.CALAMARI));
    public static final Item COOKED_CALAMARI = registerItem(resourceKey("cooked_calamari"), Item::new, new Item.Properties().food(FoodsSD.COOKED_CALAMARI));

    public static List<Item> TENT_ITEM_FAMILY = List.of(WHITE_TENT, LIGHT_GRAY_TENT, GRAY_TENT, BLACK_TENT, BROWN_TENT, RED_TENT, ORANGE_TENT, YELLOW_TENT, LIME_TENT, GREEN_TENT, CYAN_TENT, LIGHT_BLUE_TENT, BLUE_TENT, PURPLE_TENT, MAGENTA_TENT, PINK_TENT);
    public static List<Item> WOOL_ITEM_FAMILY = List.of(WHITE_WOOL, LIGHT_GRAY_WOOL, GRAY_WOOL, BLACK_WOOL, BROWN_WOOL, RED_WOOL, ORANGE_WOOL, YELLOW_WOOL, LIME_WOOL, GREEN_WOOL, CYAN_WOOL, LIGHT_BLUE_WOOL, BLUE_WOOL, PURPLE_WOOL, MAGENTA_WOOL, PINK_WOOL);
    public static List<Item> DYE_ITEM_FAMILY = List.of(WHITE_DYE, LIGHT_GRAY_DYE, GRAY_DYE, BLACK_DYE, BROWN_DYE, RED_DYE, ORANGE_DYE, YELLOW_DYE, LIME_DYE, GREEN_DYE, CYAN_DYE, LIGHT_BLUE_DYE, BLUE_DYE, PURPLE_DYE, MAGENTA_DYE, PINK_DYE);

    private static ResourceKey<Item> resourceKey(String name) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, name));
    }

    public static void registerItems() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COLORED_BLOCKS).register(entries -> {
            for (Item item : TENT_ITEM_FAMILY) {
                entries.addAfter(Items.PINK_BED, item);
            }

        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
            for (Item item : TENT_ITEM_FAMILY) {
                entries.addAfter(Items.PINK_BED, item);
            }
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> {
            entries.addAfter(Items.PUMPKIN_PIE, APPLE_PIE);
            entries.addBefore(Items.COD, CALAMARI);
            entries.addAfter(CALAMARI, COOKED_CALAMARI);
        });

        CompostingChanceRegistry.INSTANCE.add(APPLE_PIE, 1.0F);
    }
}
