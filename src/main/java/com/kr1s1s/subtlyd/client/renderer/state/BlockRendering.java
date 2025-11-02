package com.kr1s1s.subtlyd.client.renderer.state;

import com.kr1s1s.subtlyd.world.block.BlocksSD;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class BlockRendering {
    public static void init() {
        BlockRenderLayerMap.putBlock(BlocksSD.IRON_GRATE, ChunkSectionLayer.CUTOUT);
    }
}
