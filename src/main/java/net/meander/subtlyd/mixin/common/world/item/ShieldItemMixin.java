package net.meander.subtlyd.mixin.common.world.item;

import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.entity.ai.attributes.AttributesSD;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ShieldItem.class)
public class ShieldItemMixin {
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item;<init>(Lnet/minecraft/world/item/Item$Properties;)V"))
    private static Item.Properties applyShieldStrength(Item.Properties properties) {
        ItemAttributeModifiers modifiers = ItemAttributeModifiers.builder()
                .add(
                        AttributesSD.SHIELD_STRENGTH,
                        new AttributeModifier(
                                Util.identifier("base_shield_strength"),
                                5.0, 
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.ANY
                ).build();

        return properties.component(DataComponents.ATTRIBUTE_MODIFIERS, modifiers);
    }
}