package net.meander.subtlyd.data;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.meander.subtlyd.advancements.packs.AdventureAdvancementsSD;
import net.meander.subtlyd.advancements.packs.HusbandryAdvancementsSD;
import net.meander.subtlyd.sounds.SoundEventsSD;
import net.meander.subtlyd.stats.StatsSD;
import net.meander.subtlyd.tags.ItemTagsSD;
import net.meander.subtlyd.world.level.GameRulesSD;
import net.meander.subtlyd.world.level.block.BlocksSD;
import net.meander.subtlyd.world.entity.EntityTypesSD;
import net.meander.subtlyd.world.entity.ai.attributes.AttributesSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.meander.subtlyd.world.item.enchantment.EnchantmentsSD;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gamerules.GameRule;

import java.util.concurrent.CompletableFuture;

public class LanguageProviderSD extends FabricLanguageProvider {
    public LanguageProviderSD(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(packOutput, registryLookup);
    }

    private void addStat(TranslationBuilder translationBuilder, Identifier statId, String value) {
        translationBuilder.add("stat." + statId.toString().replace(':', '.'), value);
    }

    private void addMusic(TranslationBuilder translationBuilder, String musicId, String value) {
        translationBuilder.add("subtlyd.music." + musicId, value);
    }

    private void addGameRule(TranslationBuilder translationBuilder, GameRule<?> gameRule, String value) {
        translationBuilder.add("gamerule." + gameRule.id().replace(':', '.'), value);
    }

    private void addGameRuleDesc(TranslationBuilder translationBuilder, GameRule<?> gameRule, String value) {
        translationBuilder.add("gamerule." + gameRule.id().replace(':', '.') + ".description", value);
    }

    private void advancements(TranslationBuilder translationBuilder) {
        translationBuilder.add(AdventureAdvancementsSD.CAMP_FAR_AWAY_TITLE.getString(), "Tentative Accommodations");
        translationBuilder.add(AdventureAdvancementsSD.CAMP_FAR_AWAY_DESC.getString(), "Sleep in a Tent over 1km away from your respawn point");
        translationBuilder.add(AdventureAdvancementsSD.BANNER_MARKER_TITLE.getString(), "Marking Territory");
        translationBuilder.add(AdventureAdvancementsSD.BANNER_MARKER_DESC.getString(), "Use a Map on a Banner");
        translationBuilder.add(HusbandryAdvancementsSD.LIGHT_CAMPFIRE_TITLE.getString(), "Gather 'Round");
        translationBuilder.add(HusbandryAdvancementsSD.LIGHT_CAMPFIRE_DESC.getString(), "Use a Stick on an Unlit Campfire");
        translationBuilder.add(HusbandryAdvancementsSD.MAKE_STEW_TITLE.getString(), "Soup-er!");
        translationBuilder.add(HusbandryAdvancementsSD.MAKE_STEW_DESC.getString(), "Add ingredients to a Cauldron");
        translationBuilder.add(AdventureAdvancementsSD.STEALTH_ATTACK_TITLE.getString(), "I Am Bush");
        translationBuilder.add(AdventureAdvancementsSD.STEALTH_ATTACK_DESC.getString(), "Perform a sneak attack while hiding in foliage");
        translationBuilder.add("advancements.adventure.voluntary_exile.description", "Drink from an Ominous Bottle.\nMaybe consider staying away from villages for the time being...");
    }

    private void attributes(TranslationBuilder translationBuilder) {
        translationBuilder.add(AttributesSD.SHIELD_STRENGTH, "Shield Strength");
    }

    private void stats(TranslationBuilder translationBuilder) {
        addStat(translationBuilder, StatsSD.SLEEP_IN_TENT, "Times Slept in a Tent");
        addStat(translationBuilder, StatsSD.DAMAGE_BLOCKED_BY_WEAPON, "Damage Blocked by Weapon");
    }

