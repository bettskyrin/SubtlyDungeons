package net.meander.subtlyd.client.gui.components.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

public class DebugEntryEntityCullingMethod implements DebugScreenEntry {
	@Override
	public void display(final DebugScreenDisplayer displayer, final @Nullable Level serverOrClientLevel, final @Nullable LevelChunk clientChunk, final @Nullable LevelChunk serverChunk) {
		Minecraft minecraft = Minecraft.getInstance();
		String entityCullingMethod = "Culling: " + minecraft.options.entityCulling().get().caption().getString();

		displayer.addLine(entityCullingMethod);
	}

	@Override
	public boolean isAllowed(final boolean reducedDebugInfo) {
		return true;
	}
}
