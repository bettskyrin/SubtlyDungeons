package net.meander.subtlyd.mixin.common.world.item;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BowItem.class)
public class BowItemMixin {
    @ModifyArg(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BowItem;shoot(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Ljava/util/List;FFZLnet/minecraft/world/entity/LivingEntity;)V"), index = 5)
    private float modifyUncertainty(float originalInaccuracy, @Local(name = "timeHeld") int timeHeld) {
        float uncertainty = 0.25F;

        if (timeHeld >= 60) {
            return (float) (uncertainty + Mth.clamp(0.5 + (double) (10 * (timeHeld - 60)) / 140, 0.5, 10.5));
        }
        return uncertainty;
    }

    @ModifyArg(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BowItem;shoot(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Ljava/util/List;FFZLnet/minecraft/world/entity/LivingEntity;)V"), index = 7)
    private boolean fatigueCrit(boolean isCrit, @Local(name = "timeHeld") int timeHeld) {
        if (timeHeld >= 60) {
            return false;
        }
        return isCrit;
    }
}