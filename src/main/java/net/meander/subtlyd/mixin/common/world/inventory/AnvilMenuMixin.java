package net.meander.subtlyd.mixin.common.world.inventory;

import com.llamalad7.mixinextras.sugar.Local;
import net.meander.subtlyd.core.component.DataComponentsSD;
import net.meander.subtlyd.world.inventory.AnvilMenuSD;
import net.meander.subtlyd.world.item.enchantment.EnchantmentHelperSD;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
    @Shadow public abstract int getCost();
    @Shadow @Final private DataSlot cost;

    public AnvilMenuMixin(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition itemInputSlots) {
        super(menuType, containerId, inventory, access, itemInputSlots);
    }

    /**
     * Rebalances the anvil enchantment costs.
     * @param input The first anvil item.
     */
    @Inject(method = "createResult", at = @At("RETURN"))
    private void modifyCosts(CallbackInfo ci, @Local(name = "input") ItemStack input) {
        ItemStack addition = inputSlots.getItem(1);
        ItemStack result = resultSlots.getItem(0);
        boolean hasMending = EnchantmentHelperSD.checkEnchantment(input, addition, Enchantments.MENDING);
        boolean usingBook = addition.has(DataComponents.STORED_ENCHANTMENTS);

        if (AnvilMenuSD.isEnchanting(input, addition)) {
            if (hasMending && !usingBook) {
                if (!addition.isEnchanted()) {
                    result.set(DataComponents.REPAIR_COST, getCost() + 1);
                } else {
                    result.set(DataComponents.REPAIR_COST, Math.max(1, getCost() - 4));
                }
            }

            if (!(input.isDamaged() || addition.isDamaged())) {
                result.set(DataComponents.REPAIR_COST, input.getOrDefault(DataComponents.REPAIR_COST, 0));
            } else {
                result.set(DataComponents.REPAIR_COST, getCost());
            }

            int magicLevel = result.getOrDefault(DataComponentsSD.MAGIC_LEVEL, 0);
            int magicLevelIncrease = AnvilMenuSD.getMagicLevelIncrease(input, addition);
            int magicLimit = AnvilMenuSD.getMagicLimit(input, addition);

            if (magicLevel > magicLimit) {
                resultSlots.setItem(0, ItemStack.EMPTY);
            }
            result.set(DataComponentsSD.MAGIC_LEVEL, magicLevel + magicLevelIncrease);
            cost.set(Math.max(1, result.getOrDefault(DataComponentsSD.MAGIC_LEVEL, 0)));
        }
    }
}
