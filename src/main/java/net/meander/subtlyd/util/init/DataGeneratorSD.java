package net.meander.subtlyd.util.init;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.meander.subtlyd.util.data.ModelProviderSD;
import net.meander.subtlyd.util.data.RecipeProviderSD;
import net.meander.subtlyd.util.data.loot_table.BlockLootSD;
import net.meander.subtlyd.util.data.tags.*;
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

        pack.addProvider((output, registriesFuture) -> new FabricDynamicRegistryProvider(output, registriesFuture) {
            @Override
            protected void configure(HolderLookup.Provider registries, Entries entries) {
                entries.addAll(registries.lookupOrThrow(Registries.ENCHANTMENT));
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
    }
}
