package net.meander.subtlyd.client.model.geom;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.meander.subtlyd.client.model.object.equipment.HeavyShieldModel;
import net.meander.subtlyd.client.model.object.tent.TentModel;
import net.minecraft.client.model.geom.ModelLayerLocation;

import java.util.Map;

/**
 * @see net.minecraft.client.model.geom.LayerDefinitions
 */
public class LayerDefinitionsSD {
    private static final Map<ModelLayerLocation, ModelLayerRegistry.TexturedLayerDefinitionProvider> DEFINITIONS = new Object2ObjectOpenHashMap<>();

    private static void register(ModelLayerLocation layer, ModelLayerRegistry.TexturedLayerDefinitionProvider provider) {
        if (DEFINITIONS.put(layer, provider) != null) {
            throw new IllegalStateException("Duplicate layer definition for " + layer);
        }
    }

    public static void registration() {
        register(ModelLayersSD.TENT, TentModel::createBodyLayer);
        register(ModelLayersSD.HEAVY_SHIELD, HeavyShieldModel::createLayer);

        DEFINITIONS.forEach(ModelLayerRegistry::registerModelLayer);
    }
}