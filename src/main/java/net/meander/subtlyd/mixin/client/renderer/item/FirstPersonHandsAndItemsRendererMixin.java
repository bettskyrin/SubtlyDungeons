package net.meander.subtlyd.mixin.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.meander.subtlyd.tags.ItemTagsSD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class FirstPersonHandsAndItemsRendererMixin {
    @Inject(method = "submitArmWithItem", at = @At("HEAD"), cancellable = true)
    private void hideItem(
            PlayerRenderState playerState,
            FirstPersonHandsAndItemsRenderState state,
            float partialTicks,
            float xRot,
            InteractionHand hand,
            float attack,
            ItemStack itemStack,
            float inverseArmHeight,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            CallbackInfo ci
    ) {
        if (!Minecraft.getInstance().options.shieldAnimation().get()) {
            if (!state.isScoping) {
                if (playerState.avatarRenderState != null) {
                    if (playerState.avatarRenderState.isUsingItem && playerState.avatarRenderState.useItemHand == hand) {
                        if (itemStack.is(ItemTagsSD.SHIELDS)) {
                            ci.cancel();
                        }
                    }
                }
            }
        }
    }
}