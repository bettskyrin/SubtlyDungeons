package net.meander.subtlyd.client;

import net.fabricmc.api.ClientModInitializer;
import net.meander.subtlyd.client.camera.shake.CameraShakeEvents;
import net.meander.subtlyd.client.color.block.BlockColorsSD;
import net.meander.subtlyd.client.model.geom.ModelLayersSD;
import net.meander.subtlyd.client.renderer.EntityRenderersSD;
import net.meander.subtlyd.client.renderer.special.SpecialModelRenderersSD;
import net.meander.subtlyd.commands.CommandsSD;
import net.meander.subtlyd.core.particles.ParticleTypesSD;
import net.meander.subtlyd.network.PacketNetworking;
import net.meander.subtlyd.sounds.SoundEventsSD;
import net.meander.subtlyd.world.level.LevelSD;

public class ClientInitializerSD implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        core();
        level();
        render();
    }

    private void render() {
        ModelLayersSD.registration();
        SpecialModelRenderersSD.bootstrap();
        BlockColorsSD.registration();
        ParticleTypesSD.registerClient();
        EntityRenderersSD.registration();
    }

    private void level() {
        SoundEventsSD.registration();
        CameraShakeEvents.registration();
        LevelSD.registerClientEvents();
    }

    private void core() {
        PacketNetworking.registerClient();
        OptionsSD.registration();
        CommandsSD.initClient();
    }
}
