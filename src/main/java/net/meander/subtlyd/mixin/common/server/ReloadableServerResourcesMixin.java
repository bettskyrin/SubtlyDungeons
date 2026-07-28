package net.meander.subtlyd.mixin.common.server;

import net.meander.subtlyd.data.loot.packs.ChestLootSD;
import net.meander.subtlyd.data.loot.packs.EntityLootSD;
import net.meander.subtlyd.data.loot.packs.FishingLootSD;
import net.meander.subtlyd.data.loot.packs.GiftLootSD;
import net.minecraft.commands.Commands;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.component.DataComponentInitializers;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ReloadableServerResources.class)
public class ReloadableServerResourcesMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onResourceConstruct(ReloadableServerRegistries.LoadResult loadingContext, FeatureFlagSet enabledFeatures, Commands.CommandSelection commandSelection, List<Registry.PendingTags<?>> postponedTags, PermissionSet functionCompilationPermissions, List<DataComponentInitializers.PendingComponents<?>> newComponents, CallbackInfo ci  /* other params */) {
        LayeredRegistryAccess<?> layers = loadingContext.layers();
        WritableRegistry<LootTable> lootRegistry = (WritableRegistry<LootTable>) layers.compositeAccess().lookupOrThrow(Registries.LOOT_TABLE);
        HolderLookup.Provider lookupProvider = loadingContext.lookupWithUpdatedTags();

        ChestLootSD.register(lootRegistry);
        GiftLootSD.register(lootRegistry);
        FishingLootSD.register(lootRegistry, lookupProvider);
        EntityLootSD.register(lootRegistry, lookupProvider);
    }
}