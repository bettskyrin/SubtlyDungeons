package com.kr1s1s.subtlyd.util;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {
    public static final Logger LOGGER = LoggerFactory.getLogger("Subtly Dungeons");
    public static final String NAMESPACE = "subtlyd";

    /**
     * A quick log output method.
     */
    @SuppressWarnings("unused")
    public static void debug(Object logValue) {
        LOGGER.info("Debug: {}", logValue);
    }

    /**
     * @param string The path.
     * @return An identifier within the mod namespace
     */
    public static Identifier identifier(String string) {
        return Identifier.fromNamespaceAndPath(NAMESPACE, string);
    }
}
