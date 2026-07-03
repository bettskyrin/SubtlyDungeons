package net.meander.subtlyd.world.level;


import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import net.meander.subtlyd.util.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.*;

import java.util.function.ToIntFunction;

/**
 * @see GameRules
 */
public class GameRulesSD {
    public static final GameRule<Boolean> ARROW_ARSON = registerBoolean("arrow_arson", GameRuleCategory.UPDATES, true);

    public static void registration() {}

    private static GameRule<Boolean> registerBoolean(String id, GameRuleCategory category, boolean defaultValue) {
        return register(id, category, GameRuleType.BOOL, BoolArgumentType.bool(), Codec.BOOL, defaultValue, FeatureFlagSet.of(), GameRuleTypeVisitor::visitBoolean, b -> b ? 1 : 0);
    }

    private static <T> GameRule<T> register(final String id, final GameRuleCategory category, final GameRuleType typeHint, final ArgumentType<T> argumentType, final Codec<T> codec, final T defaultValue, final FeatureFlagSet requiredFeatures, final GameRules.VisitorCaller<T> visitorCaller, final ToIntFunction<T> commandResultFunction) {
        return Registry.register(BuiltInRegistries.GAME_RULE,
                Util.identifier(id),
                new GameRule<>(category, typeHint, argumentType, visitorCaller, codec, commandResultFunction, defaultValue, requiredFeatures));
    }
}
