package net.meander.subtlyd.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.meander.subtlyd.client.renderer.entity.OcclusionManager;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

/**
 * @see net.minecraft.client.Options
 */
public interface OptionsSD {
    Component CAMERA_SHAKE_TOOLTIP = Component.translatable("options.accessibility.camera_shake.tooltip");
    Component EXPERIMENTAL_GUI_TOOLTIP = CommonComponents.joinLines(Options.TOOLTIP_NEEDS_RESTART, CommonComponents.EMPTY, Component.translatable("options.experimental.gui.tooltip"));
    Component SHIELD_CROUCH_TOOLTIP = Component.translatable("options.accessibility.shield_crouch.tooltip");
    Component FANCY_ENTITIES_TOOLTIP = Component.translatable("options.fancy_entities.tooltip");
    Component SHIELD_ANIMATION_TOOLTIP = Component.translatable("options.accessibility.shield_animation.tooltip");
    Component FRUSTUM_CULLING_TOOLTIP = Component.translatable("options.entity_culling.frustum.tooltip");
    Component OCCULSION_CULLING_TOOLTIP = Component.translatable("options.entity_culling.occlusion.tooltip");

    OptionInstance<Boolean> CAMERA_SHAKE = OptionInstance.createBoolean("options.accessibility.camera_shake", OptionInstance.cachedConstantTooltip(CAMERA_SHAKE_TOOLTIP), true);
    OptionInstance<Boolean> EXPERIMENTAL_GUI = OptionInstance.createBoolean("options.experimental.gui", OptionInstance.cachedConstantTooltip(EXPERIMENTAL_GUI_TOOLTIP), true);
    OptionInstance<Boolean> SHIELD_CROUCH = OptionInstance.createBoolean("options.accessibility.shield_crouch", OptionInstance.cachedConstantTooltip(SHIELD_CROUCH_TOOLTIP), true);
    OptionInstance<Boolean> FANCY_ENTITIES = OptionInstance.createBoolean("options.fancy_entities", OptionInstance.cachedConstantTooltip(FANCY_ENTITIES_TOOLTIP), true);
    OptionInstance<Boolean> SHIELD_ANIMATION = OptionInstance.createBoolean("options.accessibility.shield_animation", OptionInstance.cachedConstantTooltip(SHIELD_ANIMATION_TOOLTIP), true);
    OptionInstance<EntityCullingMethod> ENTITY_CULLING = new OptionInstance<>(
            "options.entity_culling",
            cullingMethod -> switch (cullingMethod) {
                case FRUSTUM -> Tooltip.create(FRUSTUM_CULLING_TOOLTIP);
                case OCCLUSION -> Tooltip.create(OCCULSION_CULLING_TOOLTIP);
            },
            (_, cullingMethod) -> cullingMethod.caption(),
            new OptionInstance.Enum<>(Arrays.asList(EntityCullingMethod.values()), EntityCullingMethod.LEGACY_CODEC),
            EntityCullingMethod.OCCLUSION,
            _ -> {
                OcclusionManager.getInstance().clearCache();
                Minecraft.getInstance().options.setGraphicsPresetToCustom();
                Options.operateOnLevelExtractor(LevelExtractor::resetSampler);
            }
    );
    KeyMapping[] MACRO_KEYS = new KeyMapping[10];

    default OptionInstance<Boolean> cameraShake() {
        return CAMERA_SHAKE;
    }

    default OptionInstance<Boolean> experimentalGui() {
        return EXPERIMENTAL_GUI;
    }

    default OptionInstance<Boolean> shieldCrouch() {
        return SHIELD_CROUCH;
    }

    default OptionInstance<Boolean> fancyEntities() {
        return FANCY_ENTITIES;
    }

    default OptionInstance<Boolean> shieldAnimation() {
        return SHIELD_ANIMATION;
    }

    default OptionInstance<EntityCullingMethod> entityCulling() {
        return ENTITY_CULLING;
    }

    private static void commandMacroBindings() {
        for (int i = 0; i < 10; i++) {
            int displayNum = (i == 9) ? 0 : (i + 1);
            int defaultKey = (i == 9) ? InputConstants.KEY_0 : InputConstants.KEY_1 + i;
            MACRO_KEYS[i] = new KeyMapping("key.command_macros." + displayNum, defaultKey, KeyMappingSD.Category.COMMAND_MACROS);
        }

        Arrays.stream(MACRO_KEYS).toList().forEach(KeyMappingHelper::registerKeyMapping);
    }

    static void bindOptions() {
        UtilSD.LOGGER.debug("Binding options...");
        commandMacroBindings();
    }
}
