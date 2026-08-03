package net.meander.subtlyd.client.renderer.entity;

import net.meander.subtlyd.client.model.geom.ModelLayersSD;
import net.meander.subtlyd.util.UtilSD;
import net.meander.subtlyd.world.entity.EntityTypesSD;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

/**
 * @see EntityRenderers
 */
public class EntityRenderersSD {
    public static void registration() {
        UtilSD.LOGGER.debug("Registering entity renderers...");
        EntityRenderers.register(EntityTypesSD.TENT, context -> new TentRenderer(context, ModelLayersSD.TENT));
        EntityRenderers.register(EntityTypesSD.BLAST_FUNGUS, ThrownItemRenderer::new);
    }
}
