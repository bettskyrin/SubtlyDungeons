package com.meander.subtlyd.util.data.loot_table;

import com.meander.subtlyd.world.block.BlocksSD;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class BlockLootSD extends FabricBlockLootSubProvider {
    public BlockLootSD(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override public void generate() {
        dropSelf(BlocksSD.SNOW_BRICKS);
        dropSelf(BlocksSD.SNOW_BRICK_STAIRS);
        dropSelf(BlocksSD.SNOW_BRICK_SLAB);
        dropSelf(BlocksSD.SNOW_BRICK_WALL);
        dropSelf(BlocksSD.CHARCOAL_BLOCK);
        dropSelf(BlocksSD.IRON_GRATE);
        dropSelf(BlocksSD.CHISELED_POLISHED_DRIPSTONE);
        dropSelf(BlocksSD.POLISHED_DRIPSTONE);
        dropSelf(BlocksSD.POLISHED_DRIPSTONE_STAIRS);
        dropSelf(BlocksSD.POLISHED_DRIPSTONE_SLAB);
        dropSelf(BlocksSD.POLISHED_DRIPSTONE_WALL);
        dropSelf(BlocksSD.STONE_TILES);
        dropSelf(BlocksSD.STONE_TILE_STAIRS);
        dropSelf(BlocksSD.STONE_TILE_SLAB);
        dropSelf(BlocksSD.STONE_TILE_WALL);
        dropSelf(BlocksSD.STONE_PILLAR);
        createDoublePlantShearsDrop(BlocksSD.REEDS);
    }
}
