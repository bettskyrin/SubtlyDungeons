package net.meander.subtlyd.mixin.client.world.inventory;

import net.meander.subtlyd.world.inventory.AnvilMenuSD;
import net.meander.subtlyd.world.item.ItemStackSD;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ItemCombinerScreen<AnvilMenu> {
    @Shadow @Final private Player player;
    private int color = 0;

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

        if (player != null && !addition.isEmpty() && !player.hasInfiniteMaterials()) {
            boolean usingBook = addition.has(DataComponents.STORED_ENCHANTMENTS);

            if ((!input.isEnchanted() && !addition.isEnchanted()) && !usingBook || !addition.isEnchantable()) {
                if (menu.getCost() >= 40) {
                    color = -40864;
                    return Component.translatable("container.repair.unfixable");
                }
            } else {
                int inputEnchantability = ItemStackSD.getEnchantability(input);
                int additionEnchantability = ItemStackSD.getEnchantability(addition);

                if (menu.getCost() > AnvilMenuSD.getCostByEnchantibility(inputEnchantability, additionEnchantability)) {
                    color = -40864;
                    return Component.translatable("container.repair.unenchantable");
                } else {
                    color = -8323296;
                    return Component.translatable("container.repair.cost", menu.getCost());
                }
            }
        }
        return line;
    }

    @ModifyVariable(method = "extractLabels", at = @At(value = "STORE"), name = "color")
    private int modifyColor(int color) {
        if (this.color == 0) {
            return color;
        }
        return this.color;
    }
}
