package net.meander.subtlyd.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.meander.subtlyd.client.camera.shake.CameraShakeEventData;
import net.meander.subtlyd.client.data.models.ModelGeneratorSD;
import net.meander.subtlyd.client.data.models.PotionModelsProviderSD;
import net.meander.subtlyd.core.registries.RegistriesSD;
import net.meander.subtlyd.data.advancements.AdvancementProviderSD;
import net.meander.subtlyd.data.loot.packs.BlockLootSD;
import net.meander.subtlyd.data.loot.packs.GameplayLootSD;
import net.meander.subtlyd.data.recipies.RecipeProviderSD;
import net.meander.subtlyd.data.registries.DynamicRegistriesSD;
import net.meander.subtlyd.data.tags.*;
import net.meander.subtlyd.data.worldgen.WorldGeneratorSD;
import net.meander.subtlyd.data.worldgen.features.AquaticFeaturesSD;
import net.meander.subtlyd.data.worldgen.features.FeatureProvider;
import net.meander.subtlyd.data.worldgen.features.TreeFeaturesSD;
import net.meander.subtlyd.data.worldgen.features.VegetationFeaturesSD;
import net.meander.subtlyd.data.worldgen.placement.AquaticPlacementsSD;
import net.meander.subtlyd.data.worldgen.placement.MiscOverworldPlacementsSD;
import net.meander.subtlyd.data.worldgen.placement.VegetationPlacementsSD;
import net.meander.subtlyd.world.item.enchantment.EnchantmentsSD;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class DataGeneratorSD implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        tags(pack);
        level(pack);
        advancements(pack);
        items(pack);
        assets(pack);
    }

    private void assets(FabricDataGenerator.Pack pack) {
        pack.addProvider(PotionModelsProviderSD::new);
        pack.addProvider(ModelGeneratorSD::new);
        pack.addProvider(LanguageProviderSD::new);
    }

    private void level(FabricDataGenerator.Pack pack) {
        pack.addProvider(BiomeTagsProviderSD::new);
        pack.addProvider(WorldGeneratorSD::new);
        pack.addProvider(FeatureProvider::new);
        pack.addProvider(DynamicRegistriesSD::new);
    }

    private void tags(FabricDataGenerator.Pack pack) {
        pack.addProvider(ItemTagsProviderSD::new);
        pack.addProvider(PotionTagsProviderSD::new);
        pack.addProvider(EnchantmentTagsProviderSD::new);
        pack.addProvider(BlockTagsProviderSD::new);
        pack.addProvider(EntityTypeTagsProviderSD::new);
        pack.addProvider(DamageTypeTagsProviderSD::new);
    }

    private void advancements(FabricDataGenerator.Pack pack) {
        pack.addProvider(RecipeProviderSD::new);
        pack.addProvider(AdvancementProviderSD::new);
    }

    private void items(FabricDataGenerator.Pack pack) {
        pack.addProvider(GameplayLootSD::new);
        pack.addProvider(BlockLootSD::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.ENCHANTMENT, EnchantmentsSD::bootstrap);
        registryBuilder.add(RegistriesSD.CAMERA_SHAKE_EVENT, CameraShakeEventData::bootstrap);
        registryBuilder.add(Registries.FEATURE, TreeFeaturesSD::registration);
        registryBuilder.add(Registries.FEATURE, AquaticFeaturesSD::bootstrap);
        registryBuilder.add(Registries.FEATURE, VegetationFeaturesSD::registration);
        registryBuilder.add(Registries.PLACED_FEATURE, AquaticPlacementsSD::bootstrap);
        registryBuilder.add(Registries.PLACED_FEATURE, MiscOverworldPlacementsSD::registration);
        registryBuilder.add(Registries.PLACED_FEATURE, VegetationPlacementsSD::registration);
    }
}
