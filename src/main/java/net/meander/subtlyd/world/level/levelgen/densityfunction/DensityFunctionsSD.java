package net.meander.subtlyd.world.level.levelgen.densityfunction;

import com.mojang.serialization.MapCodec;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.densityfunction.DensityFunction;

/**
 * @see net.minecraft.world.level.levelgen.densityfunction.DensityFunctions
 */
public class DensityFunctionsSD {
    private static void register(String id, MapCodec<? extends DensityFunction> codec) {
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, UtilSD.identifier(id), codec);
    }

    public static void registration() {
        register("overworld/ocean_depth", OceanDepthFunction.CODEC);
    }
}
