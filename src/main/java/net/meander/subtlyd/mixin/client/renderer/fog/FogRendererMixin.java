package net.meander.subtlyd.mixin.client.renderer.fog;

import net.meander.subtlyd.client.renderer.entity.OcclusionManager;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Inject(method = "setupFog", at = @At("RETURN"))
    private void captureFogDistance(final Camera camera, final int renderDistanceInChunks, final DeltaTracker deltaTracker, final float darkenWorldAmount, final ClientLevel level, CallbackInfoReturnable<FogData> cir) {
        FogData fogData = cir.getReturnValue();
        float fogEnd = Math.min(fogData.environmentalEnd, fogData.renderDistanceEnd);
        
        OcclusionManager.getInstance().setCurrentFogEndSqr(fogEnd);
    }
}