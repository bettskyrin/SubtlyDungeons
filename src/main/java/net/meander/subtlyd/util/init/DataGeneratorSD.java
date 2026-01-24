package net.meander.subtlyd.util.init;

import net.meander.subtlyd.world.level.levelgen.BiomeProviderSD;
import net.meander.subtlyd.util.data.ModelProviderSD;
import net.meander.subtlyd.util.data.RecipeProviderSD;
import net.meander.subtlyd.util.data.loot_table.BlockLootSD;
import net.meander.subtlyd.util.data.tags.BiomeTagsSD;
import net.meander.subtlyd.util.data.tags.BlockTagsSD;
import net.meander.subtlyd.util.data.tags.DamageTypeTagsSD;
import net.meander.subtlyd.util.data.tags.ItemTagsSD;
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
