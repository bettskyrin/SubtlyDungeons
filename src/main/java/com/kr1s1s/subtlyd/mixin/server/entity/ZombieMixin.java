package com.kr1s1s.subtlyd.mixin.server.entity;

import com.kr1s1s.subtlyd.network.syncher.SynchedEntityDataSD;
import com.kr1s1s.subtlyd.world.entity.monster.ZombieSD;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
@SuppressWarnings("unused")
public class ZombieMixin {
    Zombie zombie = (Zombie) (Object) this;

    @Inject(method = "handleAttributes", at = @At("RETURN"))
    protected void alterAttributes(float f, CallbackInfo ci) {
        ZombieSD.alterAttributes(zombie, f);
    }

    @Inject(method = "defineSynchedData", at = @At("RETURN"))
    protected void defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(SynchedEntityDataSD.DATA_LEADER_ID, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    protected void addAdditionalSaveData(ValueOutput valueOutput, CallbackInfo ci) {
        valueOutput.putBoolean("IsLeader", (zombie.getEntityData().get(SynchedEntityDataSD.DATA_LEADER_ID)));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    protected void readAdditionalSaveData(ValueInput valueInput, CallbackInfo ci) {
        zombie.getEntityData().set(SynchedEntityDataSD.DATA_LEADER_ID, valueInput.getBooleanOr("IsLeader", false));
    }
}
