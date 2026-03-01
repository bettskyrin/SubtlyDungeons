package net.meander.subtlyd.util.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.meander.subtlyd.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagsSD extends FabricTagsProvider.EntityTypeTagsProvider {
    public EntityTypeTagsSD(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    public static final TagKey<EntityType<?>> CAN_BE_SCARED = create("can_be_scared");

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        valueLookupBuilder(CAN_BE_SCARED)
                .add(EntityType.COW)
                .add(EntityType.MOOSHROOM)
                .add(EntityType.PIG)
                .add(EntityType.SHEEP)
                .add(EntityType.CHICKEN)
                .add(EntityType.HORSE)
                .add(EntityType.DONKEY)
                .add(EntityType.MULE)
                .add(EntityType.LLAMA)
                .add(EntityType.RABBIT)
                .add(EntityType.FOX)
                .add(EntityType.CAT)
                .add(EntityType.FROG)
                .add(EntityType.GOAT)
                .add(EntityType.CAMEL)
                .add(EntityType.SNIFFER)
                .add(EntityType.STRIDER);
    }

    private static TagKey<EntityType<?>> create(String string) {
        return TagKey.create(Registries.ENTITY_TYPE, Util.identifier(string));
    }
}
