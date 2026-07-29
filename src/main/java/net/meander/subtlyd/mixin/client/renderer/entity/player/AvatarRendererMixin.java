package net.meander.subtlyd.mixin.client.renderer.entity.player;

import net.meander.subtlyd.client.model.geom.ModelLayersSD;
import net.meander.subtlyd.client.model.object.equipment.QuiverModel;
import net.meander.subtlyd.client.renderer.layer.QuiverLayer;
import net.meander.subtlyd.client.renderer.state.QuiverRenderState;
import net.meander.subtlyd.tags.ItemTagsSD;
import net.meander.subtlyd.util.UtilSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void addLayers(EntityRendererProvider.Context context, boolean slimSteve, CallbackInfo ci) {
        AvatarRenderer<?> avatarRenderer = (AvatarRenderer<?>) (Object) this;
        QuiverModel model = new QuiverModel(context.bakeLayer(ModelLayersSD.QUIVER));

        avatarRenderer.addLayer(new QuiverLayer(avatarRenderer, model));
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
    private void extractQuiverRenderState(Avatar entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
        ItemStack legsStack = entity.getItemBySlot(EquipmentSlot.LEGS);
        Item item = legsStack.getItem();
        QuiverRenderState quiverState = (QuiverRenderState) state;

        if (legsStack.is(ItemTagsSD.QUIVERS)) {
            quiverState.setHasQuiver(true);

            if (legsStack.is(ItemsSD.QUIVER)) {
                quiverState.setQuiverTexture(UtilSD.identifier("textures/entity/equipment/quiver/quiver.png"));
            } else {
                Identifier itemId = BuiltInRegistries.ITEM.getKey(item);

                quiverState.setQuiverTexture(UtilSD.identifier("textures/entity/equipment/quiver/" + itemId.getPath() + ".png"));
            }
        } else {
            quiverState.setHasQuiver(false);
        }
    }
}
