package net.meander.subtlyd.mixin.common.world.item.enchantment;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    @Shadow public abstract Component description();

    @Inject(method = { "isSupportedItem", "isPrimaryItem", "canEnchant" }, at = @At("HEAD"), cancellable = true)
    private void modifySupportedItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.is(Items.MACE)) {
            if (shouldMaceSupport(description())) {
                cir.setReturnValue(true);
            }
        }
    }

    private boolean shouldMaceSupport(Component component) {
        if (component.getContents() instanceof TranslatableContents translatable) {
            String key = translatable.getKey();
            String registry = Registries.ENCHANTMENT.identifier().getPath();

            if (key.equals(registry + "." + Enchantments.LOOTING.identifier().toLanguageKey()) || key.equals(registry + "." + Enchantments.KNOCKBACK.identifier().toLanguageKey())) {
                return true;
            }
        }

        for (Component sibling : component.getSiblings()) {
            if (shouldMaceSupport(sibling)) {
                return true;
            }
        }
        return false;
    }
}