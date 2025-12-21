package com.kr1s1s.subtlyd.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class OptionInstanceSD {
    private static final Component ACCESSIBILITY_TOOLTIP_SCREEN_SHAKE = Component.translatable("options.accessibility.screen_shake.tooltip");

    public static final OptionInstance<Boolean> SCREEN_SHAKE = OptionInstance.createBoolean(
            "options.accessibility.screen_shake", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_SCREEN_SHAKE), true
    );

    public OptionInstance<Boolean> screenShake() {
        return SCREEN_SHAKE;
    }
}
