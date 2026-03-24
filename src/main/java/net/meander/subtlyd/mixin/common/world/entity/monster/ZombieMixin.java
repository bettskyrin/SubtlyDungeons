package net.meander.subtlyd.mixin.common.world.entity.monster;

import net.meander.subtlyd.network.syncher.SynchedEntityDataSD;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public abstract class ZombieMixin {

    /**
     * Set a zombie leader
     */
    @Inject(method = "handleAttributes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/zombie/Zombie;setCanBreakDoors(Z)V"))
    private void setLeader(CallbackInfo ci) {
        Zombie zombie = (Zombie) (Object) this;
        zombie.getEntityData().set(SynchedEntityDataSD.DATA_ID_ZOMBIE_LEADER, true);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineLeaderData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(SynchedEntityDataSD.DATA_ID_ZOMBIE_LEADER, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void saveLeaderData(ValueOutput output, CallbackInfo ci) {
        Zombie zombie = (Zombie) (Object) this;

        output.putBoolean("IsLeader", zombie.getEntityData().get(SynchedEntityDataSD.DATA_ID_ZOMBIE_LEADER));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void loadLeaderData(ValueInput input, CallbackInfo ci) {
        Zombie zombie = (Zombie) (Object) this;

        if (input.contains("IsLeader")) {
            zombie.getEntityData().set(SynchedEntityDataSD.DATA_ID_ZOMBIE_LEADER, input.getBooleanOr("IsLeader", false));
        }
    }
}