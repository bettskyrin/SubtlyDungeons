package net.meander.subtlyd.util;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {
    public static final Logger LOGGER = LoggerFactory.getLogger("Subtly Dungeons");
    public static final String NAMESPACE = "subtlyd";

    /**
     * @param string The path.
     * @return An identifier within the mod namespace
     */
    public static Identifier identifier(String string) {
        return Identifier.fromNamespaceAndPath(NAMESPACE, string);
    }

    public static class GUI_COMMON {
        public static final int BACK_BUTTON_WIDTH = 60;
    }
}