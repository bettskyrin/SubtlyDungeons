package net.meander.subtlyd.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

/**
 * @see net.minecraft.world.level.storage.loot.functions.LootItemFunctions
 */
public class LootItemFunctionsSD {
    public static void registration(final Registry<MapCodec<? extends LootItemFunction>> registry) {
        Registry.register(registry, "enchant_non_humanoid_armor", EnchantNonHumanoidArmorFunction.MAP_CODEC);
        Registry.register(registry, "set_dagger", SetDaggerFunction.MAP_CODEC);
    }
}
