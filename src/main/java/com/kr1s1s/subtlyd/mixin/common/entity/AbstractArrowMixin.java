package com.kr1s1s.subtlyd.mixin.common.entity;

import com.kr1s1s.subtlyd.world.level.GameRulesSD;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {
    private final AbstractArrow arrow = (AbstractArrow) (Object) (this);
    private final Level level = arrow.level();
    @Shadow protected abstract boolean isInGround();

    @Inject(method = "onHitBlock", at = @At("RETURN"))
    private void onHitBlock(CallbackInfo ci) {
        trySetFire();
    }

    /**
     * Attempt to set a fire if a flaming arrow has landed and if gamerules allow it to.
     */
    private void trySetFire() {
        boolean bl = !arrow.isNoPhysics();
        if (level.getServer() != null && level.getServer().getWorldData().getGameRules().get(GameRulesSD.ARROW_ARSON)) {
            if (!(!level.getServer().getWorldData().getGameRules().get(GameRules.MOB_GRIEFING) && !((arrow.getOwner() instanceof Player) || arrow.getOwner() == null))) {
                if ((arrow.isOnFire() && this.isInGround()) && bl) {
                    setFire(arrow.blockPosition());
                }
            }
        }
    }

    /**
     * Determines where a fire block should be placed based on the arrow's direction.
     * @param blockPos The block the arrow landed in
     */
    private void setFire(BlockPos blockPos) {
        BlockPos arrowForward = blockPos.relative(arrow.getDirection());

        switch (findFlammableBlock(blockPos)) {
            case 1:
                level.setBlock(blockPos, BaseFireBlock.getState(level, blockPos), 0);
                break;
            case 2:
                level.setBlock(blockPos.above(), BaseFireBlock.getState(level, blockPos.above()), 0);
                break;
            case 3:
                level.setBlock(arrowForward.above(), BaseFireBlock.getState(level, arrowForward.above()), 0);
                break;
        }
    }

    /**
     * Finds a flammable block that can be lit on fire by a flaming arrow.
     * @param blockPos The location of the block the arrow has landed in.
     * @return An integer value corresponding to the block above, in front of, or equal to the arrow's target block.
     */
    private int findFlammableBlock(BlockPos blockPos) {
        BlockState bSArrow = level.getBlockState(blockPos);
        BlockState bSAbove = level.getBlockState(blockPos.above());
        BlockState bSBelow = level.getBlockState(blockPos.below());
        BlockState bSForward = level.getBlockState(blockPos.relative(arrow.getDirection()));
        BlockState bSDiagonal = level.getBlockState(blockPos.relative(arrow.getDirection()).above());

        if ((bSArrow.ignitedByLava() || bSBelow.ignitedByLava()|| bSForward.ignitedByLava() || bSArrow.is(BlockTags.FLOWERS)) && bSArrow.canBeReplaced()) {
           return 1;
        } else if ((bSArrow.ignitedByLava() && !bSArrow.canBeReplaced()) && bSAbove.canBeReplaced()) {
            return 2;
        } else if (bSForward.ignitedByLava() && bSDiagonal.canBeReplaced()) {
            return 3;
        } else if (bSAbove.ignitedByLava() && bSArrow.canBeReplaced()) {
            return 1;
        }
        return 0;
    }
}
