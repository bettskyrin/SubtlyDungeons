package com.kr1s1s.subtlyd.mixin;

import com.kr1s1s.subtlyd.SubtlyDungeons;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {
    @Shadow protected abstract boolean isInGround();
    AbstractArrow arrow = (AbstractArrow) (Object) (this);
    Level level = arrow.level();
    protected  boolean wasInAir = true;

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo ci) {
        boolean bl = !arrow.isNoPhysics();
        if ((arrow.isOnFire() && this.isInGround()) && bl) {
            setFire(arrow.blockPosition());
        }
    }

    public void setFire(BlockPos blockPos) {
        BlockPos arrowForward = blockPos.relative(arrow.getDirection());

        switch (isFlammable(blockPos)) {
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

    public int isFlammable(BlockPos blockPos) {
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
