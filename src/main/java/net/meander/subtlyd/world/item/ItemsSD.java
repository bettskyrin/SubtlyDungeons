package net.meander.subtlyd.world.item;

import net.fabricmc.fabric.api.registry.CompostableRegistry;
import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.entity.EntityTypeSD;
import net.meander.subtlyd.world.entity.TentEntity;
import net.meander.subtlyd.world.food.FoodsSD;
import net.meander.subtlyd.world.level.block.ColorCollectionSD;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.ColorCollection;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static net.minecraft.world.item.Items.registerBlock;

public class ItemsSD {
    public static final Item APPLE_PIE = registerItem(resourceKey("apple_pie"), Item::new, new Item.Properties().food(FoodsSD.APPLE_PIE));
    public static final Item CALAMARI = registerItem(resourceKey("calamari"), Item::new, new Item.Properties().food(FoodsSD.CALAMARI));
    public static final Item COOKED_CALAMARI = registerItem(resourceKey("cooked_calamari"), Item::new, new Item.Properties().food(FoodsSD.COOKED_CALAMARI));
    public static final Item POTTAGE = registerItem(resourceKey("pottage"), Item::new, new Item.Properties().food(FoodsSD.POTTAGE).stacksTo(1));
    public static final ColorCollection<Item> TENT = ColorCollectionSD.registerEntityItem("tent", (name, color) -> {
        @SuppressWarnings("unchecked")
        EntityType<TentEntity> tentType = (EntityType<TentEntity>) EntityTypeSD.TENT.pick(color);

        return registerItem(resourceKey(name), properties -> new TentItem(tentType, properties), new Item.Properties().stacksTo(1));
    });
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

    public static void registration() {
        CreativeModeTabsSD.bootstrap();
        CompostableRegistry.INSTANCE.add(APPLE_PIE, 1.0F);
    }

    private static Item registerItem(final String name, final Function<Item.Properties, Item> itemFactory, final Item.Properties properties) {
        return registerItem(ResourceKey.create(Registries.ITEM, Util.identifier(name)), itemFactory, properties);
    }

    public static Item registerItem(final ResourceKey<Item> key, final Function<Item.Properties, Item> itemFactory, final Item.Properties properties) {
        Item item = itemFactory.apply(properties.setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
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