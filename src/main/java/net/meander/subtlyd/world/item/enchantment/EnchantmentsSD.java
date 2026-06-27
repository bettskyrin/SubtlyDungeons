package net.meander.subtlyd.world.item.enchantment;

import net.meander.subtlyd.tags.DamageTypeTagsSD;
import net.meander.subtlyd.tags.EnchantmentTagsSD;
import net.meander.subtlyd.tags.ItemTagsSD;
import net.meander.subtlyd.util.Util;
import net.minecraft.advancements.predicates.DamageSourcePredicate;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.TagPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.EntityTypePredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.ApplyMobEffect;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import org.jspecify.annotations.NonNull;

public class EnchantmentsSD {
    public static final ResourceKey<Enchantment> OCCULT_PROTECTION = ResourceKey.create(Registries.ENCHANTMENT, Util.identifier("occult_protection"));
    public static final ResourceKey<Enchantment> ABRADING_CURSE = ResourceKey.create(Registries.ENCHANTMENT, Util.identifier("abrading_curse"));
    public static final ResourceKey<Enchantment> GLYPH_AFFINITY = ResourceKey.create(Registries.ENCHANTMENT, Util.identifier("glyph_affinity"));
    public static final ResourceKey<Enchantment> ILLAGERS_BANE = ResourceKey.create(Registries.ENCHANTMENT, Util.identifier("illagers_bane"));
    public static final ResourceKey<Enchantment> ENERVATION = ResourceKey.create(Registries.ENCHANTMENT, Util.identifier("enervation"));
    public static final ResourceKey<Enchantment> CLEAVING = ResourceKey.create(Registries.ENCHANTMENT, Util.identifier("cleaving"));

    /**
     * Registeres new enchantments.
     */
    @SuppressWarnings("LoggingSimilarMessage")
    public static void bootstrap(@NonNull BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<EntityType<?>> entityTypes = context.lookup(Registries.ENTITY_TYPE);

        try {
            context.register(OCCULT_PROTECTION, Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                        5,
                        4,
                        Enchantment.dynamicCost(5, 8),
                        Enchantment.dynamicCost(18, 8),
                        4,
                        EquipmentSlotGroup.ARMOR))
                    .exclusiveWith(enchantments.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE))
                    .withEffect(
                            EnchantmentEffectComponents.DAMAGE_PROTECTION, new AddValue(LevelBasedValue.perLevel(2.0F)),
                            DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTagsSD.IS_OCCULT))
                                    .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))))
                .build(OCCULT_PROTECTION.identifier())
            );
        } catch (Exception e) {
            Util.LOGGER.error("Failed to get tag for: {}: {}", OCCULT_PROTECTION.identifier(), e.getMessage());
        }

        try {
            context.register(ABRADING_CURSE, Enchantment.enchantment(
                    Enchantment.definition(
                            items.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                            1,
                            1,
                            Enchantment.constantCost(25),
                            Enchantment.constantCost(50),
                            8,
                            EquipmentSlotGroup.ANY))
                    .exclusiveWith(enchantments.getOrThrow(EnchantmentTagsSD.REPAIRS_EQUIPMENT))
                    .withEffect(EnchantmentEffectComponents.ITEM_DAMAGE, new AddValue(LevelBasedValue.constant(2.0F)),
                            MatchTool.toolMatches(ItemPredicate.Builder.item().of(items, ItemTags.ARMOR_ENCHANTABLE)))
                    .withEffect(EnchantmentEffectComponents.ITEM_DAMAGE, new AddValue(LevelBasedValue.constant(1.0F)),
                            InvertedLootItemCondition.invert(MatchTool.toolMatches(ItemPredicate.Builder.item().of(items, ItemTags.ARMOR_ENCHANTABLE))))
                .build(ABRADING_CURSE.identifier())
            );
        } catch (Exception e) {
            Util.LOGGER.error("Failed to get tag for: {}: {}", ABRADING_CURSE.identifier(), e.getMessage());
        }

        try {
            context.register(GLYPH_AFFINITY, Enchantment.enchantment(
                            Enchantment.definition(items.getOrThrow(ItemTagsSD.HAS_MAGIC_LIMIT),
                                    1,
                                    1,
                                    Enchantment.constantCost(25),
                                    Enchantment.constantCost(65),
                                    8,
                                    EquipmentSlotGroup.ANY))
                    .build(GLYPH_AFFINITY.identifier())
            );
        } catch (Exception e) {
            Util.LOGGER.error("Failed to get tag for: {}: {}", GLYPH_AFFINITY.identifier(), e.getMessage());
        }

        try {
            context.register(ILLAGERS_BANE, Enchantment.enchantment(
                    Enchantment.definition(
                                    items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                                    items.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE),
                                    5,
                                    5,
                                    Enchantment.dynamicCost(5, 8),
                                    Enchantment.dynamicCost(25, 8),
                                    2,
                                    EquipmentSlotGroup.MAINHAND))
                    .exclusiveWith(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
                    .withEffect(
                            EnchantmentEffectComponents.DAMAGE,
                            new AddValue(LevelBasedValue.perLevel(2.5F)),
                            LootItemEntityPropertyCondition.hasProperties(
                                            LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityTypes, EntityTypeTags.ILLAGER))))
                    .build(ILLAGERS_BANE.identifier())
            );
        } catch (Exception e) {
            Util.LOGGER.error("Failed to get tag for: {}: {}", ILLAGERS_BANE.identifier(), e.getMessage());
        }

        try {
            context.register(ENERVATION, Enchantment.enchantment(
                            Enchantment.definition(
                                    items.getOrThrow(ItemTagsSD.DAGGERS),
                                    items.getOrThrow(ItemTagsSD.DAGGERS),
                                    2,
                                    3,
                                    Enchantment.dynamicCost(5, 8),
                                    Enchantment.dynamicCost(25, 8),
                                    4,
                                    EquipmentSlotGroup.MAINHAND))
                    .withEffect(
                            EnchantmentEffectComponents.POST_ATTACK,
                                    EnchantmentTarget.ATTACKER,
                                    EnchantmentTarget.VICTIM,
                                    new ApplyMobEffect(
                                            HolderSet.direct(MobEffects.WEAKNESS),
                                            LevelBasedValue.perLevel(2.0F, 1.0F),
                                            LevelBasedValue.perLevel(2.0F, 2.0F),
                                            LevelBasedValue.constant(0.0F),
                                            LevelBasedValue.constant(1.0F)
                                    ))
                    .build(ENERVATION.identifier())
            );
        } catch (Exception e) {
            Util.LOGGER.error("Failed to get tag for: {}: {}", ENERVATION.identifier(), e.getMessage());
        }

        try {
            context.register(CLEAVING, Enchantment.enchantment(
                            Enchantment.definition(
                                    items.getOrThrow(ItemTags.AXES),
                                    items.getOrThrow(ItemTags.AXES),
                                    2,
                                    3,
                                    Enchantment.dynamicCost(15, 9),
                                    Enchantment.dynamicCost(65, 9),
                                    4,
                                    EquipmentSlotGroup.MAINHAND
                            ))
                    .exclusiveWith(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
                    .withEffect(
                            EnchantmentEffectComponents.DAMAGE,
                            new AddValue(LevelBasedValue.perLevel(1.0F))
                    )
                    .build(CLEAVING.identifier())
            );
        } catch (Exception e) {
            Util.LOGGER.error("Failed to register: {}: {}", CLEAVING.identifier(), e.getMessage());
        }
    }
}
