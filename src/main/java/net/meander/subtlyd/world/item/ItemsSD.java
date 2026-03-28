package net.meander.subtlyd.world.item;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.registry.CompostableRegistry;
import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.data.tags.BlockTagsSD;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.entity.EntityTypeSD;
import net.meander.subtlyd.world.entity.TentEntity;
import net.meander.subtlyd.world.food.FoodsSD;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static net.minecraft.world.item.Items.*;

public class ItemsSD {
    public static final Item APPLE_PIE = registerItem(resourceKey("apple_pie"), Item::new, new Item.Properties().food(FoodsSD.APPLE_PIE));
    public static final Item CALAMARI = registerItem(resourceKey("calamari"), Item::new, new Item.Properties().food(FoodsSD.CALAMARI));
    public static final Item COOKED_CALAMARI = registerItem(resourceKey("cooked_calamari"), Item::new, new Item.Properties().food(FoodsSD.COOKED_CALAMARI));
    public static final Item POTTAGE = registerItem(resourceKey("pottage"), Item::new, new Item.Properties().food(FoodsSD.POTTAGE).stacksTo(1));
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
    public static final Item SNOW_BRICKS = registerBlock(BlocksSD.SNOW_BRICKS);
    public static final Item SNOW_BRICK_STAIRS = registerBlock(BlocksSD.SNOW_BRICK_STAIRS);
    public static final Item SNOW_BRICK_SLAB = registerBlock(BlocksSD.SNOW_BRICK_SLAB);
    public static final Item SNOW_BRICK_WALL = registerBlock(BlocksSD.SNOW_BRICK_WALL);
    public static final Item CHARCOAL_BLOCK = registerBlock(BlocksSD.CHARCOAL_BLOCK);
    public static final Item IRON_GRATE = registerBlock(BlocksSD.IRON_GRATE);
    public static final Item CHISELED_POLISHED_DRIPSTONE = registerBlock(BlocksSD.CHISELED_POLISHED_DRIPSTONE);
    public static final Item POLISHED_DRIPSTONE = registerBlock(BlocksSD.POLISHED_DRIPSTONE);
    public static final Item POLISHED_DRIPSTONE_SLAB = registerBlock(BlocksSD.POLISHED_DRIPSTONE_SLAB);
    public static final Item POLISHED_DRIPSTONE_STAIRS = registerBlock(BlocksSD.POLISHED_DRIPSTONE_STAIRS);
    public static final Item POLISHED_DRIPSTONE_WALL = registerBlock(BlocksSD.POLISHED_DRIPSTONE_WALL);
    public static final Item STONE_PILLAR = registerBlock(BlocksSD.STONE_PILLAR);
    public static final Item STONE_TILES = registerBlock(BlocksSD.STONE_TILES);
    public static final Item STONE_TILE_STAIRS = registerBlock(BlocksSD.STONE_TILE_STAIRS);
    public static final Item STONE_TILE_SLAB = registerBlock(BlocksSD.STONE_TILE_SLAB);
    public static final Item STONE_TILE_WALL = registerBlock(BlocksSD.STONE_TILE_WALL);
    public static final Item REEDS = registerBlock(BlocksSD.REEDS);
    public static final Item WARPED_OVERHANG = registerBlock(BlocksSD.WARPED_OVERHANG);
    public static final Item BLAST_FUNGUS = registerItem("blast_fungus", BlastFungusItem::new, new Item.Properties().stacksTo(16));

