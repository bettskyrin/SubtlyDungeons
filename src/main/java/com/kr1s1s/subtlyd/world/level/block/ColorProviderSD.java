package com.kr1s1s.subtlyd.world.level.block;

import com.kr1s1s.subtlyd.world.block.BlocksSD;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.renderer.BiomeColors;

public class ColorProviderSD {
    public static void init() {
        BlockColorRegistry.register(((_, blockAndTintGetter, blockPos, _) -> {
            if (blockAndTintGetter == null || blockPos == null) {
                return -1;
            }
            return BiomeColors.getAverageFoliageColor(blockAndTintGetter, blockPos);
        }), BlocksSD.REEDS);
    }
}