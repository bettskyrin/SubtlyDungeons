package net.meander.subtlyd.mixin.client.gui.screens;

import com.mojang.blaze3d.platform.NativeImage;
import net.meander.subtlyd.client.OptionsSD;
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
    @Shadow @Final private Identifier textureLocation;
    @Shadow @Nullable private DynamicTexture texture;
    @Shadow @Final private TextureManager textureManager;
    @Shadow private void checkOpen() {}
    @Shadow public void clear() {}
    private static final boolean canChangeUi = OptionsSD.EXPERIMENTAL_GUI.get();

    /**
     * Saves a thumbnail of size 455x256 pixels
     * @param image The saved thumbnail
     */
    @Inject(method = "upload", at = @At("HEAD"), cancellable = true)
    private void upload(NativeImage image, CallbackInfo ci) {
        if (canChangeUi) {
            if (image.getWidth() == 455 && image.getHeight() == 256) {
                try {
                    this.checkOpen();
                    if (this.texture == null) {
                        this.texture = new DynamicTexture(() -> "Favicon " + this.textureLocation, image);
                    } else {
                        this.texture.setPixels(image);
                        this.texture.upload();
                    }

                    this.textureManager.register(this.textureLocation, this.texture);
                } catch (Throwable var3) {
                    image.close();
                    this.clear();
                    throw var3;
                }
            } else {
                image.close();
                throw new IllegalArgumentException("Icon must be 455x256, but was " + image.getWidth() + "x" + image.getHeight());
            }
            ci.cancel();
        }
    }
}