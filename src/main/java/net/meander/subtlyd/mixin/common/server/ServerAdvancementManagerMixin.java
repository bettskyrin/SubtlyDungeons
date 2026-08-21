package net.meander.subtlyd.mixin.common.server;

import net.meander.subtlyd.util.UtilSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.advancements.*;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.ConsumeItemTrigger;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(ServerAdvancementManager.class)
public class ServerAdvancementManagerMixin {
    @Shadow @Final @Mutable private Map<Identifier, AdvancementHolder> advancements;
    @Shadow @Final @Mutable private AdvancementTree tree;

    private static final Identifier ADVENTURE_ROOT = Identifier.withDefaultNamespace("adventure/root");
    private static final Identifier BALANCED_DIET = Identifier.withDefaultNamespace("husbandry/balanced_diet");
    private static final Identifier COUNTRY_LODE = Identifier.withDefaultNamespace("adventure/use_lodestone");
    private static final Identifier SUBSPACE_BUBBLE = Identifier.withDefaultNamespace("nether/fast_travel");
    private static final Identifier VOLUNTARY_EXILE = Identifier.withDefaultNamespace("adventure/voluntary_exile");

    private static final Item[] FOODS = new Item[]{
            ItemsSD.APPLE_PIE,
            ItemsSD.CALAMARI,
            ItemsSD.COOKED_CALAMARI,
            ItemsSD.POTTAGE,
            Items.BROWN_MUSHROOM,
            Items.RED_MUSHROOM,
            Items.SHELF_MUSHROOM,
            ItemsSD.LIGHT_STEW
    };
    private static final List<Identifier> ADVANCEMENTS = List.of(BALANCED_DIET, COUNTRY_LODE, ADVENTURE_ROOT, SUBSPACE_BUBBLE, VOLUNTARY_EXILE);

    @Inject(method = "<init>", at = @At("RETURN"))
    private void modifyAdvancements(HolderLookup.Provider registries, CallbackInfo ci) {
        Map<Identifier, AdvancementHolder> modifiedMap = new HashMap<>(advancements);
        boolean isModified = false;

        for (Identifier advancementId : ADVANCEMENTS) {
            if (modifiedMap.containsKey(advancementId)) {
                Advancement oldAdvancement = modifiedMap.get(advancementId).value();

                Optional<Identifier> parent = oldAdvancement.parent();
                Optional<DisplayInfo> displayInfo = oldAdvancement.display();
                AdvancementRewards rewards = oldAdvancement.rewards();
                Map<String, Criterion<?>> criteria = new HashMap<>(oldAdvancement.criteria());
                List<List<String>> mutableRequirements = new ArrayList<>(oldAdvancement.requirements().requirements());
                boolean sendsTelemetryEvent = oldAdvancement.sendsTelemetryEvent();

                if (advancementId == BALANCED_DIET) {
                    modifyCriteriaItems(criteria, mutableRequirements, registries, FOODS);
                } else if (advancementId == ADVENTURE_ROOT) {
                    displayInfo = modifyDisplay(displayInfo, registries, Items.BURIED_TREASURE_MAP);
                } else if (advancementId == SUBSPACE_BUBBLE) {
                    displayInfo = modifyDisplay(displayInfo, registries, Items.FILLED_MAP);
                } else if (advancementId == VOLUNTARY_EXILE) {
                    parent = Optional.of(Identifier.withDefaultNamespace("adventure/kill_a_mob"));
                    displayInfo = modifyDisplay(displayInfo, registries, Items.OMINOUS_BOTTLE);

                    criteria.clear();
                    mutableRequirements.clear();
                    addConsumeItemCriterion(criteria, mutableRequirements, registries, Items.OMINOUS_BOTTLE);
                } else if (advancementId == COUNTRY_LODE) {
                    parent = Optional.of(UtilSD.identifier("adventure/banner_marker"));
                }

                Advancement newAdvancement = new Advancement(
                        parent,
                        displayInfo,
                        rewards,
                        criteria,
                        new AdvancementRequirements(mutableRequirements),
                        sendsTelemetryEvent
                );

                modifiedMap.put(advancementId, new AdvancementHolder(advancementId, newAdvancement));

                isModified = true;
            }
        }

        if (isModified) {
            advancements = Map.copyOf(modifiedMap);

            AdvancementTree newTree = new AdvancementTree();
            newTree.addAll(advancements.values());

            for (AdvancementNode root : newTree.roots()) {
                if (root.holder().value().display().isPresent()) {
                    TreeNodePosition.run(root);
                }
            }

            tree = newTree;
        }
    }

    private void addConsumeItemCriterion(Map<String, Criterion<?>> criteria, List<List<String>> requirements, HolderLookup.Provider registries, Item item) {
        HolderGetter<Item> itemGetter = registries.lookupOrThrow(Registries.ITEM);

        Identifier itemId = BuiltInRegistries.ITEM.getKey(item);
        String criterionName = itemId.getPath();
        ItemPredicate.Builder itemPredicate = ItemPredicate.Builder.item().of(itemGetter, item);
        Criterion<?> criterion = ConsumeItemTrigger.TriggerInstance.usedItem(itemPredicate);

        criteria.put(criterionName, criterion);
        requirements.add(List.of(criterionName));
    }

    private void modifyCriteriaItems(Map<String, Criterion<?>> criteria, List<List<String>> requirements, HolderLookup.Provider registries, Item[] itemList) {
        for (Item item : itemList) {
            addConsumeItemCriterion(criteria, requirements, registries, item);
        }
    }

    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "deprecation"})
    private Optional<DisplayInfo> modifyDisplay(final Optional<DisplayInfo> displayInfo, final HolderLookup.Provider registries, final Item item) {
        return displayInfo.map(info -> {
            HolderGetter<Item> itemGetter = registries.lookupOrThrow(Registries.ITEM);
            Holder.Reference<Item> mapHolder = itemGetter.getOrThrow(item.builtInRegistryHolder().key());
            ItemStackTemplate displayItem = new ItemStackTemplate(mapHolder, 1, DataComponentPatch.EMPTY);

            return new DisplayInfo(
                    displayItem,
                    info.title(),
                    info.description(),
                    info.background(),
                    info.type(),
                    info.showToast(),
                    info.announceToChat(),
                    info.hidden());
        });
    }
}
