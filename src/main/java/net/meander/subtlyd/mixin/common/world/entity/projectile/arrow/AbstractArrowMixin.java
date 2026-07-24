package net.meander.subtlyd.mixin.common.world.entity.projectile.arrow;

import net.meander.subtlyd.sounds.SoundEventsSD;
import net.meander.subtlyd.tags.BlockTagsSD;
import net.meander.subtlyd.world.level.GameRulesSD;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {
    @Shadow protected abstract boolean isInGround();

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        final AbstractArrow arrow = (AbstractArrow) (Object) (this);

        if (arrow.level() instanceof ServerLevel && arrow.isOnFire() && arrow.tickCount == 1) {
            playFlameShootSound(arrow);
        }
    }

    @Inject(method = "onHitBlock", at = @At("RETURN"))
    private void onHitBlock(final BlockHitResult hitResult, CallbackInfo ci) {
        final AbstractArrow arrow = (AbstractArrow) (Object) (this);

        if (arrow.level() instanceof ServerLevel && arrow.isOnFire()) {
            playFlameHitSound(arrow);
        }

        trySetFire(hitResult);
    }

    @Inject(method = "onHitEntity", at = @At("RETURN"))
    private void onHitEntity(EntityHitResult hitResult, CallbackInfo ci) {
        final AbstractArrow arrow = (AbstractArrow) (Object) (this);

        if (arrow.level() instanceof ServerLevel && arrow.isOnFire()) {
            playFlameHitSound(arrow);
        }
    }

    private void playFlameShootSound(AbstractArrow arrow) {
        final Level level = arrow.level();

        level.playSound(null, arrow.getX(), arrow.getY(), arrow.getZ(), SoundEventsSD.FLAME_ARROW_SHOOT, SoundSource.PLAYERS, 0.7F, (float) level.getRandom().nextIntBetweenInclusive(10, 13) / 10);
    }

    private void playFlameHitSound (AbstractArrow arrow) {
        arrow.level().playSound(null, arrow.getX(), arrow.getY(), arrow.getZ(), SoundEventsSD.FLAME_ARROW_HIT, SoundSource.PLAYERS, 0.3F, 1.0F);
    }

    private void trySetFire(final BlockHitResult hitResult) {
        final AbstractArrow arrow = (AbstractArrow) (Object) (this);
        final MinecraftServer server = arrow.level().getServer();
        boolean hasPhysics = !arrow.isNoPhysics();

        if (server != null) {
            GameRules gameRules = server.getGameRules();

            if (gameRules.get(GameRulesSD.ARROW_ARSON)) {
                Entity owner = arrow.getOwner();

                if (server.getGameRules().get(GameRules.MOB_GRIEFING) || owner instanceof Player || owner == null) {
                    if (arrow.isOnFire() && isInGround() && hasPhysics) {
                        setFire(hitResult);
                    }
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
        final BlockPos arrowPos = hitResult.getBlockPos();
        final BlockPos arrowFwd = arrowPos.relative(hitResult.getDirection());

        BlockPos firePos = switch (findFlammableBlock(hitResult)) {
            case 2 -> arrowPos.above();
            case 3 -> arrowFwd.above();
            default -> arrowPos;
        };

        level.setBlock(firePos, BaseFireBlock.getState(level, firePos), 0);
    }

    /**
     * Finds a flammable block that can be lit on fire by a flaming arrow.
     * @param hitResult The BlockHitResult storing arrow data.
     * @return An integer value corresponding to the block above, in front of, or equal to the arrow's target block.
     */
    private int findFlammableBlock(final BlockHitResult hitResult) {
        final AbstractArrow arrow = (AbstractArrow) (Object) (this);
        final Level level = arrow.level();
        final BlockPos onPos = hitResult.getBlockPos();
        final Direction targetFace = hitResult.getDirection();

        BlockState onState = level.getBlockState(onPos);
        BlockState aboveState = level.getBlockState(onPos.above());
        BlockState belowState = level.getBlockState(onPos.below());
        BlockState forwardState = level.getBlockState(onPos.relative(targetFace));
        BlockState adjacentState = level.getBlockState(onPos.relative(targetFace).above());

        if (onState.canBeReplaced() && (onState.is(BlockTagsSD.ARROW_FLAMMABLE) || belowState.is(BlockTagsSD.ARROW_FLAMMABLE) || forwardState.is(BlockTagsSD.ARROW_FLAMMABLE) || aboveState.is(BlockTagsSD.ARROW_FLAMMABLE))) {
            return 1;
        }

        if (aboveState.canBeReplaced() && onState.is(BlockTagsSD.ARROW_FLAMMABLE)) {
            return 2;
        }

        if (adjacentState.canBeReplaced() && forwardState.is(BlockTagsSD.ARROW_FLAMMABLE)) {
            return 3;
        }

        return 0;
    }
}
