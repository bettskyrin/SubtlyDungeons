package net.meander.subtlyd.mixin.client.renderer.entity;

import net.meander.subtlyd.client.renderer.UndeadRenderStateAccessor;
import net.meander.subtlyd.network.syncher.SynchedEntityDataSD;
import net.meander.subtlyd.util.Util;
import net.minecraft.client.model.monster.zombie.ZombieModel;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.DrownedRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.monster.zombie.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractZombieRenderer.class)
public class AbstractZombieRendererMixin <T extends Zombie, S extends ZombieRenderState, M extends ZombieModel<S>> {
    private final Identifier ZOMBIE_LEADER_LOCATION = Util.identifier("textures/entity/zombie/zombie_leader.png");
    private final Identifier BABY_ZOMBIE_LEADER_LOCATION = Util.identifier("textures/entity/zombie/baby_zombie_leader.png");
    private final Identifier DROWNED_LEADER_LOCATION = Util.identifier("textures/entity/zombie/drowned_leader.png");
    private final Identifier GURGLE_LEADER_LOCATION = Util.identifier("textures/entity/zombie/gurgle_leader.png");

    /**
     * Changes the zombie leader texture to their unique design.
     */
    @Inject(method = "getTextureLocation(Lnet/minecraft/client/renderer/entity/state/ZombieRenderState;)Lnet/minecraft/resources/Identifier;",
            at = @At("RETURN"),
            cancellable = true)
    private void getTextureLocation(ZombieRenderState state, CallbackInfoReturnable<Identifier> cir) {
        Identifier leaderLocation = cir.getReturnValue();

        if (((UndeadRenderStateAccessor) state).subtlyDungeons$isLeader()) {
            @SuppressWarnings("unchecked")
            final AbstractZombieRenderer<T, S, M> renderer = (AbstractZombieRenderer<T, S, M>) (Object) this;
            if (renderer instanceof DrownedRenderer) {
                leaderLocation = state.isBaby ? GURGLE_LEADER_LOCATION : DROWNED_LEADER_LOCATION;
            } else {
                leaderLocation = state.isBaby ? BABY_ZOMBIE_LEADER_LOCATION : ZOMBIE_LEADER_LOCATION;
            }
        }
        cir.setReturnValue(leaderLocation);
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/monster/zombie/Zombie;Lnet/minecraft/client/renderer/entity/state/ZombieRenderState;F)V",
            at = @At("TAIL"))
    private void setLeaderRenderState(T entity, S state, float partialTicks, CallbackInfo ci) {
        boolean isLeader = entity.getEntityData().get(SynchedEntityDataSD.DATA_ID_ZOMBIE_LEADER);
            ((UndeadRenderStateAccessor) state).subtlyDungeons$setLeader(isLeader);
    }
}
