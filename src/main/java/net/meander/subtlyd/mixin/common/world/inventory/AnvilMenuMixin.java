package net.meander.subtlyd.mixin.common.world.inventory;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.meander.subtlyd.core.component.DataComponentsSD;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
    @Shadow @Final private DataSlot cost;
    @Shadow public abstract int getCost();

    public AnvilMenuMixin(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition itemInputSlots) {
        super(menuType, containerId, inventory, access, itemInputSlots);
    }

    /**
     * Rebalances the anvil enchantment costs.
     * @param input The first anvil item.
     */
    @Inject(method = "createResult", at = @At("RETURN"))
    private void modifyCosts(CallbackInfo ci, @Local(name = "input") ItemStack input) { // TODO Check
        ItemStack addition = inputSlots.getItem(1);
        ItemStack result = resultSlots.getItem(0);
        List<ItemStack> inputs = List.of(input, addition);
        boolean hasMending = false;
        boolean usingBook = addition.has(DataComponents.STORED_ENCHANTMENTS);


        for (ItemStack inputStack : inputs) {
            for (Object2IntMap.Entry<Holder<Enchantment>> entry : inputStack.getEnchantments().entrySet()) {
                if (entry.getKey().unwrapKey().isPresent()) {
                    if (entry.getKey().unwrapKey().get() == Enchantments.MENDING) {
                        hasMending = true;
                        input.set(DataComponents.REPAIR_COST, 0);
                        break;
                    }
                }
            }
        }

        if (hasMending && !usingBook) {
            if (!addition.isEnchanted()) {
                cost.set(1);
            } else {
                cost.set(Math.max(1, getCost() - 4));
            }
        }

        if (input.isEnchanted() || (addition.isEnchanted()) || usingBook) {
            result.set(DataComponents.REPAIR_COST, result.getOrDefault(DataComponents.REPAIR_COST, 0));
            result.set(DataComponentsSD.MAGIC_LEVEL, result.getOrDefault(DataComponentsSD.MAGIC_LEVEL, 0) + addition.getOrDefault(DataComponents.REPAIR_COST, 1));
        }
    }
}
