package net.meander.subtlyd.world.block;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
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
    }

    private static void useBlock() {
        UseBlockCallback.EVENT.register(new UnlitCampfireFunction());
        CauldronInteractionsSD.bootstrap();
    }

    private static void breakBlock() {
        PlayerBlockBreakEvents.AFTER.register(new CropExperienceFunction());
        PlayerBlockBreakEvents.AFTER.register(new SnowloggedBlockRemainderFunction());
        AttackBlockCallback.EVENT.register(new SnowloggedBlockAttackFunction());
    }
}
