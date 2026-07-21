package net.meander.subtlyd.client.gui.screens;

import net.meander.subtlyd.client.OptionsSD;
import net.meander.subtlyd.commands.CommandMacroManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.List;

public class CommandMacrosScreen extends Screen {
    private final Screen lastScreen;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 33, 33);
    private MacroList list;

    public CommandMacrosScreen(Screen lastScreen) {
        super(Component.translatable("options.command_macros.title"));
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        layout.addTitleHeader(title, font);

        list = layout.addToContents(new MacroList(minecraft));

        LinearLayout footerButtons = layout.addToFooter(LinearLayout.horizontal().spacing(8));
        footerButtons.addChild(Button.builder(CommonComponents.GUI_CANCEL, _ -> onCancel()).width(150).build());
        footerButtons.addChild(Button.builder(CommonComponents.GUI_DONE, _ -> onDone()).width(150).build());

        layout.visitWidgets(this::addRenderableWidget);
        repositionElements();
    }

    @Override
    protected void repositionElements() {
        if (list != null) {
            list.updateSize(width, layout);
        }
        layout.arrangeElements();
    }

    private void onDone() {
        for (int i = 0; i < 10; i++) {
            CommandMacroManager.macros.set(i, list.children().get(i).getValue());
        }
        CommandMacroManager.save();
        minecraft.setScreenAndShow(lastScreen);
    }

    private void onCancel() {
        minecraft.setScreenAndShow(lastScreen);
    }

    @Override
    public boolean keyPressed(final KeyEvent event) {
        if (event.isEscape()) {
            onCancel();
            return true;
        }
        return super.keyPressed(event);
    }

    private class MacroList extends ContainerObjectSelectionList<MacroEntry> {
        public MacroList(Minecraft minecraft) {
            super(
                    minecraft,
                    CommandMacrosScreen.this.width,
                    CommandMacrosScreen.this.height - layout.getHeaderHeight() - layout.getFooterHeight(),
                    layout.getHeaderHeight(),
                    45
            );

            Component altKey = Component.translatable("key.keyboard.left.alt");

            for (int i = 0; i < 10; i++) {
                addEntry(new MacroEntry(i, altKey));
            }
        }

        @Override
        public int getRowWidth() {
            return 400;
        }

        @Override
        protected int scrollBarX() {
            return (CommandMacrosScreen.this.width / 2) + 210;
        }
    }

    private class MacroEntry extends ContainerObjectSelectionList.Entry<MacroEntry> {
        private final EditBox editBox;
        private final Component label;
        private final Component shortcutLabel;

        public MacroEntry(int index, Component altKey) {
            int displayNum = (index == 9) ? 0 : (index + 1);
            label = Component.translatable("options.command_macros.entry", displayNum);
            shortcutLabel = Component.empty().append(altKey).append(" + ").append(OptionsSD.MACRO_KEYS[index].getTranslatedKeyMessage());
            editBox = new EditBox(font, 0, 0, 240, 20, Component.empty());

            editBox.setMaxLength(1024);
            editBox.setValue(CommandMacroManager.macros.get(index));
        }

        @Override
        public void extractContent(final GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
            int y = getContentY();
            int centerX = CommandMacrosScreen.this.width / 2;
            int labelX = centerX - 190;
            int boxX = centerX - 80;
            int boxY = y + 12;
            int labelY = boxY + (20 - font.lineHeight) / 2;

            graphics.text(font, label, labelX, labelY, 0xFFFFFFFF);
            graphics.text(font, shortcutLabel, boxX, y, 0xFFA0A0A0);

            editBox.setX(boxX);
            editBox.setY(boxY);
            editBox.extractRenderState(graphics, mouseX, mouseY, a);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(editBox);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of(editBox);
        }

        public String getValue() {
            return editBox.getValue();
        }
    }
}