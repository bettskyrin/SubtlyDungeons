package net.meander.subtlyd.mixin.common.world.level.block.entity;

import net.meander.subtlyd.advancements.triggers.CriteriaTriggersSD;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConduitBlockEntity.class)
public class ConduitBlockEntityMixin {
    private int effectLevel = 0;

    @Inject(
            method = "serverTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V",
                    ordinal = 0
            )
    )
    private static void onActivate(final Level level, final BlockPos pos, final BlockState state, final ConduitBlockEntity entity, CallbackInfo ci) {
        grantAdvancement(level, pos, entity);
    }

    @Inject(
            method = "serverTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/ConduitBlockEntity;updateHunting(Lnet/minecraft/world/level/block/entity/ConduitBlockEntity;Ljava/util/List;)V"
            )
    )
    private static void grantAdvancement(final Level level, final BlockPos pos, final BlockState state, final ConduitBlockEntity entity, CallbackInfo ci) {
        ConduitBlockEntityMixin conduit = (ConduitBlockEntityMixin) (Object) entity;

        if (conduit != null) {
            if (conduit.effectLevel == 6) {
                grantAdvancement(level, pos, entity);

                conduit.effectLevel = -1;
            } else if (conduit.effectLevel >= 0) {
                conduit.effectLevel = Mth.floor(entity.effectBlocks.size() / 7.0);
            }
        }
    }

    private static void grantAdvancement(final Level level, final BlockPos pos, final ConduitBlockEntity entity) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        ConduitBlockEntityMixin conduit = (ConduitBlockEntityMixin) (Object) entity;

        if (conduit != null) {
            for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, new AABB(x, y, z, x, y - 4, z).inflate(10.0, 5.0, 10.0))) {
                CriteriaTriggersSD.CONSTRUCT_CONDUIT.trigger(player, conduit.effectLevel);
            }
        }
    }
}
