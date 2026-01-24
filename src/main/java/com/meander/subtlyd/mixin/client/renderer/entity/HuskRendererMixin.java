package com.meander.subtlyd.mixin.client.renderer.entity;

import com.meander.subtlyd.client.renderer.UndeadRenderStateAccessor;
import com.meander.subtlyd.util.Util;
import net.minecraft.client.renderer.entity.HuskRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HuskRenderer.class)
public class HuskRendererMixin {
    private final Identifier HUSK_LEADER_LOCATION = Util.identifier("textures/entity/zombie/husk_leader.png");

    /**
     * Changes the husk leader texture to their unique design.
     */
    @Inject(method = "getTextureLocation(Lnet/minecraft/client/renderer/entity/state/ZombieRenderState;)Lnet/minecraft/resources/Identifier;",
            at = @At("RETURN"),
            cancellable = true)
    private void getTextureLocation(ZombieRenderState state, CallbackInfoReturnable<Identifier> cir) {
        Identifier leaderLocation = cir.getReturnValue();

        if (((UndeadRenderStateAccessor) state).subtlyDungeons$isLeader()) {
            leaderLocation = HUSK_LEADER_LOCATION;
        }
        cir.setReturnValue(leaderLocation);
    }
}
