package net.meander.subtlyd.mixin.common.world.level.block;

import net.meander.subtlyd.sounds.SoundEventsSD;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FrostedIceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FrostedIceBlock.class)
public class FrostedIceBlockMixin {
    RandomSource random = RandomSource.create();

    @Inject(method = "onPlace", at = @At("TAIL"))
    private void playFrozenSound(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston, CallbackInfo ci) {
        if (oldState.is(Blocks.WATER) && random.nextFloat() < 0.4F) {
            level.playSound(null, pos, SoundEventsSD.ICE_FREEZE, SoundSource.BLOCKS, 0.2F, 0.7F + ((float) random.nextIntBetweenInclusive(1, 3) / 10));
        }
    }
}
