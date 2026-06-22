package net.meander.subtlyd.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record StealthAttack(float bonusDamage, float angleThreshold) {
    public static final Codec<StealthAttack> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("bonus_damage").forGetter(StealthAttack::bonusDamage),
            Codec.FLOAT.fieldOf("angle_threshold").forGetter(StealthAttack::angleThreshold))
            .apply(instance, StealthAttack::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, StealthAttack> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, StealthAttack::bonusDamage, ByteBufCodecs.FLOAT, StealthAttack::angleThreshold, StealthAttack::new);
}