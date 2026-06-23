package net.meander.subtlyd.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class DaggerLootFunction extends LootItemConditionalFunction {
    public static final MapCodec<DaggerLootFunction> CODEC = RecordCodecBuilder.mapCodec(instance ->
            commonFields(instance).apply(instance, DaggerLootFunction::new)
    );

    protected DaggerLootFunction(List<LootItemCondition> predicates) {
        super(predicates);
    }

    @Override
    public MapCodec<? extends LootItemConditionalFunction> codec() {
        return CODEC;
    }

    @Override
    protected ItemStack run(final ItemStack itemStack, LootContext context) {
        ItemStack newStack = itemStack;

        if (context.getRandom().nextFloat() <= 0.2F) {
            if (itemStack.is(ItemTags.SWORDS)) {
                if (itemStack.is(Items.WOODEN_SWORD)) {
                    newStack = ItemsSD.WOODEN_DAGGER.getDefaultInstance();
                } else if (itemStack.is(Items.STONE_SWORD)) {
                    newStack = ItemsSD.STONE_DAGGER.getDefaultInstance();
                } else if (itemStack.is(Items.COPPER_SWORD)) {
                    newStack = ItemsSD.COPPER_DAGGER.getDefaultInstance();
                } else if (itemStack.is(Items.IRON_SWORD)) {
                    newStack = ItemsSD.IRON_DAGGER.getDefaultInstance();
                } else if (itemStack.is(Items.GOLDEN_SWORD)) {
                    newStack = ItemsSD.GOLDEN_DAGGER.getDefaultInstance();
                } else if (itemStack.is(Items.DIAMOND_SWORD)) {
                    newStack = ItemsSD.DIAMOND_DAGGER.getDefaultInstance();
                } else if (itemStack.is(Items.NETHERITE_SWORD)) {
                    newStack = ItemsSD.NETHERITE_DAGGER.getDefaultInstance();
                }
                newStack.applyComponents(itemStack.getComponentsPatch());
            }
        }
        return newStack;
    }

    public static LootItemConditionalFunction.Builder<?> builder() {
        return simpleBuilder(DaggerLootFunction::new);
    }
}
