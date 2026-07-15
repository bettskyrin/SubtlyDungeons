package net.meander.subtlyd.util;

import net.fabricmc.api.ClientModInitializer;
import net.meander.subtlyd.client.ClientEventsSD;
import net.meander.subtlyd.client.model.geom.ModelLayersSD;
import net.meander.subtlyd.client.renderer.EntityRenderersSD;
import net.meander.subtlyd.core.particles.ParticleTypesSD;
import net.meander.subtlyd.network.PacketNetworking;
import net.meander.subtlyd.sounds.SoundEventsSD;

public class ClientInitializerSD implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLayersSD.registration();
        EntityRenderersSD.registration();
        SoundEventsSD.registration();
        ClientEventsSD.registration();
        PacketNetworking.registerClient();
        ParticleTypesSD.registerClient();
    }
}
