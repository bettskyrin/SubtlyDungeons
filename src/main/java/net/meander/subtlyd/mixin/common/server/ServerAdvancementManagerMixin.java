package net.meander.subtlyd.mixin.common.server;

import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.ConsumeItemTrigger;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(ServerAdvancementManager.class)
public class ServerAdvancementManagerMixin {
    private static final Identifier BALANCED_DIET = Identifier.withDefaultNamespace("husbandry/balanced_diet");
    private static final Identifier COUNTRY_LODE = Identifier.withDefaultNamespace("adventure/use_lodestone");
    private static final List<Identifier> ADVANCEMENTS = List.of(BALANCED_DIET, COUNTRY_LODE);

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("HEAD"))
    private void injectAdvancementData(Map<Identifier, Advancement> preparations, ResourceManager manager, ProfilerFiller profiler, CallbackInfo ci) {
        for (Identifier advancementId : ADVANCEMENTS) {
            if (preparations.containsKey(advancementId)) {
                Advancement advancement = preparations.get(advancementId);
                Map<String, Criterion<?>> newCriteria = new HashMap<>(advancement.criteria());
                List<List<String>> newRequirements = new ArrayList<>(advancement.requirements().requirements());
                Optional<Identifier> parent = getParent(advancementId, advancement);

                modifyCriteria(advancementId, newCriteria, newRequirements);

                preparations.put(advancementId, new Advancement(
                        parent,
                        advancement.display(),
                        advancement.rewards(),
                        newCriteria,
                        new AdvancementRequirements(newRequirements),
                        advancement.sendsTelemetryEvent()
                ));
            }
        }
    }

    private void modifyBalancedDiet(Map<String, Criterion<?>> criteria, List<List<String>> requirements) {
        List<Item> foods = List.of(
                ItemsSD.APPLE_PIE,
                ItemsSD.CALAMARI,
                ItemsSD.COOKED_CALAMARI,
                ItemsSD.POTTAGE,
                Items.BROWN_MUSHROOM,
                Items.RED_MUSHROOM,
                ItemsSD.LIGHT_STEW
        );

        for (Item food : foods) {
            Identifier foodID = BuiltInRegistries.ITEM.getKey(food);
            String criteriaName = foodID.getPath();
            ItemPredicate.Builder itemPredicate = ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, food);
            Criterion<?> newCriterion = ConsumeItemTrigger.TriggerInstance.usedItem(itemPredicate);

            criteria.put(criteriaName, newCriterion);
            requirements.add(List.of(criteriaName));
        }
    }

    private void modifyCriteria(Identifier advancement, Map<String, Criterion<?>> criteria, List<List<String>> requirements) {
        if (advancement.equals(BALANCED_DIET)) {
            modifyBalancedDiet(criteria, requirements);
        }
    }

    private Optional<Identifier> getParent(Identifier advancementId, Advancement advancement) {
        if (advancementId.equals(COUNTRY_LODE)) {
            return Optional.of(Util.identifier("adventure/banner_marker"));
        }
        return advancement.parent();
    }
}
