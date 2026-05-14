package net.meander.subtlyd.data.tags;

import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.item.alchemy.PotionIdsSD;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionIds;

import java.util.concurrent.CompletableFuture;

public class PotionTagsSD extends TagsProvider<Potion> {
    public static final TagKey<Potion> CONICAL = create("conical");
    public static final TagKey<Potion> SPHERICAL = create("spherical");
    public static final TagKey<Potion> VIAL = create("vial");

    public PotionTagsSD(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.POTION, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(CONICAL)
                .add(PotionIds.STRENGTH)
                .add(PotionIds.LONG_STRENGTH)
                .add(PotionIds.STRONG_STRENGTH)
                .add(PotionIds.WEAKNESS)
                .add(PotionIds.LONG_WEAKNESS)
                .add(PotionIds.SLOW_FALLING)
                .add(PotionIds.LONG_SLOW_FALLING)
                .add(PotionIds.WIND_CHARGED)
                .add(PotionIdsSD.DECAY);
        tag(SPHERICAL)
                .add(PotionIds.WATER_BREATHING)
                .add(PotionIds.LONG_WATER_BREATHING)
                .add(PotionIds.OOZING)
                .add(PotionIds.TURTLE_MASTER)
                .add(PotionIds.LONG_TURTLE_MASTER)
                .add(PotionIds.STRONG_TURTLE_MASTER)
                .add(PotionIds.INFESTED);
        tag(VIAL)
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

    private static TagKey<Potion> create(String name) {
        return TagKey.create(Registries.POTION, Util.identifier(name));
    }
}
