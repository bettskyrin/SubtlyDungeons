package net.meander.subtlyd.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ItemTagsSD extends FabricTagsProvider.ItemTagsProvider {
    public ItemTagsSD(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    public static final TagKey<Item> TENTS = bind("tents");
    public static final TagKey<Item> LIQUID_CONSUMABLES = bind("liquid_consumables");
    public static final TagKey<Item> NON_HUMANOID_ARMOR = bind("non_humanoid_armor");
    public static final TagKey<Item> HAS_MAGIC_LIMIT = bind("has_magic_limit");

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        valueLookupBuilder(ItemTags.WOLF_FOOD)
                .add(ItemsSD.CALAMARI)
                .add(ItemsSD.COOKED_CALAMARI);
        valueLookupBuilder(ItemTags.CAT_FOOD)
                .add(ItemsSD.CALAMARI);
        valueLookupBuilder(TENTS)
            .addAll(ItemsSD.TENT.asList());
        valueLookupBuilder(LIQUID_CONSUMABLES)
                .add(Items.POTION)
                .add(Items.MILK_BUCKET)
                .add(Items.HONEY_BOTTLE)
                .add(Items.BEETROOT_SOUP)
                .add(Items.MUSHROOM_STEW)
                .add(Items.SUSPICIOUS_STEW)
                .add(Items.RABBIT_STEW)
                .add(ItemsSD.POTTAGE);
        valueLookupBuilder(NON_HUMANOID_ARMOR)
                .add(Items.LEATHER_HORSE_ARMOR)
                .add(Items.COPPER_HORSE_ARMOR)
                .add(Items.IRON_HORSE_ARMOR)
                .add(Items.GOLDEN_HORSE_ARMOR)
                .add(Items.DIAMOND_HORSE_ARMOR)
                .add(Items.NETHERITE_HORSE_ARMOR)
                .add(Items.COPPER_NAUTILUS_ARMOR)
                .add(Items.IRON_NAUTILUS_ARMOR)
                .add(Items.GOLDEN_NAUTILUS_ARMOR)
                .add(Items.DIAMOND_NAUTILUS_ARMOR)
                .add(Items.NETHERITE_NAUTILUS_ARMOR)
                .add(Items.WOLF_ARMOR);
        valueLookupBuilder(ItemTags.ARMOR_ENCHANTABLE)
                .addTag(NON_HUMANOID_ARMOR);
        valueLookupBuilder(ItemTags.EQUIPPABLE_ENCHANTABLE)
                .addTag(NON_HUMANOID_ARMOR);
        valueLookupBuilder(ItemTags.VANISHING_ENCHANTABLE)
                .addTag(NON_HUMANOID_ARMOR);
        valueLookupBuilder(HAS_MAGIC_LIMIT)
                .forceAddTag(ItemTags.ARMOR_ENCHANTABLE)
                .forceAddTag(ItemTags.EQUIPPABLE_ENCHANTABLE)
                .forceAddTag(ItemTags.DURABILITY_ENCHANTABLE)
                .forceAddTag(ItemTags.WEAPON_ENCHANTABLE)
                .forceAddTag(ItemTags.CROSSBOW_ENCHANTABLE)
                .forceAddTag(ItemTags.BOW_ENCHANTABLE);
    }

    private static TagKey<Item> bind(String string) {
        return TagKey.create(Registries.ITEM, Util.identifier(string));
    }
}