package net.meander.subtlyd.mixin.client.world.inventory;

import net.meander.subtlyd.core.component.DataComponentsSD;
import net.meander.subtlyd.world.inventory.AnvilMenuSD;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ItemCombinerScreen<AnvilMenu> {
    @Shadow @Final private Player player;
    private int color = -8323296;

    public AnvilScreenMixin(AnvilMenu menu, Inventory inventory, Component title, Identifier menuResource) {
        super(menu, inventory, title, menuResource);
    }

    /**
     * Determines whether the anvil limit is from enchanting or repairing.
     * @param line The message to give
     */
    @ModifyVariable(method = "extractLabels", at = @At(value = "STORE"), name = "line")
    private Component determineLimit(Component line) {
        ItemStack input = menu.getSlot(0).getItem();
        ItemStack addition = menu.getSlot(1).getItem();
        ItemStack result = menu.getSlot(2).getItem();

        if (player != null && !addition.isEmpty() && !player.hasInfiniteMaterials()) {
            boolean isEnchanting = AnvilMenuSD.isEnchanting(input, addition);
            int magicLevel = result.getOrDefault(DataComponentsSD.MAGIC_LEVEL, 0);
            int magicLimit = AnvilMenuSD.getMagicLimit(input, addition);

            int repairCost = input.getOrDefault(DataComponents.REPAIR_COST, 0);
            int repairLimit = AnvilMenuSD.checkEnchantment(input, addition, Enchantments.MENDING) ? 250 : 40;

            if (magicLevel > magicLimit) {
                if (isEnchanting) {
                    color = -40864;
                    return Component.translatable("container.repair.unenchantable");
                }
            }

            if (repairCost >= repairLimit) {
                color = -40864;
                return Component.translatable("container.repair.unfixable");
            }

            if (!menu.getSlot(2).mayPickup(this.player)) {
                color = -40864;
            }
            return Component.translatable("container.repair.cost", menu.getCost());
        }
        return line;
    }

    @ModifyVariable(method = "extractLabels", at = @At(value = "STORE"), name = "color")
    private int modifyColor(int color) {
        return this.color;
    }
}
