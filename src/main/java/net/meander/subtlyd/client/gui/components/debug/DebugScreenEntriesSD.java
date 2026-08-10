package net.meander.subtlyd.client.gui.components.debug;

import net.meander.subtlyd.util.UtilSD;
import net.minecraft.client.gui.components.debug.DebugEntryNoop;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.resources.Identifier;

/**
 * @see net.minecraft.client.gui.components.debug.DebugScreenEntries
 */
public class DebugScreenEntriesSD {
    public static final Identifier VISUALIZE_ENTITY_OCCLUSION = register("vizualize_entity_occlusion", new DebugEntryNoop());

    private static Identifier register(final String id, final DebugScreenEntry entry) {
        return DebugScreenEntries.register(UtilSD.identifier(id), entry);
    }
}
