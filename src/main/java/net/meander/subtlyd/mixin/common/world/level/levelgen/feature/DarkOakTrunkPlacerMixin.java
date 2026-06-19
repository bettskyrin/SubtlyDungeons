package net.meander.subtlyd.mixin.common.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(DarkOakTrunkPlacer.class)
public class DarkOakTrunkPlacerMixin {
    @ModifyArgs(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/feature/trunkplacers/TrunkPlacer;<init>(III)V"))
    private static void increaseHeight(Args args) {
        args.set(1, (int) args.get(1) + 1);
    }
}