package com.kr1s1s.subtlyd.data;

import com.kr1s1s.subtlyd.data.tags.ItemTagsSD;
import com.kr1s1s.subtlyd.world.entity.TentEntity;
import com.kr1s1s.subtlyd.world.item.ItemsSD;
import com.kr1s1s.subtlyd.world.level.block.UnlitCampfireFunction;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;

public class GameplayEventsSD {
    public static void registration() {
        UseBlockCallback.EVENT.register(new UnlitCampfireFunction());
        allowTentSleep();
        registerFuelValues();
    }

    private static void allowTentSleep() {
        EntitySleepEvents.ALLOW_BED.register((entity, _, _, _) -> {
            if (TentEntity.inTentRange(entity)) {
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });

        EntitySleepEvents.ALLOW_RESETTING_TIME.register(LivingEntity::isSleeping);
    }

    private static void registerFuelValues() {
        FuelRegistryEvents.BUILD.register(((builder, _) -> {
            builder.add(ItemsSD.CHARCOAL_BLOCK, 200 * 8 * 10);
            builder.add(ItemTagsSD.TENTS, 200 * 3);
        }));
    }
}
