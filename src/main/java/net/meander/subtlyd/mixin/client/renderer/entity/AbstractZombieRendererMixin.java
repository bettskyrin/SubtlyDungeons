package net.meander.subtlyd.mixin.client.renderer.entity;

import net.meander.subtlyd.client.renderer.entity.state.UndeadRenderStateSD;
import net.meander.subtlyd.network.syncher.EntityDataAccessors;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.client.model.HumanoidModel;
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
public class AbstractZombieRendererMixin <T extends Zombie, S extends ZombieRenderState, M extends HumanoidModel<S>> {
    private final Identifier ZOMBIE_LEADER_LOCATION = UtilSD.identifier("textures/entity/zombie/zombie_leader.png");
    private final Identifier BABY_ZOMBIE_LEADER_LOCATION = UtilSD.identifier("textures/entity/zombie/zombie_leader_baby.png");
    private final Identifier DROWNED_LEADER_LOCATION = UtilSD.identifier("textures/entity/zombie/drowned_leader.png");
    private final Identifier GURGLE_LEADER_LOCATION = UtilSD.identifier("textures/entity/zombie/drowned_leader_baby.png");

    @Inject(method = "getTextureLocation(Lnet/minecraft/client/renderer/entity/state/ZombieRenderState;)Lnet/minecraft/resources/Identifier;", at = @At("RETURN"), cancellable = true)
    private void modifyLeaderTexture(ZombieRenderState state, CallbackInfoReturnable<Identifier> cir) {
        Identifier location = cir.getReturnValue();

        if (((UndeadRenderStateSD) state).isLeader()) {
            @SuppressWarnings("unchecked")
            final AbstractZombieRenderer<T, S, M> renderer = (AbstractZombieRenderer<T, S, M>) (Object) this;

            if (renderer instanceof DrownedRenderer) {
                location = state.isBaby ? GURGLE_LEADER_LOCATION : DROWNED_LEADER_LOCATION;
            } else {
                location = state.isBaby ? BABY_ZOMBIE_LEADER_LOCATION : ZOMBIE_LEADER_LOCATION;
            }
        }
        cir.setReturnValue(location);
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/monster/zombie/Zombie;Lnet/minecraft/client/renderer/entity/state/ZombieRenderState;F)V", at = @At("TAIL"))
    private void extractLeaderRenderState(T entity, S state, float partialTicks, CallbackInfo ci) {
        boolean isLeader = entity.getEntityData().get(EntityDataAccessors.DATA_ID_ZOMBIE_LEADER);

        ((UndeadRenderStateSD) state).setLeader(isLeader);
    }
}
