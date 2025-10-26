package com.kr1s1s.subtlyd.client;

import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;

public class OptionsSD {
    private static final Component ACCESSIBILITY_TOOLTIP_DO_CAMERA_SHAKE = Component.translatable("options.doCameraShake.tooltip");
    public static final OptionInstance<Boolean> doCameraShake = OptionInstance.createBoolean(
            "options.doCameraShake", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_DO_CAMERA_SHAKE), true
    );

    public OptionInstance<Boolean> doCameraShake() {
        return doCameraShake;
    }
}
