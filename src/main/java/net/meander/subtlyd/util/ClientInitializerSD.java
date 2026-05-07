package net.meander.subtlyd.util;

import net.meander.subtlyd.client.ClientEventsSD;
import net.meander.subtlyd.client.model.geom.ModelLayersSD;
import net.meander.subtlyd.client.renderer.EntityRenderersSD;
import net.meander.subtlyd.network.PacketNetworking;
import net.meander.subtlyd.sounds.SoundEventsSD;
import net.fabricmc.api.ClientModInitializer;

public class ClientInitializerSD implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLayersSD.registration();
        EntityRenderersSD.registration();
        SoundEventsSD.registration();
        ClientEventsSD.registration();
        PacketNetworking.registerClient();
    }
}
