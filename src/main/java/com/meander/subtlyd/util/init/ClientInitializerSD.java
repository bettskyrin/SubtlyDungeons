package com.meander.subtlyd.util.init;

import com.meander.subtlyd.client.ClientTickEventsSD;
import com.meander.subtlyd.client.renderer.EntityRenderersSD;
import com.meander.subtlyd.client.renderer.state.BlockRendering;
import com.meander.subtlyd.network.PacketNetworking;
import com.meander.subtlyd.sounds.SoundEventsSD;
import net.fabricmc.api.ClientModInitializer;

public class ClientInitializerSD implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRenderersSD.registration();
        SoundEventsSD.registration();
        ClientTickEventsSD.registration();
        BlockRendering.init();
        PacketNetworking.registerClient();
    }
}
