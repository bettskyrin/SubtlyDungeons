package net.meander.subtlyd.core.component;

import net.meander.subtlyd.util.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;

public class DataComponentsSD {
    public static final DataComponentType<Integer> MAGIC_LEVEL = register("magic_level", DataComponentType.<Integer>builder().persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static void bootstrap() {}

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> DataComponentType<T> register(final String id, final DataComponentType.Builder builder) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Util.identifier(id), builder.build());
    }
}
