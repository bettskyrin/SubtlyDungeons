package net.meander.subtlyd.mixin.common.world.item;

import net.meander.subtlyd.data.tags.ItemTagsSD;
import net.meander.subtlyd.world.item.ItemStackSD;
import net.meander.subtlyd.world.item.alchemy.PotionsSD;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.enchantment.Enchantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Unique private static final List<Item> UNCOMMON_ITEMS = List.of(
            Items.NETHERITE_AXE,
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
            Items.TIPPED_ARROW
    );
    @Unique private static final List<Item> RARE_ITEMS = List.of(
            Items.WITHER_ROSE
    );

    @Unique private static final List<Potion> RARE_EFFECTS = List.of(
            PotionsSD.DECAY.value()
    );

    /**
     * Modifies the rarity level of items
     */
    @Inject(method = "getRarity", at = @At("HEAD"))
    private void getRarity(CallbackInfoReturnable<Rarity> cir) {
        ItemStack itemStack = (ItemStack) (Object) this;

        if (RARE_ITEMS.contains(itemStack.getItem()) || isRarePotion(itemStack)) {
            itemStack.set(DataComponents.RARITY, Rarity.RARE);
        } else if (UNCOMMON_ITEMS.contains(itemStack.getItem())) {
            itemStack.set(DataComponents.RARITY, Rarity.UNCOMMON);
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

    @Inject(method = "getComponents", at = @At("RETURN"), cancellable = true)
    private void getComponents(CallbackInfoReturnable<DataComponentMap> cir) {
        ItemStack itemStack = (ItemStack) (Object) this;
        DataComponentMap newDataComponentMap = null;
        DataComponentMap oldDataComponentMap = cir.getReturnValue();
        DataComponentMap.Builder builder = DataComponentMap.builder().addAll(oldDataComponentMap);

        if (newDataComponentMap == null) {
            if (itemStack.is(ItemTagsSD.LIQUID_CONSUMABLES)) {
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
                }
            } else if (itemStack.is(ItemTagsSD.NON_HUMANOID_ARMOR) || itemStack.is(Items.ELYTRA)) {
                builder.set(DataComponents.ENCHANTABLE, new Enchantable(ItemStackSD.getEnchantibilityFromMap(itemStack.getItem())));
            }
        }
        newDataComponentMap = builder.build();
        cir.setReturnValue(newDataComponentMap);
    }
}
