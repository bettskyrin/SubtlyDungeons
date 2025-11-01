package com.kr1s1s.subtlyd.data;

import com.kr1s1s.subtlyd.world.block.BlocksSD;
import com.kr1s1s.subtlyd.world.item.ItemsSD;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;


public class RecipeProviderSD extends FabricRecipeProvider {
    public RecipeProviderSD(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                for (int i = 0; i <= 15; i++) {
                    tentBuilderFromWool(ItemsSD.TENT_ITEM_LIST.get(i), ItemsSD.WOOL_ITEM_LIST.get(i));
                }

                colorItemWithDye(ItemsSD.DYE_ITEM_LIST, ItemsSD.TENT_ITEM_LIST, "tent_dye", RecipeCategory.MISC);
                this.shapeless(RecipeCategory.FOOD, ItemsSD.APPLE_PIE)
                        .group("apple_pie")
                        .requires(Items.APPLE)
                        .requires(Items.SUGAR)
                        .requires(ItemTags.EGGS)
                        .unlockedBy(getHasName(Items.APPLE), has(Items.APPLE))
                        .unlockedBy(getHasName(Items.GOLDEN_APPLE), has(Items.GOLDEN_APPLE))
                        .unlockedBy(getHasName(Items.ENCHANTED_GOLDEN_APPLE), has(Items.ENCHANTED_GOLDEN_APPLE))
                        .save(exporter);

                this.shaped(RecipeCategory.DECORATIONS, ItemsSD.UNLIT_CAMPFIRE)
                        .define('#', Items.STICK)
                        .define('X', ItemTags.LOGS)
                        .pattern(" # ")
                        .pattern("#X#")
                        .pattern("XXX")
                        .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                        .save(exporter);

                this.shapeless(RecipeCategory.FOOD, ItemsSD.POTTAGE)
                        .requires(Items.BOWL)
                        .requires(Items.CARROT)
                        .requires(Items.WHEAT)
                        .requires(Items.BROWN_MUSHROOM)
                        .unlockedBy(getHasName(Items.CARROT), has(Items.CARROT))
                        .unlockedBy(getHasName(Items.WHEAT), has(Items.WHEAT))
                        .unlockedBy(getHasName(Items.BROWN_MUSHROOM), has(Items.BROWN_MUSHROOM))
                        .unlockedBy(getHasName(Items.BOWL), has(Items.BOWL))
                        .save(exporter);

                this.shaped(RecipeCategory.BUILDING_BLOCKS, BlocksSD.STONE_PILLAR, 2)
                        .define('#', Blocks.STONE)
                        .pattern("#")
                        .pattern("#")
                        .unlockedBy(getHasName(BlocksSD.CHISELED_STONE), has(BlocksSD.CHISELED_STONE))
                        .unlockedBy(getHasName(Blocks.STONE), has(Blocks.STONE))
                        .unlockedBy(getHasName(BlocksSD.STONE_PILLAR), has(BlocksSD.STONE_PILLAR))
                        .save(exporter);

                this.shaped(RecipeCategory.BUILDING_BLOCKS, BlocksSD.STONE_TILES, 4)
                        .define('S', Blocks.STONE_BRICKS)
                        .pattern("SS")
                        .pattern("SS")
                        .unlockedBy(getHasName(Blocks.STONE_BRICKS), has(Blocks.STONE_BRICKS))
                        .save(exporter);

                this.nineBlockStorageRecipes(RecipeCategory.MISC, Items.CHARCOAL, RecipeCategory.BUILDING_BLOCKS, ItemsSD.CHARCOAL_BLOCK);
                this.twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, ItemsSD.SNOW_BRICKS, Items.SNOW_BLOCK);

                this.stairBuilder(ItemsSD.SNOW_BRICK_STAIRS, Ingredient.of(ItemsSD.SNOW_BRICKS)).unlockedBy(getHasName(ItemsSD.SNOW_BRICKS), this.has(ItemsSD.SNOW_BRICKS)).save(exporter);
                this.slab(RecipeCategory.BUILDING_BLOCKS, ItemsSD.SNOW_BRICK_SLAB, ItemsSD.SNOW_BRICKS);
                this.wall(RecipeCategory.BUILDING_BLOCKS, ItemsSD.SNOW_BRICK_WALL, ItemsSD.SNOW_BRICKS);

                this.chiseled(RecipeCategory.BUILDING_BLOCKS, ItemsSD.CHISELED_DRIPSTONE, Items.DRIPSTONE_BLOCK);
                this.polished(RecipeCategory.BUILDING_BLOCKS, ItemsSD.POLISHED_DRIPSTONE, Items.DRIPSTONE_BLOCK);
                this.stairBuilder(ItemsSD.POLISHED_DRIPSTONE_STAIRS, Ingredient.of(ItemsSD.POLISHED_DRIPSTONE)).unlockedBy(getHasName(ItemsSD.POLISHED_DRIPSTONE), this.has(ItemsSD.POLISHED_DRIPSTONE)).save(exporter);
                this.slab(RecipeCategory.BUILDING_BLOCKS, ItemsSD.POLISHED_DRIPSTONE_SLAB, ItemsSD.POLISHED_DRIPSTONE);
                this.wall(RecipeCategory.BUILDING_BLOCKS, ItemsSD.POLISHED_DRIPSTONE_WALL, ItemsSD.POLISHED_DRIPSTONE);

