package net.meander.subtlyd.world.item.alchemy;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.alchemy.Potion;

public class PotionIdsSD {
    public static final ResourceKey<Potion> DECAY = register("decay");

    private static ResourceKey<Potion> register(final String name) {
        return ResourceKey.create(Registries.POTION, UtilSD.identifier(name));
    }
}
