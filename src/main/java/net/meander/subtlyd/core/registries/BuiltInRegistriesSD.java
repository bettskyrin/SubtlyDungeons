package net.meander.subtlyd.core.registries;

import com.mojang.serialization.Lifecycle;
import net.meander.subtlyd.client.resources.camera.CameraShakeEvent;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;

public class BuiltInRegistriesSD {
    public static final Registry<CameraShakeEvent> CAMERA_SHAKE_EVENT = new MappedRegistry<>(RegistriesSD.CAMERA_SHAKE_EVENT, Lifecycle.stable());
}
