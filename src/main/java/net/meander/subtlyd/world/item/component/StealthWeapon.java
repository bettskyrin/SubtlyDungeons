package net.meander.subtlyd.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * Visibility Levels: Hidden - [0-0.2] | Obscured: (0.2-0.7] | Discrete: (0.7-1]
 */
public record StealthWeapon(float hiddenDamageBonus, float obscuredDamageBonus, float discreteDamageBonus, float hiddenThreshold, float obscuredThreshold) {
    public static final Codec<StealthWeapon> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.FLOAT.fieldOf("hidden_bonus").forGetter(StealthWeapon::hiddenDamageBonus),
                    Codec.FLOAT.fieldOf("obscured_bonus").forGetter(StealthWeapon::obscuredDamageBonus),
                    Codec.FLOAT.fieldOf("discrete_bonus").forGetter(StealthWeapon::discreteDamageBonus),
                    Codec.FLOAT.fieldOf("hidden_threshold").forGetter(StealthWeapon::hiddenThreshold),
                    Codec.FLOAT.fieldOf("obscured_threshold").forGetter(StealthWeapon::obscuredThreshold))
            .apply(instance, StealthWeapon::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, StealthWeapon> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, StealthWeapon::hiddenDamageBonus,
            ByteBufCodecs.FLOAT, StealthWeapon::obscuredDamageBonus,
            ByteBufCodecs.FLOAT, StealthWeapon::discreteDamageBonus,
            ByteBufCodecs.FLOAT, StealthWeapon::hiddenThreshold,
            ByteBufCodecs.FLOAT, StealthWeapon::obscuredThreshold,
            StealthWeapon::new);
}