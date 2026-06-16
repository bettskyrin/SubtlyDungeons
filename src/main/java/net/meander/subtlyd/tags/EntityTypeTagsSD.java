package net.meander.subtlyd.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.meander.subtlyd.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypeIds;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagsSD extends FabricTagsProvider.EntityTypeTagsProvider {
    public EntityTypeTagsSD(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    public static final TagKey<EntityType<?>> CAN_BE_SCARED = bind("can_be_scared");
    public static final TagKey<EntityType<?>> SEEKS_SHELTER = bind("seeks_shelter");
    public static final TagKey<EntityType<?>> CAN_SEEK_WARMTH = bind("can_seek_warmth");
    public static final TagKey<EntityType<?>> CAN_SEEK_SHADE = bind("can_seek_shade");
    public static final TagKey<EntityType<?>> CAN_BE_FULL = bind("can_be_full");
    public static final TagKey<EntityType<?>> NOCTURNAL = bind("nocturnal");
    public static final TagKey<EntityType<?>> FEAST_OR_FAMINE_HUNTER = bind("feast_or_famine_hunter");

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(CAN_BE_SCARED)
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
        tag(SEEKS_SHELTER)
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
        tag(CAN_SEEK_WARMTH)
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
        tag(CAN_SEEK_SHADE)
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
        tag(CAN_BE_FULL)
                .add(EntityTypeIds.WOLF)
                .add(EntityTypeIds.CAT)
                .add(EntityTypeIds.OCELOT)
                .add(EntityTypeIds.FOX)
                .add(EntityTypeIds.AXOLOTL)
                .add(EntityTypeIds.DOLPHIN)
                .add(EntityTypeIds.POLAR_BEAR);
        tag(NOCTURNAL)
                .add(EntityTypeIds.WOLF)
                .add(EntityTypeIds.OCELOT)
                .add(EntityTypeIds.FOX)
                .add(EntityTypeIds.BAT);
        tag(FEAST_OR_FAMINE_HUNTER)
                .add(EntityTypeIds.WOLF)
                .add(EntityTypeIds.POLAR_BEAR);
    }

    private static TagKey<EntityType<?>> bind(String string) {
        return TagKey.create(Registries.ENTITY_TYPE, Util.identifier(string));
    }
}
