package com.kr1s1s.subtlyd.mixin.common.entity.monster;

import com.kr1s1s.subtlyd.network.syncher.SynchedEntityDataSD;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public class ZombieMixin {
    @SuppressWarnings("DataFlowIssue")
    private final Zombie zombie = (Zombie) (Object) this;

    /**
     * Sets the custom synced entity data value for leader zombies, for tracking zombies that should have altered textures
     */
    @Inject(method = "handleAttributes",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/zombie/Zombie;setCanBreakDoors(Z)V",
            shift = At.Shift.AFTER))
    protected void setLeader(CallbackInfo ci) {
        zombie.getEntityData().set(SynchedEntityDataSD.DATA_LEADER_ID, true);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(SynchedEntityDataSD.DATA_LEADER_ID, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    protected void addAdditionalSaveData(ValueOutput output, CallbackInfo ci) {
        output.putBoolean("IsLeader", (zombie.getEntityData().get(SynchedEntityDataSD.DATA_LEADER_ID)));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    protected void readAdditionalSaveData(ValueInput output, CallbackInfo ci) {
        zombie.getEntityData().set(SynchedEntityDataSD.DATA_LEADER_ID, output.getBooleanOr("IsLeader", false));
    }
}
