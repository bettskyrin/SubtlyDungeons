package com.kr1s1s.subtlyd.data;

import com.kr1s1s.subtlyd.world.item.ItemsSD;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
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
            }

            public void tentBuilderFromWool(ItemLike tentOutput, ItemLike wool) {
                this.shaped(RecipeCategory.MISC, tentOutput)
                        .group("tent_wool")
                        .define('#', wool)
                        .define('X', Items.STICK)
                        .pattern(" # ")
                        .pattern("#X#")
                        .pattern("#X#")
                        .unlockedBy("has_" + wool.toString(), has(wool.asItem()))
                        .save(exporter);
            }
        };
    }

    @Override
    public @NotNull String getName() {
        return "Subtly Dungeons Recipe Provider";
    }
}
