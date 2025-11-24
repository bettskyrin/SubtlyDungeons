package com.kr1s1s.subtlyd.client.gui.screens;

import com.ibm.icu.text.Collator;
import com.kr1s1s.subtlyd.client.gui.screens.worldselection.WorldCreationContextSD;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class CreateBuffetWorldScreenSD extends Screen {
    private static final Component SEARCH_HINT = Component.translatable("createWorld.customize.buffet.search").withStyle(EditBox.SEARCH_HINT_STYLE);
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
        this.layout = new HeaderAndFooterLayout(this, 13 + 9 + 3 + 15, 33);
        this.biomes = worldCreationContext.worldgenLoadContext().lookupOrThrow(Registries.BIOME);
        Holder<Biome> holder = (Holder<Biome>)this.biomes.get(Biomes.PLAINS).or(() -> this.biomes.listElements().findAny()).orElseThrow();
        this.biome = (Holder<Biome>)worldCreationContext.selectedDimensions().overworld().getBiomeSource().possibleBiomes().stream().findFirst().orElse(holder);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    protected void init() {
        LinearLayout linearLayout = this.layout.addToHeader(LinearLayout.vertical().spacing(3));
        linearLayout.defaultCellSetting().alignHorizontallyCenter();
        linearLayout.addChild(new StringWidget(this.getTitle(), this.font));
        EditBox editBox = linearLayout.addChild(new EditBox(this.font, 200, 15, Component.empty()));
        CreateBuffetWorldScreenSD.BiomeList biomeList = new CreateBuffetWorldScreenSD.BiomeList();
        editBox.setHint(SEARCH_HINT);
        editBox.setResponder(biomeList::filterEntries);
        this.list = this.layout.addToContents(biomeList);
        LinearLayout linearLayout2 = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
        this.doneButton = linearLayout2.addChild(Button.builder(CommonComponents.GUI_DONE, button -> {
            this.applySettings.accept(this.biome);
            this.onClose();
        }).build());
        linearLayout2.addChild(Button.builder(CommonComponents.GUI_CANCEL, button -> this.onClose()).build());
        this.list.setSelected((CreateBuffetWorldScreenSD.BiomeList.Entry)this.list.children().stream().filter(entry -> Objects.equals(entry.biome, this.biome)).findFirst().orElse(null));
        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
        this.list.updateSize(this.width, this.layout);
    }

    void updateButtonValidity() {
        this.doneButton.active = this.list.getSelected() != null;
    }

    @Environment(EnvType.CLIENT)
    class BiomeList extends ObjectSelectionList<CreateBuffetWorldScreenSD.BiomeList.Entry> {
        BiomeList() {
            super(
                    CreateBuffetWorldScreenSD.this.minecraft,
                    CreateBuffetWorldScreenSD.this.width,
                    CreateBuffetWorldScreenSD.this.layout.getContentHeight(),
                    CreateBuffetWorldScreenSD.this.layout.getHeaderHeight(),
                    15
            );
            this.filterEntries("");
        }

        private void filterEntries(String string) {
            Collator collator = Collator.getInstance(Locale.getDefault());
            String string2 = string.toLowerCase(Locale.ROOT);
            List<CreateBuffetWorldScreenSD.BiomeList.Entry> list = CreateBuffetWorldScreenSD.this.biomes
                    .listElements()
                    .map(reference -> new CreateBuffetWorldScreenSD.BiomeList.Entry(this, reference))
                    .sorted(Comparator.comparing(entry -> entry.name.getString(), collator))
                    .filter(entry -> string.isEmpty() || entry.name.getString().toLowerCase(Locale.ROOT).contains(string2))
                    .toList();
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
    }
}
