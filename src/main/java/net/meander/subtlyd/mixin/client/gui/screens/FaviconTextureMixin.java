package net.meander.subtlyd.mixin.client.gui.screens;

import com.mojang.blaze3d.platform.NativeImage;
import net.meander.subtlyd.client.OptionsSD;
import net.meander.subtlyd.util.UtilSD;
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

    /**
     * Saves a thumbnail of size 455x256 pixels
     * @param image The saved thumbnail
     */
    @Inject(method = "upload", at = @At("HEAD"), cancellable = true)
    private void upload(NativeImage image, CallbackInfo ci) {
        if (OptionsSD.gui().get()) {
            if (image.getWidth() == 455 && image.getHeight() == 256) {
                try {
                    checkOpen();

                    if (texture == null) {
                        texture = new DynamicTexture(() -> "Favicon " + textureLocation, image);
                    } else {
                        texture.setPixels(image);
                        texture.upload();
                    }

                    textureManager.register(textureLocation, texture);
                } catch (Throwable e) {
                    image.close();
                    clear();
                    UtilSD.LOGGER.error(e.getMessage());
                }
            } else {
                image.close();
                UtilSD.LOGGER.error(new IllegalArgumentException("Icon must be 455x256, but was " + image.getWidth() + "x" + image.getHeight()).getMessage());
            }
            ci.cancel();
        }
    }
}