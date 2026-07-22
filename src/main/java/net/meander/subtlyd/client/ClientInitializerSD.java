package net.meander.subtlyd.client;

import net.fabricmc.api.ClientModInitializer;
import net.meander.subtlyd.client.camera.shake.CameraShakeEvents;
import net.meander.subtlyd.client.color.block.BlockColorsSD;
import net.meander.subtlyd.client.model.geom.LayerDefinitionsSD;
import net.meander.subtlyd.client.particle.ParticleResourcesSD;
import net.meander.subtlyd.client.renderer.entity.EntityRenderersSD;
import net.meander.subtlyd.client.renderer.special.SpecialModelRenderersSD;
import net.meander.subtlyd.commands.CommandsSD;
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
        BlockColorsSD.registration();
        ParticleResourcesSD.registerProviders();
        SpecialModelRenderersSD.bootstrap();
        LayerDefinitionsSD.registration();
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
