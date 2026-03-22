package net.meander.subtlyd.mixin.common.world.item;

import net.meander.subtlyd.util.data.tags.ItemTagsSD;
import net.meander.subtlyd.world.item.ItemStackSD;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.enchantment.Enchantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Unique private static final List<Item> uncommonItems = List.of(Items.NETHERITE_AXE,
            Items.NETHERITE_HOE,
            Items.NETHERITE_PICKAXE,
            Items.NETHERITE_SHOVEL,
            Items.NETHERITE_SPEAR,
            Items.NETHERITE_SWORD,
            Items.NETHERITE_HELMET,
            Items.NETHERITE_CHESTPLATE,
            Items.NETHERITE_LEGGINGS,
            Items.NETHERITE_BOOTS,
            Items.NETHERITE_HORSE_ARMOR,
            Items.NETHERITE_NAUTILUS_ARMOR,
            Items.OMINOUS_TRIAL_KEY,
            Items.LINGERING_POTION,
            Items.TIPPED_ARROW);
    private static final List<Item> rareItems = List.of(Items.WITHER_ROSE);
    /**
     * Modifies the rarity level of items
     */
    @Inject(method = "getRarity", at = @At("HEAD"), cancellable = true)
    private void getRarity(CallbackInfoReturnable<Rarity> cir) {
        ItemStack itemStack = (ItemStack) (Object) this;

        if (uncommonItems.contains(itemStack.getItem())) {
            cir.setReturnValue(Rarity.UNCOMMON);
        } else if (rareItems.contains(itemStack.getItem())) {
            cir.setReturnValue(Rarity.RARE);
        }
    }

    @Inject(method = "getComponents", at = @At("RETURN"), cancellable = true)
    private void getComponents(CallbackInfoReturnable<DataComponentMap> cir) {
        ItemStack itemStack = (ItemStack) (Object) this;
        DataComponentMap newDataComponentMap = null;
        DataComponentMap oldDataComponentMap = cir.getReturnValue();
        DataComponentMap.Builder builder = DataComponentMap.builder().addAll(oldDataComponentMap);

        if (itemStack.is(ItemTagsSD.LIQUID_CONSUMABLES)) {
            if (newDataComponentMap == null) {
                Consumable oldConsumable = oldDataComponentMap.get(DataComponents.CONSUMABLE);

                if (oldConsumable != null) {
                    Consumable newConsumable = new Consumable(
                            1.0F,
                            oldConsumable.animation(),
                            oldConsumable.sound(),
                            oldConsumable.hasConsumeParticles(),
                            oldConsumable.onConsumeEffects()
                    );
                    builder = DataComponentMap.builder()
                            .addAll(oldDataComponentMap)
                            .set(DataComponents.CONSUMABLE, newConsumable);

                    if (itemStack.is(Items.POTION)) {
                        builder.set(DataComponents.MAX_STACK_SIZE, 16);
                    }

                    newDataComponentMap = builder.build();
                } else {
                    newDataComponentMap = oldDataComponentMap;
                }

                if (itemStack.is(ItemTagsSD.NON_HUMANOID_ARMOR)) {
                    builder.set(DataComponents.ENCHANTABLE, new Enchantable(ItemStackSD.getEnchantability(itemStack.getItem())));
                }
            }
            cir.setReturnValue(newDataComponentMap);
        }
    }
}
