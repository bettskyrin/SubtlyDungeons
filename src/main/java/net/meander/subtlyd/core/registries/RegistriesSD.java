package net.meander.subtlyd.core.registries;

import net.meander.subtlyd.client.camera.shake.CameraShakeEvent;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class RegistriesSD {
    public static final ResourceKey<Registry<CameraShakeEvent>> CAMERA_SHAKE_EVENT = ResourceKey.createRegistryKey(UtilSD.identifier("camera_shake_event"));
}
