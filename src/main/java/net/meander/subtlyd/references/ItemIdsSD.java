package net.meander.subtlyd.references;

import net.meander.subtlyd.util.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.ColorCollection;

public class ItemIdsSD {
    public static final ResourceKey<Item> APPLE_PIE = create("apple_pie");
    public static final ResourceKey<Item> CALAMARI = create("calamari");
    public static final ResourceKey<Item> COOKED_CALAMARI = create("cooked_calamari");
    public static final ResourceKey<Item> POTTAGE = create("pottage");
    public static final ColorCollection<ResourceKey<Item>> TENT = createSimpleColored("tent");
    public static final ResourceKey<Item> BLAST_FUNGUS = create("blast_fungus");
    public static final ResourceKey<Item> COVEN_ELIXIR = create("coven_elixir");
    public static final ResourceKey<Item> LIGHT_STEW = create("light_stew");
    public static final ResourceKey<Item> DAGGER = create("dagger");

    public static ResourceKey<Item> create(final String name) {
        return ResourceKey.create(Registries.ITEM, Util.identifier(name));
    }

    public static ColorCollection<ResourceKey<Item>> createSimpleColored(final String baseName) {
        return ColorCollection.prefixWithColor(ColorCollection.create(baseName)).map(ItemIdsSD::create);
    }
}
