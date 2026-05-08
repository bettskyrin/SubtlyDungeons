package net.meander.subtlyd.world.item.consume_effects;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

public class ConsumeEffectSD {
    public record Type<T extends ConsumeEffect>(MapCodec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        public static final ConsumeEffect.Type<RemoveNegativeStatusEffectsConsumeEffect> REMOVE_NEGATIVE_EFFECTS = register(
                "remove_effects", RemoveNegativeStatusEffectsConsumeEffect.CODEC, RemoveNegativeStatusEffectsConsumeEffect.STREAM_CODEC
        );

        private static <T extends ConsumeEffect> ConsumeEffect.Type<T> register(
                final String name, final MapCodec<T> codec, final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec
        ) {
            return Registry.register(BuiltInRegistries.CONSUME_EFFECT_TYPE, name, new ConsumeEffect.Type<>(codec, streamCodec));
        }
    }
}
