package net.meander.subtlyd.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.meander.subtlyd.client.camera.shake.CameraShakeEventData;
import net.meander.subtlyd.core.registries.RegistriesSD;
import net.meander.subtlyd.data.loot_table.BlockLootSD;
import net.meander.subtlyd.data.tags.*;
import net.meander.subtlyd.world.item.enchantment.EnchantmentsSD;
import net.meander.subtlyd.world.level.levelgen.BiomeProviderSD;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;

public class DataGeneratorSD implements DataGeneratorEntrypoint {
    public static boolean isDataGeneratorRunning = System.getProperty("fabric-api.datagen") != null;

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModelProviderSD::new);
        pack.addProvider((PackOutput output) -> new PotionProviderSD(output));
        pack.addProvider(BiomeTagsSD::new);
        pack.addProvider(ItemTagsSD::new);
        pack.addProvider(PotionTagsSD::new);
        pack.addProvider(EnchantmentTagsSD::new);
        pack.addProvider(BlockTagsSD::new);
        pack.addProvider(EntityTypeTagsSD::new);
        pack.addProvider(RecipeProviderSD::new);
        pack.addProvider(BlockLootSD::new);
        pack.addProvider(DamageTypeTagsSD::new);
        pack.addProvider(BiomeProviderSD::new);
        pack.addProvider(AdvancementProviderSD::new);
        pack.addProvider(LanguageProviderSD::new);
        pack.addProvider(EnchantmentProvider::new);
        pack.addProvider(CameraShakeEventProvider::new);
	}

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.ENCHANTMENT, EnchantmentsSD::bootstrap);
        registryBuilder.add(RegistriesSD.CAMERA_SHAKE_EVENT, CameraShakeEventData::bootstrap);
    }
}
