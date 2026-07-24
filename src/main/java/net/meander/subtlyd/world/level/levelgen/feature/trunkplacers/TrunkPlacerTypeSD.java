package net.meander.subtlyd.world.level.levelgen.feature.trunkplacers;

import com.mojang.serialization.MapCodec;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

/**
 * @see TrunkPlacerType
 */
public class TrunkPlacerTypeSD {
    public static final TrunkPlacerType<BaobabTrunkPlacer> BAOBAB_TRUNK_PLACER = register("baobab_trunk_placer", BaobabTrunkPlacer.CODEC);

    private static <P extends TrunkPlacer> TrunkPlacerType<P> register(final String name, final MapCodec<P> codec) {
        return TrunkPlacerType.register(UtilSD.identifier(name).toString(), codec);
    }

    public static void registration() {}
}
