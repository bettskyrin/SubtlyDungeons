package net.meander.subtlyd.advancements.packs;

import net.meander.subtlyd.advancements.triggers.SleptInTentTrigger;
import net.meander.subtlyd.advancements.triggers.StealthAttackTrigger;
import net.meander.subtlyd.tags.BlockTagsSD;
import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.predicates.BlockPredicate;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.ItemUsedOnLocationTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

import static net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider.createPlaceholder;

/**
 * @see net.minecraft.data.advancements.packs.VanillaAdventureAdvancements
 */
public class AdventureAdvancementsSD {
    public static void register(HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> consumer) {
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
                .parent(createPlaceholder(Identifier.withDefaultNamespace("adventure/kill_a_mob")))
                .display(
                        ItemsSD.IRON_DAGGER,
                        Component.translatable("advancements.subtlyd.stealth_attack.title"),
                        Component.translatable("advancements.subtlyd.stealth_attack.description"),
                        null,
                        AdvancementType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion("do_stealth_attack", StealthAttackTrigger.TriggerInstance.stealthAttack(
                        EntityPredicate.Builder.entity().located(
                                LocationPredicate.Builder.location().setBlock(
                                        BlockPredicate.Builder.block().of(
                                                registryLookup.lookupOrThrow(Registries.BLOCK),
                                                BlockTagsSD.TALL_PLANTS
                                        )
                                )
                        )
                ))
                .save(consumer, Util.identifier("adventure/stealth_attack").toString());
    }
}
