package net.meander.subtlyd.mixin.common.world.item;

import net.meander.subtlyd.world.item.alchemy.PotionsSD;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    private static final List<Potion> RARE_EFFECTS = List.of(
            PotionsSD.DECAY.value()
    );

    @Inject(method = "getRarity", at = @At("HEAD"))
    private void modifyRarity(CallbackInfoReturnable<Rarity> cir) {
        ItemStack itemStack = (ItemStack) (Object) this;

        if (isRarePotion(itemStack)) {
            itemStack.set(DataComponents.RARITY, Rarity.RARE);
        }
    }

    private boolean isRarePotion(ItemStack itemStack) {
        if (itemStack.getComponents().has(DataComponents.POTION_CONTENTS)) {
            Optional<Holder<Potion>> potion = itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion();

            if (potion.isPresent()) {
                return RARE_EFFECTS.contains(potion.get().value());
            }
        }

        return false;
    }
}
