package net.meander.subtlyd.world.level;


import net.meander.subtlyd.util.UtilSD;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRules;

/**
 * @see GameRules
 */
public class GameRulesSD {
    public static final GameRule<Boolean> ARROW_ARSON = registerBoolean("arrow_arson", GameRuleCategory.UPDATES, true);
    public static final GameRule<Boolean> SMART_MOBS = registerBoolean("smart_mobs", GameRuleCategory.MOBS, true);

    public static void registration() {}

    private static GameRule<Boolean> registerBoolean(String id, GameRuleCategory category, boolean defaultValue) {
        return GameRules.registerBoolean(UtilSD.identifier(id).toString(), category, defaultValue);
    }
}
