package net.meander.subtlyd.mixin.client.player;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.AttackRange;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
    @ModifyReturnValue(method = "raycastHitResult", at = @At("RETURN"))
    private HitResult swingThroughFoliage(HitResult hitResult, float a, Entity cameraEntity) {
        if (hitResult instanceof BlockHitResult blockHit && cameraEntity instanceof LocalPlayer player) {
            ItemStack mainHand = player.getMainHandItem();

            if (mainHand.has(DataComponents.WEAPON)) {
                double interactionRange = player.entityInteractionRange();

                if (mainHand.has(DataComponents.ATTACK_RANGE)) {
                    AttackRange attackRange = mainHand.get(DataComponents.ATTACK_RANGE);

                    if (attackRange != null) {
                        interactionRange = attackRange.maxReach();
                    }
                }

                BlockPos pos = blockHit.getBlockPos();
                Level level = player.level();
                BlockState state = level.getBlockState(pos);

                if (state.getCollisionShape(level, pos).isEmpty()) {
                    Vec3 eyePos = player.getEyePosition(a);
                    Vec3 scaledViewVec = player.getViewVector(1.0F).scale(interactionRange);

                    Vec3 endPos = eyePos.add(scaledViewVec);
                    AABB searchBox = player.getBoundingBox().expandTowards(scaledViewVec).inflate(1.0D);

                    ClipContext solidCheckContext = new ClipContext(eyePos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player);
                    BlockHitResult solidHit = level.clip(solidCheckContext);

                    double maxDistanceSquared = Mth.square(interactionRange);

                    if (solidHit.getType() != HitResult.Type.MISS) {
                        maxDistanceSquared = solidHit.getLocation().distanceToSqr(eyePos);
                    }

                    EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                            player,
                            eyePos,
                            endPos,
                            searchBox,
                            entity -> !entity.isSpectator() && entity.isPickable(),
                            maxDistanceSquared
                    );

                    if (entityHit != null) {
                        return entityHit;
                    }
                }
            }
        }
        return hitResult;
    }
}