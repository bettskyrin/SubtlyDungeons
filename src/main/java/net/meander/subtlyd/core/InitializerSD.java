package net.meander.subtlyd.core;

import net.fabricmc.api.ModInitializer;
import net.meander.subtlyd.advancements.triggers.CriteriaTriggersSD;
import net.meander.subtlyd.commands.CommandsSD;
import net.meander.subtlyd.core.component.DataComponentsSD;
import net.meander.subtlyd.core.particles.ParticleTypesSD;
import net.meander.subtlyd.data.BlockFamiliesSD;
import net.meander.subtlyd.data.worldgen.WorldGeneratorSD;
import net.meander.subtlyd.network.PacketNetworking;
import net.meander.subtlyd.network.syncher.EntityDataAccessors;
import net.meander.subtlyd.stats.StatsSD;
import net.meander.subtlyd.util.UtilSD;
import net.meander.subtlyd.world.effect.MobEffectsSD;
import net.meander.subtlyd.world.item.ItemsSD;
import net.meander.subtlyd.world.level.GameRulesSD;
import net.meander.subtlyd.world.level.LevelSD;
import net.meander.subtlyd.world.level.block.BlocksSD;
import net.meander.subtlyd.world.level.block.entity.BlockEntityTypesSD;

@SuppressWarnings("unused")
public class InitializerSD implements ModInitializer {
    private void server() {
        PacketNetworking.registerPayloadTypes();
        ParticleTypesSD.registerServer();
        EntityDataAccessors.definitions();
    }

    private void gameplay() {
        CriteriaTriggersSD.registration();
        StatsSD.registration();
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
        BlockFamiliesSD.init();
    }

    @Override
    public void onInitialize() {
        UtilSD.LOGGER.info("Initializing Subtly Dungeons");
        server();
        gameplay();
        level();
    }
}