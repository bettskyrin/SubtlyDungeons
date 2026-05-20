package net.meander.subtlyd.server;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;

import java.util.ArrayList;
import java.util.List;

public class ServerLevelSD {
    /**
     * Gets the average downfall level of the players' biomes, and uses it to determine the amount that rainTime should be changed by each tick.
     * @return The downfall modifier
     */
    public static int getDownfallModifier(ServerLevel level) {
        float averageDownfall = 0;
        List<ServerPlayer> playersWithWeather = new ArrayList<>();

        for (ServerPlayer player : level.players()) {
            if (player.level().canHaveWeather()) {
                playersWithWeather.add(player);
                averageDownfall += level.getBiome(player.blockPosition()).value().climateSettings.downfall;
            }
        }

        averageDownfall /= Math.max(1, playersWithWeather.size());
        float difference = averageDownfall - 0.4F;

        if (difference > 0) {
            return Mth.ceil(difference * (float) UniformInt.of(200, 500).sample(level.getRandom()));
        } else if (difference < 0) {
            return Mth.ceil(difference * (float) UniformInt.of(100, 300).sample(level.getRandom()));
        }
        return 0;
    }
}
