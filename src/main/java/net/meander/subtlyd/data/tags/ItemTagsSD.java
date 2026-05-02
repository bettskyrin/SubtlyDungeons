package net.meander.subtlyd.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.meander.subtlyd.references.ItemIdsSD;
import net.meander.subtlyd.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.ItemIds;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ItemTagsSD extends FabricTagsProvider.ItemTagsProvider {
    public static final TagKey<Item> TENTS = bind("tents");
    public static final TagKey<Item> LIQUID_CONSUMABLES = bind("liquid_consumables");
    public static final TagKey<Item> NON_HUMANOID_ARMOR = bind("non_humanoid_armor");
    public static final TagKey<Item> HAS_MAGIC_LIMIT = bind("has_magic_limit");
    
    public ItemTagsSD(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(ItemTags.WOLF_FOOD)
                .add(ItemIdsSD.CALAMARI)
                .add(ItemIdsSD.COOKED_CALAMARI);
        tag(ItemTags.CAT_FOOD)
                .add(ItemIdsSD.CALAMARI);
        tag(TENTS)
            .addAll(ItemIdsSD.TENT.asList());
        tag(LIQUID_CONSUMABLES)
                .add(ItemIds.POTION)
                .add(ItemIds.MILK_BUCKET)
                .add(ItemIds.HONEY_BOTTLE)
                .add(ItemIds.BEETROOT_SOUP)
                .add(ItemIds.MUSHROOM_STEW)
                .add(ItemIds.SUSPICIOUS_STEW)
                .add(ItemIds.RABBIT_STEW)
                .add(ItemIdsSD.POTTAGE);
        tag(NON_HUMANOID_ARMOR)
                .add(ItemIds.LEATHER_HORSE_ARMOR)
                .add(ItemIds.COPPER_HORSE_ARMOR)
                .add(ItemIds.IRON_HORSE_ARMOR)
                .add(ItemIds.GOLDEN_HORSE_ARMOR)
                .add(ItemIds.DIAMOND_HORSE_ARMOR)
                .add(ItemIds.NETHERITE_HORSE_ARMOR)
                .add(ItemIds.COPPER_NAUTILUS_ARMOR)
                .add(ItemIds.IRON_NAUTILUS_ARMOR)
                .add(ItemIds.GOLDEN_NAUTILUS_ARMOR)
                .add(ItemIds.DIAMOND_NAUTILUS_ARMOR)
                .add(ItemIds.NETHERITE_NAUTILUS_ARMOR)
                .add(ItemIds.WOLF_ARMOR);
        tag(ItemTags.ARMOR_ENCHANTABLE)
                .addTag(NON_HUMANOID_ARMOR);
        tag(ItemTags.EQUIPPABLE_ENCHANTABLE)
                .addTag(NON_HUMANOID_ARMOR);
        tag(ItemTags.VANISHING_ENCHANTABLE)
                .addTag(NON_HUMANOID_ARMOR);
        tag(HAS_MAGIC_LIMIT)
                .forceAddTag(ItemTags.ARMOR_ENCHANTABLE)
                .forceAddTag(ItemTags.EQUIPPABLE_ENCHANTABLE)
                .forceAddTag(ItemTags.DURABILITY_ENCHANTABLE)
                .forceAddTag(ItemTags.WEAPON_ENCHANTABLE)
                .forceAddTag(ItemTags.CROSSBOW_ENCHANTABLE)
                .forceAddTag(ItemTags.BOW_ENCHANTABLE)
                .add(ItemIds.BOOK)
                .add(ItemIds.ENCHANTED_BOOK);
    }

    private static TagKey<Item> bind(String string) {
        return TagKey.create(Registries.ITEM, Util.identifier(string));
    }

    /**
     * Can be used to get a list of items by their item tag. Cannot be used within data generator classes.
     * @param tag The specified tag to search.
     * @return A list of items with the specified items tag.
     */
    public static List<Item> getItems(TagKey<Item> tag) {
        Iterable<Holder<Item>> holders = BuiltInRegistries.ITEM.getTagOrEmpty(tag);
        List<Item> items = new ArrayList<>(List.of());
        for  (Holder<Item> holder : holders) {
            items.add(holder.value());
        }
        return items;
    }
}