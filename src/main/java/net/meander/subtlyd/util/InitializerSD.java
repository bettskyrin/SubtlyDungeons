package net.meander.subtlyd.util;

import net.fabricmc.api.ModInitializer;
import net.meander.subtlyd.advancements.triggers.CriteriaTriggersSD;
import net.meander.subtlyd.client.camera.shake.CameraShakeEvents;
import net.meander.subtlyd.core.component.DataComponentsSD;
import net.meander.subtlyd.core.particles.ParticleTypesSD;
import net.meander.subtlyd.data.loot_table.LootSD;
import net.meander.subtlyd.network.PacketNetworking;
import net.meander.subtlyd.network.syncher.SynchedEntityDataSD;
import net.meander.subtlyd.stats.StatsSD;
import net.meander.subtlyd.world.GameEventsSD;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.block.entity.BlockEntityTypesSD;
import net.meander.subtlyd.world.effect.MobEffectsSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.meander.subtlyd.world.item.alchemy.PotionsSD;
import net.meander.subtlyd.world.level.GameRulesSD;
import net.meander.subtlyd.world.level.levelgen.BiomesSD;
import net.meander.subtlyd.world.level.storage.loot.predicates.LootItemConditionsSD;

public class InitializerSD implements ModInitializer {
    @Override public void onInitialize() {
        Util.LOGGER.info("Initializing Subtly Dungeons");
        GameRulesSD.bootstrap();
        DataComponentsSD.bootstrap();
        SynchedEntityDataSD.bootstrap();
        PacketNetworking.registerCommon();

        Util.LOGGER.info("Registering items and blocks");
        ParticleTypesSD.registerServer();
        MobEffectsSD.bootstrap();
        PotionsSD.bootstrap();
        BlocksSD.bootstrap();
        BlockEntityTypesSD.bootstrap();
        ItemsSD.bootstrap();
        LootItemConditionsSD.registration();

        Util.LOGGER.info("Registering world events");
        GameEventsSD.registration();
        CameraShakeEvents.registration();
        BiomesSD.init();
        LootSD.registration();
        CriteriaTriggersSD.bootstrap();
        StatsSD.bootstrap();
    }
}