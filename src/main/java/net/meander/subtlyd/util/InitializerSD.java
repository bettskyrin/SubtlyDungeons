package net.meander.subtlyd.util;

import net.fabricmc.api.ModInitializer;
import net.meander.subtlyd.advancements.triggers.CriteriaTriggersSD;
import net.meander.subtlyd.client.camera.shake.CameraShakeEvents;
import net.meander.subtlyd.core.component.DataComponentsSD;
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
import net.meander.subtlyd.world.level.levelgen.WorldGeneratorSD;
import net.meander.subtlyd.world.level.levelgen.feature.FeatureTypesSD;
import net.meander.subtlyd.world.level.storage.loot.LootTablesSD;
import net.meander.subtlyd.world.level.storage.loot.predicates.LootItemConditionsSD;
import net.minecraft.core.registries.BuiltInRegistries;

public class InitializerSD implements ModInitializer {
    @Override public void onInitialize() {
        Util.LOGGER.info("Initializing Subtly Dungeons");
        GameRulesSD.registration();
        DataComponentsSD.registration();
        SynchedEntityDataSD.registration();
        PacketNetworking.registerCommon();

        // Blocks & Items
        MobEffectsSD.init();
        PotionsSD.registration();
        BlocksSD.registration();
        BlockEntityTypesSD.registration();
        ItemsSD.registration();
        LootItemConditionsSD.registration();

        // World Events
        GameEventsSD.registration();
        CameraShakeEvents.registration();
        FeatureTypesSD.bootstrap(BuiltInRegistries.FEATURE_TYPE);
        WorldGeneratorSD.modifyBiomes();
        LootTablesSD.registration();
        CriteriaTriggersSD.registration();
        StatsSD.registration();
    }
}