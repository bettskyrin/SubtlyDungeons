package net.meander.subtlyd.world.item;

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.meander.subtlyd.core.component.DataComponentsSD;
import net.meander.subtlyd.tags.ItemTagsSD;
import net.meander.subtlyd.util.UtilSD;
import net.meander.subtlyd.world.entity.ai.attributes.AttributesSD;
import net.meander.subtlyd.world.item.component.StealthWeapon;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @see Item
 */
public abstract class ItemSD extends Item {
    public static final Identifier SHIELD_STRENGTH = UtilSD.identifier("shield_strength");

    public ItemSD(Properties properties) {
        super(properties);
    }

    private static void modifyComponent(Supplier<Collection<Item>> itemsSupplier, BiConsumer<DataComponentMap.Builder, Item> builderConsumer) {
        DefaultItemComponentEvents.MODIFY.register(context -> context.modify(itemsSupplier.get(), builderConsumer));
    }

    private static void modifyWeapons() {
        modifyComponent(() -> ItemTagsSD.getItems(ItemTags.AXES), (builder, item) -> {
            ItemStack itemStack = item.getDefaultInstance();

            if (itemStack.is(ItemTags.AXES)) {
                builder.set(DataComponents.WEAPON, new Weapon(1, 1.6F));
            }
        });
    }

    private static void modifyEnchantablity() {
        modifyComponent(() -> ItemTagsSD.getItems(ItemTagsSD.HAS_MAGIC_LIMIT), (builder, item) -> {
            ItemStack itemStack = item.getDefaultInstance();

            if (!itemStack.is(Items.ENCHANTED_BOOK)) {
                int magicLevel = Mth.ceil((25.0 - Math.max(itemStack.getEnchantability(), itemStack.getEnchantabilityFromMap())) / 3.0);

                builder.set(DataComponentsSD.MAGIC_LEVEL, magicLevel);
            }
        });
    }

    private static void modifyStackSize() {
        modifyComponent(() -> Collections.singleton(Items.POTION), (builder, _) -> builder.set(DataComponents.MAX_STACK_SIZE, 16));
    }

    private static void modifyConsumable() {
        modifyComponent(() -> ItemTagsSD.getItems(ItemTagsSD.LIQUID_CONSUMABLES), (builder, item) -> {
            Consumable consumable = item.components().get(DataComponents.CONSUMABLE);

            if (consumable != null) {
                builder.set(DataComponents.CONSUMABLE, new Consumable(
                        1.0F,
                        consumable.animation(),
                        consumable.sound(),
                        consumable.hasConsumeParticles(),
                        consumable.onConsumeEffects()
                ));
            }
        });
    }

    private static void modifyRarity() {
        List<Item> UNCOMMON_ITEMS = List.of(Items.NETHERITE_AXE, Items.NETHERITE_HOE, Items.NETHERITE_PICKAXE, Items.NETHERITE_SHOVEL, Items.NETHERITE_SPEAR, Items.NETHERITE_SWORD, Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS, Items.NETHERITE_HORSE_ARMOR, Items.NETHERITE_NAUTILUS_ARMOR, Items.OMINOUS_TRIAL_KEY, Items.LINGERING_POTION, Items.TIPPED_ARROW);
        List<Item> RARE_ITEMS = List.of(Items.WITHER_ROSE);

        modifyComponent(() -> UNCOMMON_ITEMS, (builder, _) -> builder.set(DataComponents.RARITY, Rarity.UNCOMMON));
        modifyComponent(() -> RARE_ITEMS, (builder, _) -> builder.set(DataComponents.RARITY, Rarity.RARE));
    }

    private static void modifyShield() {
        modifyComponent(() -> Collections.singleton(Items.SHIELD), ((builder, item) -> {
            ItemAttributeModifiers existingModifiers = item.getDefaultInstance().getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
            ItemAttributeModifiers newModifiers = existingModifiers.withModifierAdded(AttributesSD.SHIELD_STRENGTH, new AttributeModifier(ItemSD.SHIELD_STRENGTH, 5.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.ANY);

            BlocksAttacks fallback = new BlocksAttacks(
                    0.25F,
                    1.0F,
                    List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                    new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty()
            );
            BlocksAttacks existing = builder.getOrDefault(DataComponents.BLOCKS_ATTACKS, fallback);

            builder.set(DataComponents.BLOCKS_ATTACKS, new BlocksAttacks(
                    0.0F,
                    existing.disableCooldownScale(),
                    existing.damageReductions(),
                    existing.itemDamage(),
                    existing.bypassedBy(),
                    existing.blockSound(),
                    existing.disableSound()
            ));
            builder.set(DataComponents.ATTRIBUTE_MODIFIERS, newModifiers);
        }));
    }

    public static void modifyComponents() {
        modifyRarity();
        modifyConsumable();
        modifyStackSize();
        modifyEnchantablity();
        modifyWeapons();
        modifyShield();
    }

    public static class PropertiesSD extends Properties {
        public Item.Properties dagger(final ToolMaterial material, final float attackDamageBaseline, final float attackSpeedBaseline, final float stealthDamageBonus) {
            return durability(material.durability())
                    .repairable(material.repairItems())
                    .enchantable(material.enchantmentValue())
                    .component(DataComponents.ATTACK_RANGE, new AttackRange(0.0F, 2.5F, 0.0F, 3.0F, 0.1F, 0.2F))
                    .component(DataComponents.SWING_ANIMATION, new SwingAnimation(SwingAnimationType.STAB, (int) (20.0F / (4.0F + attackSpeedBaseline))))
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, attackDamageBaseline + material.attackDamageBonus(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                            .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeedBaseline, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                            .build()
                    )
                    .component(DataComponentsSD.STEALTH_WEAPON, new StealthWeapon(stealthDamageBonus, stealthDamageBonus * 0.625F, stealthDamageBonus * 0.25F, 0.2F, 0.7F))
                    .component(DataComponents.USE_EFFECTS, new UseEffects(true, false, 1.0F))
                    .component(DataComponents.TOOL, new Tool(List.of(), 1.0F, 2, false))
                    .component(DataComponents.WEAPON, new Weapon(1));
        }
    }
}
