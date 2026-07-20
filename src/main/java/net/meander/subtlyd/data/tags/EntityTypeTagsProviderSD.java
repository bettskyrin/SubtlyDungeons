package net.meander.subtlyd.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.meander.subtlyd.tags.EntityTypeTagsSD;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.EntityTypeIds;

import java.util.concurrent.CompletableFuture;

/**
 * @see net.minecraft.data.tags.EntityTypeTagsProvider
 */
public class EntityTypeTagsProviderSD extends FabricTagsProvider.EntityTypeTagsProvider {
    public EntityTypeTagsProviderSD(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(EntityTypeTagsSD.CAN_BE_SCARED)
                .add(EntityTypeIds.COW)
                .add(EntityTypeIds.MOOSHROOM)
                .add(EntityTypeIds.PIG)
                .add(EntityTypeIds.SHEEP)
                .add(EntityTypeIds.CHICKEN)
                .add(EntityTypeIds.HORSE)
                .add(EntityTypeIds.DONKEY)
                .add(EntityTypeIds.MULE)
                .add(EntityTypeIds.LLAMA)
                .add(EntityTypeIds.RABBIT)
                .add(EntityTypeIds.FOX)
                .add(EntityTypeIds.CAT)
                .add(EntityTypeIds.FROG)
                .add(EntityTypeIds.GOAT)
                .add(EntityTypeIds.CAMEL)
                .add(EntityTypeIds.SNIFFER)
                .add(EntityTypeIds.STRIDER);
        tag(EntityTypeTagsSD.SEEKS_SHELTER)
                .add(EntityTypeIds.COW)
                .add(EntityTypeIds.MOOSHROOM)
                .add(EntityTypeIds.PIG)
                .add(EntityTypeIds.SHEEP)
                .add(EntityTypeIds.CHICKEN)
                .add(EntityTypeIds.HORSE)
                .add(EntityTypeIds.DONKEY)
                .add(EntityTypeIds.MULE)
                .add(EntityTypeIds.LLAMA)
                .add(EntityTypeIds.TRADER_LLAMA)
                .add(EntityTypeIds.RABBIT)
                .add(EntityTypeIds.FOX)
                .add(EntityTypeIds.WOLF)
                .add(EntityTypeIds.CAT)
                .add(EntityTypeIds.FROG)
                .add(EntityTypeIds.GOAT)
                .add(EntityTypeIds.CAMEL)
                .add(EntityTypeIds.PANDA)
                .add(EntityTypeIds.ARMADILLO)
                .add(EntityTypeIds.POLAR_BEAR)
                .add(EntityTypeIds.SNIFFER)
                .add(EntityTypeIds.STRIDER);
        tag(EntityTypeTagsSD.CAN_SEEK_WARMTH)
                .add(EntityTypeIds.COW)
                .add(EntityTypeIds.MOOSHROOM)
                .add(EntityTypeIds.PIG)
                .add(EntityTypeIds.CHICKEN)
                .add(EntityTypeIds.HORSE)
                .add(EntityTypeIds.DONKEY)
                .add(EntityTypeIds.MULE)
                .add(EntityTypeIds.RABBIT)
                .add(EntityTypeIds.FOX)
                .add(EntityTypeIds.WOLF)
                .add(EntityTypeIds.CAT)
                .add(EntityTypeIds.FROG)
                .add(EntityTypeIds.ARMADILLO)
                .add(EntityTypeIds.TURTLE)
                .add(EntityTypeIds.SNIFFER)
                .add(EntityTypeIds.STRIDER);
        tag(EntityTypeTagsSD.CAN_SEEK_SHADE)
                .add(EntityTypeIds.COW)
                .add(EntityTypeIds.PIG)
                .add(EntityTypeIds.SHEEP)
                .add(EntityTypeIds.CHICKEN)
                .add(EntityTypeIds.RABBIT)
                .add(EntityTypeIds.FOX)
                .add(EntityTypeIds.WOLF)
                .add(EntityTypeIds.CAT)
                .add(EntityTypeIds.FROG)
                .add(EntityTypeIds.PANDA)
                .add(EntityTypeIds.GOAT)
                .add(EntityTypeIds.POLAR_BEAR)
                .add(EntityTypeIds.AXOLOTL)
                .add(EntityTypeIds.SNIFFER);
        tag(EntityTypeTagsSD.CAN_BE_FULL)
                .add(EntityTypeIds.WOLF)
                .add(EntityTypeIds.CAT)
                .add(EntityTypeIds.OCELOT)
                .add(EntityTypeIds.FOX)
                .add(EntityTypeIds.AXOLOTL)
                .add(EntityTypeIds.DOLPHIN)
                .add(EntityTypeIds.POLAR_BEAR);
        tag(EntityTypeTagsSD.NOCTURNAL)
                .add(EntityTypeIds.WOLF)
                .add(EntityTypeIds.OCELOT)
                .add(EntityTypeIds.FOX)
                .add(EntityTypeIds.BAT);
        tag(EntityTypeTagsSD.FEAST_OR_FAMINE_HUNTER)
                .add(EntityTypeIds.WOLF)
                .add(EntityTypeIds.POLAR_BEAR);
        tag(EntityTypeTagsSD.SCANSORIAL)
                .add(EntityTypeIds.SPIDER)
                .add(EntityTypeIds.CAVE_SPIDER)
                .add(EntityTypeIds.SILVERFISH)
                .add(EntityTypeIds.ENDERMITE);
    }
}
