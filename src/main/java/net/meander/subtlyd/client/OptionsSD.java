package net.meander.subtlyd.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class OptionsSD {
    private static final Component ACCESSIBILITY_TOOLTIP_CAMERA_SHAKE = Component.translatable("options.accessibility.camera_shake.tooltip");
    private static final Component EXPERIMENTAL_TOOLTIP_GUI = CommonComponents.joinLines(Component.translatable("options.needsRestart"), CommonComponents.EMPTY, Component.translatable("options.experimental.gui.tooltip"));
    private static final Component ACCESSIBILITY_TOOLTIP_SHIELD_CROUCH = Component.translatable("options.accessibility.shield_crouch.tooltip");

    public static final OptionInstance<Boolean> CAMERA_SHAKE = OptionInstance.createBoolean("options.accessibility.camera_shake", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_CAMERA_SHAKE), true);
    public static final OptionInstance<Boolean> EXPERIMENTAL_GUI = OptionInstance.createBoolean("options.experimental.gui", OptionInstance.cachedConstantTooltip(EXPERIMENTAL_TOOLTIP_GUI), true);
    public static final OptionInstance<Boolean> SHIELD_CROUCH = OptionInstance.createBoolean("options.accessibility.shield_crouch", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_SHIELD_CROUCH), true);

    public OptionInstance<Boolean> screenShake() {
        return CAMERA_SHAKE;
    }

    public OptionInstance<Boolean> ui() {
        return EXPERIMENTAL_GUI;
    }

    public OptionInstance<Boolean> shieldCrouch() {
        return SHIELD_CROUCH;
    }
}
