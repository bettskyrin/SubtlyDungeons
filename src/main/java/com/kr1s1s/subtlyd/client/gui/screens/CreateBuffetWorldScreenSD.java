package com.kr1s1s.subtlyd.client.gui.screens;

import com.ibm.icu.text.Collator;
import com.kr1s1s.subtlyd.client.gui.screens.worldselectionold.WorldCreationContextSD;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class CreateBuffetWorldScreenSD extends Screen {
    private static final Component SEARCH_HINT;
    private static final int SPACING = 3;
    private static final int SEARCH_BOX_HEIGHT = 15;
    final HeaderAndFooterLayout layout;
    private final Screen parent;
    private final Consumer<Holder<Biome>> applySettings;
    final Registry<Biome> biomes;
    private CreateBuffetWorldScreenSD.BiomeList list;
    Holder<Biome> biome;
    private Button doneButton;

    public CreateBuffetWorldScreenSD(Screen screen, WorldCreationContextSD worldCreationContext, Consumer<Holder<Biome>> consumer) {
        super(Component.translatable("createWorld.customize.buffet.title"));
        this.parent = screen;
        this.applySettings = consumer;
        Objects.requireNonNull(this.font);
        this.layout = new HeaderAndFooterLayout(this, 13 + 9 + 3 + 15, 33);
        this.biomes = worldCreationContext.worldgenLoadContext().lookupOrThrow(Registries.BIOME);
        Holder<Biome> holder = (Holder)this.biomes.get(Biomes.PLAINS).or(() -> this.biomes.listElements().findAny()).orElseThrow();
        this.biome = (Holder)worldCreationContext.selectedDimensions().overworld().getBiomeSource().possibleBiomes().stream().findFirst().orElse(holder);
    }

    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    protected void init() {
        LinearLayout linearLayout = (LinearLayout)this.layout.addToHeader(LinearLayout.vertical().spacing(3));
        linearLayout.defaultCellSetting().alignHorizontallyCenter();
        linearLayout.addChild(new StringWidget(this.getTitle(), this.font));
        EditBox editBox = (EditBox)linearLayout.addChild(new EditBox(this.font, 200, 15, Component.empty()));
        CreateBuffetWorldScreenSD.BiomeList biomeList = new CreateBuffetWorldScreenSD.BiomeList();
        editBox.setHint(SEARCH_HINT);
        Objects.requireNonNull(biomeList);
        editBox.setResponder(biomeList::filterEntries);
        this.list = (CreateBuffetWorldScreenSD.BiomeList)this.layout.addToContents(biomeList);
        LinearLayout linearLayout2 = (LinearLayout)this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
        this.doneButton = (Button)linearLayout2.addChild(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            this.applySettings.accept(this.biome);
            this.onClose();
        }).build());
        linearLayout2.addChild(Button.builder(CommonComponents.GUI_CANCEL, (button) -> this.onClose()).build());
        this.list.setSelected((CreateBuffetWorldScreenSD.BiomeList.Entry)this.list.children().stream().filter((entry) -> Objects.equals(entry.biome, this.biome)).findFirst().orElse((BiomeList.Entry) null));
        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    protected void repositionElements() {
        this.layout.arrangeElements();
        this.list.updateSize(this.width, this.layout);
    }

    void updateButtonValidity() {
        this.doneButton.active = this.list.getSelected() != null;
    }

    static {
        SEARCH_HINT = Component.translatable("createWorld.customize.buffet.search").withStyle(EditBox.SEARCH_HINT_STYLE);
    }

    @Environment(EnvType.CLIENT)
    class BiomeList extends ObjectSelectionList<BiomeList.Entry> {
        BiomeList() {
            super(CreateBuffetWorldScreenSD.this.minecraft, CreateBuffetWorldScreenSD.this.width, CreateBuffetWorldScreenSD.this.layout.getContentHeight(), CreateBuffetWorldScreenSD.this.layout.getHeaderHeight(), 15);
            this.filterEntries("");
        }

        private void filterEntries(String string) {
            Collator collator = Collator.getInstance(Locale.getDefault());
            String string2 = string.toLowerCase(Locale.ROOT);
            List<CreateBuffetWorldScreenSD.BiomeList.Entry> list = CreateBuffetWorldScreenSD.this.biomes.listElements().map((reference) -> new CreateBuffetWorldScreenSD.BiomeList.Entry(reference)).sorted(Comparator.comparing((entry) -> entry.name.getString(), collator)).filter((entry) -> string.isEmpty() || entry.name.getString().toLowerCase(Locale.ROOT).contains(string2)).toList();
            this.replaceEntries(list);
            this.refreshScrollAmount();
        }

        public void setSelected(CreateBuffetWorldScreenSD.BiomeList.Entry entry) {
            super.setSelected(entry);
            if (entry != null) {
                CreateBuffetWorldScreenSD.this.biome = entry.biome;
            }

            CreateBuffetWorldScreenSD.this.updateButtonValidity();
        }

        @Environment(EnvType.CLIENT)
        class Entry extends ObjectSelectionList.Entry<CreateBuffetWorldScreenSD.BiomeList.Entry> {
            final Holder.Reference<Biome> biome;
            final Component name;

            public Entry(final Holder.Reference<Biome> reference) {
                this.biome = reference;
                Identifier identifier = reference.key().identifier();
                String string = identifier.toLanguageKey("biome");
                if (Language.getInstance().has(string)) {
                    this.name = Component.translatable(string);
                } else {
                    this.name = Component.literal(identifier.toString());
                }

            }

            public Component getNarration() {
                return Component.translatable("narrator.select", new Object[]{this.name});
            }

            public void renderContent(GuiGraphics guiGraphics, int i, int j, boolean bl, float f) {
                guiGraphics.drawString(CreateBuffetWorldScreenSD.this.font, this.name, this.getContentX() + 5, this.getContentY() + 2, -1);
            }

            public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
                CreateBuffetWorldScreenSD.BiomeList.this.setSelected(this);
                return super.mouseClicked(mouseButtonEvent, bl);
            }
        }
    }
}
