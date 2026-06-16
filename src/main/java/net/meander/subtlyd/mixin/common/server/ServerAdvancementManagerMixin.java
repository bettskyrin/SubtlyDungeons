package net.meander.subtlyd.mixin.common.server;

import net.meander.subtlyd.advancements.AdvancementsInjectorSD;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.resources.Identifier;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ServerAdvancementManager.class)
public class ServerAdvancementManagerMixin {
    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At("HEAD"))
    private void injectAdvancementData(Map<Identifier, Advancement> preparations, ResourceManager manager, ProfilerFiller profiler, CallbackInfo ci) {
        for (Identifier advancement : AdvancementsInjectorSD.ADVANCEMENTS) {
            if (preparations.containsKey(advancement)) {
                Advancement vanillaAdvancement = preparations.get(advancement);
                Map<String, Criterion<?>> newCriteria = new HashMap<>(vanillaAdvancement.criteria());
                List<List<String>> newRequirements = new ArrayList<>(vanillaAdvancement.requirements().requirements());

                chooseAdvancement(advancement, newCriteria, newRequirements);

                Advancement modifiedAdvancement = new Advancement(
                        vanillaAdvancement.parent(),
                        vanillaAdvancement.display(),
                        vanillaAdvancement.rewards(),
                        newCriteria,
                        new AdvancementRequirements(newRequirements),
                        vanillaAdvancement.sendsTelemetryEvent()
                );

                preparations.put(advancement, modifiedAdvancement);
            }
        }
    }

    private void chooseAdvancement(Identifier advancement, Map<String, Criterion<?>> criteria, List<List<String>> requirements) {
        if (advancement.equals(AdvancementsInjectorSD.BALANCED_DIET)) {
            AdvancementsInjectorSD.balancedDiet(criteria, requirements);
        }
    }
}
