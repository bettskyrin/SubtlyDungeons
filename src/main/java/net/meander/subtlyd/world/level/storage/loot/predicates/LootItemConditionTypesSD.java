package net.meander.subtlyd.world.level.storage.loot.predicates;

import com.mojang.serialization.MapCodec;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

/**
 * @see net.minecraft.world.level.storage.loot.predicates.LootItemConditionTypes
 */
public class LootItemConditionTypesSD {
    public static void registration(Registry<MapCodec<? extends LootItemCondition>> registry) {
        Registry.register(registry, UtilSD.identifier("is_wet"), IsWetCondition.CODEC);
    }
}