                this.chiseled(RecipeCategory.BUILDING_BLOCKS, ItemsSD.CHISELED_STONE, Items.STONE);
                this.polished(RecipeCategory.BUILDING_BLOCKS, ItemsSD.POLISHED_STONE, Items.STONE);
                this.stairBuilder(ItemsSD.POLISHED_STONE_STAIRS, Ingredient.of(ItemsSD.POLISHED_STONE)).unlockedBy(getHasName(ItemsSD.POLISHED_STONE), this.has(ItemsSD.POLISHED_STONE)).save(exporter);
                this.slab(RecipeCategory.BUILDING_BLOCKS, ItemsSD.POLISHED_STONE_SLAB, ItemsSD.POLISHED_STONE);
                this.wall(RecipeCategory.BUILDING_BLOCKS, ItemsSD.POLISHED_STONE_WALL, ItemsSD.POLISHED_STONE);

                this.stairBuilder(ItemsSD.STONE_TILE_STAIRS, Ingredient.of(ItemsSD.STONE_TILES)).unlockedBy(getHasName(ItemsSD.STONE_TILES), this.has(ItemsSD.STONE_TILES)).save(exporter);
                this.slab(RecipeCategory.BUILDING_BLOCKS, ItemsSD.STONE_TILE_SLAB, ItemsSD.STONE_TILES);
                this.wall(RecipeCategory.BUILDING_BLOCKS, ItemsSD.STONE_TILE_WALL, ItemsSD.STONE_TILES);


                this.grate(BlocksSD.IRON_GRATE, Blocks.IRON_BLOCK);

                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.SNOW_BRICKS, Blocks.SNOW_BLOCK);     // Snow Bricks
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.SNOW_BRICK_STAIRS, Blocks.SNOW_BLOCK);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.SNOW_BRICK_SLAB, Blocks.SNOW_BLOCK, 2);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.SNOW_BRICK_WALL, Blocks.SNOW_BLOCK);

                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.IRON_GRATE, Blocks.IRON_BLOCK, 4);      // Iron Grate

                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.CHISELED_DRIPSTONE, Blocks.DRIPSTONE_BLOCK);     // Dripstone
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_DRIPSTONE, Blocks.DRIPSTONE_BLOCK);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_DRIPSTONE_STAIRS, Blocks.DRIPSTONE_BLOCK);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_DRIPSTONE_SLAB, Blocks.DRIPSTONE_BLOCK, 2);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_DRIPSTONE_WALL, Blocks.DRIPSTONE_BLOCK);

                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_DRIPSTONE_STAIRS, BlocksSD.POLISHED_DRIPSTONE);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_DRIPSTONE_SLAB, BlocksSD.POLISHED_DRIPSTONE, 2);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_DRIPSTONE_WALL, BlocksSD.POLISHED_DRIPSTONE);

                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.STONE_PILLAR, Blocks.STONE);       // Stone
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.CHISELED_STONE, Blocks.STONE);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_STONE, Blocks.STONE);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_STONE_STAIRS, Blocks.STONE);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_STONE_SLAB, Blocks.STONE, 2);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_STONE_WALL, Blocks.STONE);

                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.STONE_BRICKS, BlocksSD.POLISHED_STONE);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.STONE_BRICK_STAIRS, BlocksSD.POLISHED_STONE);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.STONE_BRICK_SLAB, BlocksSD.POLISHED_STONE, 2);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, Blocks.STONE_BRICK_WALL, BlocksSD.POLISHED_STONE);

                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_STONE_STAIRS, BlocksSD.POLISHED_STONE);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_STONE_SLAB, BlocksSD.POLISHED_STONE, 2);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.POLISHED_STONE_WALL, BlocksSD.POLISHED_STONE);

                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.STONE_TILES, Blocks.STONE);      // Stone Tiles
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.STONE_TILE_STAIRS, Blocks.STONE);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.STONE_TILE_SLAB, Blocks.STONE, 2);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.STONE_TILE_WALL, Blocks.STONE);

                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.STONE_TILES, BlocksSD.POLISHED_STONE);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.STONE_TILE_STAIRS, BlocksSD.POLISHED_STONE);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.STONE_TILE_SLAB, BlocksSD.POLISHED_STONE, 2);
                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.STONE_TILE_WALL, BlocksSD.POLISHED_STONE);

                this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, BlocksSD.STONE_TILES, Blocks.STONE_BRICKS);

                this.cookRecipesSD("smoking", RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new, 100);
                this.cookRecipesSD("campfire_cooking", RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new, 600);
            }

            public <T extends AbstractCookingRecipe> void cookRecipesSD(String string, RecipeSerializer<T> recipeSerializer, AbstractCookingRecipe.Factory<T> factory, int i) {
                this.simpleCookingRecipe(string, recipeSerializer, factory, i, ItemsSD.CALAMARI, ItemsSD.COOKED_CALAMARI, 0.35F);
            }

            public void tentBuilderFromWool(ItemLike tentOutput, ItemLike wool) {
                this.shaped(RecipeCategory.MISC, tentOutput)
                        .group("tent_wool")
                        .define('#', wool)
                        .define('X', Items.STICK)
                        .pattern(" # ")
                        .pattern("#X#")
                        .pattern("#X#")
                        .unlockedBy(has(wool.asItem()).toString(), has(wool.asItem()))
                        .save(exporter);
            }
        };
    }

    @Override
    public @NotNull String getName() {
        return "Subtly Dungeons Recipe Provider";
    }
}
