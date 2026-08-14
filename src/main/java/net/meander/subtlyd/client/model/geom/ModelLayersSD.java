package net.meander.subtlyd.client.model.geom;

import com.google.common.collect.Sets;
import net.meander.subtlyd.util.UtilSD;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;

import java.util.Set;

/**
 * @see ModelLayers
 */
public class ModelLayersSD {
    private static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();

    public static final ModelLayerLocation TENT = register("tent");
    public static final ModelLayerLocation HEAVY_SHIELD = register("heavy_shield");
    public static final ModelLayerLocation QUIVER = register("quiver");

    private static ModelLayerLocation register(String path) {
        return register(path, "main");
    }

    private static ModelLayerLocation register(String path, String layer) {
        ModelLayerLocation location = new ModelLayerLocation(UtilSD.identifier(path), layer);
        if (!ALL_MODELS.add(location)) {
            throw new IllegalStateException("Duplicate model layer registration for " + location);
        }

        return location;
    }
}
