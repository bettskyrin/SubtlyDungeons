package net.meander.subtlyd.advancements;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.meander.subtlyd.advancements.triggers.SleptInTentTrigger;
import net.meander.subtlyd.tags.ItemTagsSD;
import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.predicates.BlockPredicate;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.advancements.triggers.ItemUsedOnLocationTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdvancementProviderSD extends FabricAdvancementProvider {
    public AdvancementProviderSD(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
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
                .addCriterion("camped_far_away", SleptInTentTrigger.TriggerInstance.campedFarAway(1000))
                .save(consumer, Util.identifier("adventure/camp_far_away").toString());
        Advancement.Builder.advancement()
                .parent(createPlaceholder(Identifier.withDefaultNamespace("adventure/root")))
                .display(
                        Items.FILLED_MAP,
                        Component.translatable("advancements.subtlyd.banner_marker.title"),
                        Component.translatable("advancements.subtlyd.banner_marker.description"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("created_banner_marker", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(registryLookup.lookupOrThrow(Registries.BLOCK), BlockTags.BANNERS)),
                        ItemPredicate.Builder.item().of(registryLookup.lookupOrThrow(Registries.ITEM), Items.FILLED_MAP)))
                .save(consumer, Util.identifier("adventure/banner_marker").toString());
        Advancement.Builder.advancement()
                .parent(createPlaceholder(Identifier.withDefaultNamespace("husbandry/root")))
                .display(
                        Items.CAMPFIRE,
                        Component.translatable("advancements.subtlyd.light_campfire.title"),
                        Component.translatable("advancements.subtlyd.light_campfire.description"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("try_light_campfire_with_stick", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(registryLookup.lookupOrThrow(Registries.BLOCK), Blocks.CAMPFIRE)),
                        ItemPredicate.Builder.item().of(registryLookup.lookupOrThrow(Registries.ITEM), Items.STICK)))
                .save(consumer, Util.identifier("husbandry/light_campfire").toString());
        Advancement.Builder.advancement()
                .parent(createPlaceholder(Util.identifier("husbandry/light_campfire")))
                .display(
                        ItemsSD.LIGHT_STEW,
                        Component.translatable("advancements.subtlyd.make_stew.title"),
                        Component.translatable("advancements.subtlyd.make_stew.description"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("add_stew_ingredient", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(registryLookup.lookupOrThrow(Registries.BLOCK), BlockTags.CAULDRONS)),
                        ItemPredicate.Builder.item().of(registryLookup.lookupOrThrow(Registries.ITEM), ItemTagsSD.STEW_INGREDIENT)))
                .save(consumer, Util.identifier("husbandry/make_stew").toString());

        // TODO Sneak Attack
    }
}
