package net.meander.subtlyd.data.recipies;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ColorCollection;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;


public class RecipeProviderSD extends FabricRecipeProvider {
    public RecipeProviderSD(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider registries, @NonNull RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override public void buildRecipes() {
                /* BUILDING BLOCKS */
                shaped(RecipeCategory.BUILDING_BLOCKS, BlocksSD.STONE_PILLAR, 2)
                   .define('#', Blocks.STONE)
                   .pattern("#")
                   .pattern("#")
                   .unlockedBy(getHasName(Blocks.STONE), has(Blocks.STONE))
                   .unlockedBy(getHasName(BlocksSD.STONE_PILLAR), has(BlocksSD.STONE_PILLAR))
                   .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS, BlocksSD.STONE_TILES, 4)
                        .define('S', Blocks.STONE_BRICKS)
                        .pattern("SS")
                        .pattern("SS")
                        .unlockedBy(getHasName(Blocks.STONE_BRICKS), has(Blocks.STONE_BRICKS))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS, BlocksSD.SOUL_JACK_O_LANTERN)
                        .define('A', Blocks.CARVED_PUMPKIN)
                        .define('B', Blocks.SOUL_TORCH)
                        .pattern("A")
                        .pattern("B")
                        .unlockedBy(getHasName(Blocks.SOUL_TORCH), has(Blocks.SOUL_TORCH))
                        .save(output);

