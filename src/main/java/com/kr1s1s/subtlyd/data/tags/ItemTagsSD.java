package com.kr1s1s.subtlyd.data.tags;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.world.item.ItemsSD;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class ItemTagsSD extends FabricTagProvider.ItemTagProvider {
    public ItemTagsSD(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    public static final TagKey<Item> SNOW_BRICKS = create("snow_bricks");
    public static final TagKey<Item> TENTS = create("tents");

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        valueLookupBuilder(net.minecraft.tags.ItemTags.WOLF_FOOD)
                .add(ItemsSD.CALAMARI)
                .add(ItemsSD.COOKED_CALAMARI);
        valueLookupBuilder(net.minecraft.tags.ItemTags.CAT_FOOD)
                .add(ItemsSD.CALAMARI);
        valueLookupBuilder(SNOW_BRICKS)
                .add(ItemsSD.SNOW_BRICKS)
                .add(ItemsSD.SNOW_BRICK_STAIRS)
                .add(ItemsSD.SNOW_BRICK_SLAB);
        valueLookupBuilder(TENTS)
            .addAll(ItemsSD.TENT_ITEM_FAMILY);
    }

    private static TagKey<Item> create(String string) {
        return TagKey.create(Registries.ITEM, SubtlyDungeons.resourceLocation(string));
    }
}