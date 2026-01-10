package com.kr1s1s.subtlyd.client.gui.screens.worldselectionold;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface CreateWorldCallbackSD {
    boolean create(
            CreateWorldScreenSD createWorldScreen, LayeredRegistryAccess<RegistryLayer> layeredRegistryAccess, PrimaryLevelData primaryLevelData, @Nullable Path path
    );
}