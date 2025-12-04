package com.kr1s1s.subtlyd.client;

import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;

public class OptionsSD {
    private static final Component ACCESSIBILITY_TOOLTIP_SCREEN_SHAKE = Component.translatable("options.screen_shake.tooltip");
    public static final OptionInstance<Boolean> SCREEN_SHAKE = OptionInstance.createBoolean(
            "options.screen_shake", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_SCREEN_SHAKE), true
    );

    public OptionInstance<Boolean> cameraShake() {
        return SCREEN_SHAKE;
    }
}
