package net.meander.subtlyd.mixin.common.world.level.block.grower;

import net.meander.subtlyd.data.worldgen.features.TreeFeaturesSD;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.grower.TreeGrower;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(TreeGrower.class)
public class TreeGrowerMixin {
    @ModifyArgs(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/grower/TreeGrower;<init>(Ljava/lang/String;Lnet/minecraft/util/random/WeightedList;Lnet/minecraft/util/random/WeightedList;Lnet/minecraft/util/random/WeightedList;Lnet/minecraft/resources/ResourceKey;)V", ordinal = 6))
    private static void addBaobab(Args args) {
        String name = args.get(0);
        
        if (name.equals("acacia")) {
            args.set(2, WeightedList.of(TreeFeaturesSD.BAOBAB));
        }
    }
}