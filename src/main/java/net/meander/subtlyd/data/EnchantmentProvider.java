package net.meander.subtlyd.data;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.meander.subtlyd.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;

import java.util.concurrent.CompletableFuture;

public class EnchantmentProvider extends FabricDynamicRegistryProvider {
    public EnchantmentProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        try {
            entries.addAll(registries.lookupOrThrow(Registries.ENCHANTMENT));
        } catch (Exception e) {
            Util.LOGGER.error("Failed to configure dynamic registries: {}", e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "Subtly Dungeons Enchantments";
    }
}
