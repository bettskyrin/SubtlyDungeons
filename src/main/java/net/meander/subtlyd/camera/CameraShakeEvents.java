package net.meander.subtlyd.camera;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.meander.subtlyd.client.resources.camera.CameraShakeEvent;
import net.meander.subtlyd.core.registries.RegistriesSD;
import net.meander.subtlyd.util.Util;
import net.minecraft.resources.ResourceKey;

public class CameraShakeEvents {
    public static final ResourceKey<CameraShakeEvent> WARDEN_ROAR = register("warden_roar");
    public static final ResourceKey<CameraShakeEvent> WARDEN_SONIC_BOOM = register("warden_sonic_boom");
    public static final ResourceKey<CameraShakeEvent> WARDEN_EMERGE = register("warden_emerge");
    public static final ResourceKey<CameraShakeEvent> WARDEN_DIG = register("warden_dig");
    public static final ResourceKey<CameraShakeEvent> RAVAGER_ROAR = register("ravager_roar");
    public static final ResourceKey<CameraShakeEvent> ENDER_DRAGON_AMBIENT = register("ender_dragon_ambient");
    public static final ResourceKey<CameraShakeEvent> WEATHER_END_FLASH = register("weather_end_flash");
    public static final ResourceKey<CameraShakeEvent> END_GATEWAY_SPAWN = register("end_gateway_spawn");
    public static final ResourceKey<CameraShakeEvent> LIGHTNING_BOLT_IMPACT = register("lightning_bolt_impact");
    public static final ResourceKey<CameraShakeEvent> DRAGON_FIREBALL_EXPLODE = register("dragon_fireball_explode");
    public static final ResourceKey<CameraShakeEvent> GENERIC_EXPLODE = register("generic_explode");
    public static final ResourceKey<CameraShakeEvent> MACE_SMASH_AIR = register("mace_smash_air");
    public static final ResourceKey<CameraShakeEvent> MACE_SMASH_GROUND = register("mace_smash_ground");
    public static final ResourceKey<CameraShakeEvent> MACE_SMASH_GROUND_HEAVY = register("mace_smash_ground_heavy");
    public static final ResourceKey<CameraShakeEvent> EVOKER_FANGS_APPEAR = register("evoker_fangs_appear");

    public static ResourceKey<CameraShakeEvent> register(String id) {
        return ResourceKey.create(RegistriesSD.CAMERA_SHAKE_EVENT, Util.identifier(id));
    }

    public static void registration() {
        DynamicRegistries.registerSynced(RegistriesSD.CAMERA_SHAKE_EVENT, CameraShakeEvent.CODEC);
    }
}
