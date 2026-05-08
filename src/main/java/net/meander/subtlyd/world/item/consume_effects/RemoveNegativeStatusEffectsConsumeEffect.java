package net.meander.subtlyd.world.item.consume_effects;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;

public record RemoveNegativeStatusEffectsConsumeEffect() implements ConsumeEffect {
    public static final RemoveNegativeStatusEffectsConsumeEffect INSTANCE = new RemoveNegativeStatusEffectsConsumeEffect();
    public static final MapCodec<RemoveNegativeStatusEffectsConsumeEffect> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, RemoveNegativeStatusEffectsConsumeEffect> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public ConsumeEffect.Type<RemoveNegativeStatusEffectsConsumeEffect> getType() {
        return ConsumeEffectSD.Type.REMOVE_NEGATIVE_EFFECTS;
    }

    @Override
    public boolean apply(final Level level, final ItemStack stack, final LivingEntity user) {
        for (Holder<MobEffect> effect : BuiltInRegistries.MOB_EFFECT.stream().map(BuiltInRegistries.MOB_EFFECT::wrapAsHolder).toList()) {
            if (effect.value().getCategory() == MobEffectCategory.HARMFUL) {
                user.removeEffect(effect);
            }
        }
        return true;
    }
}
