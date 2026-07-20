package net.meander.subtlyd.core;

import net.fabricmc.api.ModInitializer;
import net.meander.subtlyd.advancements.triggers.CriteriaTriggersSD;
import net.meander.subtlyd.commands.CommandsSD;
import net.meander.subtlyd.core.component.DataComponentsSD;
import net.meander.subtlyd.data.worldgen.WorldGeneratorSD;
import net.meander.subtlyd.network.PacketNetworking;
import net.meander.subtlyd.network.syncher.SynchedEntityDataSD;
import net.meander.subtlyd.stats.StatsSD;
import net.meander.subtlyd.util.Util;
import net.meander.subtlyd.world.block.BlocksSD;
import net.meander.subtlyd.world.block.entity.BlockEntityTypesSD;
import net.meander.subtlyd.world.effect.MobEffectsSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.meander.subtlyd.world.level.GameRulesSD;
import net.meander.subtlyd.world.level.LevelSD;
import net.meander.subtlyd.world.level.storage.loot.functions.LootItemFunctionsSD;
import net.meander.subtlyd.world.level.storage.loot.predicates.LootItemConditionsSD;
import net.minecraft.core.registries.BuiltInRegistries;

@SuppressWarnings("unused")
public class InitializerSD implements ModInitializer {
    @Override
    public void onInitialize() {
        Util.LOGGER.info("Initializing Subtly Dungeons");
        server();
        gameplay();
        level();
    }

    private void server() {
        SynchedEntityDataSD.registration();
        PacketNetworking.registerCommon();
    }

    private void gameplay() {
        CriteriaTriggersSD.registration();
        StatsSD.registration();
        LootItemConditionsSD.registration();
        LootItemFunctionsSD.bootstrap(BuiltInRegistries.LOOT_FUNCTION_TYPE);
        CommandsSD.registration();
        GameRulesSD.registration();
        LevelSD.registerEvents();
    }

    private void level() {
        WorldGeneratorSD.BiomeModifier.run();
        DataComponentsSD.registration();
        MobEffectsSD.init();
        BlocksSD.registration();
        BlockEntityTypesSD.registration();
        ItemsSD.registration();
    }
}