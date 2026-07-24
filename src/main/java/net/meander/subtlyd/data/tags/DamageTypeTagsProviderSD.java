package net.meander.subtlyd.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.meander.subtlyd.tags.DamageTypeTagsSD;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

import java.util.concurrent.CompletableFuture;

/**
 * @see net.minecraft.data.tags.DamageTypeTagsProvider
 */
public class DamageTypeTagsProviderSD extends FabricTagsProvider<DamageType> {
    public DamageTypeTagsProviderSD(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(packOutput, Registries.DAMAGE_TYPE, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(DamageTypeTagsSD.IS_OCCULT)
                .add(DamageTypes.MAGIC)
                .add(DamageTypes.INDIRECT_MAGIC)
                .add(DamageTypes.SONIC_BOOM)
                .add(DamageTypes.THORNS)
                .add(DamageTypes.WITHER);
        tag(DamageTypeTagsSD.CAUSES_FLOCK_PANIC)
                .forceAddTag(DamageTypeTags.PANIC_CAUSES)
                .removeTag(DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES)
                .add(DamageTypes.LIGHTNING_BOLT);
    }
}
