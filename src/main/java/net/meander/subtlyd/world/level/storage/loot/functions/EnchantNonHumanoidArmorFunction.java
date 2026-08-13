package net.meander.subtlyd.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.meander.subtlyd.tags.ItemTagsSD;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

/**
 * @see EnchantRandomlyFunction
 */
public class EnchantNonHumanoidArmorFunction extends LootItemConditionalFunction {
    public static final MapCodec<EnchantNonHumanoidArmorFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> commonFields(instance).apply(instance, EnchantNonHumanoidArmorFunction::new));

    protected EnchantNonHumanoidArmorFunction(Optional<Holder<LootItemCondition>> condition) {
        super(condition);
    }

    @Override
    public MapCodec<? extends LootItemConditionalFunction> codec() {
        return MAP_CODEC;
    }

    @Override
    protected ItemStack run(ItemStack itemStack, LootContext context) {
        HolderLookup.Provider registries = context.getLevel().registryAccess();
        HolderGetter<Enchantment> enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);

        if (itemStack.is(ItemTagsSD.NON_HUMANOID_ARMOR)) {
            return EnchantRandomlyFunction.randomApplicableEnchantment(enchantments).build().apply(itemStack, context);
        }

        return itemStack;
    }

    public static LootItemConditionalFunction.Builder<?> enchantNonHumanoidArmor() {
        return simpleBuilder(EnchantNonHumanoidArmorFunction::new);
    }
}
