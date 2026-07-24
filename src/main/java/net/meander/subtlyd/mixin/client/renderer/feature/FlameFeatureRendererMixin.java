package net.meander.subtlyd.mixin.client.renderer.feature;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.meander.subtlyd.client.renderer.feature.FlameFeatureRendererSD;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FlameFeatureRenderer.class)
public class FlameFeatureRendererMixin {
    /**
     * Changes the fire type that's rendered on entities.
     * @param fire1 An original fire texture atlas sprite
     * @param fire2 An original fire texture atlas sprite
     * @param original The original method
     */
    @WrapOperation(method = "buildGroup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer;prepare(Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer$Submit;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V"))
    private void setFireType(FlameFeatureRenderer instance, FlameFeatureRenderer.Submit submit, VertexConsumer buffer, TextureAtlasSprite fire1, TextureAtlasSprite fire2, Operation<Void> original, @Local(argsOnly = true, name = "context") FeatureFrameContext context) {
        if (((FlameFeatureRendererSD.Submit) (Object) submit).isSoulFire()) {
            AtlasManager atlasManager = context.atlasManager();
            TextureAtlasSprite soulFire1 = atlasManager.get(Sheets.BLOCKS_MAPPER.defaultNamespaceApply("soul_fire_0"));
            TextureAtlasSprite soulFire2 = atlasManager.get(Sheets.BLOCKS_MAPPER.defaultNamespaceApply("soul_fire_1"));

            original.call(instance, submit, buffer, soulFire1, soulFire2);
        } else {
            original.call(instance, submit, buffer, fire1, fire2);
        }
    }

    @Mixin(FlameFeatureRenderer.Submit.class)
    public static class SubmitMixin implements FlameFeatureRendererSD.Submit {
        private boolean isSoulFire = false;

        @Override
        public boolean isSoulFire() {
            return isSoulFire;
        }

        @Override
        public void setSoulFire(boolean isSoulFire) {
            this.isSoulFire = isSoulFire;
        }
    }
}
