package com.kr1s1s.subtlyd.mixin.client.gui.screens;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.gui.screens.FaviconTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FaviconTexture.class)
public class FaviconTextureMixin {
    @Shadow @Final Identifier textureLocation;
    @Shadow @Nullable DynamicTexture texture;
    @Shadow @Final TextureManager textureManager;
    @Shadow private void checkOpen() {}
    @Shadow private void clear() {}


    @Inject(method = "upload", at = @At("HEAD"), cancellable = true)
    public void upload(NativeImage nativeImage, CallbackInfo ci) {
        ci.cancel();
        if (nativeImage.getWidth() == 455 && nativeImage.getHeight() == 256) {
            try {
                this.checkOpen();
                if (this.texture == null) {
                    this.texture = new DynamicTexture(() -> "Favicon " + this.textureLocation, nativeImage);
                } else {
                    this.texture.setPixels(nativeImage);
                    this.texture.upload();
                }

                this.textureManager.register(this.textureLocation, this.texture);
            } catch (Throwable var3) {
                nativeImage.close();
                this.clear();
                throw var3;
            }
        } else {
            nativeImage.close();
            throw new IllegalArgumentException("Icon must be 455x256, but was " + nativeImage.getWidth() + "x" + nativeImage.getHeight());
        }
    }
}
