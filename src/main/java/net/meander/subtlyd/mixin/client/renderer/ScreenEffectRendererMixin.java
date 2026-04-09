package net.meander.subtlyd.mixin.client.renderer;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {
    /**
     * Prevents players from having fire rendered on them in first person.
     */
    @Redirect(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isOnFire()Z"))
    private boolean isOnFire(LocalPlayer localPlayer) {
        return localPlayer.isOnFire() && !localPlayer.hasEffect(MobEffects.FIRE_RESISTANCE);
    }
}
