package net.meander.subtlyd.mixin.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
import net.minecraft.client.renderer.entity.state.HorseRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.NautilusRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SimpleEquipmentLayer.class)
public abstract class SimpleEquipmentLayerMixin <S extends LivingEntityRenderState> {
    /**
     * Enables invisible armor for high enchantibility armors.
     */
    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;FF)V", at = @At("HEAD"), cancellable  = true)
    private void invisibilityCompatibility(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot, CallbackInfo ci) {
        if (state.isInvisible) {
            if (state instanceof HorseRenderState || state instanceof NautilusRenderState) {
                ItemStack armor = getArmorPiece(state);
                Enchantable enchantable = armor.get(DataComponents.ENCHANTABLE);

                if (enchantable != null && enchantable.value() >= 15) {
                    ci.cancel();
                }
            }
        }
    }

    /**
     * Returns the correct armor piece depending on the entity.
     * @param state The render state
     * @return The proper armor piece as an ItemStack.
     * @param <S> The render state type.
     */
    private static <S extends LivingEntityRenderState> ItemStack getArmorPiece(S state) {
        if (state instanceof HorseRenderState horseState) {
            if (!horseState.bodyArmorItem.isEmpty()) {
                return horseState.bodyArmorItem;
            }
        } else if (state instanceof NautilusRenderState nautilusState) {
            if (!nautilusState.bodyArmorItem.isEmpty()) {
                return nautilusState.bodyArmorItem;
            }
        }
        return ItemStack.EMPTY;
    }
}
