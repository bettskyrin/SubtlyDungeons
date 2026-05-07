package net.meander.subtlyd.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.meander.subtlyd.client.camera.shake.CameraShakeEventData;
import net.meander.subtlyd.core.registries.RegistriesSD;
import net.meander.subtlyd.data.loot_table.BlockLootSD;
import net.meander.subtlyd.data.tags.*;
import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.item.enchantment.EnchantmentsSD;
import net.meander.subtlyd.world.level.levelgen.BiomeProviderSD;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class DataGeneratorSD implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModelProviderSD::new);
        pack.addProvider(BiomeTagsSD::new);
        pack.addProvider(ItemTagsSD::new);
        pack.addProvider(EnchantmentTagsSD::new);
        pack.addProvider(BlockTagsSD::new);
        pack.addProvider(EntityTypeTagsSD::new);
        pack.addProvider(RecipeProviderSD::new);
        pack.addProvider(BlockLootSD::new);
        pack.addProvider(DamageTypeTagsSD::new);
        pack.addProvider(BiomeProviderSD::new);
        pack.addProvider(LanguageProviderSD::new);

        pack.addProvider((output, registriesFuture) -> new FabricDynamicRegistryProvider(output, registriesFuture) {
            @Override
            protected void configure(HolderLookup.Provider registries, Entries entries) {
                try {
                    entries.addAll(registries.lookupOrThrow(Registries.ENCHANTMENT));
                    entries.addAll(registries.lookupOrThrow(RegistriesSD.CAMERA_SHAKE_EVENT));
                } catch (Exception e) {
                    Util.LOGGER.error("Failed to configure dynamic registries: {}", e.getMessage());
                }
            }

            @Override
            public String getName() {
                return "Subtly Dungeons Dynamic Registries";
            }
        });
	}

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.ENCHANTMENT, EnchantmentsSD::bootstrap);
        registryBuilder.add(RegistriesSD.CAMERA_SHAKE_EVENT, CameraShakeEventData::bootstrap);
    }
}
