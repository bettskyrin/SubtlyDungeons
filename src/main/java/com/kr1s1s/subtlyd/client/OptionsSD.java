package com.kr1s1s.subtlyd.client;

import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;

public class OptionsSD {
    private static final Component ACCESSIBILITY_TOOLTIP_CAMERA_SHAKE = Component.translatable("options.camera_shake.tooltip");
    public static final OptionInstance<Boolean> CAMERA_SHAKE = OptionInstance.createBoolean(
            "options.camera_shake", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_CAMERA_SHAKE), true
    );

    public OptionInstance<Boolean> cameraShake() {
        return CAMERA_SHAKE;
    }
}
