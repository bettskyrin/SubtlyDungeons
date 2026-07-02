package net.meander.subtlyd.world.level.storage.loot.predicates;

import com.mojang.serialization.MapCodec;
import net.meander.subtlyd.util.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

/**
 * @see net.minecraft.world.level.storage.loot.predicates.LootItemConditions
 */
public class LootItemConditionsSD {
    private static final Registry<MapCodec<? extends LootItemCondition>> REGISTRY = BuiltInRegistries.LOOT_CONDITION_TYPE;

    public static void registration() {
        Registry.register(REGISTRY, Util.identifier("is_wet"), IsWetCondition.CODEC);
    }
}
