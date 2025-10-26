package com.kr1s1s.subtlyd.world.level;

import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;

import java.util.Comparator;
import java.util.Map;

public class GameRulesSD {
    public static final GameRules.Key<GameRules.BooleanValue> RULE_DOARROWARSON = GameRuleRegistry.register("doArrowArson", GameRules.Category.UPDATES, GameRuleFactory.createBooleanRule(true));
    public static void init() {}
}
