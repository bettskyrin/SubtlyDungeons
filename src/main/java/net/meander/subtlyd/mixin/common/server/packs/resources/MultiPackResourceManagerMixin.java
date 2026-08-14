package net.meander.subtlyd.mixin.common.server.packs.resources;

import net.meander.subtlyd.server.packs.VirtualResourceRegistry;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(MultiPackResourceManager.class)
public class MultiPackResourceManagerMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true, name = "packs")
    private static List<PackResources> addVirtualPack(List<PackResources> packs) {
        List<PackResources> augmentedPacks = new ArrayList<>(packs);

        augmentedPacks.add(VirtualResourceRegistry.getVirtualPack());
        return augmentedPacks;
    }
}