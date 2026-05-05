package net.meander.subtlyd.mixin.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.meander.subtlyd.client.renderer.ChargedTridentState;
import net.meander.subtlyd.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.object.projectile.TridentModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.TridentSpecialRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentSpecialRenderer.class)
public class TridentSpecialRendererMixin {
    @Shadow @Final private TridentModel model;
    Minecraft minecraft = Minecraft.getInstance();

    @Inject(method = "submit", at = @At("TAIL"))
    private void renderElectricity(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor, CallbackInfo ci) {
        if (ChargedTridentState.IS_CHARGED.get() && minecraft.player != null) {
            float scrollTime = (float) minecraft.player.tickCount + minecraft.getDeltaTracker().getGameTimeDeltaTicks();
            RenderType auraType = RenderTypes.energySwirl(Util.identifier("textures/item/electric_charge.png"),
                    scrollTime * 0.01F,
                    scrollTime * 0.01F
            );

            poseStack.pushPose();
            poseStack.scale(1.03F, 1.03F, 1.03F); // TODO Check
            // TODO First person

            submitNodeCollector.submitCustomGeometry(poseStack, auraType, (pose, vertexConsumer) -> {
                PoseStack newStack = new PoseStack();
                newStack.last().pose().set(pose.pose());
                newStack.last().normal().set(pose.normal());

                model.renderToBuffer(newStack, vertexConsumer, lightCoords, overlayCoords, 0xFFFFFFFF);
            });

            poseStack.popPose();
        }
    }
}
