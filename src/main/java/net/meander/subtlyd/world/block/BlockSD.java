package net.meander.subtlyd.world.block;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.BlockEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.meander.subtlyd.core.cauldron.CauldronInteractionsSD;
import net.meander.subtlyd.world.level.block.function.*;
import net.minecraft.world.level.block.Block;

/**
 * @see Block
 */
public class BlockSD {
    public static void registerEvents() {
        useBlock();
        breakBlock();
        useItemOn();
    }

    private static void useBlock() {
        UseBlockCallback.EVENT.register(new UnlitCampfireFunction());
        UseBlockCallback.EVENT.register(new StewCauldronFunction());
        UseBlockCallback.EVENT.register(new PotionCauldronFunction());
    }

    private static void breakBlock() {
        PlayerBlockBreakEvents.AFTER.register(new CropExperienceFunction());
        PlayerBlockBreakEvents.AFTER.register(new SnowloggedBlockLayerFunction());
        AttackBlockCallback.EVENT.register(new SnowloggedBlockAttackFunction());
    }

    private static void useItemOn() {
        BlockEvents.USE_ITEM_ON.register(new SnowlogBlockFunction());
        CauldronInteractionsSD.bootstrap();
    }
}
