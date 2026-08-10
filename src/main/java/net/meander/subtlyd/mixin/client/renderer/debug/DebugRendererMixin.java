package net.meander.subtlyd.mixin.client.renderer.debug;

import net.meander.subtlyd.client.gui.components.debug.DebugScreenEntriesSD;
import net.meander.subtlyd.client.renderer.debug.EntityOcclusionDebugRenderer;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.debug.DebugRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin {
    @Shadow @Final private List<DebugRenderer.SimpleDebugRenderer> renderers;

    @Inject(method = "refreshRendererList", at = @At("TAIL"))
    private void addCustomRenderers(CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntriesSD.VISUALIZE_ENTITY_OCCLUSION)) {
            renderers.add(new EntityOcclusionDebugRenderer(minecraft, UtilSD.occlusionManager));
        }
    }
}