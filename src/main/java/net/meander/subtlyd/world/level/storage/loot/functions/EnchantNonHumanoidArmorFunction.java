package net.meander.subtlyd.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.meander.subtlyd.tags.ItemTagsSD;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class EnchantNonHumanoidArmorFunction extends LootItemConditionalFunction {
    public static final MapCodec<EnchantNonHumanoidArmorFunction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            commonFields(instance).apply(instance, EnchantNonHumanoidArmorFunction::new)
    );

    protected EnchantNonHumanoidArmorFunction(List<LootItemCondition> predicates) {
        super(predicates);
    }

    @Override
    public MapCodec<? extends LootItemConditionalFunction> codec() {
        return CODEC;
    }

    /**
     * Function for randomly enchanting non-humanoid armors.
     */
    @Override
    protected ItemStack run(ItemStack itemStack, LootContext context) {
        HolderLookup.Provider registries = context.getLevel().registryAccess();

        if (itemStack.is(ItemTagsSD.NON_HUMANOID_ARMOR)) {
            return EnchantRandomlyFunction
                    .randomApplicableEnchantment(registries)
                    .build()
                    .apply(itemStack, context);
        }
        return itemStack;
    }

    public static LootItemConditionalFunction.Builder<?> builder() {
        return simpleBuilder(EnchantNonHumanoidArmorFunction::new);
    }
}
