package net.meander.subtlyd.advancements.triggers;

import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

/**
 * @see net.minecraft.advancements.triggers.CriteriaTriggers
 */
public class CriteriaTriggersSD {
    public static final SleptInTentTrigger SLEPT_IN_TENT = register("slept_in_tent", new SleptInTentTrigger());
    public static final StealthAttackTrigger STEALTH_ATTACK = register("stealth_attack", new StealthAttackTrigger());

    public static void registration() {}

    public static <T extends CriterionTrigger<?>> T register(final String name, final T criterion) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, name, criterion);
    }
}
