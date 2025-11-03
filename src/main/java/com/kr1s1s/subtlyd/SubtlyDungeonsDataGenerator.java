package com.kr1s1s.subtlyd;

import com.kr1s1s.subtlyd.world.level.levelgen.BiomeProviderSD;
import com.kr1s1s.subtlyd.data.ModelProviderSD;
import com.kr1s1s.subtlyd.data.RecipeProviderSD;
import com.kr1s1s.subtlyd.data.loot_table.BlockLootSD;
import com.kr1s1s.subtlyd.data.loot_table.LootSD;
import com.kr1s1s.subtlyd.data.tags.BiomeTagsSD;
import com.kr1s1s.subtlyd.data.tags.BlockTagsSD;
import com.kr1s1s.subtlyd.data.tags.DamageTypeTagsSD;
import com.kr1s1s.subtlyd.data.tags.ItemTagsSD;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

@SuppressWarnings("unused")
public class SubtlyDungeonsDataGenerator implements DataGeneratorEntrypoint {
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
        LootSD.registration();
	}
}
