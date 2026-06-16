package net.meander.subtlyd.mixin.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.meander.subtlyd.client.renderer.ChargedTridentState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStackRenderState.class)
public class ItemStackRenderStateMixin implements ChargedTridentState.Accessor {
    private boolean subtlyd$charged = false;

    @Override
    public boolean subtlyd$isCharged() {
        return subtlyd$charged;
    }

    @Override
    public void subtlyd$setCharged(boolean charged) {
        subtlyd$charged = charged;
    }

    @Inject(method = "submit", at = @At("HEAD"))
    private void pushChargedState(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, int outlineColor, CallbackInfo ci) {
        if (subtlyd$isCharged()) {
            ChargedTridentState.CHANNELING_CHARGE.set(true);
        }
    }

    @Inject(method = "submit", at = @At("TAIL"))
    private void popChargedState(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, int outlineColor, CallbackInfo ci) {
        ChargedTridentState.CHANNELING_CHARGE.remove();
    }
}
