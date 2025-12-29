package com.kr1s1s.subtlyd.data.tags;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.world.item.ItemsSD;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ItemTagsSD extends FabricTagProvider.ItemTagProvider {
    public ItemTagsSD(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    public static final TagKey<Item> TENTS = create("tents");

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        valueLookupBuilder(ItemTags.WOLF_FOOD)
                .add(ItemsSD.CALAMARI)
                .add(ItemsSD.COOKED_CALAMARI);
        valueLookupBuilder(ItemTags.CAT_FOOD)
                .add(ItemsSD.CALAMARI);
        valueLookupBuilder(TENTS)
            .addAll(ItemsSD.TENT_ITEM_LIST);
    }

    private static TagKey<Item> create(String string) {
        return TagKey.create(Registries.ITEM, SubtlyDungeons.identifier(string));
    }

    /**
     * Can be used to get a list of items by item tag. Cannot be used within data generator classes.
     * @param tag The specified tag to search.
     * @return A list of items with the specified item tag.
     */
    public static List<Item> getItems(TagKey<Item> tag) {
        Iterable<Holder<Item>> holders = BuiltInRegistries.ITEM.getTagOrEmpty(tag);
        List<Item> items = new java.util.ArrayList<>(List.of());
        for  (Holder<Item> holder : holders) {
            items.add(holder.value());
        }
        return items;
    }
}