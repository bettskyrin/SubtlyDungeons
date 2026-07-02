package net.meander.subtlyd.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.meander.subtlyd.advancements.AdvancementProviderSD;
import net.meander.subtlyd.client.camera.shake.CameraShakeEventData;
import net.meander.subtlyd.client.camera.shake.CameraShakeEventProvider;
import net.meander.subtlyd.client.data.model.ModelProviderSD;
import net.meander.subtlyd.client.data.model.PotionModelProviderSD;
import net.meander.subtlyd.core.registries.RegistriesSD;
import net.meander.subtlyd.data.recipies.RecipeProviderSD;
import net.meander.subtlyd.data.worldgen.features.AquaticFeaturesSD;
import net.meander.subtlyd.data.worldgen.features.VegetationFeaturesSD;
import net.meander.subtlyd.data.worldgen.placement.AquaticPlacementsSD;
import net.meander.subtlyd.data.worldgen.placement.MiscOverworldPlacementsSD;
import net.meander.subtlyd.data.worldgen.placement.VegetationPlacementsSD;
import net.meander.subtlyd.tags.*;
import net.meander.subtlyd.world.item.enchantment.EnchantmentProvider;
import net.meander.subtlyd.world.item.enchantment.EnchantmentsSD;
import net.meander.subtlyd.world.level.levelgen.WorldGeneratorSD;
import net.meander.subtlyd.world.level.levelgen.feature.FeatureProvider;
import net.meander.subtlyd.data.loot.packs.BlockLootSD;
import net.meander.subtlyd.world.level.storage.loot.LootTablesSD;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;

public class DataGeneratorSD implements DataGeneratorEntrypoint {
    public static boolean isDataGeneratorRunning = System.getProperty("fabric-api.datagen") != null;

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModelProviderSD::new);
        pack.addProvider((PackOutput output) -> new PotionModelProviderSD(output));
        pack.addProvider(BiomeTagsSD::new);
        pack.addProvider((PackOutput output) -> new WorldGeneratorSD(output));
        pack.addProvider(ItemTagsSD::new);
        pack.addProvider(PotionTagsSD::new);
        pack.addProvider(EnchantmentTagsSD::new);
        pack.addProvider(BlockTagsSD::new);
        pack.addProvider(EntityTypeTagsSD::new);
        pack.addProvider(RecipeProviderSD::new);
        pack.addProvider(LootTablesSD::create);
        pack.addProvider(BlockLootSD::new);
        pack.addProvider(DamageTypeTagsSD::new);
        pack.addProvider(FeatureProvider::new);
        pack.addProvider(AdvancementProviderSD::new);
        pack.addProvider(LanguageProviderSD::new);
        pack.addProvider(EnchantmentProvider::new);
        pack.addProvider(CameraShakeEventProvider::new);
	}

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.ENCHANTMENT, EnchantmentsSD::bootstrap);
        registryBuilder.add(RegistriesSD.CAMERA_SHAKE_EVENT, CameraShakeEventData::bootstrap);
        registryBuilder.add(Registries.FEATURE, AquaticFeaturesSD::bootstrap);
        registryBuilder.add(Registries.FEATURE, VegetationFeaturesSD::bootstrap);
        registryBuilder.add(Registries.PLACED_FEATURE, AquaticPlacementsSD::bootstrap);
        registryBuilder.add(Registries.PLACED_FEATURE, MiscOverworldPlacementsSD::bootstrap);
        registryBuilder.add(Registries.PLACED_FEATURE, VegetationPlacementsSD::bootstrap);
    }
}
