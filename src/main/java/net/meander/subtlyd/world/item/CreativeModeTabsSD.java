package net.meander.subtlyd.world.item;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.meander.subtlyd.util.UtilSD;
import net.meander.subtlyd.world.item.alchemy.PotionsSD;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

import java.util.List;

import static net.meander.subtlyd.world.item.ItemsSD.*;
import static net.minecraft.world.item.Items.*;

/**
 * @see CreativeModeTabs
 */
public class CreativeModeTabsSD {
    private static final List<DyeColor> gameplayColorOrder = List.of(
            DyeColor.WHITE,
            DyeColor.LIGHT_GRAY,
            DyeColor.GRAY,
            DyeColor.BLACK,
            DyeColor.BROWN,
            DyeColor.RED,
            DyeColor.ORANGE,
            DyeColor.YELLOW,
            DyeColor.LIME,
            DyeColor.GREEN,
            DyeColor.CYAN,
            DyeColor.LIGHT_BLUE,
            DyeColor.BLUE,
            DyeColor.PURPLE,
            DyeColor.MAGENTA,
            DyeColor.PINK
    ).reversed();

    public static void registration() {
        UtilSD.LOGGER.debug("Setting creative mode tab entries...");
        buildingBlocks();
        naturalBlocks();
        coloredBlocks();
        functionalBlocks();
        foodAndDrinks();
        combat();
    }

