package net.meander.subtlyd.world.item;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.meander.subtlyd.tags.BlockTagsSD;
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

public class CreativeModeTabsSD {
    public static void bootstrap() {
        List<DyeColor> gameplayColorOrder = List.of(
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

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            BlockTagsSD.getBlocks(BlockTagsSD.SNOW_BRICKS).forEach(block -> entries.insertBefore(SANDSTONE, block));
            entries.insertBefore(COAL_BLOCK, CHARCOAL_BLOCK);
            entries.insertAfter(IRON_BLOCK, IRON_GRATE);
            BlockTagsSD.getBlocks(BlockTagsSD.STONE_TILES).forEach(block -> entries.insertBefore(GRANITE, block));
            BlockTagsSD.getBlocks(BlockTagsSD.DRIPSTONE).forEach(block -> entries.insertBefore(GRANITE, block));
            entries.insertAfter(STONE_SLAB, STONE_PILLAR);
            entries.insertAfter(BASALT, BASALT_SLAB);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> {
            entries.insertAfter(BUSH, REEDS);
            entries.insertAfter(WARPED_WART_BLOCK, WARPED_OVERHANG);
            entries.insertAfter(BASALT, BASALT_SLAB);
            entries.insertAfter(JACK_O_LANTERN, SOUL_JACK_O_LANTERN);
            entries.insertBefore(WILDFLOWERS, PERSE_WILDFLOWERS);
            entries.insertAfter(RED_MUSHROOM, BLUE_GLOWSHROOM);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COLORED_BLOCKS).register(entries -> {
            gameplayColorOrder.forEach(color -> entries.insertAfter(BED.pink(), TENT.pick(color)));
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
            TENT.forEach(tent -> entries.insertBefore(CANDLE, tent));
            entries.insertAfter(CAMPFIRE, UNLIT_CAMPFIRE);
            entries.insertAfter(SOUL_CAMPFIRE, UNLIT_SOUL_CAMPFIRE);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> {
            entries.insertAfter(PUMPKIN_PIE, APPLE_PIE);
            entries.insertBefore(COD, CALAMARI);
            entries.insertAfter(CALAMARI, COOKED_CALAMARI);
            entries.insertAfter(RABBIT_STEW, POTTAGE);
            entries.insertAfter(DRIED_KELP, BROWN_MUSHROOM);
            entries.insertAfter(BROWN_MUSHROOM, RED_MUSHROOM);
            entries.insertAfter(RED_MUSHROOM, BLUE_GLOWSHROOM);
            entries.getDisplayStacks().removeIf(stack ->
                    stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(PotionsSD.DECAY)
            );
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
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(entries -> {
            entries.insertAfter(END_CRYSTAL, BLAST_FUNGUS);
        });
    }
}
