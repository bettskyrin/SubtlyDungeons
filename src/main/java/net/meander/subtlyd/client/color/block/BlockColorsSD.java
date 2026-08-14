package net.meander.subtlyd.client.color.block;

import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.meander.subtlyd.util.UtilSD;
import net.meander.subtlyd.world.level.block.BlocksSD;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.color.block.BlockTintSources;

import java.util.List;

/**
 * @see net.minecraft.client.color.block.BlockColors
 */
public class BlockColorsSD {
    private static final BlockTintSource BLANK_LAYER = BlockTintSources.constant(-1);

    public static void registration() {
        UtilSD.LOGGER.debug("Registering block colors...");
        BlockColorRegistry.register(List.of(BLANK_LAYER, BlockTintSources.grass()), BlocksSD.PERSE_WILDFLOWERS);
        BlockColorRegistry.register(BlockTintSourcesSD.potionCauldron(), BlocksSD.POTION_CAULDRON);
    }
}
