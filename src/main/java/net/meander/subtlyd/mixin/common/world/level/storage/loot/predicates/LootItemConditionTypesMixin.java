package net.meander.subtlyd.mixin.common.world.level.storage.loot.predicates;

import com.mojang.serialization.MapCodec;
import net.meander.subtlyd.world.level.storage.loot.predicates.LootItemConditionTypesSD;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootItemConditionTypes.class)
public class LootItemConditionTypesMixin {
    @Inject(method = "bootstrap", at = @At("RETURN"))
    private static void registerConditions(Registry<MapCodec<? extends LootItemCondition>> registry, CallbackInfoReturnable<MapCodec<? extends LootItemCondition>> cir) {
        LootItemConditionTypesSD.registration(registry);
    }
}