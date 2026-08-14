package net.meander.subtlyd.core.registries;

import net.meander.subtlyd.client.camera.shake.CameraShakeEvent;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

/**
 * @see net.minecraft.core.registries.Registries
 */
public class RegistriesSD {
    public static final ResourceKey<Registry<CameraShakeEvent>> CAMERA_SHAKE_EVENT = createRegistryKey("camera_shake_event");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(final String name) {
        return ResourceKey.createRegistryKey(UtilSD.identifier(name));
    }
}
