package net.meander.subtlyd.mixin.client.player;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerPickMixin {
    @ModifyReturnValue(method = "pick(Lnet/minecraft/world/entity/Entity;DDF)Lnet/minecraft/world/phys/HitResult;", at = @At("RETURN"))
    private static HitResult swingThroughFoliage(HitResult hitResult, Entity cameraEntity, double blockInteractionRange, double entityInteractionRange, float partialTicks) {
        if (hitResult instanceof BlockHitResult blockHit) {
            LocalPlayer player = (LocalPlayer) cameraEntity;

            if (player.getMainHandItem().has(DataComponents.WEAPON)) {
                BlockState state = player.level().getBlockState(blockHit.getBlockPos());

                if (state.getCollisionShape(player.level(), blockHit.getBlockPos()).isEmpty()) {
                    Vec3 eyePos = player.getEyePosition(partialTicks);
                    Vec3 viewVec = player.getViewVector(1.0F);
                    Vec3 endPos = eyePos.add(viewVec.scale(entityInteractionRange));
                    
                    AABB searchBox = player.getBoundingBox().expandTowards(viewVec.scale(entityInteractionRange)).inflate(1.0D);

                    EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                            player, 
                            eyePos, 
                            endPos, 
                            searchBox,
                            entity -> !entity.isSpectator() && entity.isPickable(),
                            Mth.square(entityInteractionRange)
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