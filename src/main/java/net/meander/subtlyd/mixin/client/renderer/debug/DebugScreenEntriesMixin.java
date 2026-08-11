package net.meander.subtlyd.mixin.client.renderer.debug;

import net.meander.subtlyd.client.gui.components.debug.DebugScreenEntriesSD;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.gui.components.debug.DebugScreenEntryStatus;
import net.minecraft.client.gui.components.debug.DebugScreenProfile;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(DebugScreenEntries.class)
public abstract class DebugScreenEntriesMixin {
    @Shadow @Final @Mutable public static Map<DebugScreenProfile, Map<Identifier, DebugScreenEntryStatus>> PROFILES;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyProfiles(CallbackInfo ci) {
        Map<DebugScreenProfile, Map<Identifier, DebugScreenEntryStatus>> mutableProfiles = new HashMap<>(PROFILES);
        Map<Identifier, DebugScreenEntryStatus> performanceEntries = new HashMap<>(mutableProfiles.getOrDefault(DebugScreenProfile.PERFORMANCE, Map.of()));

        performanceEntries.put(DebugScreenEntriesSD.ENTITY_CULLING_METHOD, DebugScreenEntryStatus.IN_OVERLAY);
        mutableProfiles.put(DebugScreenProfile.PERFORMANCE, Map.copyOf(performanceEntries));

        PROFILES = Map.copyOf(mutableProfiles);
    }
}