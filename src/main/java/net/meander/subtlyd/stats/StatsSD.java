package net.meander.subtlyd.stats;

import net.meander.subtlyd.util.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class StatsSD {
    public static final Identifier SLEEP_IN_TENT = makeCustomStat("sleep_in_tent", StatFormatter.DEFAULT);

    public static void registration() {}

    private static Identifier makeCustomStat(final String id, final StatFormatter formatter) {
        Identifier location = Util.identifier(id);
        Registry.register(BuiltInRegistries.CUSTOM_STAT, location, location);
        Stats.CUSTOM.get(location, formatter);
        return location;
    }
}
