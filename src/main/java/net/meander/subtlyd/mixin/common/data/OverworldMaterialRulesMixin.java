package net.meander.subtlyd.mixin.common.data;

import net.meander.subtlyd.world.level.biome.BiomesSD;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.material.OverworldMaterialRules;
import net.minecraft.data.worldgen.material.VanillaMaterialConditions;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.material.MaterialRules;
import net.minecraft.world.level.levelgen.material.condition.MaterialCondition;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OverworldMaterialRules.class)
public class OverworldMaterialRulesMixin {
    @Inject(method = "registerSurface", at = @At("RETURN"), cancellable = true)
    private static void registerGravelBeachSurface(BootstrapContext<MaterialRule> context, MaterialRule sulfurCaveBands, CallbackInfoReturnable<MaterialRule> cir) {
        HolderGetter<MaterialCondition> conditions = context.lookup(Registries.MATERIAL_CONDITION);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        MaterialCondition onCeiling = MaterialRules.getCondition(conditions, VanillaMaterialConditions.ON_CEILING);
        MaterialCondition onFloor = MaterialRules.getCondition(conditions, VanillaMaterialConditions.ON_FLOOR);
        MaterialCondition deepUnderFloor = MaterialRules.getCondition(conditions, VanillaMaterialConditions.DEEP_UNDER_FLOOR);
        MaterialCondition notUnderDeepWater = MaterialRules.getCondition(conditions, VanillaMaterialConditions.NOT_UNDER_DEEP_WATER);

        MaterialRule stone = MaterialRules.state(Blocks.STONE.defaultBlockState());
        MaterialRule gravel = MaterialRules.state(Blocks.GRAVEL.defaultBlockState());
        
        MaterialRule gravelOrStoneIfCeiling = MaterialRules.sequence(
            MaterialRules.ifTrue(onCeiling, stone), 
            gravel
        );

        MaterialRule gravelBeachRule = MaterialRules.ifTrue(
            MaterialRules.isBiome(biomes, BiomesSD.GRAVEL_BEACH),
            MaterialRules.sequence(
                MaterialRules.ifTrue(onFloor, gravelOrStoneIfCeiling),
                MaterialRules.ifTrue(
                    notUnderDeepWater, 
                    MaterialRules.ifTrue(deepUnderFloor, stone)
                )
            )
        );

        cir.setReturnValue(MaterialRules.sequence(gravelBeachRule, cir.getReturnValue()));
    }
}