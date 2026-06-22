package net.meander.subtlyd.world.item;

import net.meander.subtlyd.core.component.DataComponentsSD;
import net.meander.subtlyd.world.item.component.StealthAttack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwingAnimationType;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.*;

public abstract class ItemSD extends Item {
    public ItemSD(Properties properties) {
        super(properties);
    }

    public static class PropertiesSD extends Properties {
        public Item.Properties dagger(final ToolMaterial material, final float attackDamageBaseline, final float attackSpeedBaseline, final float stealthDamageBonus) {
            return this.durability(material.durability())
                    .repairable(material.repairItems())
                    .enchantable(material.enchantmentValue())
                    .component(DataComponents.ATTACK_RANGE, new AttackRange(0.0F, 2.5F, 0.0F, 3.0F, 0.1F, 0.2F))
                    .component(DataComponents.SWING_ANIMATION, new SwingAnimation(SwingAnimationType.STAB, (int) (20.0F / (4.0F + attackSpeedBaseline))))
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, attackDamageBaseline + material.attackDamageBonus(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                            .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeedBaseline, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                            .build())
                    .component(DataComponentsSD.STEALTH_ATTACK, new StealthAttack(stealthDamageBonus, -0.5F))
                    .component(DataComponents.USE_EFFECTS, new UseEffects(true, false, 1.0F))
                    .component(DataComponents.WEAPON, new Weapon(1));
        }
    }
}
