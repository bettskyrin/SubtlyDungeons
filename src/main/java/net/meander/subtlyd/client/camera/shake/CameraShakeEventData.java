package net.meander.subtlyd.client.camera.shake;

import net.meander.subtlyd.core.registries.RegistriesSD;
import net.meander.subtlyd.sounds.SoundEventsSD;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class CameraShakeEventData {
    public static void bootstrap(BootstrapContext<CameraShakeEvent> context) {
        context.register(CameraShakeEvents.WARDEN_ROAR, getEvent(SoundEvents.WARDEN_ROAR, 32, 50));
        context.register(CameraShakeEvents.WARDEN_SONIC_BOOM, getEvent(SoundEvents.WARDEN_SONIC_BOOM, 64, 25));
        context.register(CameraShakeEvents.WARDEN_EMERGE, getEvent(SoundEvents.WARDEN_EMERGE, 128, 110));
        context.register(CameraShakeEvents.WARDEN_DIG, getEvent(SoundEvents.WARDEN_DIG, 128, 25));
        context.register(CameraShakeEvents.RAVAGER_ROAR, getEvent(SoundEvents.RAVAGER_ROAR, 32, 25));
        context.register(CameraShakeEvents.ENDER_DRAGON_AMBIENT, getEvent(SoundEvents.ENDER_DRAGON_AMBIENT, 64, 25));
        context.register(CameraShakeEvents.WEATHER_END_FLASH, getEvent(SoundEvents.WEATHER_END_FLASH, 64, 25));
        context.register(CameraShakeEvents.END_GATEWAY_SPAWN, getEvent(SoundEvents.END_GATEWAY_SPAWN, 512, 25));
        context.register(CameraShakeEvents.LIGHTNING_BOLT_IMPACT, getEvent(SoundEvents.LIGHTNING_BOLT_IMPACT, 32, 20));
        context.register(CameraShakeEvents.DRAGON_FIREBALL_EXPLODE, getEvent(SoundEvents.DRAGON_FIREBALL_EXPLODE, 32, 25));
        context.register(CameraShakeEvents.GENERIC_EXPLODE, getEvent(SoundEvents.GENERIC_EXPLODE.value(), 16, 20));
        context.register(CameraShakeEvents.MACE_SMASH_AIR, getEvent(SoundEvents.MACE_SMASH_AIR, 8, 10));
        context.register(CameraShakeEvents.MACE_SMASH_GROUND, getEvent(SoundEvents.MACE_SMASH_GROUND, 8, 10));
        context.register(CameraShakeEvents.MACE_SMASH_GROUND_HEAVY, getEvent(SoundEvents.MACE_SMASH_GROUND_HEAVY, 10, 15));
        context.register(CameraShakeEvents.EVOKER_FANGS_APPEAR, getEvent(SoundEventsSD.EVOKER_FANGS_APPEAR, 10, 30));
    }

    public static ResourceKey<CameraShakeEvent> register(String id) {
        return ResourceKey.create(RegistriesSD.CAMERA_SHAKE_EVENT, UtilSD.identifier(id));
    }

    private static CameraShakeEvent getEvent(SoundEvent soundEvent, int range, int durationTicks) {
        return new CameraShakeEvent(soundEvent.location(), range, durationTicks, 4.0F);
    }
}
