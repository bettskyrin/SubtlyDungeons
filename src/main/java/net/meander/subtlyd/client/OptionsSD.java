package net.meander.subtlyd.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class OptionsSD {
    private static final Component ACCESSIBILITY_TOOLTIP_CAMERA_SHAKE = Component.translatable("options.accessibility.camera_shake.tooltip");
    private static final Component VIDEO_TOOLTIP_EXPERIMENTAL_GUI = CommonComponents.joinLines(Component.translatable("options.needsRestart"), CommonComponents.EMPTY, Component.translatable("options.experimental.gui.tooltip"));
    private static final Component ACCESSIBILITY_TOOLTIP_SHIELD_CROUCH = Component.translatable("options.accessibility.shield_crouch.tooltip");
    private static final Component VIDEO_TOOLTIP_ADVANCED_ENTITY_ANIMATIONS = Component.translatable("options.advanced_entity_animations.tooltip");


    public static final OptionInstance<Boolean> CAMERA_SHAKE = OptionInstance.createBoolean("options.accessibility.camera_shake", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_CAMERA_SHAKE), true);
    public static final OptionInstance<Boolean> EXPERIMENTAL_GUI = OptionInstance.createBoolean("options.experimental.gui", OptionInstance.cachedConstantTooltip(VIDEO_TOOLTIP_EXPERIMENTAL_GUI), true);
    public static final OptionInstance<Boolean> SHIELD_CROUCH = OptionInstance.createBoolean("options.accessibility.shield_crouch", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_SHIELD_CROUCH), true);
    public static final OptionInstance<Boolean> ADVANCED_ENTITY_ANIMATIONS = OptionInstance.createBoolean("options.advanced_entity_animations", OptionInstance.cachedConstantTooltip(VIDEO_TOOLTIP_ADVANCED_ENTITY_ANIMATIONS), true);

    public OptionInstance<Boolean> screenShake() {
        return CAMERA_SHAKE;
    }

    public OptionInstance<Boolean> ui() {
        return EXPERIMENTAL_GUI;
    }

    public OptionInstance<Boolean> shieldCrouch() {
        return SHIELD_CROUCH;
    }

    public OptionInstance<Boolean> advancedEntityAnimations() {
        return ADVANCED_ENTITY_ANIMATIONS;
    }
}
