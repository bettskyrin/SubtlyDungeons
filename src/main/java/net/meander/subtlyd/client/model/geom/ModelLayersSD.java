package net.meander.subtlyd.client.model.geom;

import com.google.common.collect.Sets;
import net.meander.subtlyd.util.Util;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.world.level.block.ColorCollection;

import java.util.Set;

/**
 * @see ModelLayers
 */
public class ModelLayersSD extends ModelLayers {
    public static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();
    public static final ColorCollection<ModelLayerLocation> TENT = ColorCollection.prefixWithColor(ColorCollection.create("tent")).map(ModelLayersSD::register);
    public static final ModelLayerLocation HEAVY_SHIELD = register("heavy_shield");

    public static void registration() {
        registerColorCollection(TENT);
        registerModel(HEAVY_SHIELD.model().getPath());
    }

    private static void registerModel(String string) {
        ModelLayerLocation modelLayerLocation = register(string);

        if (!ALL_MODELS.add(modelLayerLocation)) {
            throw new IllegalStateException("Duplicate registration for " + modelLayerLocation);
        }
    }

    private static ModelLayerLocation register(String string) {
        return new ModelLayerLocation(Util.identifier(string), "main");
    }

    private static void registerColorCollection(ColorCollection<ModelLayerLocation> collection) {
        collection.forEach(location -> registerModel(location.model().getPath()));
    }
}
