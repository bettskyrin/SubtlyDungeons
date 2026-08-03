package net.meander.subtlyd.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

/**
 * @see net.minecraft.client.Options
 */
public class OptionsSD {
    public static final Component CAMERA_SHAKE_TOOLTIP = Component.translatable("options.accessibility.camera_shake.tooltip");
    public static final Component EXPERIMENTAL_GUI_TOOLTIP = CommonComponents.joinLines(Component.translatable("options.needsRestart"), CommonComponents.EMPTY, Component.translatable("options.experimental.gui.tooltip"));
    public static final Component SHIELD_CROUCH_TOOLTIP = Component.translatable("options.accessibility.shield_crouch.tooltip");
    public static final Component ADVANCED_ENTITY_ANIMATIONS_TOOLTIP = Component.translatable("options.advanced_entity_animations.tooltip");
    public static final Component SHIELD_ANIMATION_TOOLTIP = Component.translatable("options.accessibility.shield_animation.tooltip");
    private static final OptionInstance<Boolean> CAMERA_SHAKE = OptionInstance.createBoolean("options.accessibility.camera_shake", OptionInstance.cachedConstantTooltip(CAMERA_SHAKE_TOOLTIP), true);
    private static final OptionInstance<Boolean> EXPERIMENTAL_GUI = OptionInstance.createBoolean("options.experimental.gui", OptionInstance.cachedConstantTooltip(EXPERIMENTAL_GUI_TOOLTIP), true);
    private static final OptionInstance<Boolean> SHIELD_CROUCH = OptionInstance.createBoolean("options.accessibility.shield_crouch", OptionInstance.cachedConstantTooltip(SHIELD_CROUCH_TOOLTIP), true);
    private static final OptionInstance<Boolean> ADVANCED_ENTITY_ANIMATIONS = OptionInstance.createBoolean("options.advanced_entity_animations", OptionInstance.cachedConstantTooltip(ADVANCED_ENTITY_ANIMATIONS_TOOLTIP), true);
    private static final OptionInstance<Boolean> SHIELD_ANIMATION = OptionInstance.createBoolean("options.accessibility.shield_animation", OptionInstance.cachedConstantTooltip(SHIELD_ANIMATION_TOOLTIP), true);
    public static final KeyMapping[] MACRO_KEYS = new KeyMapping[10];

    public static OptionInstance<Boolean> cameraShake() {
        return CAMERA_SHAKE;
    }

    public static OptionInstance<Boolean> gui() {
        return EXPERIMENTAL_GUI;
    }

    public static OptionInstance<Boolean> shieldCrouch() {
        return SHIELD_CROUCH;
    }

    public static OptionInstance<Boolean> advancedEntityAnimations() {
        return ADVANCED_ENTITY_ANIMATIONS;
    }

    public static OptionInstance<Boolean> shieldAnimation() {
        return SHIELD_ANIMATION;
    }

    private static void commandMacroBindings() {
        for (int i = 0; i < 10; i++) {
            int displayNum = (i == 9) ? 0 : (i + 1);
            int defaultKey = (i == 9) ? InputConstants.KEY_0 : InputConstants.KEY_1 + i;
            MACRO_KEYS[i] = new KeyMapping("key.command_macros." + displayNum, defaultKey, KeyMappingSD.Category.COMMAND_MACROS);
        }

        Arrays.stream(MACRO_KEYS).toList().forEach(KeyMappingHelper::registerKeyMapping);
    }

    public static void bindOptions() {
        UtilSD.LOGGER.debug("Binding options...");
        commandMacroBindings();
    }
}
