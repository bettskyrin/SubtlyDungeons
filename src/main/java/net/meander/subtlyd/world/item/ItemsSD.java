package net.meander.subtlyd.world.item;

import net.meander.subtlyd.references.BlockItemIdsSD;
import net.meander.subtlyd.references.ItemIdsSD;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.entity.EntityTypesSD;
import net.meander.subtlyd.world.food.FoodsSD;
import net.meander.subtlyd.world.item.component.ConsumablesSD;
import net.minecraft.core.component.DataComponents;
import net.minecraft.references.BlockItemId;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.ColorCollection;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public class ItemsSD {
    public static final Item APPLE_PIE = Items.registerItem(ItemIdsSD.APPLE_PIE, Item::new, new Item.Properties()
            .food(FoodsSD.APPLE_PIE));
    public static final Item CALAMARI = Items.registerItem(ItemIdsSD.CALAMARI, Item::new, new Item.Properties()
            .food(FoodsSD.CALAMARI));
    public static final Item COOKED_CALAMARI = Items.registerItem(ItemIdsSD.COOKED_CALAMARI, Item::new, new Item.Properties()
            .food(FoodsSD.COOKED_CALAMARI));
    public static final Item POTTAGE = Items.registerItem(ItemIdsSD.POTTAGE, Item::new, new Item.Properties()
            .food(FoodsSD.POTTAGE)
            .stacksTo(1));
    public static final ColorCollection<Item> TENT = ColorCollection.registerItems(ItemIdsSD.TENT,
            (id, color) -> Items.registerItem(id, properties -> new TentItem(EntityTypesSD.TENT.pick(color), properties), new Item.Properties()
                    .stacksTo(1)));
    public static final Item UNLIT_CAMPFIRE = registerBlockSD(BlockItemIdsSD.UNLIT_CAMPFIRE, Blocks.CAMPFIRE, (properties -> properties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
            .component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(CampfireBlock.LIT, false))));
    public static final Item UNLIT_SOUL_CAMPFIRE = registerBlockSD(BlockItemIdsSD.UNLIT_SOUL_CAMPFIRE, Blocks.SOUL_CAMPFIRE, (properties -> properties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
            .component(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(CampfireBlock.LIT, false))));
    public static final Item SNOW_BRICKS = Items.registerBlock(BlockItemIdsSD.SNOW_BRICKS, BlocksSD.SNOW_BRICKS);
    public static final Item SNOW_BRICK_STAIRS = Items.registerBlock(BlockItemIdsSD.SNOW_BRICK_STAIRS, BlocksSD.SNOW_BRICK_STAIRS);
    public static final Item SNOW_BRICK_SLAB = Items.registerBlock(BlockItemIdsSD.SNOW_BRICK_SLAB, BlocksSD.SNOW_BRICK_SLAB);
    public static final Item SNOW_BRICK_WALL = Items.registerBlock(BlockItemIdsSD.SNOW_BRICK_WALL, BlocksSD.SNOW_BRICK_WALL);
    public static final Item CHARCOAL_BLOCK = Items.registerBlock(BlockItemIdsSD.CHARCOAL_BLOCK, BlocksSD.CHARCOAL_BLOCK);
    public static final Item IRON_GRATE = Items.registerBlock(BlockItemIdsSD.IRON_GRATE, BlocksSD.IRON_GRATE);
    public static final Item CHISELED_POLISHED_DRIPSTONE = Items.registerBlock(BlockItemIdsSD.CHISELED_POLISHED_DRIPSTONE, BlocksSD.CHISELED_POLISHED_DRIPSTONE);
    public static final Item POLISHED_DRIPSTONE = Items.registerBlock(BlockItemIdsSD.POLISHED_DRIPSTONE, BlocksSD.POLISHED_DRIPSTONE);
    public static final Item POLISHED_DRIPSTONE_SLAB = Items.registerBlock(BlockItemIdsSD.POLISHED_DRIPSTONE_SLAB, BlocksSD.POLISHED_DRIPSTONE_SLAB);
    public static final Item POLISHED_DRIPSTONE_STAIRS = Items.registerBlock(BlockItemIdsSD.POLISHED_DRIPSTONE_STAIRS, BlocksSD.POLISHED_DRIPSTONE_STAIRS);
    public static final Item POLISHED_DRIPSTONE_WALL = Items.registerBlock(BlockItemIdsSD.POLISHED_DRIPSTONE_WALL, BlocksSD.POLISHED_DRIPSTONE_WALL);
    public static final Item STONE_PILLAR = Items.registerBlock(BlockItemIdsSD.STONE_PILLAR, BlocksSD.STONE_PILLAR);
    public static final Item STONE_TILES = Items.registerBlock(BlockItemIdsSD.STONE_TILES, BlocksSD.STONE_TILES);
    public static final Item STONE_TILE_STAIRS = Items.registerBlock(BlockItemIdsSD.STONE_TILE_STAIRS, BlocksSD.STONE_TILE_STAIRS);
    public static final Item STONE_TILE_SLAB = Items.registerBlock(BlockItemIdsSD.STONE_TILE_SLAB, BlocksSD.STONE_TILE_SLAB);
    public static final Item STONE_TILE_WALL = Items.registerBlock(BlockItemIdsSD.STONE_TILE_WALL, BlocksSD.STONE_TILE_WALL);
    public static final Item REEDS = Items.registerBlock(BlockItemIdsSD.REEDS, BlocksSD.REEDS);
    public static final Item WARPED_OVERHANG = Items.registerBlock(BlockItemIdsSD.WARPED_OVERHANG, BlocksSD.WARPED_OVERHANG);
    public static final Item BLAST_FUNGUS = Items.registerItem(ItemIdsSD.BLAST_FUNGUS, BlastFungusItem::new, new Item.Properties()
            .stacksTo(16));
    public static final Item BASALT_SLAB = Items.registerBlock(BlockItemIdsSD.BASALT_SLAB, BlocksSD.BASALT_SLAB);
    public static final Item COVEN_ELIXIR = Items.registerItem(ItemIdsSD.COVEN_ELIXIR, Item::new, new Item.Properties()
            .stacksTo(16)
            .rarity(Rarity.UNCOMMON)
            .component(DataComponents.CONSUMABLE, ConsumablesSD.COVEN_ELIXIR)
            .usingConvertsTo(Items.GLASS_BOTTLE));
    public static final Item SOUL_JACK_O_LANTERN = Items.registerBlock(BlockItemIdsSD.SOUL_JACK_O_LANTERN, BlocksSD.SOUL_JACK_O_LANTERN);
    public static final Item PERSE_WILDFLOWERS = Items.registerBlock(BlockItemIdsSD.PERSE_WILDFLOWERS, BlocksSD.PERSE_WILDFLOWERS);
    public static final Item BLUE_GLOWSHROOM = Items.registerBlock(BlockItemIdsSD.BLUE_GLOWSHROOM, BlocksSD.BLUE_GLOWSHROOM);
    public static final Item BLUE_GLOWSHROOM_BLOCK = Items.registerBlock(BlockItemIdsSD.BLUE_GLOWSHROOM_BLOCK, BlocksSD.BLUE_GLOWSHROOM_BLOCK);

    public static void registration() {
        CreativeModeTabsSD.registration();
    }

    private static Item registerBlockSD(final BlockItemId id, final Block block, final UnaryOperator<Item.Properties> propertiesFunction) {
        return registerBlockSD(id, block, (b, p) -> new BlockItem(b, propertiesFunction.apply(p)));
    }

    private static Item registerBlockSD(final BlockItemId id, final Block block, final BiFunction<Block, Item.Properties, Item> itemFactory) {
        return registerBlockSD(id, block, itemFactory, new Item.Properties());
    }

    private static Item registerBlockSD(final BlockItemId id, final Block block, final BiFunction<Block, Item.Properties, Item> itemFactory, final Item.Properties properties) {
        return Items.registerItem(id.item(), p -> itemFactory.apply(block, p), properties.useBlockDescriptionPrefix().requiredFeatures(block.requiredFeatures()));
    }
}