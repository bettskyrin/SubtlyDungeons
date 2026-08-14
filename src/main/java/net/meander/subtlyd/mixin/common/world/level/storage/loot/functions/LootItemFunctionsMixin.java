package net.meander.subtlyd.mixin.common.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import net.meander.subtlyd.world.level.storage.loot.functions.LootItemFunctionsSD;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootItemFunctions.class)
public class LootItemFunctionsMixin {
    @Inject(method = "bootstrap", at = @At("RETURN"))
    private static void registerFunctions(Registry<MapCodec<? extends LootItemFunction>> registry, CallbackInfoReturnable<MapCodec<? extends LootItemFunction>> cir) {
        LootItemFunctionsSD.bootstrap(registry);
    }
}