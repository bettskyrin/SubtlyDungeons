package net.meander.subtlyd.mixin.common.world.item;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public class PotionItemMixin {
    @Inject(method = "getName", at = @At("RETURN"), cancellable = true)
    private void modifyPotionName(ItemStack itemStack, CallbackInfoReturnable<Component> cir) {
        PotionContents contents = itemStack.get(DataComponents.POTION_CONTENTS);

        if (contents != null) {
            contents.potion().flatMap(Holder::unwrapKey).ifPresent(potionKey -> {
                String path = potionKey.identifier().getPath();

                if (path.startsWith("long_")) {
                    cir.setReturnValue(Component.translatable("item.subtlyd.potion.long_potion", cir.getReturnValue()));
                } else if (path.startsWith("strong_")) {
                    cir.setReturnValue(Component.translatable("item.subtlyd.potion.strong_potion", cir.getReturnValue()));
                }
            });
        }
    }
}
