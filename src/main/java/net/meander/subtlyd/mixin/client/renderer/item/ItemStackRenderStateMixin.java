package net.meander.subtlyd.mixin.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.meander.subtlyd.client.renderer.entity.state.ChargedTridentState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStackRenderState.class)
public class ItemStackRenderStateMixin implements ChargedTridentState.Accessor {
    private boolean isCharged = false;

    @Override
    public boolean isCharged() {
        return isCharged;
    }

    @Override
    public void setCharged(boolean isCharged) {
        this.isCharged = isCharged;
    }

    @Inject(method = "submit", at = @At("HEAD"))
    private void pushChargedState(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, int outlineColor, CallbackInfo ci) {
        if (isCharged()) {
            ChargedTridentState.CHANNELING_CHARGE.set(true);
        }
    }

    @Inject(method = "submit", at = @At("TAIL"))
    private void popChargedState(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, int outlineColor, CallbackInfo ci) {
        ChargedTridentState.CHANNELING_CHARGE.remove();
    }
}
