package net.meander.subtlyd.mixin.common.world.level.biome;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Pair;
import net.meander.subtlyd.world.level.biome.BiomesSD;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(OverworldBiomeBuilder.class)
public class OverworldBiomeBuilderMixin {
    @Shadow @Final private Climate.Parameter[] temperatures;

    @Inject(method = "pickBeachBiome", at = @At("RETURN"), cancellable = true)
    private void pickGravelBeach(int temperatureIndex, int humidityIndex, CallbackInfoReturnable<ResourceKey<Biome>> cir) {
        if (cir.getReturnValue() == Biomes.BEACH) {
            if (temperatureIndex == 1) {
                cir.setReturnValue(BiomesSD.GRAVEL_BEACH);
            }
        }
    }

    @WrapOperation(
            method = "addValleys",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/biome/OverworldBiomeBuilder;addSurfaceBiome(Ljava/util/function/Consumer;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;FLnet/minecraft/resources/ResourceKey;)V"
            )
    )
    private void splitWarmRivers(
            OverworldBiomeBuilder instance,
            Consumer<Pair<Climate.ParameterPoint,
            ResourceKey<Biome>>> biomes,
            Climate.Parameter temperature,
            Climate.Parameter humidity,
            Climate.Parameter continentalness,
            Climate.Parameter erosion,
            Climate.Parameter weirdness,
            float offset, ResourceKey<Biome> second,
            Operation<Void> original
    ) {
        if (second == Biomes.RIVER) {
            Climate.Parameter temperateRiver = Climate.Parameter.span(temperatures[1], temperatures[3]);
            Climate.Parameter warmRiver = temperatures[4];

            original.call(instance, biomes, temperateRiver, humidity, continentalness, erosion, weirdness, offset, Biomes.RIVER);
            original.call(instance, biomes, warmRiver, humidity, continentalness, erosion, weirdness, offset, BiomesSD.WARM_RIVER);
        } else {
            original.call(instance, biomes, temperature, humidity, continentalness, erosion, weirdness, offset, second);
        }
    }
}