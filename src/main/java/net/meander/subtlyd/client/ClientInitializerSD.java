package net.meander.subtlyd.client;

import net.fabricmc.api.ClientModInitializer;
import net.meander.subtlyd.client.camera.shake.CameraShakeEvents;
import net.meander.subtlyd.client.color.block.BlockColorsSD;
import net.meander.subtlyd.client.model.geom.ModelLayersSD;
import net.meander.subtlyd.client.renderer.EntityRenderersSD;
import net.meander.subtlyd.client.renderer.special.SpecialModelRenderersSD;
import net.meander.subtlyd.core.particles.ParticleTypesSD;
import net.meander.subtlyd.network.PacketNetworking;
import net.meander.subtlyd.sounds.SoundEventsSD;
import net.meander.subtlyd.world.level.LevelSD;

public class ClientInitializerSD implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        network();
        level();
        render();
    }

    private void render() {
        ModelLayersSD.registration();
        SpecialModelRenderersSD.bootstrap();
        BlockColorsSD.registration();
        ParticleTypesSD.registration();
        EntityRenderersSD.registration();
    }

    private void level() {
        SoundEventsSD.registration();
        CameraShakeEvents.registration();
        LevelSD.registerClientEvents();
    }

    private void network() {
        PacketNetworking.registerClient();
    }
}
