package net.meander.subtlyd.world.level;


import net.meander.subtlyd.util.Util;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.ToIntFunction;

public class GameRulesSD {
    public static final GameRule<Boolean> ARROW_ARSON = registerBoolean("arrow_arson", GameRuleCategory.UPDATES, true);

    public static void bootstrap() {}

    private static GameRule<Boolean> registerBoolean(String string, GameRuleCategory gameRuleCategory, boolean bl) {
        return register(
                string,
                gameRuleCategory,
                GameRuleType.BOOL,
                BoolArgumentType.bool(),
                Codec.BOOL,
                bl,
                FeatureFlagSet.of(),
                GameRuleTypeVisitor::visitBoolean,
                boolean_ -> boolean_ ? 1 : 0
        );
    }

    private static <T> GameRule<T> register(
            String string,
            GameRuleCategory gameRuleCategory,
            GameRuleType gameRuleType,
            ArgumentType<T> argumentType,
            Codec<T> codec,
            T object,
            FeatureFlagSet featureFlagSet,
            GameRules.VisitorCaller<@NotNull T> visitorCaller,
            ToIntFunction<T> toIntFunction
    ) {
        return Registry.register(
                BuiltInRegistries.GAME_RULE,
                Util.identifier(string),
                new GameRule<>(gameRuleCategory, gameRuleType, argumentType, visitorCaller, codec, toIntFunction, object, featureFlagSet)
        );
    }
}
