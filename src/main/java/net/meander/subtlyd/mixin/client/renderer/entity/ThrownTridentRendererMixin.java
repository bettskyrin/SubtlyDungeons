package net.meander.subtlyd.mixin.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.meander.subtlyd.client.renderer.ChargedTridentState;
import net.meander.subtlyd.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.object.projectile.TridentModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTridentRenderer.class)
public class ThrownTridentRendererMixin  {
    @Shadow @Final private TridentModel model;

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/projectile/arrow/ThrownTrident;Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;F)V",
            at = @At("TAIL"))
    private void extractAuraState(ThrownTrident entity, ThrownTridentRenderState state, float partialTicks, CallbackInfo ci) {
        boolean isCharged = ((ChargedTridentState.Accessor) entity).subtlyDungeons$isCharged();

        ((ChargedTridentState.Accessor) state).subtlyDungeons$setCharged(isCharged);
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/ThrownTridentRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/OrderedSubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/resources/Identifier;IIILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
                    shift = At.Shift.AFTER))
    private void renderAura(ThrownTridentRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (((ChargedTridentState.Accessor) state).subtlyDungeons$isCharged() && minecraft.player != null) {

            float scrollTime = (float) minecraft.player.tickCount + minecraft.getDeltaTracker().getGameTimeDeltaTicks();
            RenderType auraRenderType = RenderTypes.energySwirl(Util.identifier("textures/item/electric_charge.png"),
                    scrollTime * 0.01F,
                    scrollTime * 0.01F
            );

            poseStack.pushPose();
            submitNodeCollector.submitCustomGeometry(poseStack, auraRenderType, (pose, vertexConsumer) -> {
                PoseStack newStack = new PoseStack();

                newStack.last().pose().set(pose.pose());
                newStack.last().normal().set(pose.normal());
                newStack.scale(1.0F, 1.0F, 1.0F);
                model.renderToBuffer(newStack, vertexConsumer, state.lightCoords, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            });

            poseStack.popPose();
        }
    }
}
