package com.kr1s1s.subtlyd.client.gui.screens.worldselectionold;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.gamerules.GameRuleMap;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record InitialWorldCreationOptionsSD(
        WorldCreationUiStateSD.SelectedGameMode selectedGameMode, GameRuleMap gameRuleOverwrites, @Nullable ResourceKey<FlatLevelGeneratorPreset> flatLevelPreset
) {
}
