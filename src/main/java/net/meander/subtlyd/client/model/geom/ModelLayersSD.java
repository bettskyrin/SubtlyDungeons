package net.meander.subtlyd.client.model.geom;

import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.meander.subtlyd.client.model.HeavyShieldModel;
import net.meander.subtlyd.client.model.TentModel;
import net.meander.subtlyd.util.Util;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.world.level.block.ColorCollection;

import java.util.Map;
import java.util.Set;

/**
 * @see ModelLayers
 */
public class ModelLayersSD extends ModelLayers {
    public static final Set<ModelLayerLocation> ALL_MODELS = Sets.newHashSet();
    public static final Map<ModelLayerLocation, ModelLayerRegistry.TexturedLayerDefinitionProvider> LOCATION_PROVIDER_MAP = new Object2ObjectOpenHashMap<>();

    public static final ColorCollection<ModelLayerLocation> TENT = ColorCollection.prefixWithColor(ColorCollection.create("tent")).map(ModelLayersSD::register);
    public static final ModelLayerLocation HEAVY_SHIELD = register("heavy_shield");

    public static void registration() {
        registerColorCollection(TENT, TentModel::createBodyLayer);
        registerModel(HEAVY_SHIELD.model().getPath(), HeavyShieldModel::createLayer);
    }

    private static void registerModel(String id, ModelLayerRegistry.TexturedLayerDefinitionProvider provider) {
        ModelLayerLocation modelLayerLocation = register(id);

        if (!ALL_MODELS.add(modelLayerLocation)) {
            throw new IllegalStateException("Duplicate bootstrap for " + modelLayerLocation);
        } else {
            LOCATION_PROVIDER_MAP.put(modelLayerLocation, provider);
        }
    }

    private static ModelLayerLocation register(String id) {
        return new ModelLayerLocation(Util.identifier(id), "main");
    }

    private static void registerColorCollection(ColorCollection<ModelLayerLocation> collection, ModelLayerRegistry.TexturedLayerDefinitionProvider provider) {
        collection.forEach(location -> registerModel(location.model().getPath(), provider));
    }
}
