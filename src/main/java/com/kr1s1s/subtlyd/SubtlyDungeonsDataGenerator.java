package com.kr1s1s.subtlyd;

import com.kr1s1s.subtlyd.data.ModelProviderSD;
import com.kr1s1s.subtlyd.data.RecipeProviderSD;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class SubtlyDungeonsDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(RecipeProviderSD::new);
        pack.addProvider(ModelProviderSD::new);
	}
}
