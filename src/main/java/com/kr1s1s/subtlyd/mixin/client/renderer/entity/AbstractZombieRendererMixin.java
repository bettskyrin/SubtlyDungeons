package com.kr1s1s.subtlyd.mixin.client.renderer.entity;

import com.kr1s1s.subtlyd.client.renderer.ZombieRenderStateAccessor;
import com.kr1s1s.subtlyd.util.Util;
import net.minecraft.client.model.monster.zombie.ZombieModel;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.monster.zombie.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractZombieRenderer.class)
public class AbstractZombieRendererMixin <T extends Zombie, S extends ZombieRenderState, M extends ZombieModel<S>>{
    @SuppressWarnings("unchecked")
    private final AbstractZombieRenderer<T, S, M> renderer = (AbstractZombieRenderer<T, S, M>) (Object) this;
    private final Identifier ZOMBIE_LEADER_LOCATION = Util.identifier("textures/entity/zombie/zombie_leader.png");
    private final Identifier DROWNED_LEADER_LOCATION = Util.identifier("textures/entity/zombie/drowned_leader.png");
    private final Identifier HUSK_LEADER_LOCATION = Util.identifier("textures/entity/zombie/husk_leader.png");

    /**
     * Changes the zombie leader texture to their unique design.
     */
    @Inject(method = "getTextureLocation(Lnet/minecraft/client/renderer/entity/state/ZombieRenderState;)Lnet/minecraft/resources/Identifier;",
            at = @At("RETURN"),
            cancellable = true)
    private void getTextureLocation(ZombieRenderState state, CallbackInfoReturnable<Identifier> cir) {
        Identifier leaderLocation = cir.getReturnValue();

        if (((ZombieRenderStateAccessor) state).subtlyDungeons$isLeader()) {
            if (renderer instanceof DrownedRenderer) {
                leaderLocation = DROWNED_LEADER_LOCATION;
            } else if (renderer instanceof HuskRenderer) {
                leaderLocation = HUSK_LEADER_LOCATION;
            } else {
                leaderLocation = ZOMBIE_LEADER_LOCATION;
            }
        }
        cir.setReturnValue(leaderLocation);
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/monster/zombie/Zombie;Lnet/minecraft/client/renderer/entity/state/ZombieRenderState;F)V",
            at = @At("TAIL"))
    private void setLeaderRenderState(T entity, S state, float partialTicks, CallbackInfo ci) {
        double ZOMBIE_BASE_HEALTH_POINTS = 20D;
        ((ZombieRenderStateAccessor) state).subtlyDungeons$setLeader((entity.getMaxHealth() > ZOMBIE_BASE_HEALTH_POINTS && entity.canBreakDoors()));
    }
}
