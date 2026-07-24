package net.meander.subtlyd.world.item.enchantment;

import net.meander.subtlyd.tags.DamageTypeTagsSD;
import net.meander.subtlyd.tags.EnchantmentTagsSD;
import net.meander.subtlyd.tags.ItemTagsSD;
import net.meander.subtlyd.util.UtilSD;
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
import net.minecraft.world.damagesource.DamageType;
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

/**
 * @see net.minecraft.world.item.enchantment.Enchantments
 */
public class EnchantmentsSD {
    public static final ResourceKey<Enchantment> OCCULT_PROTECTION = ResourceKey.create(Registries.ENCHANTMENT, UtilSD.identifier("occult_protection"));
    public static final ResourceKey<Enchantment> ABRADING_CURSE = ResourceKey.create(Registries.ENCHANTMENT, UtilSD.identifier("abrading_curse"));
    public static final ResourceKey<Enchantment> GLYPH_AFFINITY = ResourceKey.create(Registries.ENCHANTMENT, UtilSD.identifier("glyph_affinity"));
    public static final ResourceKey<Enchantment> ILLAGERS_BANE = ResourceKey.create(Registries.ENCHANTMENT, UtilSD.identifier("illagers_bane"));
    public static final ResourceKey<Enchantment> ENERVATION = ResourceKey.create(Registries.ENCHANTMENT, UtilSD.identifier("enervation"));
    public static final ResourceKey<Enchantment> CLEAVING = ResourceKey.create(Registries.ENCHANTMENT, UtilSD.identifier("cleaving"));

    public static void bootstrap(@NonNull BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);
        HolderGetter<Enchantment> enchantments = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<EntityType<?>> entityTypes = context.lookup(Registries.ENTITY_TYPE);
        HolderGetter<DamageType> damageTypes = context.lookup(Registries.DAMAGE_TYPE);

        ResourceKey<Enchantment> currentEnchantment = OCCULT_PROTECTION;

        try {
            context.register(currentEnchantment, Enchantment.enchantment(
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
                            DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(damageTypes, DamageTypeTagsSD.IS_OCCULT))
                                    .tag(TagPredicate.isNot(damageTypes, DamageTypeTags.BYPASSES_INVULNERABILITY))))
                .build(currentEnchantment.identifier())
            );

            currentEnchantment = ABRADING_CURSE;

            context.register(currentEnchantment, Enchantment.enchantment(
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
                .build(currentEnchantment.identifier())
            );

            currentEnchantment = GLYPH_AFFINITY;

            context.register(currentEnchantment, Enchantment.enchantment(
                            Enchantment.definition(items.getOrThrow(ItemTagsSD.HAS_MAGIC_LIMIT),
                                    1,
                                    1,
                                    Enchantment.constantCost(25),
                                    Enchantment.constantCost(65),
                                    8,
                                    EquipmentSlotGroup.ANY))
                    .build(currentEnchantment.identifier())
            );

            currentEnchantment = ILLAGERS_BANE;

            context.register(currentEnchantment, Enchantment.enchantment(
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
                    .build(currentEnchantment.identifier())
            );

            currentEnchantment = ENERVATION;

            context.register(currentEnchantment, Enchantment.enchantment(
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
                    .build(currentEnchantment.identifier())
            );

            currentEnchantment = CLEAVING;

            context.register(currentEnchantment, Enchantment.enchantment(
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
                    .build(currentEnchantment.identifier())
            );
        } catch (Exception e) {
            UtilSD.LOGGER.error("Failed to register {}: {}", currentEnchantment.identifier(), e.getMessage());
        }
    }
}
