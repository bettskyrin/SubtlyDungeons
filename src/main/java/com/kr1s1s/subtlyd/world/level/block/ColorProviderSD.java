package com.kr1s1s.subtlyd.world.level.block;

import com.kr1s1s.subtlyd.world.block.BlocksSD;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.BiomeColors;

public class ColorProviderSD {
    public static void init() {
        ColorProviderRegistry.BLOCK.register(((blockState, blockAndTintGetter, blockPos, i) -> {
            if (blockAndTintGetter == null || blockPos == null) {
                return -1;
            }

            return BiomeColors.getAverageFoliageColor(blockAndTintGetter, blockPos);
        }), BlocksSD.REEDS);
    }
}
