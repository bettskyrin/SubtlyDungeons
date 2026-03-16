package net.meander.subtlyd.util;

import net.minecraft.network.chat.MutableComponent;
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
     * A non-debug logging method.
     * @param component The message to log.
     */
    public static void log(MutableComponent component) {
        LOGGER.info(component.getString());
    }

    /**
     * @param string The path.
     * @return An identifier within the mod namespace
     */
    public static Identifier identifier(String string) {
        return Identifier.fromNamespaceAndPath(NAMESPACE, string);
    }

    public static class Server {
        public static boolean isModded = false;
    }

    public static class Globals {
        public static int BACK_BUTTON_WIDTH = 60;
    }
}