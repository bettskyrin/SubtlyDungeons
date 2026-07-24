package net.meander.subtlyd.client.data.models;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;

public class ModelGeneratorSD extends FabricModelProvider {
    public ModelGeneratorSD(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        BlockModelGeneratorsSD blockModelGeneratorsSD = new BlockModelGeneratorsSD();

        blockModelGeneratorsSD.generateBlockModels(blockModelGenerators);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        ItemModelGeneratorsSD itemModelGeneratorsSD = new ItemModelGeneratorsSD();

        itemModelGeneratorsSD.generateItemModels(itemModelGenerators);
    }
}
