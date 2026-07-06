package net.meander.subtlyd.mixin.common.world.level.levelgen.feature.treedecorators;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.treedecorators.ShelfMushroomDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;

@Mixin(ShelfMushroomDecorator.class)
public class ShelfMushroomDecoratorMixin {
    @Redirect(method = "placeOnStandingTree", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/feature/treedecorators/ShelfMushroomDecorator;isWithinDecoratableHeight(Lnet/minecraft/core/BlockPos;I)Z"))
    private static boolean modifySuperBirch(BlockPos pos, int treeBaseY, @Local(name = "context", argsOnly = true) TreeDecorator.Context context) {
        if (context.checkBlock(pos, Predicate.isEqual(Blocks.BIRCH_LOG.defaultBlockState()))) {
            return isWithinDecoratableHeightForSuperBirch(pos, treeBaseY);
        } else {
            return ShelfMushroomDecorator.isWithinDecoratableHeight(pos, treeBaseY);
        }
    }

    private static boolean isWithinDecoratableHeightForSuperBirch(final BlockPos pos, final int treeBaseY) {
        int dy = pos.getY() - treeBaseY;
        return dy >= 1 && dy <= 15;
    }
}
