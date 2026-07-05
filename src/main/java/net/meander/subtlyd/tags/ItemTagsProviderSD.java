package net.meander.subtlyd.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.meander.subtlyd.references.BlockItemIdsSD;
import net.meander.subtlyd.references.ItemIdsSD;
import net.minecraft.core.HolderLookup;
import net.minecraft.references.BlockItemIds;
import net.minecraft.references.ItemIds;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

/**
 * @see net.minecraft.data.tags.VanillaItemTagsProvider
 */
public class ItemTagsProviderSD extends FabricTagsProvider.ItemTagsProvider {
    public ItemTagsProviderSD(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(ItemTags.WOLF_FOOD)
                .add(ItemIdsSD.CALAMARI)
                .add(ItemIdsSD.COOKED_CALAMARI);
        tag(ItemTags.CAT_FOOD)
                .add(ItemIdsSD.CALAMARI);
        tag(ItemTagsSD.TENTS)
            .addAll(ItemIdsSD.TENT.asList());
        tag(ItemTagsSD.LIQUID_CONSUMABLES)
                .add(ItemIds.POTION)
                .add(ItemIds.MILK_BUCKET)
                .add(ItemIds.HONEY_BOTTLE)
                .add(ItemIds.BEETROOT_SOUP)
                .add(ItemIds.MUSHROOM_STEW)
                .add(ItemIds.SUSPICIOUS_STEW)
                .add(ItemIds.RABBIT_STEW)
                .add(ItemIdsSD.POTTAGE)
                .add(ItemIdsSD.COVEN_ELIXIR);
        tag(ItemTagsSD.NON_HUMANOID_ARMOR)
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
        tag(ItemTagsSD.DAGGERS)
                .add(ItemIdsSD.WOODEN_DAGGER)
                .add(ItemIdsSD.STONE_DAGGER)
                .add(ItemIdsSD.COPPER_DAGGER)
                .add(ItemIdsSD.IRON_DAGGER)
                .add(ItemIdsSD.GOLDEN_DAGGER)
                .add(ItemIdsSD.DIAMOND_DAGGER)
                .add(ItemIdsSD.NETHERITE_DAGGER);
        tag(ItemTagsSD.SWEEPING_WEAPON)
                .forceAddTag(ItemTags.AXES)
                .forceAddTag(ItemTags.SWORDS)
                .add(ItemIds.MACE);
        tag(ItemTags.ARMOR_ENCHANTABLE)
                .addTag(ItemTagsSD.NON_HUMANOID_ARMOR);
        tag(ItemTags.DURABILITY_ENCHANTABLE)
                .add(ItemIds.WOLF_ARMOR)
                .addTag(ItemTagsSD.DAGGERS)
                .add(ItemIdsSD.HEAVY_SHIELD);
        tag(ItemTags.EQUIPPABLE_ENCHANTABLE)
                .addTag(ItemTagsSD.NON_HUMANOID_ARMOR);
        tag(ItemTags.VANISHING_ENCHANTABLE)
                .addTag(ItemTagsSD.NON_HUMANOID_ARMOR);
        tag(ItemTags.MELEE_WEAPON_ENCHANTABLE)
                .addTag(ItemTagsSD.DAGGERS);
        tag(ItemTags.SHARP_WEAPON_ENCHANTABLE)
                .addTag(ItemTagsSD.DAGGERS);
        tag(ItemTags.SWEEPING_ENCHANTABLE)
                .addTag(ItemTagsSD.SWEEPING_WEAPON);
        tag(ItemTags.FIRE_ASPECT_ENCHANTABLE)
                .forceAddTag(ItemTags.AXES);
        tag(ItemTags.MINING_LOOT_ENCHANTABLE)
                .addTag(ItemTagsSD.DAGGERS);
        tag(ItemTags.LUNGE_ENCHANTABLE)
                .addTag(ItemTagsSD.DAGGERS);
        tag(ItemTagsSD.CAN_PARRY_SWORDS)
                .forceAddTag(ItemTags.SWORDS);
        tag(ItemTagsSD.CAN_PARRY_DAGGERS)
                .forceAddTag(ItemTagsSD.DAGGERS);
        tag(ItemTagsSD.HAS_MAGIC_LIMIT)
                .forceAddTag(ItemTags.ARMOR_ENCHANTABLE)
                .forceAddTag(ItemTags.EQUIPPABLE_ENCHANTABLE)
                .forceAddTag(ItemTags.DURABILITY_ENCHANTABLE)
                .forceAddTag(ItemTags.WEAPON_ENCHANTABLE)
                .forceAddTag(ItemTags.CROSSBOW_ENCHANTABLE)
                .forceAddTag(ItemTags.BOW_ENCHANTABLE)
                .add(ItemIds.BOOK)
                .add(ItemIds.ENCHANTED_BOOK);
        tag(ItemTags.SULFUR_CUBE_ARCHETYPE_REGULAR)
                .add(BlockItemIdsSD.STONE_PILLAR.item())
                .add(BlockItemIdsSD.STONE_TILES.item())
                .add(BlockItemIdsSD.CHISELED_POLISHED_DRIPSTONE.item())
                .add(BlockItemIdsSD.POLISHED_DRIPSTONE.item())
                .add(BlockItemIdsSD.CHARCOAL_BLOCK.item());
        tag(ItemTags.SULFUR_CUBE_ARCHETYPE_FAST_SLIDING)
                .add(BlockItemIdsSD.SNOW_BRICKS.item());
        tag(ItemTags.SULFUR_CUBE_ARCHETYPE_SLOW_FLAT)
                .add(BlockItemIdsSD.IRON_GRATE.item());
        tag(ItemTagsSD.STEW_INGREDIENT)
                .forceAddTag(ItemTags.MUSHROOMS)
                .add(BlockItemIds.CARROT_CROP.item())
                .add(ItemIds.BEETROOT)
                .add(ItemIds.WHEAT)
                .add(ItemIds.BAKED_POTATO)
                .add(ItemIds.COOKED_RABBIT)
                .add(BlockItemIds.ALLIUM.item())
                .add(BlockItemIds.AZURE_BLUET.item())
                .add(BlockItemIds.OPEN_EYEBLOSSOM.item())
                .add(BlockItemIds.BLUE_ORCHID.item())
                .add(BlockItemIds.DANDELION.item())
                .add(BlockItemIds.GOLDEN_DANDELION.item())
                .add(BlockItemIds.CLOSED_EYEBLOSSOM.item())
                .add(BlockItemIds.CORNFLOWER.item())
                .add(BlockItemIds.LILY_OF_THE_VALLEY.item())
                .add(BlockItemIds.OXEYE_DAISY.item())
                .add(BlockItemIds.POPPY.item())
                .add(BlockItemIds.TORCHFLOWER.item())
                .add(BlockItemIds.ORANGE_TULIP.item())
                .add(BlockItemIds.PINK_TULIP.item())
                .add(BlockItemIds.RED_TULIP.item())
                .add(BlockItemIds.WHITE_TULIP.item())
                .add(BlockItemIds.WITHER_ROSE.item());
        tag(ItemTags.PIGLIN_LOVED)
                .add(ItemIdsSD.GOLDEN_DAGGER);
        tag(ItemTags.LLAMA_TEMPT_ITEMS)
                .add(ItemIds.WHEAT);
        tag(ItemTagsSD.QUIVERS)
                .add(ItemIdsSD.QUIVER)
                .addAll(ItemIdsSD.DYED_QUIVER.asList());
        tag(ItemTagsSD.SHIELDS)
                .add(ItemIds.SHIELD)
                .add(ItemIdsSD.HEAVY_SHIELD);
    }
}