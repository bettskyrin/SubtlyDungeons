package net.meander.subtlyd.mixin.client.renderer;

import com.llamalad7.mixinextras.sugar.Local;
import net.meander.subtlyd.client.renderer.feature.FlameFeatureRendererSubmitAccessor;
import net.meander.subtlyd.client.renderer.state.EntityRenderStateAccessor;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.submit.SubmitNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SubmitNodeCollection.class)
public class SubmitNodeCollectionMixin {
    @ModifyArg(method = "submitFlame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/phase/SimpleFeatureRenderPhase;submit(Lnet/minecraft/client/renderer/feature/submit/SubmitNode;)V"))
    private SubmitNode subtlyd$onSoulFire(SubmitNode submit, @Local(argsOnly = true, name = "renderState") EntityRenderState renderState) {
        if (submit instanceof FlameFeatureRendererSubmitAccessor accessor) {
            if (renderState.displayFireAnimation) {
                accessor.subtlyd$setSoulFire(((EntityRenderStateAccessor) renderState).isOnSoulFire());
            }
        }
        return submit;
    }
}
