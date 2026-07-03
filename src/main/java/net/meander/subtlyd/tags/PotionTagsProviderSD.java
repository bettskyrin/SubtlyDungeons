package net.meander.subtlyd.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.meander.subtlyd.world.item.alchemy.PotionIdsSD;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionIds;

import java.util.concurrent.CompletableFuture;

/**
 * @see net.minecraft.data.tags.PotionTagsProvider
 */
public class PotionTagsProviderSD extends FabricTagsProvider<Potion> {
    public PotionTagsProviderSD(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, Registries.POTION, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(PotionTagsSD.CONICAL)
                .add(PotionIds.STRENGTH)
                .add(PotionIds.LONG_STRENGTH)
                .add(PotionIds.STRONG_STRENGTH)
                .add(PotionIds.WEAKNESS)
                .add(PotionIds.LONG_WEAKNESS)
                .add(PotionIds.SLOW_FALLING)
                .add(PotionIds.LONG_SLOW_FALLING)
                .add(PotionIds.WIND_CHARGED)
                .add(PotionIdsSD.DECAY);
        tag(PotionTagsSD.SPHERICAL)
                .add(PotionIds.WATER_BREATHING)
                .add(PotionIds.LONG_WATER_BREATHING)
                .add(PotionIds.OOZING)
                .add(PotionIds.TURTLE_MASTER)
                .add(PotionIds.LONG_TURTLE_MASTER)
                .add(PotionIds.STRONG_TURTLE_MASTER)
                .add(PotionIds.INFESTED);
        tag(PotionTagsSD.VIAL)
                .add(PotionIds.SWIFTNESS)
                .add(PotionIds.STRONG_SWIFTNESS)
                .add(PotionIds.LONG_SWIFTNESS)
                .add(PotionIds.SLOWNESS)
                .add(PotionIds.STRONG_SLOWNESS)
                .add(PotionIds.LONG_SLOWNESS)
                .add(PotionIds.LEAPING)
                .add(PotionIds.LONG_LEAPING)
                .add(PotionIds.STRONG_LEAPING)
                .add(PotionIds.WEAVING);
    }
}
