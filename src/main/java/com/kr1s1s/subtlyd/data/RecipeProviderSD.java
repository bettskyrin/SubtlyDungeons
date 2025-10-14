package com.kr1s1s.subtlyd.data;

import com.kr1s1s.subtlyd.world.item.ItemsSD;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.ItemLike;
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
                    tentBuilderFromWool(ItemsSD.TENT_ITEM_FAMILY.get(i), ItemsSD.WOOL_ITEM_FAMILY.get(i));
                }

                colorItemWithDye(ItemsSD.DYE_ITEM_FAMILY, ItemsSD.TENT_ITEM_FAMILY, "tent_dye", RecipeCategory.MISC);
                this.shapeless(RecipeCategory.FOOD, ItemsSD.APPLE_PIE)
                        .group("apple_pie")
                        .requires(Items.APPLE)
                        .requires(Items.SUGAR)
                        .requires(ItemTags.EGGS)
                        .unlockedBy(has(Items.APPLE).toString(), has(Items.APPLE))
                        .unlockedBy(has(Items.GOLDEN_APPLE).toString(), has(Items.GOLDEN_APPLE))
                        .unlockedBy(has(Items.ENCHANTED_GOLDEN_APPLE).toString(), has(Items.ENCHANTED_GOLDEN_APPLE))
                        .save(exporter);

                this.shaped(RecipeCategory.DECORATIONS, ItemsSD.UNLIT_CAMPFIRE)
                        .group("unlit_campfire")
                        .define('#', Items.STICK)
                        .define('X', ItemTags.LOGS)
                        .pattern(" # ")
                        .pattern("#X#")
                        .pattern("XXX")
                        .unlockedBy(has(Items.STICK).toString(), has(Items.STICK))
                        .save(exporter);

                this.shapeless(RecipeCategory.FOOD, ItemsSD.POTTAGE)
                        .group("pottage")
                        .requires(Items.BOWL)
                        .requires(Items.CARROT)
                        .requires(Items.WHEAT)
                        .requires(Items.BROWN_MUSHROOM)
                        .unlockedBy(has(Items.CARROT).toString(), has(Items.CARROT))
                        .unlockedBy(has(Items.WHEAT).toString(), has(Items.WHEAT))
                        .unlockedBy(has(Items.BROWN_MUSHROOM).toString(), has(Items.BROWN_MUSHROOM))
                        .unlockedBy(has(Items.BOWL).toString(), has(Items.BOWL))
                        .save(exporter);

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
