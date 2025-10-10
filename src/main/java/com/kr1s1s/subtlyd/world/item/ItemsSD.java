package com.kr1s1s.subtlyd.world.item;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.world.entity.EntityTypeSD;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

import java.util.List;

import static net.minecraft.world.item.Items.registerItem;

public class ItemsSD {
    public static Item WHITE_TENT = registerItem(resourceKey("white_tent"), properties -> new TentItem(EntityTypeSD.WHITE_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item ORANGE_TENT = registerItem(resourceKey("orange_tent"), properties -> new TentItem(EntityTypeSD.ORANGE_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item MAGENTA_TENT = registerItem(resourceKey("magenta_tent"), properties -> new TentItem(EntityTypeSD.MAGENTA_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item LIGHT_BLUE_TENT = registerItem(resourceKey("light_blue_tent"), properties -> new TentItem(EntityTypeSD.LIGHT_BLUE_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item YELLOW_TENT = registerItem(resourceKey("yellow_tent"), properties -> new TentItem(EntityTypeSD.YELLOW_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item LIME_TENT = registerItem(resourceKey("lime_tent"), properties -> new TentItem(EntityTypeSD.LIME_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item PINK_TENT = registerItem(resourceKey("pink_tent"), properties -> new TentItem(EntityTypeSD.PINK_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item GRAY_TENT = registerItem(resourceKey("gray_tent"), properties -> new TentItem(EntityTypeSD.GRAY_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item LIGHT_GRAY_TENT = registerItem(resourceKey("light_gray_tent"), properties -> new TentItem(EntityTypeSD.LIGHT_GRAY_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item CYAN_TENT = registerItem(resourceKey("cyan_tent"), properties -> new TentItem(EntityTypeSD.CYAN_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item PURPLE_TENT = registerItem(resourceKey("purple_tent"), properties -> new TentItem(EntityTypeSD.PURPLE_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item BLUE_TENT = registerItem(resourceKey("blue_tent"), properties -> new TentItem(EntityTypeSD.BLUE_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item BROWN_TENT = registerItem(resourceKey("brown_tent"), properties -> new TentItem(EntityTypeSD.BROWN_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item GREEN_TENT = registerItem(resourceKey("green_tent"), properties -> new TentItem(EntityTypeSD.GREEN_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item RED_TENT = registerItem(resourceKey("red_tent"), properties -> new TentItem(EntityTypeSD.RED_TENT, properties), new Item.Properties().stacksTo(1));
    public static Item BLACK_TENT = registerItem(resourceKey("black_tent"), properties -> new TentItem(EntityTypeSD.BLACK_TENT, properties), new Item.Properties().stacksTo(1));

    public static List<ItemStack> TENTS = List.of(WHITE_TENT.getDefaultInstance(), ORANGE_TENT.getDefaultInstance(), MAGENTA_TENT.getDefaultInstance(), LIGHT_BLUE_TENT.getDefaultInstance(), YELLOW_TENT.getDefaultInstance(), LIME_TENT.getDefaultInstance(), PINK_TENT.getDefaultInstance(), GRAY_TENT.getDefaultInstance(), LIGHT_GRAY_TENT.getDefaultInstance(), CYAN_TENT.getDefaultInstance(), PURPLE_TENT.getDefaultInstance(), BLUE_TENT.getDefaultInstance(), BROWN_TENT.getDefaultInstance(), GREEN_TENT.getDefaultInstance(), RED_TENT.getDefaultInstance(), BLACK_TENT.getDefaultInstance());

    private static ResourceKey<Item> resourceKey(String name) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, name));
    }

    public static void registerItems() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COLORED_BLOCKS).register(entries -> {
            entries.addAfter(Items.PINK_BED, TENTS);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
            entries.addAfter(Items.PINK_BED, TENTS);
        });
    }
}
