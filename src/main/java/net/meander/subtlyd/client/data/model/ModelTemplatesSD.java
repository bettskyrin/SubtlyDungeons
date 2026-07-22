package net.meander.subtlyd.client.data.model;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;

import java.util.Optional;

/**
 * @see net.minecraft.client.data.models.model.ModelTemplates
 */
public class ModelTemplatesSD {
    public static final ModelTemplate OVERHANG_BLOCK = new ModelTemplate(Optional.of(UtilSD.identifier("block/overhang_block")), Optional.empty(), TextureSlot.NORTH, TextureSlot.EAST, TextureSlot.SOUTH, TextureSlot.WEST, TextureSlot.PARTICLE);
}
