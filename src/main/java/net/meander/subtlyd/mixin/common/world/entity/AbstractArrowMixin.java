package net.meander.subtlyd.mixin.common.world.entity;

import net.meander.subtlyd.world.level.GameRulesSD;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {
    @Shadow protected abstract boolean isInGround();

    @Inject(method = "onHitBlock", at = @At("RETURN"))
    private void onHitBlock(final BlockHitResult hitResult, CallbackInfo ci) {
        trySetFire(hitResult);
    }

    /**
     * Attempt to set a fire if a flaming arrow has landed and if gamerules allow it to.
     */
    private void trySetFire(final BlockHitResult hitResult) {
        final AbstractArrow arrow = (AbstractArrow) (Object) (this);
        final Level level = arrow.level();
        boolean bl = !arrow.isNoPhysics();
        if (level.getServer() != null && level.getServer().getGameRules().get(GameRulesSD.ARROW_ARSON)) {
            if (!(!level.getServer().getGameRules().get(GameRules.MOB_GRIEFING) && !((arrow.getOwner() instanceof Player) || arrow.getOwner() == null))) {
                if ((arrow.isOnFire() && this.isInGround()) && bl) {
                    setFire(hitResult);
                }
            }
        }
    }

    /**
     * Determines where a fire block should be placed based on the arrow's direction.
     * @param hitResult The BlockHitResult storing arrow data
     */
    private void setFire(final BlockHitResult hitResult) {
        final AbstractArrow arrow = (AbstractArrow) (Object) (this);
        final Level level = arrow.level();
        BlockPos arrowPos = hitResult.getBlockPos();
        BlockPos arrowFwd = arrowPos.relative(hitResult.getDirection());

        switch (findFlammableBlock(hitResult)) {
            case 1:
                level.setBlock(arrowPos, BaseFireBlock.getState(level, arrowPos), 0);
                break;
            case 2:
                level.setBlock(arrowPos.above(), BaseFireBlock.getState(level, arrowPos.above()), 0);
                break;
            case 3:
                level.setBlock(arrowFwd.above(), BaseFireBlock.getState(level, arrowFwd.above()), 0);
                break;
        }
    }

    /**
     * Finds a flammable block that can be lit on fire by a flaming arrow.
     * @param hitResult The BlockHitResult storing arrow data.
     * @return An integer value corresponding to the block above, in front of, or equal to the arrow's target block.
     */
    private int findFlammableBlock(final BlockHitResult hitResult) {
        final AbstractArrow arrow = (AbstractArrow) (Object) (this);
        final Level level = arrow.level();
        BlockPos onPos = hitResult.getBlockPos();
        Direction targetFace = hitResult.getDirection();

        BlockState onBS = level.getBlockState(onPos);
        BlockState upBS = level.getBlockState(onPos.above());
        BlockState downBS = level.getBlockState(onPos.below());
        BlockState fwdBS = level.getBlockState(onPos.relative(targetFace));
        BlockState adjBS = level.getBlockState(onPos.relative(targetFace).above());

        if ((onBS.ignitedByLava() || downBS.ignitedByLava()|| fwdBS.ignitedByLava() || onBS.is(BlockTags.FLOWERS)) && onBS.canBeReplaced()) {
           return 1;
        } else if ((onBS.ignitedByLava() && !onBS.canBeReplaced()) && upBS.canBeReplaced()) {
            return 2;
        } else if (fwdBS.ignitedByLava() && adjBS.canBeReplaced()) {
            return 3;
        } else if (upBS.ignitedByLava() && onBS.canBeReplaced()) {
            return 1;
        }
        return 0;
    }
}
