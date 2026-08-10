package net.meander.subtlyd.mixin.client;

import net.meander.subtlyd.client.EntityCullingMethod;
import net.minecraft.client.GraphicsPreset;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GraphicsPreset.class)
public class GraphicsPresetMixin {
    @Inject(method = "apply", at = @At(value = "TAIL"))
    private void apply(Minecraft minecraft, CallbackInfo ci) {
        GraphicsPreset graphicsPreset = (GraphicsPreset) (Object) this;
        OptionsSubScreen screen = minecraft.gui != null && minecraft.gui.screen() instanceof OptionsSubScreen subScreen ? subScreen : null;

        switch (graphicsPreset) {
            case FAST: {
                GraphicsPreset.set(screen, minecraft.options.fancyEntities(), false);
                GraphicsPreset.set(screen, minecraft.options.entityCulling(), EntityCullingMethod.OCCLUSION);
                break;
            }

            case FANCY: {
                GraphicsPreset.set(screen, minecraft.options.fancyEntities(), true);
                GraphicsPreset.set(screen, minecraft.options.entityCulling(), EntityCullingMethod.OCCLUSION);
                break;
            }

            case FABULOUS: {
                GraphicsPreset.set(screen, minecraft.options.fancyEntities(), true);
                GraphicsPreset.set(screen, minecraft.options.entityCulling(), EntityCullingMethod.OCCLUSION);
            }
        }
    }
}
