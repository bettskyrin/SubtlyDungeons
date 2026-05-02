package net.meander.subtlyd.client.renderer;

import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.meander.subtlyd.client.model.TentModel;
import net.meander.subtlyd.client.model.geom.ModelLayersSD;
import net.meander.subtlyd.world.entity.EntityTypeSD;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.level.block.ColorCollection;

public class EntityRenderersSD extends EntityRenderers {
    public static void registration() {
        ColorCollection.zipApply(EntityTypeSD.TENT, ModelLayersSD.TENT,
                (type,  tent) -> EntityRenderers.register(type, (context) -> new TentRenderer(context, tent)));
        register(EntityTypeSD.BLAST_FUNGUS, ThrownItemRenderer::new);

        for (ModelLayerLocation modelLayerLocation : ModelLayersSD.ALL_MODELS) {
            ModelLayerRegistry.registerModelLayer(modelLayerLocation, TentModel::createBodyLayer);
        }
    }
}
