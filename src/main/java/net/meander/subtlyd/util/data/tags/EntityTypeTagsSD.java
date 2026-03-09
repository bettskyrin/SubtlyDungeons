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
    public static final TagKey<EntityType<?>> SEEKS_SHELTER = create("seeks_shelter");
    public static final TagKey<EntityType<?>> CAN_SEEK_WARMTH = create("can_seek_warmth");
    public static final TagKey<EntityType<?>> CAN_SEEK_SHADE = create("can_seek_shade");

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
        valueLookupBuilder(SEEKS_SHELTER)
                .add(EntityType.COW)
                .add(EntityType.MOOSHROOM)
                .add(EntityType.PIG)
                .add(EntityType.SHEEP)
                .add(EntityType.CHICKEN)
                .add(EntityType.HORSE)
                .add(EntityType.DONKEY)
                .add(EntityType.MULE)
                .add(EntityType.LLAMA)
                .add(EntityType.TRADER_LLAMA)
                .add(EntityType.RABBIT)
                .add(EntityType.FOX)
                .add(EntityType.WOLF)
                .add(EntityType.CAT)
                .add(EntityType.FROG)
                .add(EntityType.GOAT)
                .add(EntityType.CAMEL)
                .add(EntityType.PANDA)
                .add(EntityType.ARMADILLO)
                .add(EntityType.POLAR_BEAR)
                .add(EntityType.SNIFFER)
                .add(EntityType.STRIDER);
        valueLookupBuilder(CAN_SEEK_WARMTH)
                .add(EntityType.COW)
                .add(EntityType.MOOSHROOM)
                .add(EntityType.PIG)
                .add(EntityType.CHICKEN)
                .add(EntityType.HORSE)
                .add(EntityType.DONKEY)
                .add(EntityType.MULE)
                .add(EntityType.LLAMA)
                .add(EntityType.TRADER_LLAMA)
                .add(EntityType.RABBIT)
                .add(EntityType.FOX)
                .add(EntityType.WOLF)
                .add(EntityType.CAT)
                .add(EntityType.FROG)
                .add(EntityType.CAMEL)
                .add(EntityType.PANDA)
                .add(EntityType.ARMADILLO)
                .add(EntityType.TURTLE)
                .add(EntityType.SNIFFER)
                .add(EntityType.STRIDER);
        valueLookupBuilder(CAN_SEEK_SHADE)
                .add(EntityType.COW)
                .add(EntityType.PIG)
                .add(EntityType.SHEEP)
                .add(EntityType.CHICKEN)
                .add(EntityType.RABBIT)
                .add(EntityType.FOX)
                .add(EntityType.WOLF)
                .add(EntityType.CAT)
                .add(EntityType.FROG)
                .add(EntityType.GOAT)
                .add(EntityType.POLAR_BEAR)
                .add(EntityType.AXOLOTL)
                .add(EntityType.SNIFFER);
    }

    private static TagKey<EntityType<?>> create(String string) {
        return TagKey.create(Registries.ENTITY_TYPE, Util.identifier(string));
    }
}
