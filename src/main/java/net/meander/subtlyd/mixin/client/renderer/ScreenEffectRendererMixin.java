package net.meander.subtlyd.mixin.client.renderer;

import net.meander.subtlyd.world.entity.EntitySD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {
    @Shadow @Final private SpriteGetter sprites;

    /**
     * Prevents players from having fire rendered on them in first person.
     */
    @Redirect(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isOnFire()Z"))
    private boolean isOnFire(LocalPlayer localPlayer) {
        return localPlayer.isOnFire() && !localPlayer.hasEffect(MobEffects.FIRE_RESISTANCE);
    }

    /**
     * Allows soul fire overlay in first person
     * @param fireSprite The original fire sprite.
     * @return The new fire sprite
     */
    @ModifyArg(method = "submit", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/ScreenEffectRenderer;submitFire(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V"),
    index = 2)
    private TextureAtlasSprite setFireType(TextureAtlasSprite fireSprite) {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player != null && fireSprite == sprites.get(ModelBakery.FIRE_1)) {
            if (EntitySD.shouldSoulFireBurn(player)) {
                return sprites.get(Sheets.BLOCKS_MAPPER.defaultNamespaceApply("soul_fire_1"));
            }
        }
        return fireSprite;
    }
}
