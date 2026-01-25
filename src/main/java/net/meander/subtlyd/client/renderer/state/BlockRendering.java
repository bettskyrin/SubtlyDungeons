package net.meander.subtlyd.client.renderer.state;

import net.meander.subtlyd.world.block.BlocksSD;
import net.fabricmc.fabric.api.client.rendering.v1.ChunkSectionLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class BlockRendering {
    public static void init() {
        ChunkSectionLayerMap.putBlock(BlocksSD.IRON_GRATE, ChunkSectionLayer.CUTOUT);
        ChunkSectionLayerMap.putBlock(BlocksSD.REEDS, ChunkSectionLayer.CUTOUT);
    }
}
