package net.meander.subtlyd.world.item;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.meander.subtlyd.data.tags.BlockTagsSD;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;

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
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> {
            entries.insertAfter(BUSH, REEDS);
            entries.insertAfter(WARPED_WART_BLOCK, WARPED_OVERHANG);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COLORED_BLOCKS).register(entries -> {
            gameplayColorOrder.forEach(color -> entries.insertAfter(BED.pink(), TENT.pick(color)));
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
            TENT.forEach(tent -> entries.insertBefore(CANDLE, tent));
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
    }
}
