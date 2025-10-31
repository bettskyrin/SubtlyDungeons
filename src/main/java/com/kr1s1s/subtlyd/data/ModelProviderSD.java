package com.kr1s1s.subtlyd.data;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import com.kr1s1s.subtlyd.world.block.BlocksSD;
import com.kr1s1s.subtlyd.world.item.ItemsSD;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.core.Direction;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Optional;

public class ModelProviderSD extends FabricModelProvider {

    public ModelProviderSD(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerator) {
        blockModelGenerator.family(BlocksSD.SNOW_BRICKS).generateFor(new BlockFamily.Builder(BlocksSD.SNOW_BRICKS).stairs(BlocksSD.SNOW_BRICK_STAIRS).slab(BlocksSD.SNOW_BRICK_SLAB).getFamily());
        blockModelGenerator.createTrivialCube(BlocksSD.CHARCOAL_BLOCK);
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
    }
    // TODO Use Datagen for Snowy Grass Blocks
}