                nineBlockStorageRecipes(RecipeCategory.MISC, Items.CHARCOAL, RecipeCategory.BUILDING_BLOCKS, ItemsSD.CHARCOAL_BLOCK);
                twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, ItemsSD.SNOW_BRICKS, Items.SNOW_BLOCK);

                stairBuilder(ItemsSD.SNOW_BRICK_STAIRS, Ingredient.of(ItemsSD.SNOW_BRICKS)).unlockedBy(getHasName(ItemsSD.SNOW_BRICKS), has(ItemsSD.SNOW_BRICKS)).save(output);
                slab(RecipeCategory.BUILDING_BLOCKS, ItemsSD.SNOW_BRICK_SLAB, ItemsSD.SNOW_BRICKS);
                wall(RecipeCategory.BUILDING_BLOCKS, ItemsSD.SNOW_BRICK_WALL, ItemsSD.SNOW_BRICKS);

                chiseled(RecipeCategory.BUILDING_BLOCKS, ItemsSD.CHISELED_POLISHED_DRIPSTONE, ItemsSD.POLISHED_DRIPSTONE_SLAB);
                polished(RecipeCategory.BUILDING_BLOCKS, ItemsSD.POLISHED_DRIPSTONE, Items.DRIPSTONE_BLOCK);
                stairBuilder(ItemsSD.POLISHED_DRIPSTONE_STAIRS, Ingredient.of(ItemsSD.POLISHED_DRIPSTONE)).unlockedBy(getHasName(ItemsSD.POLISHED_DRIPSTONE), has(ItemsSD.POLISHED_DRIPSTONE)).save(output);
                slab(RecipeCategory.BUILDING_BLOCKS, ItemsSD.POLISHED_DRIPSTONE_SLAB, ItemsSD.POLISHED_DRIPSTONE);
                wall(RecipeCategory.BUILDING_BLOCKS, ItemsSD.POLISHED_DRIPSTONE_WALL, ItemsSD.POLISHED_DRIPSTONE);

                stairBuilder(ItemsSD.STONE_TILE_STAIRS, Ingredient.of(ItemsSD.STONE_TILES)).unlockedBy(getHasName(ItemsSD.STONE_TILES), has(ItemsSD.STONE_TILES)).save(output);
                slab(RecipeCategory.BUILDING_BLOCKS, ItemsSD.STONE_TILE_SLAB, ItemsSD.STONE_TILES);
                wall(RecipeCategory.BUILDING_BLOCKS, ItemsSD.STONE_TILE_WALL, ItemsSD.STONE_TILES);

                grate(BlocksSD.IRON_GRATE, Blocks.IRON_BLOCK);

                threeByThreePacker(RecipeCategory.BUILDING_BLOCKS, Blocks.WARPED_WART_BLOCK, ItemsSD.WARPED_OVERHANG);

                slab(RecipeCategory.BUILDING_BLOCKS, ItemsSD.BASALT_SLAB, Items.BASALT);

                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.SNOW_BRICKS, Blocks.SNOW_BLOCK);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.SNOW_BRICK_STAIRS, Blocks.SNOW_BLOCK);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.SNOW_BRICK_SLAB, Blocks.SNOW_BLOCK, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.SNOW_BRICK_WALL, Blocks.SNOW_BLOCK);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.IRON_GRATE, Blocks.IRON_BLOCK, 4);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.CHISELED_POLISHED_DRIPSTONE, Blocks.DRIPSTONE_BLOCK);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_DRIPSTONE, Blocks.DRIPSTONE_BLOCK);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_DRIPSTONE_STAIRS, Blocks.DRIPSTONE_BLOCK);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_DRIPSTONE_SLAB, Blocks.DRIPSTONE_BLOCK, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_DRIPSTONE_WALL, Blocks.DRIPSTONE_BLOCK);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_DRIPSTONE_STAIRS, BlocksSD.POLISHED_DRIPSTONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_DRIPSTONE_SLAB, BlocksSD.POLISHED_DRIPSTONE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_DRIPSTONE_WALL, BlocksSD.POLISHED_DRIPSTONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.STONE_PILLAR, Blocks.STONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.STONE_TILES, Blocks.STONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.STONE_TILES, Blocks.STONE_BRICKS);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.STONE_TILE_STAIRS, Blocks.STONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.STONE_TILE_SLAB, Blocks.STONE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.STONE_TILE_WALL, Blocks.STONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.BASALT_SLAB, Blocks.BASALT, 2);

                /* DECORATIONS */
                shaped(RecipeCategory.DECORATIONS, ItemsSD.UNLIT_CAMPFIRE)
                        .define('#', Items.STICK)
                        .define('X', ItemTags.LOGS)
                        .pattern(" # ")
                        .pattern("#X#")
                        .pattern("XXX")
                        .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                        .save(output);

                /* FOOD */
                shapeless(RecipeCategory.FOOD, ItemsSD.APPLE_PIE)
                        .group("apple_pie")
                        .requires(Items.APPLE)
                        .requires(Items.SUGAR)
                        .requires(ItemTags.EGGS)
                        .unlockedBy(getHasName(Items.APPLE), has(Items.APPLE))
                        .unlockedBy(getHasName(Items.GOLDEN_APPLE), has(Items.GOLDEN_APPLE))
                        .unlockedBy(getHasName(Items.ENCHANTED_GOLDEN_APPLE), has(Items.ENCHANTED_GOLDEN_APPLE))
                        .save(output);

                shapeless(RecipeCategory.FOOD, ItemsSD.POTTAGE)
                        .requires(Items.BOWL)
                        .requires(Items.CARROT)
                        .requires(Items.WHEAT)
                        .requires(Items.BROWN_MUSHROOM)
                        .unlockedBy(getHasName(Items.CARROT), has(Items.CARROT))
                        .unlockedBy(getHasName(Items.WHEAT), has(Items.WHEAT))
                        .unlockedBy(getHasName(Items.BROWN_MUSHROOM), has(Items.BROWN_MUSHROOM))
                        .unlockedBy(getHasName(Items.BOWL), has(Items.BOWL))
                        .save(output);

                cookRecipesSD(ItemsSD.CALAMARI, 0.35F, ItemsSD.COOKED_CALAMARI);

                /* MISC */
                ColorCollection.zipApply(ItemsSD.TENT, Blocks.WOOL, this::tent);
                colorItemWithDye(Items.DYE.asList(), ItemsSD.TENT.asList(), "tent_dye", RecipeCategory.MISC);
                oneToOneConversionRecipe(Items.DYE.purple(), BlocksSD.PERSE_WILDFLOWERS, "purple_dye");

                /* COMBAT */
                shapeless(RecipeCategory.COMBAT, ItemsSD.BLAST_FUNGUS, 2)
                        .requires(Items.WARPED_FUNGUS)
                        .requires(Items.CRIMSON_FUNGUS)
                        .unlockedBy(getHasName(Items.CRIMSON_FUNGUS), has(Items.CRIMSON_FUNGUS))
                        .unlockedBy(getHasName(Items.WARPED_FUNGUS), has(Items.WARPED_FUNGUS))
                        .save(output);

                dagger(ItemsSD.WOODEN_DAGGER, ItemTags.WOODEN_TOOL_MATERIALS);
                dagger(ItemsSD.STONE_DAGGER, ItemTags.STONE_TOOL_MATERIALS);
                dagger(ItemsSD.COPPER_DAGGER, ItemTags.COPPER_TOOL_MATERIALS);
                dagger(ItemsSD.IRON_DAGGER, ItemTags.IRON_TOOL_MATERIALS);
                dagger(ItemsSD.GOLDEN_DAGGER, ItemTags.GOLD_TOOL_MATERIALS);
                dagger(ItemsSD.DIAMOND_DAGGER, ItemTags.DIAMOND_TOOL_MATERIALS);
                netheriteSmithing(ItemsSD.DIAMOND_DAGGER, RecipeCategory.COMBAT, ItemsSD.NETHERITE_DAGGER);
            }

            private String getHasName(TagKey<Item> material) {
                return "has_" + material.location().getPath();
            }

            private void cookRecipesSD(Item ingredient, float experience, Item result) {
                simpleCookingRecipe("smelting", SmeltingRecipe::new, 200, ingredient, result, experience);
                simpleCookingRecipe("smoking", SmokingRecipe::new, 100, ingredient, result, experience);
                simpleCookingRecipe("campfire_cooking", CampfireCookingRecipe::new, 600, ingredient, result, experience);
            }

            private void tent(ItemLike tentOutput, ItemLike wool) {
                shaped(RecipeCategory.MISC, tentOutput)
                        .group("tent_wool")
                        .define('#', wool)
                        .define('X', Items.STICK)
                        .pattern(" # ")
                        .pattern("#X#")
                        .pattern("#X#")
                        .unlockedBy(getHasName(wool.asItem()), has(wool.asItem()))
                        .save(output);
            }

            private void dagger(ItemLike daggerOutput, TagKey<Item> material) {
                shaped(RecipeCategory.COMBAT, daggerOutput)
                        .group("dagger")
                        .define('#', material)
                        .define('X', Items.STICK)
                        .pattern(" # ")
                        .pattern("X  ")
                        .unlockedBy(getHasName(material), has(material))
                        .save(output);
            }
        };
    }

    @Override public @NotNull String getName() {
        return "Subtly Dungeons Recipe Provider";
    }
}
