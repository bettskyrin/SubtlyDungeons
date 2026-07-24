package net.meander.subtlyd.client.color.block;

import net.fabricmc.fabric.api.client.rendering.v1.BlockTintsFactory;
import net.meander.subtlyd.world.level.block.entity.PotionCauldronBlockEntity;
import net.minecraft.world.item.alchemy.PotionContents;

/**
 * @see net.minecraft.client.color.block.BlockTintSources
 */
public class BlockTintSourcesSD {
    public static BlockTintsFactory potionCauldron() {
        return (_, view, pos, tintValues) -> {
            int cauldronColor = 0x385DC6; // Water

            if (view.getBlockEntity(pos) instanceof PotionCauldronBlockEntity cauldronBlock) {
                if (cauldronBlock.getPotion() != null) {
                    cauldronColor = PotionContents.getColorOptional(cauldronBlock.getPotion().value().getEffects()).orElse(cauldronColor);
                }
            }
            tintValues.add(cauldronColor);
        };
    }
}