    private void blocks(TranslationBuilder translationBuilder) {
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
        translationBuilder.add(BlocksSD.OAK_WOOD_SLAB, "Oak Wood Slab");
        translationBuilder.add(BlocksSD.SPRUCE_WOOD_SLAB, "Spruce Wood Slab");
        translationBuilder.add(BlocksSD.BIRCH_WOOD_SLAB, "Birch Wood Slab");
        translationBuilder.add(BlocksSD.JUNGLE_WOOD_SLAB, "Jungle Wood Slab");
        translationBuilder.add(BlocksSD.ACACIA_WOOD_SLAB, "Acacia Wood Slab");
        translationBuilder.add(BlocksSD.DARK_OAK_WOOD_SLAB, "Dark Oak Wood Slab");
        translationBuilder.add(BlocksSD.MANGROVE_WOOD_SLAB, "Mangrove Wood Slab");
        translationBuilder.add(BlocksSD.POPLAR_WOOD_SLAB, "Poplar Wood Slab");
        translationBuilder.add(BlocksSD.CHERRY_WOOD_SLAB, "Cherry Wood Slab");
        translationBuilder.add(BlocksSD.PALE_OAK_WOOD_SLAB, "Pale Oak Wood Slab");
        translationBuilder.add(BlocksSD.CRIMSON_HYPHAE_SLAB, "Crimson Hyphae Slab");
        translationBuilder.add(BlocksSD.WARPED_HYPHAE_SLAB, "Warped Hyphae Slab");
        translationBuilder.add(BlocksSD.OAK_WOOD_STAIRS, "Oak Wood Stairs");
        translationBuilder.add(BlocksSD.SPRUCE_WOOD_STAIRS, "Spruce Wood Stairs");
        translationBuilder.add(BlocksSD.BIRCH_WOOD_STAIRS, "Birch Wood Stairs");
        translationBuilder.add(BlocksSD.JUNGLE_WOOD_STAIRS, "Jungle Wood Stairs");
        translationBuilder.add(BlocksSD.ACACIA_WOOD_STAIRS, "Acacia Wood Stairs");
        translationBuilder.add(BlocksSD.DARK_OAK_WOOD_STAIRS, "Dark Oak Wood Stairs");
        translationBuilder.add(BlocksSD.MANGROVE_WOOD_STAIRS, "Mangrove Wood Stairs");
        translationBuilder.add(BlocksSD.POPLAR_WOOD_STAIRS, "Poplar Wood Stairs");
        translationBuilder.add(BlocksSD.CHERRY_WOOD_STAIRS, "Cherry Wood Stairs");
        translationBuilder.add(BlocksSD.PALE_OAK_WOOD_STAIRS, "Pale Oak Wood Stairs");
        translationBuilder.add(BlocksSD.CRIMSON_HYPHAE_STAIRS, "Crimson Hyphae Stairs");
        translationBuilder.add(BlocksSD.WARPED_HYPHAE_STAIRS, "Warped Hyphae Stairs");
        translationBuilder.add(BlocksSD.STRIPPED_OAK_WOOD_SLAB, "Stripped Oak Wood Slab");
        translationBuilder.add(BlocksSD.STRIPPED_SPRUCE_WOOD_SLAB, "Stripped Spruce Wood Slab");
        translationBuilder.add(BlocksSD.STRIPPED_BIRCH_WOOD_SLAB, "Stripped Birch Wood Slab");
        translationBuilder.add(BlocksSD.STRIPPED_JUNGLE_WOOD_SLAB, "Stripped Jungle Wood Slab");
        translationBuilder.add(BlocksSD.STRIPPED_ACACIA_WOOD_SLAB, "Stripped Acacia Wood Slab");
        translationBuilder.add(BlocksSD.STRIPPED_DARK_OAK_WOOD_SLAB, "Stripped Dark Oak Wood Slab");
        translationBuilder.add(BlocksSD.STRIPPED_MANGROVE_WOOD_SLAB, "Stripped Mangrove Wood Slab");
        translationBuilder.add(BlocksSD.STRIPPED_POPLAR_WOOD_SLAB, "Stripped Poplar Wood Slab");
        translationBuilder.add(BlocksSD.STRIPPED_CHERRY_WOOD_SLAB, "Stripped Cherry Wood Slab");
        translationBuilder.add(BlocksSD.STRIPPED_PALE_OAK_WOOD_SLAB, "Stripped Pale Oak Wood Slab");
        translationBuilder.add(BlocksSD.STRIPPED_CRIMSON_HYPHAE_SLAB, "Stripped Crimson Hyphae Slab");
        translationBuilder.add(BlocksSD.STRIPPED_WARPED_HYPHAE_SLAB, "Stripped Warped Hyphae Slab");
        translationBuilder.add(BlocksSD.STRIPPED_OAK_WOOD_STAIRS, "Stripped Oak Wood Stairs");
        translationBuilder.add(BlocksSD.STRIPPED_SPRUCE_WOOD_STAIRS, "Stripped Spruce Wood Stairs");
        translationBuilder.add(BlocksSD.STRIPPED_BIRCH_WOOD_STAIRS, "Stripped Birch Wood Stairs");
        translationBuilder.add(BlocksSD.STRIPPED_JUNGLE_WOOD_STAIRS, "Stripped Jungle Wood Stairs");
        translationBuilder.add(BlocksSD.STRIPPED_ACACIA_WOOD_STAIRS, "Stripped Acacia Wood Stairs");
        translationBuilder.add(BlocksSD.STRIPPED_DARK_OAK_WOOD_STAIRS, "Stripped Dark Oak Wood Stairs");
        translationBuilder.add(BlocksSD.STRIPPED_MANGROVE_WOOD_STAIRS, "Stripped Mangrove Wood Stairs");
        translationBuilder.add(BlocksSD.STRIPPED_POPLAR_WOOD_STAIRS, "Stripped Poplar Wood Stairs");
        translationBuilder.add(BlocksSD.STRIPPED_CHERRY_WOOD_STAIRS, "Stripped Cherry Wood Stairs");
        translationBuilder.add(BlocksSD.STRIPPED_PALE_OAK_WOOD_STAIRS, "Stripped Pale Oak Wood Stairs");
        translationBuilder.add(BlocksSD.STRIPPED_CRIMSON_HYPHAE_STAIRS, "Stripped Crimson Hyphae Stairs");
        translationBuilder.add(BlocksSD.STRIPPED_WARPED_HYPHAE_STAIRS, "Stripped Warped Hyphae Stairs");

        translationBuilder.add(BlocksSD.TERRACOTTA_STAIRS, "Terracotta Stairs");

        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_STAIRS.black(), "Black Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_STAIRS.blue(), "Blue Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_STAIRS.brown(), "Brown Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_STAIRS.cyan(), "Cyan Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_STAIRS.gray(), "Gray Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_STAIRS.green(), "Green Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_STAIRS.lightBlue(), "Light Blue Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_STAIRS.lightGray(), "Light Gray Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_STAIRS.lime(), "Lime Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_STAIRS.magenta(), "Magenta Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_STAIRS.orange(), "Orange Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_STAIRS.pink(), "Pink Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_STAIRS.purple(), "Purple Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_STAIRS.red(), "Red Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_STAIRS.white(), "White Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_STAIRS.yellow(), "Yellow Terracotta Stairs");

        translationBuilder.add(BlocksSD.TERRACOTTA_SLAB, "Terracotta Slab");

        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_SLAB.black(), "Black Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_SLAB.blue(), "Blue Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_SLAB.brown(), "Brown Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_SLAB.cyan(), "Cyan Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_SLAB.gray(), "Gray Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_SLAB.green(), "Green Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_SLAB.lightBlue(), "Light Blue Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_SLAB.lightGray(), "Light Blue Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_SLAB.lime(), "Lime Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_SLAB.magenta(), "Magenta Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_SLAB.orange(), "Orange Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_SLAB.pink(), "Pink Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_SLAB.purple(), "Purple Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_SLAB.red(), "Red Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_SLAB.white(), "White Terracotta Stairs");
        translationBuilder.add(BlocksSD.DYED_TERRACOTTA_SLAB.yellow(), "Yellow Terracotta Stairs");
    }

    private void gui(TranslationBuilder translationBuilder) {
        translationBuilder.add("createWorld.tailored.biome_scale", "Biome Scale");
        translationBuilder.add("createWorld.tailored.continent_scale", "Continent Scale");
        translationBuilder.add("createWorld.tailored.erosion_scale", "Erosion Scale");
        translationBuilder.add("createWorld.tailored.master_scale", "Master World Generation Scale");
        translationBuilder.add("createWorld.tailored.pack", "Tailored World Generation");
        translationBuilder.add("createWorld.tailored.title", "Tailored World Generation");

        translationBuilder.add("selectWorld.select", "Play");

        translationBuilder.add("options.accessibility.camera_shake", "Camera Shake");
        translationBuilder.add("options.accessibility.camera_shake.tooltip", "Toggles the camera shake effect.");
        translationBuilder.add("options.accessibility.shield_animation", "Shield Animation");
        translationBuilder.add("options.accessibility.shield_animation.tooltip", "Toggles the visibility of active shields in the First Person view.");
        translationBuilder.add("options.accessibility.shield_crouch", "Shield on Crouch");
        translationBuilder.add("options.accessibility.shield_crouch.tooltip", "Toggles activating shields by crouching.");
        translationBuilder.add("options.fancy_entities", "Fancy Entities");
        translationBuilder.add("options.fancy_entities.tooltip", "Toggles fancy entity animations. Turning this off may cause entity hitboxes to line up incorrectly with their models.");
        translationBuilder.add("options.experimental.gui", "Experimental GUI");
        translationBuilder.add("options.experimental.gui.tooltip", "Toggles the experimental GUI changes from Subtly Dungeons.");
        translationBuilder.add("options.command_macros", "Command Macros..." );
        translationBuilder.add("options.command_macros.title", "Command Macros" );
        translationBuilder.add("options.command_macros.entry", "Command Macro %s");
        translationBuilder.add("options.entity_culling", "Entity Culling");
        translationBuilder.add("options.entity_culling.frustum", "Frustum");
        translationBuilder.add("options.entity_culling.frustum.tooltip", "Culling method that hides entities that are outside the player's FOV.");
        translationBuilder.add("options.entity_culling.occlusion", "Occlusion");
        translationBuilder.add("options.entity_culling.occlusion.tooltip", "Culling method that hides entities that are hidden behind blocks. This method is combined with frustum culling.");

        translationBuilder.add("key.category.subtlyd.command_macros", "Command Macros");
        translationBuilder.add("key.command_macros.0", "Command Macro 0");
        translationBuilder.add("key.command_macros.1", "Command Macro 1");
        translationBuilder.add("key.command_macros.2", "Command Macro 2");
        translationBuilder.add("key.command_macros.3", "Command Macro 3");
        translationBuilder.add("key.command_macros.4", "Command Macro 4");
        translationBuilder.add("key.command_macros.5", "Command Macro 5");
        translationBuilder.add("key.command_macros.6", "Command Macro 6");
        translationBuilder.add("key.command_macros.7", "Command Macro 7");
        translationBuilder.add("key.command_macros.8", "Command Macro 8");
        translationBuilder.add("key.command_macros.9", "Command Macro 9");

        translationBuilder.add("container.repair.unenchantable", "Magic Capacity Met!");
        translationBuilder.add("container.repair.unfixable", "Unrepairable!");

        addGameRule(translationBuilder, GameRulesSD.ARROW_ARSON, "Allow flaming arrow griefing");
        addGameRuleDesc(translationBuilder, GameRulesSD.ARROW_ARSON, "If enabled, flaming arrows can set fire to their environment");
        addGameRule(translationBuilder, GameRulesSD.ADVANCED_MOBS, "Advanced mob behavior");
        addGameRuleDesc(translationBuilder, GameRulesSD.ADVANCED_MOBS, "Allows advanced mob behaviors (including nocturnal hunting, flock panicking, etc.)");
        addGameRule(translationBuilder, GameRulesSD.BLADE_CLASH_WINDOW, "Blade clash window");
        addGameRuleDesc(translationBuilder, GameRulesSD.BLADE_CLASH_WINDOW, "The window of time (in ticks) that two entities must attack each other within, to trigger a blade clash. A value of 0 disables this feature.");

        translationBuilder.add("multiplayer.stopSleeping", "Stop Sleeping");
    }

    private void commands(TranslationBuilder translationBuilder) {
        translationBuilder.add("commands.camerashake.success.add.multiple", "Applying camera shake to %s players");
        translationBuilder.add("commands.camerashake.success.add.single", "Applying camera shake to %s");
        translationBuilder.add("commands.camerashake.success.stop.multiple", "Stopping camera shake for %s players");
        translationBuilder.add("commands.camerashake.success.stop.single", "Stopping camera shake for %s");
    }

    private void enchantments(TranslationBuilder translationBuilder) {
        translationBuilder.addEnchantment(EnchantmentsSD.ABRADING_CURSE, "Curse of Abrading");
        translationBuilder.addEnchantment(EnchantmentsSD.ENERVATION, "Enervation");
        translationBuilder.addEnchantment(EnchantmentsSD.CLEAVING, "Cleaving");
        translationBuilder.addEnchantment(EnchantmentsSD.GLYPH_AFFINITY, "Glyph Affinity");
        translationBuilder.addEnchantment(EnchantmentsSD.ILLAGERS_BANE, "Illager's Bane");
        translationBuilder.addEnchantment(EnchantmentsSD.OCCULT_PROTECTION, "Occult Protection");
    }

    private void entities(TranslationBuilder translationBuilder) {
        translationBuilder.add(EntityTypesSD.TENT, "Tent");
        translationBuilder.add(EntityTypesSD.TENT.getDescriptionId() + ".occupied", "This tent is occupied");
        translationBuilder.add(EntityTypesSD.TENT.getDescriptionId() + ".too_far_away", "You may not rest now; the tent is too far away");
        translationBuilder.add(EntityTypesSD.BLAST_FUNGUS, "Blast Fungus");
    }

    private void items(TranslationBuilder translationBuilder) {
        translationBuilder.add(Items.LINGERING_POTION.getDescriptionId() + ".effect.decay", "Lingering Potion of Decay");
        translationBuilder.add(Items.POTION.getDescriptionId() + ".effect.decay", "Potion of Decay");
        translationBuilder.add(Items.SPLASH_POTION.getDescriptionId() + ".effect.decay", "Splash Potion of Decay");
        translationBuilder.add(Items.TIPPED_ARROW.getDescriptionId() + ".effect.decay", "Arrow of Decay");
        translationBuilder.add(ItemsSD.QUIVER.getDescriptionId() + ".empty.description", "Can hold 4 stacks of any arrow type");

        translationBuilder.add(ItemsSD.APPLE_PIE, "Apple Pie");
        translationBuilder.add(ItemsSD.BLAST_FUNGUS, "Blast Fungus");
        translationBuilder.add(ItemsSD.CALAMARI, "Calamari");
        translationBuilder.add(ItemsSD.COOKED_CALAMARI, "Cooked Calamari");
        translationBuilder.add(ItemsSD.COPPER_DAGGER, "Copper Dagger");
        translationBuilder.add(ItemsSD.DIAMOND_DAGGER, "Diamond Dagger");
        translationBuilder.add(ItemsSD.HEAVY_SHIELD, "Heavy Shield");
        translationBuilder.add(ItemsSD.IRON_DAGGER, "Iron Dagger");
        translationBuilder.add(ItemsSD.COVEN_ELIXIR, "Elixir of the Coven");
        translationBuilder.add(ItemsSD.GOLDEN_DAGGER, "Golden Dagger");
        translationBuilder.add(ItemsSD.LIGHT_STEW, "Light Stew");
        translationBuilder.add(ItemsSD.NETHERITE_DAGGER, "Netherite Dagger");
        translationBuilder.add("item.subtlyd.potion.long_potion", "Long %s");
        translationBuilder.add("item.subtlyd.potion.strong_potion", "Strong %s");
        translationBuilder.add(ItemsSD.POTTAGE, "Pottage");
        translationBuilder.add(ItemsSD.QUIVER, "Quiver");
        translationBuilder.add(ItemsSD.STONE_DAGGER, "Stone Dagger");
        translationBuilder.add(ItemsSD.WOODEN_DAGGER, "Wooden Dagger");
        translationBuilder.add(ItemsSD.TENT.black(), "Black Tent");
        translationBuilder.add(ItemsSD.TENT.blue(), "Blue Tent");
        translationBuilder.add(ItemsSD.TENT.brown(), "Brown Tent");
        translationBuilder.add(ItemsSD.TENT.cyan(), "Cyan Tent");
        translationBuilder.add(ItemsSD.TENT.gray(), "Gray Tent");
        translationBuilder.add(ItemsSD.TENT.green(), "Green Tent");
        translationBuilder.add(ItemsSD.TENT.lightBlue(), "Light Blue Tent");
        translationBuilder.add(ItemsSD.TENT.lightGray(), "Light Gray Tent");
        translationBuilder.add(ItemsSD.TENT.lime(), "Lime Tent");
        translationBuilder.add(ItemsSD.TENT.magenta(), "Magenta Tent");
        translationBuilder.add(ItemsSD.TENT.orange(), "Orange Tent");
        translationBuilder.add(ItemsSD.TENT.pink(), "Pink Tent");
        translationBuilder.add(ItemsSD.TENT.purple(), "Purple Tent");
        translationBuilder.add(ItemsSD.TENT.red(), "Red Tent");
        translationBuilder.add(ItemsSD.TENT.white(), "White Tent");
        translationBuilder.add(ItemsSD.TENT.yellow(), "Yellow Tent");
        translationBuilder.add(ItemsSD.HEAVY_SHIELD.getDescriptionId() + "." + DyeColor.BLACK.getName(), "Black Heavy Shield");
        translationBuilder.add(ItemsSD.HEAVY_SHIELD.getDescriptionId() + "." + DyeColor.BLUE.getName(), "Blue Heavy Shield");
        translationBuilder.add(ItemsSD.HEAVY_SHIELD.getDescriptionId() + "." + DyeColor.BROWN.getName(), "Brown Heavy Shield");
        translationBuilder.add(ItemsSD.HEAVY_SHIELD.getDescriptionId() + "." + DyeColor.CYAN.getName(), "Cyan Heavy Shield");
        translationBuilder.add(ItemsSD.HEAVY_SHIELD.getDescriptionId() + "." + DyeColor.GRAY.getName(), "Gray Heavy Shield");
        translationBuilder.add(ItemsSD.HEAVY_SHIELD.getDescriptionId() + "." + DyeColor.GREEN.getName(), "Green Heavy Shield");
        translationBuilder.add(ItemsSD.HEAVY_SHIELD.getDescriptionId() + "." + DyeColor.LIGHT_BLUE.getName(), "Light Blue Heavy Shield");
        translationBuilder.add(ItemsSD.HEAVY_SHIELD.getDescriptionId() + "." + DyeColor.LIGHT_GRAY.getName(), "Light Gray Heavy Shield");
        translationBuilder.add(ItemsSD.HEAVY_SHIELD.getDescriptionId() + "." + DyeColor.LIME.getName(), "Lime Heavy Shield");
        translationBuilder.add(ItemsSD.HEAVY_SHIELD.getDescriptionId() + "." + DyeColor.MAGENTA.getName(), "Magenta Heavy Shield");
        translationBuilder.add(ItemsSD.HEAVY_SHIELD.getDescriptionId() + "." + DyeColor.ORANGE.getName(), "Orange Heavy Shield");
        translationBuilder.add(ItemsSD.HEAVY_SHIELD.getDescriptionId() + "." + DyeColor.PINK.getName(), "Pink Heavy Shield");
        translationBuilder.add(ItemsSD.HEAVY_SHIELD.getDescriptionId() + "." + DyeColor.PURPLE.getName(), "Purple Heavy Shield");
        translationBuilder.add(ItemsSD.HEAVY_SHIELD.getDescriptionId() + "." + DyeColor.RED.getName(), "Red Heavy Shield");
        translationBuilder.add(ItemsSD.HEAVY_SHIELD.getDescriptionId() + "." + DyeColor.WHITE.getName(), "White Heavy Shield");
        translationBuilder.add(ItemsSD.HEAVY_SHIELD.getDescriptionId() + "." + DyeColor.YELLOW.getName(), "Yellow Heavy Shield");
        translationBuilder.add(ItemsSD.DYED_QUIVER.black(), "Black Quiver");
        translationBuilder.add(ItemsSD.DYED_QUIVER.blue(), "Blue Quiver");
        translationBuilder.add(ItemsSD.DYED_QUIVER.brown(), "Brown Quiver");
        translationBuilder.add(ItemsSD.DYED_QUIVER.cyan(), "Cyan Quiver");
        translationBuilder.add(ItemsSD.DYED_QUIVER.gray(), "Gray Quiver");
        translationBuilder.add(ItemsSD.DYED_QUIVER.green(), "Green Quiver");
        translationBuilder.add(ItemsSD.DYED_QUIVER.lightBlue(), "Light Blue Quiver");
        translationBuilder.add(ItemsSD.DYED_QUIVER.lightGray(), "Light Gray Quiver");
        translationBuilder.add(ItemsSD.DYED_QUIVER.lime(), "Lime Quiver");
        translationBuilder.add(ItemsSD.DYED_QUIVER.magenta(), "Magenta Quiver");
        translationBuilder.add(ItemsSD.DYED_QUIVER.orange(), "Orange Quiver");
        translationBuilder.add(ItemsSD.DYED_QUIVER.pink(), "Pink Quiver");
        translationBuilder.add(ItemsSD.DYED_QUIVER.purple(), "Purple Quiver");
        translationBuilder.add(ItemsSD.DYED_QUIVER.red(), "Red Quiver");
        translationBuilder.add(ItemsSD.DYED_QUIVER.white(), "White Quiver");
        translationBuilder.add(ItemsSD.DYED_QUIVER.yellow(), "Yellow Quiver");
    }

    private void sounds(TranslationBuilder translationBuilder) {
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
        translationBuilder.add(SoundEventsSD.LEAVES_AMBIENT.value(), "Leaves stir");
        translationBuilder.add(SoundEventsSD.GRASS_AMBIENT, "Crickets chirp");
    }

    private void music(TranslationBuilder translationBuilder) {
        addMusic(translationBuilder, "alone_with_the_sky", "Crispin Hands - Alone With the Sky");
        addMusic(translationBuilder, "ashes", "Peter Hont - Ashes");
        addMusic(translationBuilder, "basalt_deltas", "Peter Hont - Basalt Deltas");
        addMusic(translationBuilder, "cacti_canyon", "Johan Johnson - Cacti Canyon");
        addMusic(translationBuilder, "cellar", "Johan Johnson, Peter Hont - Cellar");
        addMusic(translationBuilder, "chris", "C418 - Chris");
        addMusic(translationBuilder, "cliffs_and_canyons", "Crispin Hands - Cliffs and Canyons");
        addMusic(translationBuilder, "coral_rise", "Peter Hont - Coral Rise");
        addMusic(translationBuilder, "creeper_pit", "Peter Hont - Creeper Pit");
        addMusic(translationBuilder, "crimson_forest", "Eugnosis - Crimson Forest");
        addMusic(translationBuilder, "dalarna", "Peter Hont - Dalarna");
        addMusic(translationBuilder, "desert_temple", "Johan Johnson - Desert Temple");
        addMusic(translationBuilder, "door", "C418 - Door");
        addMusic(translationBuilder, "droopy_likes_ricochet", "C418 - Droopy Likes Ricochet");
        addMusic(translationBuilder, "droopy_likes_your_face", "C418 - Droopy Likes Your Face");
        addMusic(translationBuilder, "equinoxe", "C418 - Équinoxe");
        addMusic(translationBuilder, "excuse", "C418 - Excuse");
        addMusic(translationBuilder, "finnbacka", "Peter Hont - Finnbacka");
        addMusic(translationBuilder, "fizz", "Johan Johnson - Fizz");
        addMusic(translationBuilder, "guldrum", "Peter Hont - Guldrum");
        addMusic(translationBuilder, "halland", "Johan Johnson - Halland");
        addMusic(translationBuilder, "haven", "Johan Johnson - Haven");
        addMusic(translationBuilder, "hydrothermal_vent", "Peter Hont - Hydrothermal Vent");
        addMusic(translationBuilder, "intertile", "Peter Hont - Intertile");
        addMusic(translationBuilder, "molten_monument", "Grant Kirkhope - Molten Monument");
        addMusic(translationBuilder, "primal_oil_sect", "Peter Hont - Primal Oil Sect");
        addMusic(translationBuilder, "pumpkin_pastures", "Johan Johnson - Pumpkin Pastures");
        addMusic(translationBuilder, "radiant_ravine", "Grant Kirkhope - Radiant Ravine");
        addMusic(translationBuilder, "rest_in_reefs", "Peter Hont - Rest in Reefs");
        addMusic(translationBuilder, "secrets_in_the_forest", "Crispin Hands - Secrets in the Forest");
        addMusic(translationBuilder, "skogsstuga", "Peter Hont - Skogsstuga");
        addMusic(translationBuilder, "soggier_cave", "Johan Johnson - Soggier Cave");
        addMusic(translationBuilder, "soulsand_valley", "Rostislav Trifonov - Soulsand Valley");
        addMusic(translationBuilder, "squid_coast", "Johan Johnson - Squid Coast");
        addMusic(translationBuilder, "the_abyssal_monument", "Grant Kirkhope - The Abyssal Monument");
        addMusic(translationBuilder, "the_bilge", "Peter Hont - The Bilge");
        addMusic(translationBuilder, "the_green_expanse", "Crispin Hands - The Green Expanse");
        addMusic(translationBuilder, "top", "Peter Hont - Top");
        addMusic(translationBuilder, "tropical_slime_scramble", "Peter Hont - Tropical Slime Scramble");
        addMusic(translationBuilder, "twilight_cavern", "Peter Hont - Twilight Cavern");
        addMusic(translationBuilder, "wanderlust", "Peter Hont - Wanderlust");
        addMusic(translationBuilder, "warped_forest", "Eugnosis - Warped Forest");
        addMusic(translationBuilder, "windswept_peaks", "Peter Hont - Windswept Peaks");
    }

    private void tags(TranslationBuilder translationBuilder) {
        translationBuilder.add(ItemTagsSD.CAN_PARRY_DAGGERS, "Can Parry Daggers");
        translationBuilder.add(ItemTagsSD.CAN_PARRY_SWORDS, "Can Parry Swords");
        translationBuilder.add(ItemTagsSD.DAGGERS, "Daggers");
        translationBuilder.add(ItemTagsSD.HAS_MAGIC_LIMIT, "Has Magic Limit");
        translationBuilder.add(ItemTagsSD.LIQUID_CONSUMABLES, "Liquid Consumables");
        translationBuilder.add(ItemTagsSD.NON_HUMANOID_ARMOR, "Non Humanoid Armor");
        translationBuilder.add(ItemTagsSD.STEW_INGREDIENT, "Stew Ingredient");
        translationBuilder.add(ItemTagsSD.SWEEPING_WEAPON, "Sweeping Weapon");
        translationBuilder.add(ItemTagsSD.TENTS, "Tents");
        translationBuilder.add(ItemTagsSD.QUIVERS, "Quivers");
        translationBuilder.add(ItemTagsSD.SHIELDS, "Shields");
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        advancements(translationBuilder);
        attributes(translationBuilder);
        stats(translationBuilder);
        blocks(translationBuilder);
        gui(translationBuilder);
        commands(translationBuilder);
        enchantments(translationBuilder);
        entities(translationBuilder);
        items(translationBuilder);
        sounds(translationBuilder);
        music(translationBuilder);
        tags(translationBuilder);
    }
}
