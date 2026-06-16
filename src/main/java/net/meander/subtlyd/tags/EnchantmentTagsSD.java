package net.meander.subtlyd.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.item.enchantment.EnchantmentsSD;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.concurrent.CompletableFuture;

public class EnchantmentTagsSD extends FabricTagsProvider<Enchantment> {
    public static TagKey<Enchantment> INCREASES_MAGIC_LIMIT = bind("increases_magic_limit");
    public static TagKey<Enchantment> REPAIRS_EQUIPMENT = bind("repairs_equipment");

    public EnchantmentTagsSD(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, Registries.ENCHANTMENT, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        getOrCreateRawBuilder(EnchantmentTags.TREASURE)
                .addOptionalElement(EnchantmentsSD.ABRADING_CURSE.identifier())
                .addOptionalElement(EnchantmentsSD.GLYPH_AFFINITY.identifier());
        getOrCreateRawBuilder(EnchantmentTags.NON_TREASURE)
                .addOptionalElement(EnchantmentsSD.OCCULT_PROTECTION.identifier())
                .addOptionalElement(EnchantmentsSD.GLYPH_AFFINITY.identifier());
        getOrCreateRawBuilder(EnchantmentTags.ON_RANDOM_LOOT)
                .addOptionalElement(EnchantmentsSD.ABRADING_CURSE.identifier());
        getOrCreateRawBuilder(EnchantmentTags.ON_TRADED_EQUIPMENT)
                .addOptionalElement(EnchantmentsSD.ILLAGERS_BANE.identifier());
        getOrCreateRawBuilder(EnchantmentTags.TRADEABLE)
                .addOptionalElement(EnchantmentsSD.ABRADING_CURSE.identifier())
                .addOptionalElement(EnchantmentsSD.ILLAGERS_BANE.identifier());
        getOrCreateRawBuilder(EnchantmentTags.CURSE)
                .addOptionalElement(EnchantmentsSD.ABRADING_CURSE.identifier());
        getOrCreateRawBuilder(EnchantmentTags.ARMOR_EXCLUSIVE)
                .addOptionalElement(EnchantmentsSD.OCCULT_PROTECTION.identifier());
        getOrCreateRawBuilder(INCREASES_MAGIC_LIMIT)
                .addOptionalElement(EnchantmentsSD.GLYPH_AFFINITY.identifier());
        getOrCreateRawBuilder(REPAIRS_EQUIPMENT)
                .addOptionalElement(Enchantments.MENDING.identifier())
                .addOptionalElement(Enchantments.UNBREAKING.identifier());
    }

    private static TagKey<Enchantment> bind(final String name) {
        return TagKey.create(Registries.ENCHANTMENT, Util.identifier(name));
    }
}
