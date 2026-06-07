package net.meander.subtlyd.data;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.meander.subtlyd.advancements.triggers.PlayerTriggerSD;
import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdvancementProviderSD extends FabricAdvancementProvider {
    protected AdvancementProviderSD(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> consumer) {
        Advancement.Builder.advancement()
                .parent(createPlaceholder(Identifier.withDefaultNamespace("adventure/sleep_in_bed")))
                .display(
                        ItemsSD.TENT.red(),
                        Component.translatable("advancements.subtlyd.camp_far_away.title"),
                        Component.translatable("advancements.subtlyd.camp_far_away.description"),
                        null,
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        false

                )
                .addCriterion("camped_far_away", PlayerTriggerSD.TriggerInstance.campedFarAway(1000))
                .save(consumer, Util.identifier("adventure/camp_far_away").toString());
    }
}
