package net.meander.subtlyd.core.component;

import net.meander.subtlyd.util.UtilSD;
import net.meander.subtlyd.world.item.component.StealthWeapon;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.DyeColor;

import java.util.function.UnaryOperator;

/**
 * @see net.minecraft.core.component.DataComponents
 */
public class DataComponentsSD {
    public static final DataComponentType<Integer> MAGIC_LEVEL = register("magic_level", (b) -> b.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DataComponentType<StealthWeapon> STEALTH_WEAPON = register("stealth_attack", (b) -> b.persistent(StealthWeapon.CODEC).networkSynchronized(StealthWeapon.STREAM_CODEC));
    public static final DataComponentType<DyeColor> TENT_COLOR = register("tent/color", (b) -> b.persistent(DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC));

    public static void registration() {
        UtilSD.LOGGER.debug("Registering data components...");}

    private static <T> DataComponentType<T> register(final String id, final UnaryOperator<DataComponentType.Builder<T>> builder) {
        return DataComponents.register(UtilSD.identifier(id).toString(), builder);
    }
}
