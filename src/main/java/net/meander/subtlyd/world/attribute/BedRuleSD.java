package net.meander.subtlyd.world.attribute;

import net.meander.subtlyd.world.entity.player.PlayerSD;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.entity.player.Player;

/**
 * @see BedRule
 */
public class BedRuleSD {
    public static PlayerSD.TentSleepingProblem asProblem(BedRule rule) {
        return new Player.TentSleepingProblem(rule.errorMessage().orElse(null));
    }
}
