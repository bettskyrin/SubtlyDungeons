package net.meander.subtlyd.data.recipies;

import net.meander.subtlyd.world.item.alchemy.PotionsSD;
import net.minecraft.data.recipes.BrewingProvider;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;

public class BrewingProviderSD extends BrewingProvider {
    public BrewingProviderSD(RecipeOutput output) {
        super(output);
    }

    @Override
    protected void addContainers() {
        addContainer(Items.POTION);
        addContainer(Items.SPLASH_POTION);
        addContainer(Items.LINGERING_POTION);
    }

    @Override
    protected void addContainerTransformations() {}

    @Override
    protected void buildMixes() {
        buildStartMix(Items.WITHER_SKELETON_SKULL, PotionsSD.DECAY);
    }
}
