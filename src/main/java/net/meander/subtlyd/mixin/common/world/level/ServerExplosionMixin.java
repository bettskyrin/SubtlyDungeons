package net.meander.subtlyd.mixin.common.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerExplosion.class)
public abstract class ServerExplosionMixin {
    @Shadow public abstract ServerLevel level();

    @Redirect(
            method = "createFire",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/BaseFireBlock;getState(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;")
    )
    private BlockState setSoulFireFromWither(BlockGetter level, BlockPos pos) {
        if (level().getGameRules().get(GameRules.MOB_GRIEFING) && ((Explosion) this).getDirectSourceEntity() instanceof WitherSkull) {
            BlockPos belowPos = pos.below();

            if (level.getBlockState(belowPos).isSolidRender()) {
                level().setBlockAndUpdate(belowPos, Blocks.SOUL_SOIL.defaultBlockState());
            }

            return Blocks.SOUL_FIRE.defaultBlockState();
        }
        return BaseFireBlock.getState(level, pos);
    }
}
