package net.meander.subtlyd.client.camera.shake;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.meander.subtlyd.core.registries.RegistriesSD;
import net.meander.subtlyd.util.Util;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class CameraShakeEventProvider extends FabricDynamicRegistryProvider {
    public CameraShakeEventProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        try {
            entries.addAll(registries.lookupOrThrow(RegistriesSD.CAMERA_SHAKE_EVENT));
        } catch (Exception e) {
            Util.LOGGER.error("Failed to configure dynamic registries: {}", e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "Camera Shake Events";
    }
}
