package net.meander.subtlyd.advancements.packs;

import net.meander.subtlyd.advancements.triggers.ConstructConduitTrigger;
import net.meander.subtlyd.advancements.triggers.SleptInTentTrigger;
import net.meander.subtlyd.advancements.triggers.StealthAttackTrigger;
import net.meander.subtlyd.tags.BlockTagsSD;
import net.meander.subtlyd.util.UtilSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.predicates.BlockPredicate;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
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
    public static final Component CAMP_FAR_AWAY_TITLE = Component.translatable("advancements.subtlyd.camp_far_away.title");
    public static final Component CAMP_FAR_AWAY_DESC = Component.translatable("advancements.subtlyd.camp_far_away.description");
    public static final Component BANNER_MARKER_TITLE = Component.translatable("advancements.subtlyd.banner_marker.title");
    public static final Component BANNER_MARKER_DESC = Component.translatable("advancements.subtlyd.banner_marker.description");
    public static final Component STEALTH_ATTACK_TITLE = Component.translatable("advancements.subtlyd.stealth_attack.title");
    public static final Component STEALTH_ATTACK_DESC = Component.translatable("advancements.subtlyd.stealth_attack.description");
    public static final Component CREATE_CONDUIT_TITLE = Component.translatable("advancements.subtlyd.create_conduit.title");
    public static final Component CREATE_CONDUIT_DESC = Component.translatable("advancements.subtlyd.create_conduit.description");
    public static final Component CREATE_FULL_CONDUIT_TITLE = Component.translatable("advancements.subtlyd.create_full_conduit.title");
    public static final Component CREATE_FULL_CONDUIT_DESC = Component.translatable("advancements.subtlyd.create_full_conduit.description");

    public static void register(HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> consumer) {
        AdvancementHolder root = createPlaceholder(Identifier.withDefaultNamespace("adventure/root"));

        Advancement.Builder.advancement()
                .parent(createPlaceholder(Identifier.withDefaultNamespace("adventure/sleep_in_bed")))
                .display(
                        ItemsSD.TENT.red(),
                        CAMP_FAR_AWAY_TITLE,
                        CAMP_FAR_AWAY_DESC,
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        false

                )
                .addCriterion("camped_far_away", SleptInTentTrigger.TriggerInstance.campedFarAway(1000))
                .save(consumer, UtilSD.identifier("adventure/camp_far_away"));
        Advancement.Builder.advancement()
                .parent(root)
                .display(
                        Items.FILLED_MAP,
                        BANNER_MARKER_TITLE,
                        BANNER_MARKER_DESC,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("created_banner_marker", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                        LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(registryLookup.lookupOrThrow(Registries.BLOCK), BlockTags.BANNERS)),
                        ItemPredicate.Builder.item().of(registryLookup.lookupOrThrow(Registries.ITEM), Items.FILLED_MAP)))
                .save(consumer, UtilSD.identifier("adventure/banner_marker"));
        Advancement.Builder.advancement()
                .parent(createPlaceholder(Identifier.withDefaultNamespace("adventure/kill_a_mob")))
                .display(
                        ItemsSD.IRON_DAGGER,
                        STEALTH_ATTACK_TITLE,
                        STEALTH_ATTACK_DESC,
                        AdvancementType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion("do_stealth_attack", StealthAttackTrigger.TriggerInstance.stealthAttack(
                        EntityPredicate.Builder.entity().located(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(registryLookup.lookupOrThrow(Registries.BLOCK), BlockTagsSD.TALL_PLANTS)))))
                .save(consumer, UtilSD.identifier("adventure/stealth_attack"));
        AdvancementHolder createConduit = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        Items.HEART_OF_THE_SEA,
                        CREATE_CONDUIT_TITLE,
                        CREATE_CONDUIT_DESC,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("conduit", ConstructConduitTrigger.TriggerInstance.constructedConduit())
                .save(consumer, UtilSD.identifier("adventure/create_conduit"));
        Advancement.Builder.advancement()
                .parent(createConduit)
                .display(
                        Items.HEART_OF_THE_SEA,
                        CREATE_FULL_CONDUIT_TITLE,
                        CREATE_FULL_CONDUIT_DESC,
                        AdvancementType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion("conduit", ConstructConduitTrigger.TriggerInstance.constructedConduit(MinMaxBounds.Ints.exactly(6)))
                .save(consumer, UtilSD.identifier("adventure/create_full_conduit"));
    }
}
