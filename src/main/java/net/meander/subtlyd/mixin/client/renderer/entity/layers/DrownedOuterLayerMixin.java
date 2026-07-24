package net.meander.subtlyd.mixin.client.renderer.entity.layers;

import net.meander.subtlyd.client.renderer.entity.state.UndeadRenderStateSD;
import net.meander.subtlyd.util.UtilSD;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.DrownedOuterLayer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(DrownedOuterLayer.class)
public class DrownedOuterLayerMixin {
    private static final Identifier DROWNED_OUTER_LAYER_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/drowned_outer_layer.png");
    private static final Identifier GURGLE_OUTER_LAYER_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/drowned_outer_layer_baby.png");
    private static final Identifier DROWNED_LEADER_OUTER_LAYER_LOCATION = UtilSD.identifier("textures/entity/zombie/drowned_leader_outer_layer.png");
    private static final Identifier GURGLE_LEADER_OUTER_LAYER_LOCATION = UtilSD.identifier("textures/entity/zombie/drowned_leader_outer_layer_baby.png");
    private Identifier layerLocation;

    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/ZombieRenderState;FF)V", at = @At("HEAD"))
    private void declareLayerLocation(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, ZombieRenderState state, float yRot, float xRot, CallbackInfo ci) {
        layerLocation = ((UndeadRenderStateSD) state).isLeader() ? (state.isBaby ? GURGLE_LEADER_OUTER_LAYER_LOCATION : DROWNED_LEADER_OUTER_LAYER_LOCATION) : (state.isBaby ? GURGLE_OUTER_LAYER_LOCATION : DROWNED_OUTER_LAYER_LOCATION);
    }

    @ModifyArg(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/ZombieRenderState;FF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/DrownedOuterLayer;coloredCutoutModelCopyLayerRender(Lnet/minecraft/client/model/Model;Lnet/minecraft/resources/Identifier;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;II)V"),
            index = 1
    )
    private Identifier setLeaderOuterLayer(Identifier identifier) {
        return layerLocation;
    }
}
