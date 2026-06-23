package net.meander.subtlyd.data;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.meander.subtlyd.tags.ItemTagsSD;
import net.meander.subtlyd.sounds.SoundEventsSD;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.entity.EntityTypesSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.meander.subtlyd.world.item.enchantment.EnchantmentsSD;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class LanguageProviderSD extends FabricLanguageProvider {
    public LanguageProviderSD(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        final String englishUS = "en_us";

        super(packOutput, englishUS, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("advancements.subtlyd.camp_far_away.title", "Tentative Accommodations");
        translationBuilder.add("advancements.subtlyd.camp_far_away.description", "Sleep in a Tent over 1km away from your respawn point");
        translationBuilder.add("advancements.subtlyd.banner_marker.title", "Marking Territory");
        translationBuilder.add("advancements.subtlyd.banner_marker.description", "Use a Map on a Banner");
        translationBuilder.add("advancements.subtlyd.light_campfire.title", "Gather 'Round");
        translationBuilder.add("advancements.subtlyd.light_campfire.description", "Use a Stick on an Unlit Campfire");
        translationBuilder.add("advancements.subtlyd.make_stew.title", "Soup-er!");
        translationBuilder.add("advancements.subtlyd.make_stew.description", "Add ingredients to a Cauldron");

        translationBuilder.add(Blocks.CAMPFIRE, "Lit Campfire");
        translationBuilder.add(Blocks.SOUL_CAMPFIRE, "Lit Soul Campfire");
        translationBuilder.add(BlocksSD.BASALT_SLAB, "Basalt Slab");
        translationBuilder.add(BlocksSD.CHARCOAL_BLOCK, "Block of Charcoal");
        translationBuilder.add(BlocksSD.CHISELED_POLISHED_DRIPSTONE, "Chiseled Polished Dripstone");
        translationBuilder.add(BlocksSD.IRON_GRATE, "Iron Grate");
        translationBuilder.add(BlocksSD.PERSE_WILDFLOWERS, "Perse Wildflowers");
        translationBuilder.add(BlocksSD.POLISHED_DRIPSTONE, "Polished Dripstone");
        translationBuilder.add(BlocksSD.POLISHED_DRIPSTONE_SLAB, "Polished Dripstone Slab");
        translationBuilder.add(BlocksSD.POLISHED_DRIPSTONE_STAIRS, "Polished Dripstone Stairs");
        translationBuilder.add(BlocksSD.POLISHED_DRIPSTONE_WALL, "Polished Dripstone Wall");
        translationBuilder.add(BlocksSD.REEDS, "Reeds");
        translationBuilder.add(BlocksSD.SNOW_BRICK_SLAB, "Snow Brick Slab");
        translationBuilder.add(BlocksSD.SNOW_BRICK_STAIRS, "Snow Brick Stairs");
        translationBuilder.add(BlocksSD.SNOW_BRICK_WALL, "Snow Brick Wall");
        translationBuilder.add(BlocksSD.SNOW_BRICKS, "Snow Bricks");
        translationBuilder.add(BlocksSD.SOUL_JACK_O_LANTERN, "Soul Jack o'Lantern");
        translationBuilder.add(BlocksSD.STONE_PILLAR, "Stone Pillar");
        translationBuilder.add(BlocksSD.STONE_TILE_SLAB, "Stone Tile Slab");
        translationBuilder.add(BlocksSD.STONE_TILE_STAIRS, "Stone Tile Stairs");
        translationBuilder.add(BlocksSD.STONE_TILE_WALL, "Stone Tile Wall");
        translationBuilder.add(BlocksSD.STONE_TILES, "Stone Tiles");
        translationBuilder.add(ItemsSD.UNLIT_CAMPFIRE, "Campfire");
        translationBuilder.add(ItemsSD.UNLIT_SOUL_CAMPFIRE, "Soul Campfire");
        translationBuilder.add(BlocksSD.WARPED_OVERHANG, "Warped Overhang");

        translationBuilder.add("createWorld.tailored.biome_scale", "Biome Scale");
        translationBuilder.add("createWorld.tailored.continent_scale", "Continent Scale");
        translationBuilder.add("createWorld.tailored.master_scale", "Master World Generation Scale");
        translationBuilder.add("createWorld.tailored.pack", "Tailored World Generation");
        translationBuilder.add("createWorld.tailored.title", "Tailored World Generation");

        translationBuilder.add("container.repair.unenchantable", "Magic Capacity Met!");
        translationBuilder.add("container.repair.unfixable", "Unrepairable!");

        translationBuilder.add("commands.camerashake.success.add.multiple", "Applying camera shake to %s players");
        translationBuilder.add("commands.camerashake.success.add.single", "Applying camera shake to %s");
        translationBuilder.add("commands.camerashake.success.stop.multiple", "Stopping camera shake for %s players");
        translationBuilder.add("commands.camerashake.success.stop.single", "Stopping camera shake for %s");

        translationBuilder.addEnchantment(EnchantmentsSD.ABRADING_CURSE, "Curse of Abrading");
        translationBuilder.addEnchantment(EnchantmentsSD.ENERVATION, "Enervation");
        translationBuilder.addEnchantment(EnchantmentsSD.CLEAVING, "Cleaving");
        translationBuilder.addEnchantment(EnchantmentsSD.GLYPH_AFFINITY, "Glyph Affinity");
        translationBuilder.addEnchantment(EnchantmentsSD.ILLAGERS_BANE, "Illager's Bane");
        translationBuilder.addEnchantment(EnchantmentsSD.OCCULT_PROTECTION, "Occult Protection");

        translationBuilder.add(EntityTypesSD.TENT.black(), "Black Tent");
        translationBuilder.add(EntityTypesSD.TENT.blue(), "Blue Tent");
        translationBuilder.add(EntityTypesSD.TENT.brown(), "Brown Tent");
        translationBuilder.add(EntityTypesSD.TENT.cyan(), "Cyan Tent");
        translationBuilder.add(EntityTypesSD.TENT.gray(), "Gray Tent");
        translationBuilder.add(EntityTypesSD.TENT.green(), "Green Tent");
        translationBuilder.add(EntityTypesSD.TENT.lightBlue(), "Light Blue Tent");
        translationBuilder.add(EntityTypesSD.TENT.lightGray(), "Light Gray Tent");
        translationBuilder.add(EntityTypesSD.TENT.lime(), "Lime Tent");
        translationBuilder.add(EntityTypesSD.TENT.magenta(), "Magenta Tent");
        translationBuilder.add(EntityTypesSD.TENT.orange(), "Orange Tent");
        translationBuilder.add(EntityTypesSD.TENT.pink(), "Pink Tent");
        translationBuilder.add(EntityTypesSD.TENT.purple(), "Purple Tent");
        translationBuilder.add(EntityTypesSD.TENT.red(), "Red Tent");
        translationBuilder.add("entity.subtlyd.tent.occupied", "This tent is occupied");
        translationBuilder.add("entity.subtlyd.tent.too_far_away", "You may not rest now; the tent is too far away");
        translationBuilder.add(EntityTypesSD.TENT.white(), "White Tent");
        translationBuilder.add(EntityTypesSD.TENT.yellow(), "Yellow Tent");

        translationBuilder.add("gamerule.subtlyd.arrow_arson", "Allow flaming arrow griefing");
        translationBuilder.add("gamerule.subtlyd.arrow_arson.description", "If enabled, flaming arrows can set fire to their environment");

        translationBuilder.add("item.minecraft.lingering_potion.effect.decay", "Lingering Potion of Decay");
        translationBuilder.add("item.minecraft.potion.effect.decay", "Potion of Decay");
        translationBuilder.add("item.minecraft.splash_potion.effect.decay", "Splash Potion of Decay");
        translationBuilder.add("item.minecraft.tipped_arrow.effect.decay", "Arrow of Decay");

        translationBuilder.add(ItemsSD.APPLE_PIE, "Apple Pie");
        translationBuilder.add(ItemsSD.TENT.black(), "Black Tent");
        translationBuilder.add(ItemsSD.BLAST_FUNGUS, "Blast Fungus");
        translationBuilder.add(ItemsSD.TENT.blue(), "Blue Tent");
        translationBuilder.add(ItemsSD.TENT.brown(), "Brown Tent");
        translationBuilder.add(ItemsSD.CALAMARI, "Calamari");
        translationBuilder.add(ItemsSD.COOKED_CALAMARI, "Cooked Calamari");
        translationBuilder.add(ItemsSD.TENT.cyan(), "Cyan Tent");
        translationBuilder.add(ItemsSD.DIAMOND_DAGGER, "Diamond Dagger");
        translationBuilder.add(ItemsSD.IRON_DAGGER, "Iron Dagger");
        translationBuilder.add(ItemsSD.COVEN_ELIXIR, "Elixir of the Coven");
        translationBuilder.add(ItemsSD.GOLDEN_DAGGER, "Golden Dagger");
        translationBuilder.add(ItemsSD.TENT.gray(), "Gray Tent");
        translationBuilder.add(ItemsSD.TENT.green(), "Green Tent");
        translationBuilder.add(ItemsSD.TENT.lightBlue(), "Light Blue Tent");
        translationBuilder.add(ItemsSD.TENT.lightGray(), "Light Gray Tent");
        translationBuilder.add(ItemsSD.LIGHT_STEW, "Light Stew");
        translationBuilder.add(ItemsSD.TENT.lime(), "Lime Tent");
        translationBuilder.add(ItemsSD.TENT.magenta(), "Magenta Tent");
        translationBuilder.add(ItemsSD.NETHERITE_DAGGER, "Netherite Dagger");
        translationBuilder.add(ItemsSD.TENT.orange(), "Orange Tent");
        translationBuilder.add(ItemsSD.TENT.pink(), "Pink Tent");
        translationBuilder.add("item.subtlyd.potion.long_potion", "Long %s");
        translationBuilder.add("item.subtlyd.potion.strong_potion", "Strong %s");
        translationBuilder.add(ItemsSD.POTTAGE, "Pottage");
        translationBuilder.add(ItemsSD.TENT.purple(), "Purple Tent");
        translationBuilder.add(ItemsSD.TENT.red(), "Red Tent");
        translationBuilder.add(ItemsSD.STONE_DAGGER, "Stone Dagger");
        translationBuilder.add(ItemsSD.WOODEN_DAGGER, "Wooden Dagger");
        translationBuilder.add(ItemsSD.TENT.white(), "White Tent");
        translationBuilder.add(ItemsSD.TENT.yellow(), "Yellow Tent");

        translationBuilder.add("multiplayer.stopSleeping", "Stop Sleeping");

        translationBuilder.add("options.accessibility.camera_shake", "Camera Shake");
        translationBuilder.add("options.accessibility.camera_shake.tooltip", "Toggles the camera shake effect.");
        translationBuilder.add("options.experimental.gui", "Experimental GUI");
        translationBuilder.add("options.experimental.gui.tooltip", "Toggles the experimental GUI changes from Subtly Dungeons.");
        translationBuilder.add("options.difficulty.easy.info", "Hostile creatures spawn but deal less damage. Hunger bar depletes and drains health down to 5 hearts.");
        translationBuilder.add("options.difficulty.hard.info", "Hostile creatures spawn and deal more damage. Hunger bar depletes and drains all health.");
        translationBuilder.add("options.difficulty.normal.info", "Hostile creatures spawn and deal standard damage. Hunger bar depletes and drains health down to half a heart.");
        translationBuilder.add("options.difficulty.peaceful.info", "No hostile creatures and only some neutral creatures spawn. Hunger bar doesn't deplete and health replenishes over time.");

        translationBuilder.add("selectWorld.select", "Play");

        translationBuilder.add("stat.subtlyd.sleep_in_tent", "Times Slept in a Tent");
        translationBuilder.add("stat.subtlyd.damage_blocked_by_weapon", "Damage Blocked by Weapon");

        translationBuilder.add(SoundEventsSD.WIND, "Wind howls");
        translationBuilder.add(SoundEventsSD.BUSH_IDLE, "Windy sounds");
        translationBuilder.add(SoundEventsSD.STEW_SERVED, "Stew is served");
        translationBuilder.add(SoundEventsSD.STEW_STEWS, "Something stews");
        translationBuilder.add(SoundEventsSD.ICE_FREEZE, "Frosty noises");
        translationBuilder.add(SoundEventsSD.AREA_EFFECT_CLOUD_GAS, "Effect cloud hisses");
        translationBuilder.add(SoundEventsSD.FLAME_ARROW_HIT, "Flaming arrow hits");
        translationBuilder.add(SoundEventsSD.FLAME_ARROW_SHOOT, "Flaming arrow fired");
        translationBuilder.add(SoundEventsSD.BLAST_FUNGUS_EXPLODE.value(), "Blast Fungus explodes");
        translationBuilder.add(SoundEventsSD.ENDER_DRAGON_BREATH, "Dragon's Breath hisses");
        translationBuilder.add(SoundEventsSD.EVOKER_FANGS_APPEAR, "Ground rumbles");
        translationBuilder.add(SoundEventsSD.WITHER_SKELETONS_SUMMONED, "Souls wail");
        translationBuilder.add(SoundEventsSD.BLAST_FUNGUS_THROW, "Blast Fungus squelches");
        translationBuilder.add(SoundEventsSD.STICK_LIGHT, "Stick rubs against log");
        translationBuilder.add(SoundEventsSD.TRIDENT_CHARGED, "Trident crackles");
        translationBuilder.add(SoundEventsSD.TRIDENT_CHARGING, "Trident crackles");
        translationBuilder.add(SoundEventsSD.BLADE_CLASH, "Blades clang");
        translationBuilder.add(SoundEventsSD.BLADE_WOOD_CLASH, "Blades clack");

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

        translationBuilder.add(ItemTagsSD.CAN_PARRY_DAGGERS, "Can Parry Daggers");
        translationBuilder.add(ItemTagsSD.CAN_PARRY_SWORDS, "Can Parry Swords");
        translationBuilder.add(ItemTagsSD.DAGGERS, "Daggers");
        translationBuilder.add(ItemTagsSD.HAS_MAGIC_LIMIT, "Has Magic Limit");
        translationBuilder.add(ItemTagsSD.LIQUID_CONSUMABLES, "Liquid Consumables");
        translationBuilder.add(ItemTagsSD.NON_HUMANOID_ARMOR, "Non Humanoid Armor");
        translationBuilder.add(ItemTagsSD.STEW_INGREDIENT, "Stew Ingredient");
        translationBuilder.add(ItemTagsSD.TENTS, "Tents");
    }
}
