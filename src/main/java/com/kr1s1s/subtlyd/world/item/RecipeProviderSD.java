package com.kr1s1s.subtlyd.world.item;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public class RecipeProviderSD extends FabricRecipeProvider {
    public RecipeProviderSD(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) { // TODO
        return null;
    }

    @Override
    public String getName() {
        return "";
    }
}
