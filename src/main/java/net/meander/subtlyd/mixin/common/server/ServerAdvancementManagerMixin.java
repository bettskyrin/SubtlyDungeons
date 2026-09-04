package net.meander.subtlyd.mixin.common.server;

import net.meander.subtlyd.util.UtilSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.meander.subtlyd.world.level.biome.BiomesSD;
import net.minecraft.advancements.*;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.advancements.triggers.ConsumeItemTrigger;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.PlayerTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
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
    private static final Identifier ADVENTURING_TIME = Identifier.withDefaultNamespace("adventure/adventuring_time");

    private static final List<Item> FOODS = List.of(
            ItemsSD.APPLE_PIE,
            ItemsSD.CALAMARI,
            ItemsSD.COOKED_CALAMARI,
            ItemsSD.POTTAGE,
            Items.BROWN_MUSHROOM,
            Items.RED_MUSHROOM,
            Items.SHELF_MUSHROOM,
            ItemsSD.LIGHT_STEW
    );
    private static final List<ResourceKey<Biome>> BIOMES = List.of(
            BiomesSD.GRAVEL_BEACH
    );
    private static final List<Identifier> ADVANCEMENTS = List.of(
            BALANCED_DIET,
            COUNTRY_LODE,
            ADVENTURE_ROOT,
            SUBSPACE_BUBBLE,
            VOLUNTARY_EXILE,
            ADVENTURING_TIME
    );

    @Inject(method = "<init>", at = @At("RETURN"))
    private void modifyAdvancements(HolderLookup.Provider registries, CallbackInfo ci) {
        Map<Identifier, AdvancementHolder> modifiedMap = new HashMap<>(advancements);
        boolean isModified = false;

        for (Identifier advancementId : ADVANCEMENTS) {
            if (modifiedMap.containsKey(advancementId)) {
                ModifiedAdvancement modified = getModifiedAdvancement(registries, advancementId, modifiedMap);

                Advancement newAdvancement = new Advancement(
                        modified.parent(),
                        modified.displayInfo(),
                        modified.rewards(),
                        modified.criteria(),
                        new AdvancementRequirements(modified.mutableRequirements()),
                        modified.sendsTelemetryEvent()
                );

                modifiedMap.put(advancementId, new AdvancementHolder(advancementId, newAdvancement));

                isModified = true;
            }
        }

        if (isModified) {
            AdvancementTree newTree = new AdvancementTree();
            advancements = Map.copyOf(modifiedMap);

            newTree.addAll(advancements.values());

            for (AdvancementNode root : newTree.roots()) {
                if (root.holder().value().display().isPresent()) {
                    TreeNodePosition.run(root);
                }
            }

            tree = newTree;
        }
    }

    private void addCriterion(Map<String, Criterion<?>> criteria, Criterion<?> criterion, List<List<String>> requirements, String criterionName) {
        if (criterion != null) {
            criteria.put(criterionName, criterion);
        }

        requirements.add(List.of(criterionName));
    }

    @SuppressWarnings("unchecked")
    private void modifyCriteria(Map<String, Criterion<?>> criteria, List<List<String>> requirements, HolderLookup.Provider registries, List<?> criteriaList) {
        for (Object entry : criteriaList) {
            Identifier id;
            String criterionName;
            Criterion<?> criterion;

            if (criteriaList == FOODS) {
                Item item = (Item) entry;
                id = BuiltInRegistries.ITEM.getKey(item);
                HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);

                ItemPredicate.Builder itemPredicate = ItemPredicate.Builder.item().of(items, item);
                criterion = ConsumeItemTrigger.TriggerInstance.usedItem(itemPredicate);
            } else if (criteriaList == BIOMES) {
                if (entry instanceof ResourceKey<?> key) {
                    ResourceKey<Biome> biome = (ResourceKey<Biome>) key;
                    HolderGetter<Biome> biomes = registries.lookupOrThrow(Registries.BIOME);
                    id = biome.identifier();
                    criterion = PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inBiome(biomes.getOrThrow(biome)));
                } else {
                    continue;
                }
            } else {
                return;
            }

            criterionName = id.getPath();

            addCriterion(criteria, criterion, requirements, criterionName);
        }
    }

    @SuppressWarnings("deprecation")
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

    private ModifiedAdvancement getModifiedAdvancement(HolderLookup.Provider registries, Identifier advancementId, Map<Identifier, AdvancementHolder> modifiedMap) {
        Advancement original = modifiedMap.get(advancementId).value();

        Optional<Identifier> parent = original.parent();
        Optional<DisplayInfo> displayInfo = original.display();
        AdvancementRewards rewards = original.rewards();
        Map<String, Criterion<?>> criteria = new HashMap<>(original.criteria());
        List<List<String>> mutableRequirements = new ArrayList<>(original.requirements().requirements());
        boolean sendsTelemetryEvent = original.sendsTelemetryEvent();

        if (advancementId == BALANCED_DIET) {
            modifyCriteria(criteria, mutableRequirements, registries, FOODS);
        } else if (advancementId == ADVENTURE_ROOT) {
            displayInfo = modifyDisplay(displayInfo, registries, Items.BURIED_TREASURE_MAP);
        } else if (advancementId == SUBSPACE_BUBBLE) {
            displayInfo = modifyDisplay(displayInfo, registries, Items.FILLED_MAP);
        } else if (advancementId == VOLUNTARY_EXILE) {
            parent = Optional.of(Identifier.withDefaultNamespace("adventure/kill_a_mob"));
            displayInfo = modifyDisplay(displayInfo, registries, Items.OMINOUS_BOTTLE);

            criteria.clear();
            mutableRequirements.clear();
            modifyCriteria(criteria, mutableRequirements, registries, Collections.singletonList(Items.OMINOUS_BOTTLE));
        } else if (advancementId == COUNTRY_LODE) {
            parent = Optional.of(UtilSD.identifier("adventure/banner_marker"));
        } else if (advancementId == ADVENTURING_TIME) {
            modifyCriteria(criteria, mutableRequirements, registries, BIOMES);
        }

        return new ModifiedAdvancement(parent, displayInfo, rewards, criteria, mutableRequirements, sendsTelemetryEvent);
    }

    private record ModifiedAdvancement(Optional<Identifier> parent, Optional<DisplayInfo> displayInfo, AdvancementRewards rewards, Map<String, Criterion<?>> criteria, List<List<String>> mutableRequirements, boolean sendsTelemetryEvent) {
    }
}
