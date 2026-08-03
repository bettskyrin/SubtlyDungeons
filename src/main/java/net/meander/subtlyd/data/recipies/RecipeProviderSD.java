package net.meander.subtlyd.data.recipies;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.meander.subtlyd.tags.ItemTagsSD;
import net.meander.subtlyd.util.UtilSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.meander.subtlyd.world.level.block.BlocksSD;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamilies;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.data.recipes.TransmuteRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ColorCollection;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @see RecipeProvider
 * @see net.minecraft.data.recipes.packs.VanillaRecipeProvider
 */
public class RecipeProviderSD extends FabricRecipeProvider {
    public RecipeProviderSD(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        return new RecipeProvider(recipes, advancements) {
            @Override
            public void buildRecipes() {
                buildingBlocks();
                decorations();
                food();
                blasting();
                misc();
                combat();
                new BrewingProviderSD(output).buildRecipes();
            }

            private void cookRecipesSD(Item ingredient, float experience, Item result) {
                simpleCookingRecipe("smelting", SmeltingRecipe::new, 200, ingredient, result, experience);
                simpleCookingRecipe("smoking", SmokingRecipe::new, 100, ingredient, result, experience);
                simpleCookingRecipe("campfire_cooking", CampfireCookingRecipe::new, 600, ingredient, result, experience);
            }

            private void blasting() {
                oreBlasting(List.of(Items.RAW_IRON_BLOCK), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.BLOCKS, Items.IRON_BLOCK, 6.3F, 100, "iron_block");
                oreBlasting(List.of(Items.RAW_COPPER_BLOCK), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.BLOCKS, Items.COPPER_BLOCK.weathering().unaffected(), 6.3F, 100, "copper_block");
                oreBlasting(List.of(Items.RAW_GOLD_BLOCK), RecipeCategory.BUILDING_BLOCKS, CookingBookCategory.BLOCKS, Items.GOLD_BLOCK, 9.0F, 100, "gold_block");
            }

            private void tentRecipe(ItemLike tentOutput, ItemLike wool) {
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

            public void dyedQuiverRecipe(final Item dye, final Item dyedResult) {
                TransmuteRecipeBuilder.transmute(RecipeCategory.COMBAT, tag(ItemTagsSD.QUIVERS), Ingredient.of(dye), dyedResult)
                        .group("quiver_dye")
                        .unlockedBy(getHasName(dye), has(dye))
                        .save(output);
            }

            private void daggerRecipe(ItemLike daggerOutput, TagKey<Item> material) {
                shaped(RecipeCategory.COMBAT, daggerOutput)
                        .define('#', material)
                        .define('X', Items.STICK)
                        .pattern(" # ")
                        .pattern("X  ")
                        .unlockedBy(getHasName(material), has(material))
                        .save(output);
            }

            private void buildingBlocks() {
                RecipeCategory category = RecipeCategory.BUILDING_BLOCKS;

                BlockFamilies.getAllFamilies()
                        .filter(family -> {
                            boolean hasCustomBaseBlock = BuiltInRegistries.BLOCK.getKey(family.getBaseBlock()).getNamespace().equals(UtilSD.NAMESPACE);
                            boolean hasCustomVariant = family.getVariants().values().stream().anyMatch(variant -> BuiltInRegistries.BLOCK.getKey(variant).getNamespace().equals(UtilSD.NAMESPACE));

                            return hasCustomBaseBlock || hasCustomVariant;
                        }).forEach(family -> generateRecipes(family, FeatureFlagSet.of(FeatureFlags.VANILLA)));

                shaped(category, BlocksSD.SOUL_JACK_O_LANTERN)
                        .define('#', Blocks.CARVED_PUMPKIN)
                        .define('S', Blocks.SOUL_TORCH)
                        .pattern("#")
                        .pattern("S")
                        .unlockedBy(getHasName(Blocks.SOUL_TORCH), has(Blocks.SOUL_TORCH))
                        .save(output);

                nineBlockStorageRecipes(RecipeCategory.MISC, Items.CHARCOAL, category, ItemsSD.CHARCOAL_BLOCK);
                threeByThreePacker(category, Blocks.WARPED_WART_BLOCK, ItemsSD.WARPED_OVERHANG);

                grate(BlocksSD.IRON_GRATE, Blocks.IRON_BLOCK);
                stonecutterResultFromBase(category, BlocksSD.IRON_GRATE, Blocks.IRON_BLOCK, 4);

                slab(category, ItemsSD.BASALT_SLAB, Items.BASALT);
                stonecutterResultFromBase(category, BlocksSD.BASALT_SLAB, Blocks.BASALT, 2);
                shaped(category, BlocksSD.STONE_PILLAR, 2)
                        .define('#', Blocks.STONE)
                        .pattern("#")
                        .pattern("#")
                        .unlockedBy(getHasName(Blocks.STONE), has(Blocks.STONE))
                        .save(output);

                shaped(category, BlocksSD.STONE_TILES, 4)
                        .define('S', Blocks.STONE_BRICKS)
                        .pattern("SS")
                        .pattern("SS")
                        .unlockedBy(getHasName(Blocks.STONE_BRICKS), has(Blocks.STONE_BRICKS))
                        .save(output);

                stonecutterResultFromBase(category, BlocksSD.STONE_PILLAR, Blocks.STONE);
                stonecutterResultFromBase(category, BlocksSD.STONE_TILES, Blocks.STONE);
                stonecutterResultFromBase(category, BlocksSD.STONE_TILES, Blocks.STONE_BRICKS);
            }

            private void decorations() {
                RecipeCategory category = RecipeCategory.DECORATIONS;

                shaped(category, ItemsSD.UNLIT_CAMPFIRE)
                        .define('#', Items.STICK)
                        .define('X', ItemTags.LOGS)
                        .pattern(" # ")
                        .pattern("#X#")
                        .pattern("XXX")
                        .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                        .save(output);
            }

            private void food() {
                RecipeCategory category = RecipeCategory.FOOD;

                shapeless(category, ItemsSD.APPLE_PIE)
                        .group("apple_pie")
                        .requires(Items.APPLE)
                        .requires(Items.SUGAR)
                        .requires(ItemTags.EGGS)
                        .unlockedBy(getHasName(Items.APPLE), has(Items.APPLE))
                        .unlockedBy(getHasName(Items.GOLDEN_APPLE), has(Items.GOLDEN_APPLE))
                        .unlockedBy(getHasName(Items.ENCHANTED_GOLDEN_APPLE), has(Items.ENCHANTED_GOLDEN_APPLE))
                        .save(output);

                shapeless(category, ItemsSD.POTTAGE)
                        .requires(Items.BOWL)
                        .requires(Items.CARROT)
                        .requires(Items.WHEAT)
                        .requires(ItemTags.MUSHROOMS)
                        .unlockedBy(getHasName(Items.CARROT), has(Items.CARROT))
                        .unlockedBy(getHasName(Items.WHEAT), has(Items.WHEAT))
                        .unlockedBy(getHasName(Items.BROWN_MUSHROOM), has(Items.BROWN_MUSHROOM))
                        .unlockedBy(getHasName(Items.BROWN_MUSHROOM), has(Items.BROWN_MUSHROOM))
                        .unlockedBy(getHasName(Items.BOWL), has(Items.BOWL))
                        .save(output);

                cookRecipesSD(ItemsSD.CALAMARI, 0.35F, ItemsSD.COOKED_CALAMARI);
            }

            private void misc() {
                RecipeCategory category = RecipeCategory.MISC;

                ColorCollection.zipApply(ItemsSD.TENT, Blocks.WOOL, this::tentRecipe);
                colorItemWithDye(Items.DYE.asList(), ItemsSD.TENT.asList(), "tent_dye", category);
                oneToOneConversionRecipe(Items.DYE.purple(), BlocksSD.PERSE_WILDFLOWERS, "purple_dye");
            }

            private void combat() {
                RecipeCategory category = RecipeCategory.DECORATIONS;

                shapeless(category, ItemsSD.BLAST_FUNGUS, 2)
                        .requires(Items.WARPED_FUNGUS)
                        .requires(Items.CRIMSON_FUNGUS)
                        .unlockedBy(getHasName(Items.CRIMSON_FUNGUS), has(Items.CRIMSON_FUNGUS))
                        .unlockedBy(getHasName(Items.WARPED_FUNGUS), has(Items.WARPED_FUNGUS))
                        .save(output);

                shaped(category, ItemsSD.QUIVER)
                        .define('#', Items.STRING)
                        .define('X', Items.LEATHER)
                        .pattern("X X")
                        .pattern("X#X")
                        .pattern("XXX")
                        .unlockedBy(getHasName(Items.ARROW), has(Items.ARROW))
                        .save(output);
                ColorCollection.zipApply(Items.DYE, ItemsSD.DYED_QUIVER, this::dyedQuiverRecipe);

                daggerRecipe(ItemsSD.WOODEN_DAGGER, ItemTags.WOODEN_TOOL_MATERIALS);
                daggerRecipe(ItemsSD.STONE_DAGGER, ItemTags.STONE_TOOL_MATERIALS);
                daggerRecipe(ItemsSD.COPPER_DAGGER, ItemTags.COPPER_TOOL_MATERIALS);
                daggerRecipe(ItemsSD.IRON_DAGGER, ItemTags.IRON_TOOL_MATERIALS);
                daggerRecipe(ItemsSD.GOLDEN_DAGGER, ItemTags.GOLD_TOOL_MATERIALS);
                daggerRecipe(ItemsSD.DIAMOND_DAGGER, ItemTags.DIAMOND_TOOL_MATERIALS);
                netheriteSmithing(ItemsSD.DIAMOND_DAGGER, category, ItemsSD.NETHERITE_DAGGER);
                SpecialRecipeBuilder.special(() -> new ShieldDecorationRecipe(tag(ItemTags.BANNERS), Ingredient.of(ItemsSD.HEAVY_SHIELD), new ItemStackTemplate(ItemsSD.HEAVY_SHIELD)))
                        .save(output, "heavy_shield_decoration");
            }

            private String getHasName(TagKey<Item> material) {
                return "has_" + material.location().getPath();
            }
        };
    }

    @Override public @NotNull String getName() {
        return "Subtly Dungeons Recipe Provider";
    }
}
