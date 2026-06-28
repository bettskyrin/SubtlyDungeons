package net.meander.subtlyd.mixin.client.player;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.AttackRange;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
    @ModifyReturnValue(method = "raycastHitResult", at = @At("RETURN"))
    private HitResult swingThroughFoliage(HitResult hitResult, float a, Entity cameraEntity) {
        if (hitResult.getType() == HitResult.Type.BLOCK && hitResult instanceof BlockHitResult blockHit) {
            LocalPlayer player = (LocalPlayer) cameraEntity;
            ItemStack mainHand = player.getMainHandItem();

            if (mainHand.has(DataComponents.WEAPON)) {

                double actualReach = player.entityInteractionRange();
                if (mainHand.has(DataComponents.ATTACK_RANGE)) {
                    AttackRange attackRange = mainHand.get(DataComponents.ATTACK_RANGE);
                    if (attackRange != null) {
                        actualReach = attackRange.maxReach();
                    }
                }

                BlockState state = player.level().getBlockState(blockHit.getBlockPos());

                if (state.getCollisionShape(player.level(), blockHit.getBlockPos()).isEmpty()) {
                    Vec3 eyePos = player.getEyePosition(a);
                    Vec3 viewVec = player.getViewVector(1.0F);

                    Vec3 endPos = eyePos.add(viewVec.scale(actualReach));
                    AABB searchBox = player.getBoundingBox().expandTowards(viewVec.scale(actualReach)).inflate(1.0D);

                    ClipContext solidCheckContext = new ClipContext(eyePos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player);
                    BlockHitResult solidHit = player.level().clip(solidCheckContext);

                    double maxDistanceSquared = Mth.square(actualReach);

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