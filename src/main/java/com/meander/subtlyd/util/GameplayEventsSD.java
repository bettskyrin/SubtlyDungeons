package com.meander.subtlyd.util;

import com.meander.subtlyd.commands.CommandsSD;
import com.meander.subtlyd.util.data.tags.ItemTagsSD;
import com.meander.subtlyd.world.entity.TentEntity;
import com.meander.subtlyd.world.item.ItemsSD;
import com.meander.subtlyd.world.level.block.UnlitCampfireFunction;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.registry.FuelValueEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;

public class GameplayEventsSD {
    public static void registration() {
        UseBlockCallback.EVENT.register(new UnlitCampfireFunction());
        allowTentSleep();
        registerFuelValues();
        registerCommands();
    }

    /**
     * Enables tent sleeping.
     */
    private static void allowTentSleep() {
        EntitySleepEvents.ALLOW_BED.register((livingEntity, _, _, _) -> {
            TentEntity tent = TentEntity.getTent(livingEntity, false);

            if (tent != null) {
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
        EntitySleepEvents.ALLOW_RESETTING_TIME.register(LivingEntity::isSleeping);
    }

    /**
     * Registers new fuel materials.
     */
    private static void registerFuelValues() {
        FuelValueEvents.BUILD.register(((builder, _) -> {
            builder.add(ItemsSD.CHARCOAL_BLOCK, 200 * 8 * 10);
            builder.add(ItemTagsSD.TENTS, 200 * 3);
        }));
    }

    /**
     * Registers custom commands.
     */
    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, _, _) -> CommandsSD.register(dispatcher)));
    }
}
