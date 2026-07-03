package net.meander.subtlyd.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.meander.subtlyd.world.item.enchantment.EnchantmentsSD;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.concurrent.CompletableFuture;

/**
 * @see net.minecraft.data.tags.EnchantmentTagsProvider
 */
public class EnchantmentTagsProviderSD extends FabricTagsProvider<Enchantment> {
    public EnchantmentTagsProviderSD(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, Registries.ENCHANTMENT, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        getOrCreateRawBuilder(EnchantmentTags.TREASURE)
                .addOptionalElement(EnchantmentsSD.ABRADING_CURSE.identifier())
                .addOptionalElement(EnchantmentsSD.GLYPH_AFFINITY.identifier());
        getOrCreateRawBuilder(EnchantmentTags.NON_TREASURE)
                .addOptionalElement(EnchantmentsSD.OCCULT_PROTECTION.identifier())
                .addOptionalElement(EnchantmentsSD.GLYPH_AFFINITY.identifier())
                .addOptionalElement(EnchantmentsSD.ENERVATION.identifier())
                .addOptionalElement(EnchantmentsSD.CLEAVING.identifier());
        getOrCreateRawBuilder(EnchantmentTags.ON_RANDOM_LOOT)
                .addOptionalElement(EnchantmentsSD.ABRADING_CURSE.identifier());
        getOrCreateRawBuilder(EnchantmentTags.ON_TRADED_EQUIPMENT)
                .addOptionalElement(EnchantmentsSD.ILLAGERS_BANE.identifier());
        getOrCreateRawBuilder(EnchantmentTags.TRADEABLE)
                .addOptionalElement(EnchantmentsSD.ABRADING_CURSE.identifier())
                .addOptionalElement(EnchantmentsSD.ILLAGERS_BANE.identifier())
                .addOptionalElement(EnchantmentsSD.CLEAVING.identifier());
        getOrCreateRawBuilder(EnchantmentTags.CURSE)
                .addOptionalElement(EnchantmentsSD.ABRADING_CURSE.identifier());
        getOrCreateRawBuilder(EnchantmentTags.ARMOR_EXCLUSIVE)
                .addOptionalElement(EnchantmentsSD.OCCULT_PROTECTION.identifier());
        getOrCreateRawBuilder(EnchantmentTags.DAMAGE_EXCLUSIVE)
                .addOptionalElement(EnchantmentsSD.CLEAVING.identifier());
        getOrCreateRawBuilder(EnchantmentTagsSD.INCREASES_MAGIC_LIMIT)
                .addOptionalElement(EnchantmentsSD.GLYPH_AFFINITY.identifier());
        getOrCreateRawBuilder(EnchantmentTagsSD.REPAIRS_EQUIPMENT)
                .addOptionalElement(Enchantments.MENDING.identifier())
                .addOptionalElement(Enchantments.UNBREAKING.identifier());
    }
}
