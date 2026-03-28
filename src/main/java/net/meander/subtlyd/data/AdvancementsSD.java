package net.meander.subtlyd.data;

import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.ConsumeItemTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Map;

public class AdvancementsSD {
    /**
     * List of Advancements to inject into
     */
    public static final Identifier BALANCED_DIET = Identifier.withDefaultNamespace("husbandry/balanced_diet");
    public static final List<Identifier> ADVANCEMENTS = List.of(BALANCED_DIET);

    /**
     * Adds new food items to the balanced diet advancement.
     * @param criteria Advancement criteria (e.g., eat an item).
     * @param requirements Advancement requirements (e.g., unlock the root advancement).
     */
    public static void balancedDiet(Map<String, Criterion<?>> criteria, List<List<String>> requirements) {
        Item[] foods = {
                ItemsSD.APPLE_PIE,
                ItemsSD.CALAMARI,
                ItemsSD.COOKED_CALAMARI,
                ItemsSD.POTTAGE,
                Items.BROWN_MUSHROOM,
                Items.RED_MUSHROOM
        };

        for (Item food : foods) {
            Identifier foodID = BuiltInRegistries.ITEM.getKey(food);
            String criteriaName = foodID.getPath();
            ItemPredicate.Builder itemPredicate = ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, food);
            Criterion<?> newCriterion = ConsumeItemTrigger.TriggerInstance.usedItem(itemPredicate);

            criteria.put(criteriaName, newCriterion);
            requirements.add(List.of(criteriaName));
        }
    }
}
