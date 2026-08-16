package net.meander.subtlyd.mixin.client.renderer.entity;

import net.minecraft.client.renderer.entity.GuardianRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GuardianRenderer.class)
public class GuardianRendererMixin {
    private static final Identifier GUARDIAN_BEAM_LOCATION = Identifier.withDefaultNamespace("textures/entity/guardian/guardian_beam.png");
    private static final RenderType EMISSIVE_BEAM = RenderTypes.eyes(GUARDIAN_BEAM_LOCATION);

    @ModifyArg(
        method = "renderBeam",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitCustomGeometry(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;Lnet/minecraft/client/renderer/SubmitNodeCollector$CustomGeometryRenderer;)V"
        ),
        index = 1
    )
    private static RenderType setEmissive(RenderType original) {
        return EMISSIVE_BEAM;
    }
}