package net.meander.subtlyd.mixin.common.world.item;

import net.meander.subtlyd.util.data.tags.ItemTagsSD;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.Consumable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    final ItemStack itemStack = (ItemStack) (Object) this;
    DataComponentMap newDataComponentMap = null;
    /**
     * Modifies the rarity level of items
     */
    @Inject(method = "getRarity", at = @At("HEAD"), cancellable = true)
    private void getRarity(CallbackInfoReturnable<Rarity> cir) {
        List<Item> uncommonItems = List.of(Items.NETHERITE_AXE,
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
        List<Item> rareItems = List.of(Items.WITHER_ROSE);

        if (uncommonItems.contains(itemStack.getItem())) {
            cir.setReturnValue(Rarity.UNCOMMON);
        } else if (rareItems.contains(itemStack.getItem())) {
            cir.setReturnValue(Rarity.RARE);
        }
    }

    @Inject(method = "getComponents", at = @At("RETURN"), cancellable = true)
    private void getComponents(CallbackInfoReturnable<DataComponentMap> cir) {
        if (itemStack.is(ItemTagsSD.LIQUID_CONSUMABLES)) {
            if (newDataComponentMap == null) {
                DataComponentMap oldDataComponentMap = cir.getReturnValue();
                DataComponentMap.Builder builder = DataComponentMap.builder().addAll(oldDataComponentMap);
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
            }
            cir.setReturnValue(newDataComponentMap);
        }
    }
}
