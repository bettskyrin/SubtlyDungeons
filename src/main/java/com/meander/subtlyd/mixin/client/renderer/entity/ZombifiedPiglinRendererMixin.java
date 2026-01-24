package com.meander.subtlyd.mixin.client.renderer.entity;

import com.meander.subtlyd.client.renderer.UndeadRenderStateAccessor;
import com.meander.subtlyd.util.Util;
import net.minecraft.client.renderer.entity.ZombifiedPiglinRenderer;
import net.minecraft.client.renderer.entity.state.ZombifiedPiglinRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombifiedPiglinRenderer.class)
public class ZombifiedPiglinRendererMixin {
    private final Identifier ZOMBIFIED_PIGLIN_LEADER_LOCATION = Util.identifier("textures/entity/piglin/zombified_piglin_leader.png");

    /**
     * Changes the zombified piglin leader texture to their unique design.
     */
    @Inject(method = "getTextureLocation(Lnet/minecraft/client/renderer/entity/state/ZombifiedPiglinRenderState;)Lnet/minecraft/resources/Identifier;",
            at = @At("RETURN"),
            cancellable = true)
    private void getTextureLocation(ZombifiedPiglinRenderState state, CallbackInfoReturnable<Identifier> cir) {
        Identifier leaderLocation = cir.getReturnValue();

        if (((UndeadRenderStateAccessor) state).subtlyDungeons$isLeader()) {
                leaderLocation = ZOMBIFIED_PIGLIN_LEADER_LOCATION;
        }
        cir.setReturnValue(leaderLocation);
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/monster/zombie/ZombifiedPiglin;Lnet/minecraft/client/renderer/entity/state/ZombifiedPiglinRenderState;F)V",
            at = @At("TAIL"))
    private void setLeaderRenderState(ZombifiedPiglin entity, ZombifiedPiglinRenderState state, float partialTicks, CallbackInfo ci) {
        double ZOMBIE_BASE_HEALTH_POINTS = 20D;
        ((UndeadRenderStateAccessor) state).subtlyDungeons$setLeader(entity.getMaxHealth() > ZOMBIE_BASE_HEALTH_POINTS);
    }
}
