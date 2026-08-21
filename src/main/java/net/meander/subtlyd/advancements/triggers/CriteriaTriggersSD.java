package net.meander.subtlyd.advancements.triggers;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

/**
 * @see net.minecraft.advancements.triggers.CriteriaTriggers
 */
public class CriteriaTriggersSD {
    public static final SleptInTentTrigger SLEPT_IN_TENT = register("slept_in_tent", new SleptInTentTrigger());
    public static final StealthAttackTrigger STEALTH_ATTACK = register("stealth_attack", new StealthAttackTrigger());
    public static final ConstructConduitTrigger CONSTRUCT_CONDUIT = register("construct_conduit", new ConstructConduitTrigger());

    public static void registration() {
        UtilSD.LOGGER.debug("Registering criteria triggers...");}

    public static <T extends CriterionTrigger<?>> T register(final String name, final T criterion) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, name, criterion);
    }
}
