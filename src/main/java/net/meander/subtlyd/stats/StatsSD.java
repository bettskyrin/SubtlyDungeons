package net.meander.subtlyd.stats;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

/**
 * @see Stats
 */
public class StatsSD {
    public static final Identifier SLEEP_IN_TENT = makeCustomStat("sleep_in_tent", StatFormatter.DEFAULT);
    public static final Identifier DAMAGE_BLOCKED_BY_WEAPON = makeCustomStat("damaged_blocked_by_weapon", StatFormatter.DIVIDE_BY_TEN);

    public static void registration() {
        UtilSD.LOGGER.debug("Registering statistics...");}

    private static Identifier makeCustomStat(final String id, final StatFormatter formatter) {
        Identifier location = UtilSD.identifier(id);

        Registry.register(BuiltInRegistries.CUSTOM_STAT, location, location);
        Stats.CUSTOM.get(location, formatter);
        return location;
    }
}
