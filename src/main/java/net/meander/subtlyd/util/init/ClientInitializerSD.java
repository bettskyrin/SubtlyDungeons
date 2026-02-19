package net.meander.subtlyd.util.init;

import net.meander.subtlyd.client.ClientTickEventsSD;
import net.meander.subtlyd.client.renderer.EntityRenderersSD;
import net.meander.subtlyd.network.PacketNetworking;
import net.meander.subtlyd.sounds.SoundEventsSD;
import net.fabricmc.api.ClientModInitializer;

public class ClientInitializerSD implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRenderersSD.registration();
        SoundEventsSD.registration();
        ClientTickEventsSD.registration();
        PacketNetworking.registerClient();
    }
}
