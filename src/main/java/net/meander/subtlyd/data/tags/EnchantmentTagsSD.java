package net.meander.subtlyd.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.meander.subtlyd.world.item.enchantment.EnchantmentsSD;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.concurrent.CompletableFuture;

public class EnchantmentTagsSD extends FabricTagsProvider<Enchantment> {
    public EnchantmentTagsSD(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, Registries.ENCHANTMENT, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        getOrCreateRawBuilder(EnchantmentTags.IN_ENCHANTING_TABLE)
                .addOptionalElement(EnchantmentsSD.OCCULT_PROTECTION.identifier());
        getOrCreateRawBuilder(EnchantmentTags.ARMOR_EXCLUSIVE)
                .addOptionalElement(EnchantmentsSD.OCCULT_PROTECTION.identifier());
        getOrCreateRawBuilder(EnchantmentTags.ON_TRADED_EQUIPMENT)
                .addOptionalElement(EnchantmentsSD.OCCULT_PROTECTION.identifier());
        getOrCreateRawBuilder(EnchantmentTags.CURSE)
                .addOptionalElement(EnchantmentsSD.DECAYING_CURSE.identifier());
    }
}
