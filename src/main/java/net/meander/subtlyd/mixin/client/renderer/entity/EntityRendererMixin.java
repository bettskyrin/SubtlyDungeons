package net.meander.subtlyd.mixin.client.renderer.entity;

import net.meander.subtlyd.client.renderer.state.EntityRenderStateAccessor;
import net.meander.subtlyd.world.entity.EntitySD;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;F)V",
            at = @At("TAIL"))
    private void extractFireData(Entity entity, EntityRenderState state, float partialTicks, CallbackInfo ci) {
        if (entity.isOnFire()) {
            EntityRenderStateAccessor accessor = (EntityRenderStateAccessor) state;

            accessor.subtlyd$setSoulFire(EntitySD.shouldSoulFireBurn(entity));
        }
    }
}
