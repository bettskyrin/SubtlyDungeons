package com.meander.subtlyd.util.init;

import com.meander.subtlyd.world.level.levelgen.BiomeProviderSD;
import com.meander.subtlyd.util.data.ModelProviderSD;
import com.meander.subtlyd.util.data.RecipeProviderSD;
import com.meander.subtlyd.util.data.loot_table.BlockLootSD;
import com.meander.subtlyd.util.data.tags.BiomeTagsSD;
import com.meander.subtlyd.util.data.tags.BlockTagsSD;
import com.meander.subtlyd.util.data.tags.DamageTypeTagsSD;
import com.meander.subtlyd.util.data.tags.ItemTagsSD;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGeneratorSD implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModelProviderSD::new);
        pack.addProvider(BiomeTagsSD::new);
        pack.addProvider(ItemTagsSD::new);
        pack.addProvider(BlockTagsSD::new);
        pack.addProvider(RecipeProviderSD::new);
        pack.addProvider(BlockLootSD::new);
        pack.addProvider(DamageTypeTagsSD::new);
        pack.addProvider(BiomeProviderSD::new);
	}
}
