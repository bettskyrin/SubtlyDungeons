package net.meander.subtlyd.client.model.geom;

import com.google.common.collect.Sets;
import net.meander.subtlyd.util.Util;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.world.level.block.ColorCollection;

import java.util.Set;

public class ModelLayersSD extends ModelLayers {
    public static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();
    public static final ColorCollection<ModelLayerLocation> TENT = ColorCollection.make(color -> register("tent/" + color));

    private static ModelLayerLocation register(String string) {
        ModelLayerLocation modelLayerLocation = createLocation(string);
        if (!ALL_MODELS.add(modelLayerLocation)) {
            throw new IllegalStateException("Duplicate registration for " + modelLayerLocation);
        } else {
            return modelLayerLocation;
        }
    }

    private static ModelLayerLocation createLocation(String string) {
        return new ModelLayerLocation(Util.identifier(string), "main");
    }
}
