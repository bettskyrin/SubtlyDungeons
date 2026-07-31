package net.meander.subtlyd.world.level;


import net.meander.subtlyd.util.UtilSD;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRules;

/**
 * @see GameRules
 */
public class GameRulesSD {
    public static final GameRule<Boolean> ARROW_ARSON = registerBoolean("arrow_arson", GameRuleCategory.UPDATES, true);
    public static final GameRule<Boolean> ADVANCED_MOBS = registerBoolean("advanced_mobs", GameRuleCategory.MOBS, true);
    public static final GameRule<Integer> BLADE_CLASH_WINDOW = registerInteger("blade_clash_window", GameRuleCategory.PLAYER, 10, 0);

    public static void registration() {}

    private static GameRule<Boolean> registerBoolean(final String id, final GameRuleCategory category, final boolean defaultValue) {
        return GameRules.registerBoolean(UtilSD.identifier(id).toString(), category, defaultValue);
    }

    private static GameRule<Integer> registerInteger(final String id, final GameRuleCategory category, final int defaultValue, final int min) {
        return registerInteger(id, category, defaultValue, min, Integer.MAX_VALUE);
    }

    private static GameRule<Integer> registerInteger(final String id, final GameRuleCategory category, final int defaultValue, final int min, final int max) {
        return GameRules.registerInteger(UtilSD.identifier(id).toString(), category, defaultValue, min, max, FeatureFlagSet.of());
    }
}
