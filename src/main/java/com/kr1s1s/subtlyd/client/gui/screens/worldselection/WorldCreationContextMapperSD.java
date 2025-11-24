package com.kr1s1s.subtlyd.client.gui.screens.worldselection;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.worldselection.DataPackReloadCookie;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface WorldCreationContextMapperSD {
    WorldCreationContextSD apply(
            ReloadableServerResources reloadableServerResources, LayeredRegistryAccess<RegistryLayer> layeredRegistryAccess, DataPackReloadCookie dataPackReloadCookie
    );
}