    private static void buildingBlocks() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            entries.insertBefore(SANDSTONE, SNOW_BRICKS);
            entries.insertAfter(SNOW_BRICKS, SNOW_BRICK_STAIRS);
            entries.insertAfter(SNOW_BRICK_STAIRS, SNOW_BRICK_SLAB);
            entries.insertAfter(SNOW_BRICK_SLAB, SNOW_BRICK_WALL);
            entries.insertBefore(COAL_BLOCK, CHARCOAL_BLOCK);
            entries.insertAfter(IRON_BLOCK, IRON_GRATE);
            entries.insertBefore(GRANITE, STONE_TILES);
            entries.insertAfter(STONE_TILES, STONE_TILE_STAIRS);
            entries.insertAfter(STONE_TILE_STAIRS, STONE_TILE_SLAB);
            entries.insertAfter(STONE_TILE_SLAB, STONE_TILE_WALL);
            entries.insertBefore(GRANITE, POLISHED_DRIPSTONE);
            entries.insertAfter(POLISHED_DRIPSTONE, POLISHED_DRIPSTONE_STAIRS);
            entries.insertAfter(POLISHED_DRIPSTONE_STAIRS, POLISHED_DRIPSTONE_SLAB);
            entries.insertAfter(POLISHED_DRIPSTONE_SLAB, POLISHED_DRIPSTONE_WALL);
            entries.insertAfter(POLISHED_DRIPSTONE_WALL, CHISELED_POLISHED_DRIPSTONE);
            entries.insertAfter(STONE_SLAB, STONE_PILLAR);
            entries.insertAfter(BASALT, BASALT_SLAB);
            entries.insertAfter(OAK_WOOD, OAK_WOOD_STAIRS);
            entries.insertAfter(OAK_WOOD_STAIRS, OAK_WOOD_SLAB);
            entries.insertAfter(SPRUCE_WOOD, SPRUCE_WOOD_STAIRS);
            entries.insertAfter(SPRUCE_WOOD_STAIRS, SPRUCE_WOOD_SLAB);
            entries.insertAfter(BIRCH_WOOD, BIRCH_WOOD_STAIRS);
            entries.insertAfter(BIRCH_WOOD_STAIRS, BIRCH_WOOD_SLAB);
            entries.insertAfter(JUNGLE_WOOD, JUNGLE_WOOD_STAIRS);
            entries.insertAfter(JUNGLE_WOOD_STAIRS, JUNGLE_WOOD_SLAB);
            entries.insertAfter(ACACIA_WOOD, ACACIA_WOOD_STAIRS);
            entries.insertAfter(ACACIA_WOOD_STAIRS, ACACIA_WOOD_SLAB);
            entries.insertAfter(DARK_OAK_WOOD, DARK_OAK_WOOD_STAIRS);
            entries.insertAfter(DARK_OAK_WOOD_STAIRS, DARK_OAK_WOOD_SLAB);
            entries.insertAfter(MANGROVE_WOOD, MANGROVE_WOOD_STAIRS);
            entries.insertAfter(MANGROVE_WOOD_STAIRS, MANGROVE_WOOD_SLAB);
            entries.insertAfter(CHERRY_WOOD, CHERRY_WOOD_STAIRS);
            entries.insertAfter(CHERRY_WOOD_STAIRS, CHERRY_WOOD_SLAB);
            entries.insertAfter(PALE_OAK_WOOD, PALE_OAK_WOOD_STAIRS);
            entries.insertAfter(PALE_OAK_WOOD_STAIRS, PALE_OAK_WOOD_SLAB);
            entries.insertAfter(POPLAR_WOOD, POPLAR_WOOD_STAIRS);
            entries.insertAfter(POPLAR_WOOD_STAIRS, POPLAR_WOOD_SLAB);
            entries.insertAfter(CRIMSON_HYPHAE, CRIMSON_HYPHAE_STAIRS);
            entries.insertAfter(CRIMSON_HYPHAE_STAIRS, CRIMSON_HYPHAE_SLAB);
            entries.insertAfter(WARPED_HYPHAE, WARPED_HYPHAE_STAIRS);
            entries.insertAfter(WARPED_HYPHAE_STAIRS, WARPED_HYPHAE_SLAB);
            entries.insertAfter(STRIPPED_OAK_WOOD, STRIPPED_OAK_WOOD_STAIRS);
            entries.insertAfter(STRIPPED_OAK_WOOD_STAIRS, STRIPPED_OAK_WOOD_SLAB);
            entries.insertAfter(STRIPPED_SPRUCE_WOOD, STRIPPED_SPRUCE_WOOD_STAIRS);
            entries.insertAfter(STRIPPED_SPRUCE_WOOD_STAIRS, STRIPPED_SPRUCE_WOOD_SLAB);
            entries.insertAfter(STRIPPED_BIRCH_WOOD, STRIPPED_BIRCH_WOOD_STAIRS);
            entries.insertAfter(STRIPPED_BIRCH_WOOD_STAIRS, STRIPPED_BIRCH_WOOD_SLAB);
            entries.insertAfter(STRIPPED_JUNGLE_WOOD, STRIPPED_JUNGLE_WOOD_STAIRS);
            entries.insertAfter(STRIPPED_JUNGLE_WOOD_STAIRS, STRIPPED_JUNGLE_WOOD_SLAB);
            entries.insertAfter(STRIPPED_ACACIA_WOOD, STRIPPED_ACACIA_WOOD_STAIRS);
            entries.insertAfter(STRIPPED_ACACIA_WOOD_STAIRS, STRIPPED_ACACIA_WOOD_SLAB);
            entries.insertAfter(STRIPPED_DARK_OAK_WOOD, STRIPPED_DARK_OAK_WOOD_STAIRS);
            entries.insertAfter(STRIPPED_DARK_OAK_WOOD_STAIRS, STRIPPED_DARK_OAK_WOOD_SLAB);
            entries.insertAfter(STRIPPED_MANGROVE_WOOD, STRIPPED_MANGROVE_WOOD_STAIRS);
            entries.insertAfter(STRIPPED_MANGROVE_WOOD_STAIRS, STRIPPED_MANGROVE_WOOD_SLAB);
            entries.insertAfter(STRIPPED_CHERRY_WOOD, STRIPPED_CHERRY_WOOD_STAIRS);
            entries.insertAfter(STRIPPED_CHERRY_WOOD_STAIRS, STRIPPED_CHERRY_WOOD_SLAB);
            entries.insertAfter(STRIPPED_PALE_OAK_WOOD, STRIPPED_PALE_OAK_WOOD_STAIRS);
            entries.insertAfter(STRIPPED_PALE_OAK_WOOD_STAIRS, STRIPPED_PALE_OAK_WOOD_SLAB);
            entries.insertAfter(STRIPPED_POPLAR_WOOD, STRIPPED_POPLAR_WOOD_STAIRS);
            entries.insertAfter(STRIPPED_POPLAR_WOOD_STAIRS, STRIPPED_POPLAR_WOOD_SLAB);
            entries.insertAfter(STRIPPED_CRIMSON_HYPHAE, STRIPPED_CRIMSON_HYPHAE_STAIRS);
            entries.insertAfter(STRIPPED_CRIMSON_HYPHAE_STAIRS, STRIPPED_CRIMSON_HYPHAE_SLAB);
            entries.insertAfter(STRIPPED_WARPED_HYPHAE, STRIPPED_WARPED_HYPHAE_STAIRS);
            entries.insertAfter(STRIPPED_WARPED_HYPHAE_STAIRS, STRIPPED_WARPED_HYPHAE_SLAB);
        });
    }

    private static void naturalBlocks() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> {
            entries.insertAfter(BUSH, REEDS);
            entries.insertAfter(WARPED_WART_BLOCK, WARPED_OVERHANG);
            entries.insertAfter(BASALT, BASALT_SLAB);
            entries.insertAfter(JACK_O_LANTERN, SOUL_JACK_O_LANTERN);
            entries.insertBefore(WILDFLOWERS, PERSE_WILDFLOWERS);
        });
    }

    private static void coloredBlocks() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COLORED_BLOCKS).register(entries -> {
            gameplayColorOrder.forEach(color -> entries.insertAfter(BED.pink(), TENT.pick(color)));
        });
    }

    private static void functionalBlocks() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
            TENT.forEach(tent -> entries.insertBefore(CANDLE, tent));
            entries.insertAfter(CAMPFIRE, UNLIT_CAMPFIRE);
            entries.insertAfter(SOUL_CAMPFIRE, UNLIT_SOUL_CAMPFIRE);
        });
    }

    private static void foodAndDrinks() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> {
            entries.insertAfter(PUMPKIN_PIE, APPLE_PIE);
            entries.insertBefore(COD, CALAMARI);
            entries.insertAfter(CALAMARI, COOKED_CALAMARI);
            entries.insertAfter(RABBIT_STEW, POTTAGE);
            entries.insertAfter(DRIED_KELP, BROWN_MUSHROOM);
            entries.insertAfter(BROWN_MUSHROOM, RED_MUSHROOM);
            entries.insertAfter(RED_MUSHROOM, SHELF_MUSHROOM);
            entries.getDisplayStacks().removeIf(stack -> stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(PotionsSD.DECAY));
            entries.insertAfter(itemStack -> itemStack.is(POTION) && itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.STRONG_POISON),
                    List.of(PotionContents.createItemStack(POTION, PotionsSD.DECAY)),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            entries.insertAfter(itemStack -> itemStack.is(SPLASH_POTION) && itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.STRONG_POISON),
                    List.of(PotionContents.createItemStack(SPLASH_POTION, PotionsSD.DECAY)),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            entries.insertAfter(itemStack -> itemStack.is(LINGERING_POTION) && itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.STRONG_POISON),
                    List.of(PotionContents.createItemStack(LINGERING_POTION, PotionsSD.DECAY)),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            entries.insertBefore(POTION, COVEN_ELIXIR);
            entries.insertAfter(BEETROOT_SOUP, LIGHT_STEW);
        });
    }

    private static void combat() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(entries -> {
            entries.insertAfter(END_CRYSTAL, BLAST_FUNGUS);
            entries.insertAfter(NETHERITE_AXE, WOODEN_DAGGER);
            entries.insertAfter(WOODEN_DAGGER, STONE_DAGGER);
            entries.insertAfter(STONE_DAGGER, COPPER_DAGGER);
            entries.insertAfter(COPPER_DAGGER, IRON_DAGGER);
            entries.insertAfter(IRON_DAGGER, GOLDEN_DAGGER);
            entries.insertAfter(GOLDEN_DAGGER, DIAMOND_DAGGER);
            entries.insertAfter(DIAMOND_DAGGER, NETHERITE_DAGGER);
            entries.insertBefore(ARROW, QUIVER);
            entries.insertAfter(SHIELD, HEAVY_SHIELD);
            gameplayColorOrder.forEach(color -> entries.insertAfter(QUIVER, DYED_QUIVER.pick(color)));
        });
    }

}
