package com.kr1s1s.subtlyd;

import com.kr1s1s.subtlyd.client.ClientTickEventsSD;
import com.kr1s1s.subtlyd.client.renderer.*;
import com.kr1s1s.subtlyd.sounds.SoundEventsSD;
import net.fabricmc.api.ClientModInitializer;

public class SubtlyDungeonsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRenderersSD.runRegistration();
        SoundEventsSD.init();
        ClientTickEventsSD.init();
    }
}
