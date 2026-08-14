package net.meander.subtlyd.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.meander.subtlyd.world.item.ItemsSD;
import net.minecraft.core.Holder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

/**
 * Replaces weapons with their dagger equivalents.
 */
public class SetDaggerFunction extends LootItemConditionalFunction {
    public static final MapCodec<SetDaggerFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> commonFields(instance).apply(instance, SetDaggerFunction::new));

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public SetDaggerFunction(Optional<Holder<LootItemCondition>> condition) {
        super(condition);
    }

    @Override
    public MapCodec<SetDaggerFunction> codec() {
        return MAP_CODEC;
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
            } else if (itemStack.is(ItemTags.SPEARS)) {
                if (itemStack.is(Items.WOODEN_SPEAR)) {
                    newStack = ItemsSD.WOODEN_DAGGER.getDefaultInstance();
                } else if (itemStack.is(Items.STONE_SPEAR)) {
                    newStack = ItemsSD.STONE_DAGGER.getDefaultInstance();
                } else if (itemStack.is(Items.COPPER_SPEAR)) {
                    newStack = ItemsSD.COPPER_DAGGER.getDefaultInstance();
                } else if (itemStack.is(Items.IRON_SPEAR)) {
                    newStack = ItemsSD.IRON_DAGGER.getDefaultInstance();
                } else if (itemStack.is(Items.GOLDEN_SPEAR)) {
                    newStack = ItemsSD.GOLDEN_DAGGER.getDefaultInstance();
                } else if (itemStack.is(Items.DIAMOND_SPEAR)) {
                    newStack = ItemsSD.DIAMOND_DAGGER.getDefaultInstance();
                } else if (itemStack.is(Items.NETHERITE_SPEAR)) {
                    newStack = ItemsSD.NETHERITE_DAGGER.getDefaultInstance();
                }
                newStack.applyComponents(itemStack.getComponentsPatch());
            }
        }
        return newStack;
    }

    public static LootItemConditionalFunction.Builder<?> setDagger() {
        return simpleBuilder(SetDaggerFunction::new);
    }
}
