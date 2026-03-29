package net.meander.subtlyd.world.item.enchantment;

import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.data.tags.DamageTypeTagsSD;
import net.minecraft.advancements.criterion.DamageSourcePredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.TagPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;

public class EnchantmentsSD {
    public static final ResourceKey<Enchantment> OCCULT_PROTECTION = ResourceKey.create(Registries.ENCHANTMENT, Util.identifier("occult_protection"));
    public static final ResourceKey<Enchantment> ABRADING_CURSE = ResourceKey.create(Registries.ENCHANTMENT, Util.identifier("abrading_curse"));

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);

        context.register(OCCULT_PROTECTION, Enchantment.enchantment(
            Enchantment.definition(items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                    5,
                    4,
                    Enchantment.dynamicCost(5, 8),
                    Enchantment.dynamicCost(18, 8),
                    4,
                    EquipmentSlotGroup.ARMOR))
                .exclusiveWith(enchantments.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE))
                .withEffect(EnchantmentEffectComponents.DAMAGE_PROTECTION, new AddValue(LevelBasedValue.perLevel(2.0F)),
                        DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTagsSD.IS_OCCULT))
                                .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))))
            .build(OCCULT_PROTECTION.identifier())
        );
        context.register(ABRADING_CURSE, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                        1,
                        1,
                        Enchantment.constantCost(25),
                        Enchantment.constantCost(50),
                        8,
                        EquipmentSlotGroup.ANY))
                .withEffect(EnchantmentEffectComponents.ITEM_DAMAGE, new AddValue(LevelBasedValue.constant(1.0F)),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().of(items, ItemTags.ARMOR_ENCHANTABLE)))
                .withEffect(EnchantmentEffectComponents.ITEM_DAMAGE, new AddValue(LevelBasedValue.constant(1.0F)),
                        InvertedLootItemCondition.invert(MatchTool.toolMatches(ItemPredicate.Builder.item().of(items, ItemTags.ARMOR_ENCHANTABLE))))
            .build(ABRADING_CURSE.identifier())
        );
    }
}
