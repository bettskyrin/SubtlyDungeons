package com.kr1s1s.subtlyd.world.item;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.world.entity.EntitySD;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import static net.minecraft.world.item.Items.registerItem;

public class ItemsSD {
    public static Item WHITE_TENT =
            WHITE_TENT = registerItem(resourceKey("white_tent"), properties -> new TentItem(EntitySD.WHITE_TENT, properties), new Item.Properties().stacksTo(1));
    private static ResourceKey<Item> resourceKey(String name) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(SubtlyDungeons.MOD_ID, name));
    }

    public static void registerItems() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COLORED_BLOCKS).register(entries -> {
            entries.addAfter(Items.PINK_BED, WHITE_TENT);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
            entries.addAfter(Items.PINK_BED, WHITE_TENT);
        });
    }
}
