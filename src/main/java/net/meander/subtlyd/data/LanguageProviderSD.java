package net.meander.subtlyd.data;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class LanguageProviderSD extends FabricLanguageProvider {
    public LanguageProviderSD(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(packOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("block.minecraft.campfire", "Lit Campfire");
        translationBuilder.add("block.subtlyd.basalt_slab", "Basalt Slab");
        translationBuilder.add("block.subtlyd.charcoal_block", "Block of Charcoal");
        translationBuilder.add("block.subtlyd.chiseled_polished_dripstone", "Chiseled Polished Dripstone");
        translationBuilder.add("block.subtlyd.chiseled_stone", "Chiseled Stone");
        translationBuilder.add("block.subtlyd.iron_grate", "Iron Grate");
        translationBuilder.add("block.subtlyd.polished_dripstone", "Polished Dripstone");
        translationBuilder.add("block.subtlyd.polished_dripstone_slab", "Polished Dripstone Slab");
        translationBuilder.add("block.subtlyd.polished_dripstone_stairs", "Polished Dripstone Stairs");
        translationBuilder.add("block.subtlyd.polished_dripstone_wall", "Polished Dripstone Wall");
        translationBuilder.add("block.subtlyd.reeds", "Reeds");
        translationBuilder.add("block.subtlyd.snow_brick_slab", "Snow Brick Slab");
        translationBuilder.add("block.subtlyd.snow_brick_stairs", "Snow Brick Stairs");
        translationBuilder.add("block.subtlyd.snow_brick_wall", "Snow Brick Wall");
        translationBuilder.add("block.subtlyd.snow_bricks", "Snow Bricks");
        translationBuilder.add("block.subtlyd.stone_pillar", "Stone Pillar");
        translationBuilder.add("block.subtlyd.stone_tile_slab", "Stone Tile Slab");
        translationBuilder.add("block.subtlyd.stone_tile_stairs", "Stone Tile Stairs");
        translationBuilder.add("block.subtlyd.stone_tile_wall", "Stone Tile Wall");
        translationBuilder.add("block.subtlyd.stone_tiles", "Stone Tiles");
        translationBuilder.add("block.subtlyd.unlit_campfire", "Campfire");
        translationBuilder.add("block.subtlyd.warped_overhang", "Warped Overhang");

        translationBuilder.add("container.repair.unenchantable", "Magic Capacity Met!");
        translationBuilder.add("container.repair.unfixable", "Unrepairable!");

        translationBuilder.add("commands.camerashake.success.add.multiple", "Applying camera shake to %s players");
        translationBuilder.add("commands.camerashake.success.add.single", "Applying camera shake to %s");
        translationBuilder.add("commands.camerashake.success.stop.multiple", "Stopping camera shake for %s players");
        translationBuilder.add("commands.camerashake.success.stop.single", "Stopping camera shake for %s");

        translationBuilder.add("enchantment.subtlyd.abrading_curse", "Curse of Abrading");
        translationBuilder.add("enchantment.subtlyd.glyph_affinity", "Glyph Affinity");
        translationBuilder.add("enchantment.subtlyd.occult_protection", "Occult Protection");

        translationBuilder.add("entity.subtlyd.black_tent", "Black Tent");
        translationBuilder.add("entity.subtlyd.blue_tent", "Blue Tent");
        translationBuilder.add("entity.subtlyd.brown_tent", "Brown Tent");
        translationBuilder.add("entity.subtlyd.cyan_tent", "Cyan Tent");
        translationBuilder.add("entity.subtlyd.gray_tent", "Gray Tent");
        translationBuilder.add("entity.subtlyd.green_tent", "Green Tent");
        translationBuilder.add("entity.subtlyd.light_blue_tent", "Light Blue Tent");
        translationBuilder.add("entity.subtlyd.light_gray_tent", "Light Gray Tent");
        translationBuilder.add("entity.subtlyd.lime_tent", "Lime Tent");
        translationBuilder.add("entity.subtlyd.magenta_tent", "Magenta Tent");
        translationBuilder.add("entity.subtlyd.orange_tent", "Orange Tent");
        translationBuilder.add("entity.subtlyd.pink_tent", "Pink Tent");
        translationBuilder.add("entity.subtlyd.purple_tent", "Purple Tent");
        translationBuilder.add("entity.subtlyd.red_tent", "Red Tent");
        translationBuilder.add("entity.subtlyd.tent.occupied", "This tent is occupied");
        translationBuilder.add("entity.subtlyd.tent.too_far_away", "You may not rest now; the tent is too far away");
        translationBuilder.add("entity.subtlyd.white_tent", "White Tent");
        translationBuilder.add("entity.subtlyd.yellow_tent", "Yellow Tent");

        translationBuilder.add("gamerule.subtlyd.arrow_arson", "Allow flaming arrow griefing");
        translationBuilder.add("gamerule.subtlyd.arrow_arson.description", "If enabled, flaming arrows can set fire to their environment");

        translationBuilder.add("item.minecraft.lingering_potion.effect.decay", "Lingering Potion of Decay");
        translationBuilder.add("item.minecraft.potion.effect.decay", "Potion of Decay");
        translationBuilder.add("item.minecraft.splash_potion.effect.decay", "Splash Potion of Decay");
        translationBuilder.add("item.minecraft.tipped_arrow.effect.decay", "Arrow of Decay");

        translationBuilder.add("item.subtlyd.apple_pie", "Apple Pie");
        translationBuilder.add("item.subtlyd.black_tent", "Black Tent");
        translationBuilder.add("item.subtlyd.blast_fungus", "Blast Fungus");
        translationBuilder.add("item.subtlyd.blue_tent", "Blue Tent");
        translationBuilder.add("item.subtlyd.brown_tent", "Brown Tent");
        translationBuilder.add("item.subtlyd.calamari", "Calamari");
        translationBuilder.add("item.subtlyd.cooked_calamari", "Cooked Calamari");
        translationBuilder.add("item.subtlyd.cyan_tent", "Cyan Tent");
        translationBuilder.add("item.subtlyd.gray_tent", "Gray Tent");
        translationBuilder.add("item.subtlyd.green_tent", "Green Tent");
        translationBuilder.add("item.subtlyd.light_blue_tent", "Light Blue Tent");
        translationBuilder.add("item.subtlyd.light_gray_tent", "Light Gray Tent");
        translationBuilder.add("item.subtlyd.lime_tent", "Lime Tent");
        translationBuilder.add("item.subtlyd.magenta_tent", "Magenta Tent");
        translationBuilder.add("item.subtlyd.orange_tent", "Orange Tent");
        translationBuilder.add("item.subtlyd.pink_tent", "Pink Tent");
        translationBuilder.add("item.subtlyd.potion.long_potion", "Long %s");
        translationBuilder.add("item.subtlyd.potion.strong_potion", "Strong %s");
        translationBuilder.add("item.subtlyd.pottage", "Pottage");
        translationBuilder.add("item.subtlyd.purple_tent", "Purple Tent");
        translationBuilder.add("item.subtlyd.red_tent", "Red Tent");
        translationBuilder.add("item.subtlyd.white_tent", "White Tent");
        translationBuilder.add("item.subtlyd.yellow_tent", "Yellow Tent");

        translationBuilder.add("multiplayer.stopSleeping", "Stop Sleeping");

        translationBuilder.add("options.accessibility.camera_shake", "Camera Shake");
        translationBuilder.add("options.accessibility.camera_shake.tooltip", "Toggles the camera shake effect.");
        translationBuilder.add("options.difficulty.easy.info", "Hostile creatures spawn but deal less damage. Hunger bar depletes and drains health down to 5 hearts.");
        translationBuilder.add("options.difficulty.hard.info", "Hostile creatures spawn and deal more damage. Hunger bar depletes and drains all health.");
        translationBuilder.add("options.difficulty.normal.info", "Hostile creatures spawn and deal standard damage. Hunger bar depletes and drains health down to half a heart.");
        translationBuilder.add("options.difficulty.peaceful.info", "No hostile creatures and only some neutral creatures spawn. Hunger bar doesn't deplete and health replenishes over time.");

        translationBuilder.add("selectWorld.select", "Play");

        translationBuilder.add("subtitles.subtlyd.block.air.idle", "Wind howls");
        translationBuilder.add("subtitles.subtlyd.block.bush.idle", "Wind blows");
        translationBuilder.add("subtitles.subtlyd.block.frosted_ice.freeze", "Frosty noises");
        translationBuilder.add("subtitles.subtlyd.entity.blast_fungus.explode", "Blast Fungus explodes");
        translationBuilder.add("subtitles.subtlyd.entity.evoker_fangs.appear", "Ground rumbles");
        translationBuilder.add("subtitles.subtlyd.entity.wither_skeleton.summon", "Souls wail");
        translationBuilder.add("subtitles.subtlyd.item.blast_fungus.throw", "Blast Fungus squelches");
        translationBuilder.add("subtitles.subtlyd.item.stick.light", "Stick rubs against log");

        translationBuilder.add("subtlyd.music.alone_with_the_sky", "Crispin Hands - Alone With the Sky");
        translationBuilder.add("subtlyd.music.ashes", "Peter Hont - Ashes");
        translationBuilder.add("subtlyd.music.basalt_deltas", "Peter Hont - Basalt Deltas");
        translationBuilder.add("subtlyd.music.cacti_canyon", "Johan Johnson - Cacti Canyon");
        translationBuilder.add("subtlyd.music.cellar", "Johan Johnson, Peter Hont - Cellar");
        translationBuilder.add("subtlyd.music.cliffs_and_canyons", "Crispin Hands - Cliffs and Canyons");
        translationBuilder.add("subtlyd.music.coral_rise", "Peter Hont - Coral Rise");
        translationBuilder.add("subtlyd.music.creeper_pit", "Peter Hont - Creeper Pit");
        translationBuilder.add("subtlyd.music.crimson_forest", "Eugnosis - Crimson Forest");
        translationBuilder.add("subtlyd.music.dalarna", "Peter Hont - Dalarna");
        translationBuilder.add("subtlyd.music.desert_temple", "Johan Johnson - Desert Temple");
        translationBuilder.add("subtlyd.music.droopy_likes_ricochet", "C418 - Droopy Likes Ricochet");
        translationBuilder.add("subtlyd.music.droopy_likes_your_face", "C418 - Droopy Likes Your Face");
        translationBuilder.add("subtlyd.music.excuse", "C418 - Excuse");
        translationBuilder.add("subtlyd.music.finnbacka", "Peter Hont - Finnbacka");
        translationBuilder.add("subtlyd.music.fizz", "Johan Johnson - Fizz");
        translationBuilder.add("subtlyd.music.guldrum", "Peter Hont - Guldrum");
        translationBuilder.add("subtlyd.music.halland", "Johan Johnson - Halland");
        translationBuilder.add("subtlyd.music.haven", "Johan Johnson - Haven");
        translationBuilder.add("subtlyd.music.hydrothermal_vent", "Peter Hont - Hydrothermal Vent");
        translationBuilder.add("subtlyd.music.intertile", "Peter Hont - Intertile");
        translationBuilder.add("subtlyd.music.molten_monument", "Grant Kirkhope - Molten Monument");
        translationBuilder.add("subtlyd.music.primal_oil_sect", "Peter Hont - Primal Oil Sect");
        translationBuilder.add("subtlyd.music.pumpkin_pastures", "Johan Johnson - Pumpkin Pastures");
        translationBuilder.add("subtlyd.music.radiant_ravine", "Grant Kirkhope - Radiant Ravine");
        translationBuilder.add("subtlyd.music.rest_in_reefs", "Peter Hont - Rest in Reefs");
        translationBuilder.add("subtlyd.music.secrets_in_the_forest", "Crispin Hands - Secrets in the Forest");
        translationBuilder.add("subtlyd.music.skogsstuga", "Peter Hont - Skogsstuga");
        translationBuilder.add("subtlyd.music.soggier_cave", "Johan Johnson - Soggier Cave");
        translationBuilder.add("subtlyd.music.soulsand_valley", "Rostislav Trifonov - Soulsand Valley");
        translationBuilder.add("subtlyd.music.squid_coast", "Johan Johnson - Squid Coast");
        translationBuilder.add("subtlyd.music.the_abyssal_monument", "Grant Kirkhope - The Abyssal Monument");
        translationBuilder.add("subtlyd.music.the_bilge", "Peter Hont - The Bilge");
        translationBuilder.add("subtlyd.music.the_green_expanse", "Crispin Hands - The Green Expanse");
        translationBuilder.add("subtlyd.music.top", "Peter Hont - Top");
        translationBuilder.add("subtlyd.music.tropical_slime_scramble", "Peter Hont - Tropical Slime Scramble");
        translationBuilder.add("subtlyd.music.twilight_cavern", "Peter Hont - Twilight Cavern");
        translationBuilder.add("subtlyd.music.wanderlust", "Peter Hont - Wanderlust");
        translationBuilder.add("subtlyd.music.warped_forest", "Eugnosis - Warped Forest");
        translationBuilder.add("subtlyd.music.windswept_peaks", "Peter Hont - Windswept Peaks");

        translationBuilder.add("tag.item.subtlyd.has_magic_limit", "Has Magic Limit");
        translationBuilder.add("tag.item.subtlyd.liquid_consumables", "Liquid Consumables");
        translationBuilder.add("tag.item.subtlyd.non_humanoid_armor", "Non Humanoid Armor");
        translationBuilder.add("tag.item.subtlyd.tents", "Tents");
    }
}
