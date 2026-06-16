package net.meander.subtlyd.client.model.geom;

import com.google.common.collect.Sets;
import net.meander.subtlyd.util.Util;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.world.level.block.ColorCollection;

import java.util.Set;

public class ModelLayersSD extends ModelLayers {
    public static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();
    public static final ColorCollection<ModelLayerLocation> TENT = ColorCollection.prefixWithColor(ColorCollection.create("tent")).map(ModelLayersSD::createLocation);

    public static void registration() {
        registerColorCollection(TENT);
    }

    private static void register(String string) {
        ModelLayerLocation modelLayerLocation = createLocation(string);

        if (!ALL_MODELS.add(modelLayerLocation)) {
            throw new IllegalStateException("Duplicate bootstrap for " + modelLayerLocation);
        }
    }

    private static ModelLayerLocation createLocation(String string) {
        return new ModelLayerLocation(Util.identifier(string), "main");
    }

    private static void registerColorCollection(ColorCollection<ModelLayerLocation> collection) {
        collection.forEach(location -> register(location.model().getPath()));
    }
}
