package net.meander.subtlyd.client;

import net.meander.subtlyd.util.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

/**
 * @see KeyMapping
 */
public class KeyMappingSD {
    /**
     * @see KeyMapping.Category
     */
    public record Category(Identifier id) {
        public static final KeyMapping.Category COMMAND_MACROS = register("command_macros");

        private static KeyMapping.Category register(String name) {
            return KeyMapping.Category.register(Util.identifier(name));
        }
    }
}
