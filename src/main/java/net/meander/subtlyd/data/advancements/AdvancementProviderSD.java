package net.meander.subtlyd.data.advancements;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.meander.subtlyd.advancements.packs.AdventureAdvancementsSD;
import net.meander.subtlyd.advancements.packs.HusbandryAdvancementsSD;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * @see net.minecraft.data.advancements.AdvancementProvider
 */
public class AdvancementProviderSD extends FabricAdvancementProvider {
    public AdvancementProviderSD(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> consumer) {
        AdventureAdvancementsSD.register(registryLookup, consumer);
        HusbandryAdvancementsSD.register(registryLookup, consumer);
    }
}
