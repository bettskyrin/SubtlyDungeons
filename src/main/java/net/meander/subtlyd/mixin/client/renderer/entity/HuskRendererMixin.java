package net.meander.subtlyd.mixin.client.renderer.entity;

import net.meander.subtlyd.client.renderer.entity.state.UndeadRenderStateSD;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.client.renderer.entity.HuskRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HuskRenderer.class)
public class HuskRendererMixin {
    private final Identifier HUSK_LEADER_LOCATION = UtilSD.identifier("textures/entity/zombie/husk_leader.png");
    private final Identifier BABY_HUSK_LEADER_LOCATION = UtilSD.identifier("textures/entity/zombie/husk_leader_baby.png");

    /**
     * Changes the husk leader texture to their unique design.
     */
    @Inject(method = "getTextureLocation(Lnet/minecraft/client/renderer/entity/state/ZombieRenderState;)Lnet/minecraft/resources/Identifier;",
            at = @At("RETURN"),
            cancellable = true)
    private void getTextureLocation(ZombieRenderState state, CallbackInfoReturnable<Identifier> cir) {
        Identifier leaderLocation = cir.getReturnValue();

        if (((UndeadRenderStateSD) state).isLeader()) {
            leaderLocation = state.isBaby ? BABY_HUSK_LEADER_LOCATION : HUSK_LEADER_LOCATION;
        }
        cir.setReturnValue(leaderLocation);
    }
}
