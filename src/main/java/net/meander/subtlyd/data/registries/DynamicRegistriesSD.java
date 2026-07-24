package net.meander.subtlyd.data.registries;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;

import java.util.concurrent.CompletableFuture;

/**
 * @see net.minecraft.data.registries.VanillaRegistries
 */
public class DynamicRegistriesSD extends FabricDynamicRegistryProvider {
    public DynamicRegistriesSD(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        try {
            entries.addAll(registries.lookupOrThrow(net.meander.subtlyd.core.registries.RegistriesSD.CAMERA_SHAKE_EVENT));
            entries.addAll(registries.lookupOrThrow(Registries.ENCHANTMENT));
        } catch (Exception e) {
            UtilSD.LOGGER.error("Failed to configure dynamic registries: {}", e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "Dynamic Registries";
    }
}