    /**
     * Item tags would be cleaner but are un-ordered collections.
     */
    public static List<Item> TENT_ITEM_LIST = List.of(WHITE_TENT, LIGHT_GRAY_TENT, GRAY_TENT, BLACK_TENT, BROWN_TENT, RED_TENT, ORANGE_TENT, YELLOW_TENT, LIME_TENT, GREEN_TENT, CYAN_TENT, LIGHT_BLUE_TENT, BLUE_TENT, PURPLE_TENT, MAGENTA_TENT, PINK_TENT);
    public static List<Item> WOOL_ITEM_LIST = List.of(WHITE_WOOL, LIGHT_GRAY_WOOL, GRAY_WOOL, BLACK_WOOL, BROWN_WOOL, RED_WOOL, ORANGE_WOOL, YELLOW_WOOL, LIME_WOOL, GREEN_WOOL, CYAN_WOOL, LIGHT_BLUE_WOOL, BLUE_WOOL, PURPLE_WOOL, MAGENTA_WOOL, PINK_WOOL);
    public static List<Item> DYE_ITEM_LIST = List.of(WHITE_DYE, LIGHT_GRAY_DYE, GRAY_DYE, BLACK_DYE, BROWN_DYE, RED_DYE, ORANGE_DYE, YELLOW_DYE, LIME_DYE, GREEN_DYE, CYAN_DYE, LIGHT_BLUE_DYE, BLUE_DYE, PURPLE_DYE, MAGENTA_DYE, PINK_DYE);

    public static void registration() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            for (ItemLike itemLike : BlockTagsSD.getBlocks(BlockTagsSD.SNOW_BRICKS)) {
                entries.insertBefore(SANDSTONE, itemLike);
            }
            entries.insertBefore(COAL_BLOCK, CHARCOAL_BLOCK);
            entries.insertAfter(IRON_BLOCK, IRON_GRATE);

            for (ItemLike itemLike : BlockTagsSD.getBlocks(BlockTagsSD.STONE_TILES)) {
                entries.insertBefore(GRANITE, itemLike);
            }

            for (ItemLike itemLike : BlockTagsSD.getBlocks(BlockTagsSD.DRIPSTONE)) {
                entries.insertBefore(GRANITE, itemLike);
            }

            entries.insertAfter(STONE_SLAB, STONE_PILLAR);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> {
            entries.insertAfter(BUSH, REEDS);
            entries.insertAfter(WARPED_WART_BLOCK, WARPED_OVERHANG);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COLORED_BLOCKS).register(entries -> {
            for (ItemLike itemLike : TENT_ITEM_LIST.reversed()) {
                entries.insertAfter(PINK_BED, itemLike);
            }
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
            for (ItemLike itemLike : TENT_ITEM_LIST.reversed()) {
                entries.insertAfter(PINK_BED, itemLike);
            }
            entries.insertAfter(CAMPFIRE, UNLIT_CAMPFIRE);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> {
            entries.insertAfter(PUMPKIN_PIE, APPLE_PIE);
            entries.insertBefore(COD, CALAMARI);
            entries.insertAfter(CALAMARI, COOKED_CALAMARI);
            entries.insertAfter(RABBIT_STEW, POTTAGE);
            entries.insertAfter(DRIED_KELP, BROWN_MUSHROOM);
            entries.insertAfter(BROWN_MUSHROOM, RED_MUSHROOM);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(entries -> {
            entries.insertAfter(END_CRYSTAL, BLAST_FUNGUS);
        });

        CompostableRegistry.INSTANCE.add(APPLE_PIE, 1.0F);
    }

    private static Item registerItem(final String name, final Function<Item.Properties, Item> itemFactory, final Item.Properties properties) {
        return registerItem(ResourceKey.create(Registries.ITEM, Util.identifier(name)), itemFactory, properties);
    }

    public static Item registerItem(final ResourceKey<Item> key, final Function<Item.Properties, Item> itemFactory, final Item.Properties properties) {
        Item item = itemFactory.apply(properties.setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    @NotNull private static Item registerTentItem(String string, EntityType<TentEntity> entityType) {
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

    private static ResourceKey<Item> blockIdToItemIdSD(String location) {
        return ResourceKey.create(Registries.ITEM, Util.identifier(location));
    }

    private static ResourceKey<Item> resourceKey(String name) {
        return ResourceKey.create(Registries.ITEM, Util.identifier(name));
    }
}