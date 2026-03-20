package net.meander.subtlyd.util.data.tags;

import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.item.ItemsSD;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ItemTagsSD extends FabricTagsProvider.ItemTagsProvider {
    public ItemTagsSD(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    public static final TagKey<Item> TENTS = create("tents");
    public static final TagKey<Item> LIQUID_CONSUMABLES = create("liquid_consumables");

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        valueLookupBuilder(ItemTags.WOLF_FOOD)
                .add(ItemsSD.CALAMARI)
                .add(ItemsSD.COOKED_CALAMARI);
        valueLookupBuilder(ItemTags.CAT_FOOD)
                .add(ItemsSD.CALAMARI);
        valueLookupBuilder(TENTS)
            .addAll(ItemsSD.TENT_ITEM_LIST);
        valueLookupBuilder(LIQUID_CONSUMABLES)
                .add(Items.POTION)
                .add(Items.MILK_BUCKET)
                .add(Items.HONEY_BOTTLE)
                .add(Items.BEETROOT_SOUP)
                .add(Items.MUSHROOM_STEW)
                .add(Items.SUSPICIOUS_STEW)
                .add(Items.RABBIT_STEW)
                .add(ItemsSD.POTTAGE);
    }

    private static TagKey<Item> create(String string) {
        return TagKey.create(Registries.ITEM, Util.identifier(string));
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