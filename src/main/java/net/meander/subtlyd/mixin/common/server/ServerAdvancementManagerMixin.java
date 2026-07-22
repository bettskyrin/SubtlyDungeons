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
    private static final Identifier BALANCED_DIET = Identifier.withDefaultNamespace("husbandry/balanced_diet");
    private static final Identifier COUNTRY_LODE = Identifier.withDefaultNamespace("adventure/use_lodestone");
    private static final Identifier ADVENTURE_ROOT = Identifier.withDefaultNamespace("adventure/root");

    private static final List<Identifier> ADVANCEMENTS = List.of(BALANCED_DIET, COUNTRY_LODE, ADVENTURE_ROOT);

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onAdvancementsLoaded(HolderLookup.Provider registries, CallbackInfo ci) {
        Map<Identifier, AdvancementHolder> modifiedMap = new HashMap<>(advancements);
        boolean isModified = false;

        for (Identifier advancementId : ADVANCEMENTS) {
            if (modifiedMap.containsKey(advancementId)) {
                Advancement oldAdvancement = modifiedMap.get(advancementId).value();

                Optional<Identifier> parent = getParent(advancementId, oldAdvancement);
                Optional<DisplayInfo> displayInfo = oldAdvancement.display();
                AdvancementRewards rewards = oldAdvancement.rewards();
                Map<String, Criterion<?>> criteria = new HashMap<>(oldAdvancement.criteria());
                List<List<String>> mutableRequirements = new ArrayList<>(oldAdvancement.requirements().requirements());
                boolean sendsTelemetryEvent = oldAdvancement.sendsTelemetryEvent();

                if (advancementId.equals(BALANCED_DIET)) {
                    modifyBalancedDiet(criteria, mutableRequirements, registries);
                } else if (advancementId.equals(ADVENTURE_ROOT)) {
                    displayInfo = modifyAdventureRootDisplay(displayInfo, registries);
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

    private void modifyBalancedDiet(Map<String, Criterion<?>> criteria, List<List<String>> requirements, HolderLookup.Provider registries) {
        Item[] foods = new Item[]{
                ItemsSD.APPLE_PIE,
                ItemsSD.CALAMARI,
                ItemsSD.COOKED_CALAMARI,
                ItemsSD.POTTAGE,
                Items.BROWN_MUSHROOM,
                Items.RED_MUSHROOM,
                Items.SHELF_MUSHROOM,
                ItemsSD.LIGHT_STEW
        };
        HolderGetter<Item> itemGetter = registries.lookupOrThrow(Registries.ITEM);

        for (Item food : foods) {
            Identifier foodID = BuiltInRegistries.ITEM.getKey(food);
            String criteriaName = foodID.getPath();
            ItemPredicate.Builder itemPredicate = ItemPredicate.Builder.item().of(itemGetter, food);
            Criterion<?> newCriterion = ConsumeItemTrigger.TriggerInstance.usedItem(itemPredicate);

            criteria.put(criteriaName, newCriterion);
            requirements.add(List.of(criteriaName));
        }
    }

    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "deprecation"})
    private Optional<DisplayInfo> modifyAdventureRootDisplay(Optional<DisplayInfo> displayInfo, HolderLookup.Provider registries) {
        return displayInfo.map(info -> {
            HolderGetter<Item> itemGetter = registries.lookupOrThrow(Registries.ITEM);
            Holder.Reference<Item> mapHolder = itemGetter.getOrThrow(Items.FILLED_MAP.builtInRegistryHolder().key());
            ItemStackTemplate mapTemplate = new ItemStackTemplate(mapHolder, 1, DataComponentPatch.EMPTY);

            return new DisplayInfo(
                    mapTemplate,
                    info.getTitle(),
                    info.getDescription(),
                    info.getBackground(),
                    info.getType(),
                    info.shouldShowToast(),
                    info.shouldAnnounceChat(),
                    info.isHidden());
        });
    }

    private Optional<Identifier> getParent(Identifier advancementId, Advancement advancement) {
        if (advancementId.equals(COUNTRY_LODE)) {
            return Optional.of(UtilSD.identifier("adventure/banner_marker"));
        }
        return advancement.parent();
    }
}
