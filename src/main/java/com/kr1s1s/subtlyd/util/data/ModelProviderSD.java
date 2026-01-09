package com.kr1s1s.subtlyd.util.data;

import com.kr1s1s.subtlyd.world.block.BlocksSD;
import com.kr1s1s.subtlyd.world.item.ItemsSD;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class ModelProviderSD extends FabricModelProvider {
    public ModelProviderSD(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerator) {
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
        blockModelGenerator.createTrivialCube(BlocksSD.CHARCOAL_BLOCK);
        blockModelGenerator.createTrivialCube(BlocksSD.CHISELED_POLISHED_DRIPSTONE);
        blockModelGenerator.createAxisAlignedPillarBlock(BlocksSD.STONE_PILLAR, TexturedModel.COLUMN);
        createCubeFromVanilla(Blocks.IRON_BARS, BlocksSD.IRON_GRATE, blockModelGenerator);
        blockModelGenerator.createDoublePlant(BlocksSD.REEDS, BlockModelGenerators.PlantType.NOT_TINTED);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        for (Item item : ItemsSD.TENT_ITEM_LIST) {
            itemModelGenerator.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
        }
        itemModelGenerator.generateFlatItem(ItemsSD.APPLE_PIE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.CALAMARI, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.COOKED_CALAMARI, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.UNLIT_CAMPFIRE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.POTTAGE, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ItemsSD.REEDS, ModelTemplates.FLAT_ITEM);
    }

    public static void createCubeFromVanilla(Block vanillaBlock, Block newBlock, BlockModelGenerators blockModelGenerators) {
        TextureMapping mapping = TextureMapping.cube(vanillaBlock);
        Identifier model = ModelTemplates.CUBE_ALL.create(newBlock, mapping, blockModelGenerators.modelOutput);
        MultiVariant multiVariant = BlockModelGenerators.plainVariant(model);
        blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(newBlock, multiVariant));
    }
}
