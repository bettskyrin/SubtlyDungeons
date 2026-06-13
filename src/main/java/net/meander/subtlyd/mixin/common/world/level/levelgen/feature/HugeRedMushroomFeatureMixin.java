package net.meander.subtlyd.mixin.common.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.HugeRedMushroomFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(HugeRedMushroomFeature.class)
public class HugeRedMushroomFeatureMixin {
    @ModifyConstant(method = "makeCap", constant = @Constant(intValue = 3))
    private int modifyCapShape(int originalDepth) {
        return originalDepth - 1;
    }
}
