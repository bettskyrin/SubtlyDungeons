package net.meander.subtlyd.mixin.client.renderer.entity;

import net.meander.subtlyd.client.renderer.entity.state.UndeadRenderStateSD;
import net.meander.subtlyd.network.syncher.EntityDataAccessors;
import net.meander.subtlyd.util.UtilSD;
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
    private final Identifier ZOMBIFIED_PIGLIN_LEADER_LOCATION = UtilSD.identifier("textures/entity/piglin/zombified_piglin_leader.png");
    private final Identifier BABY_ZOMBIFIED_PIGLIN_LEADER_LOCATION = UtilSD.identifier("textures/entity/piglin/baby_zombified_piglin_leader.png");

    @Inject(method = "getTextureLocation(Lnet/minecraft/client/renderer/entity/state/ZombifiedPiglinRenderState;)Lnet/minecraft/resources/Identifier;", at = @At("RETURN"), cancellable = true)
    private void getTextureLocation(ZombifiedPiglinRenderState state, CallbackInfoReturnable<Identifier> cir) {
        Identifier leaderLocation = cir.getReturnValue();

        if (((UndeadRenderStateSD) state).isLeader()) {
                leaderLocation = state.isBaby ? BABY_ZOMBIFIED_PIGLIN_LEADER_LOCATION : ZOMBIFIED_PIGLIN_LEADER_LOCATION;
        }

        cir.setReturnValue(leaderLocation);
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/monster/zombie/ZombifiedPiglin;Lnet/minecraft/client/renderer/entity/state/ZombifiedPiglinRenderState;F)V", at = @At("TAIL"))
    private void setLeaderRenderState(ZombifiedPiglin entity, ZombifiedPiglinRenderState state, float partialTicks, CallbackInfo ci) {
        boolean isLeader = entity.getEntityData().get(EntityDataAccessors.DATA_ID_ZOMBIE_LEADER);

        ((UndeadRenderStateSD) state).setLeader(isLeader);
    }
}
