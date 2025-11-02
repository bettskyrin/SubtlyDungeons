package com.kr1s1s.subtlyd.data;

import com.kr1s1s.subtlyd.world.block.BlocksSD;
import com.kr1s1s.subtlyd.world.item.ItemsSD;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashSet;
import java.util.List;

public class ModelProviderSD extends FabricModelProvider {
    public ModelProviderSD(FabricDataOutput output) {
        super(output);
    }

    private void init() {
        ItemsSD.BLOCKS_SET.addAll(List.of(ItemsSD.CUBE_ALL, ItemsSD.COLUMN));
        ItemsSD.ITEMS_SET.add(ItemsSD.FLAT_ITEMS);
    }

    @Override public void generateBlockStateModels(BlockModelGenerators blockModelGenerator) {
        init();
        blockModelGenerator.family(BlocksSD.SNOW_BRICKS).generateFor(new BlockFamily.Builder(BlocksSD.SNOW_BRICKS)
                .stairs(BlocksSD.SNOW_BRICK_STAIRS)
                .slab(BlocksSD.SNOW_BRICK_SLAB)
                .wall(BlocksSD.SNOW_BRICK_WALL).getFamily());
        blockModelGenerator.family(BlocksSD.POLISHED_DRIPSTONE).generateFor(new BlockFamily.Builder(BlocksSD.POLISHED_DRIPSTONE)
                .stairs(BlocksSD.POLISHED_DRIPSTONE_STAIRS)
                .slab(BlocksSD.POLISHED_DRIPSTONE_SLAB)
                .wall(BlocksSD.POLISHED_DRIPSTONE_WALL).getFamily());
        blockModelGenerator.family(BlocksSD.STONE_TILES).generateFor(new BlockFamily.Builder(BlocksSD.STONE_TILES)
                .stairs(BlocksSD.STONE_TILE_STAIRS)
                .slab(BlocksSD.STONE_TILE_SLAB)
                .wall(BlocksSD.STONE_TILE_WALL).getFamily());
//        blockModelGenerator.createTrivialCube(BlocksSD.CHARCOAL_BLOCK);
        createCubeFromVanilla(Blocks.IRON_BARS, BlocksSD.IRON_GRATE, blockModelGenerator);
//        blockModelGenerator.createAxisAlignedPillarBlock(BlocksSD.STONE_PILLAR, TexturedModel.COLUMN);
//        blockModelGenerator.createTrivialCube(BlocksSD.CHISELED_POLISHED_DRIPSTONE);

        for (HashSet<?> hashSet : ItemsSD.BLOCKS_SET) {
            if (hashSet.equals(ItemsSD.CUBE_ALL)) {
                for (Object block : hashSet) {
                    blockModelGenerator.createTrivialCube((Block) block);
                }
            } else if (hashSet.equals(ItemsSD.COLUMN)) {
                for (Object block : hashSet) {
                    blockModelGenerator.createAxisAlignedPillarBlock((Block) block, TexturedModel.COLUMN);
                }
            }
        }
    }

    @Override public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        for (HashSet<?> hashSet : ItemsSD.BLOCKS_SET) {
            if (hashSet.equals(ItemsSD.FLAT_ITEMS)) {
                for (Object item : hashSet) {
                    itemModelGenerator.generateFlatItem((Item) item, ModelTemplates.FLAT_ITEM);
                }
            }
        }
    }

    public static void createCubeFromVanilla(Block vanillaBlock, Block newBlock, BlockModelGenerators blockModelGenerators) {
        TextureMapping mapping = TextureMapping.cube(vanillaBlock);
        ResourceLocation model = ModelTemplates.CUBE_ALL.create(newBlock, mapping, blockModelGenerators.modelOutput);
        MultiVariant multiVariant = BlockModelGenerators.plainVariant(model);
        blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(newBlock, multiVariant));
    }
}
