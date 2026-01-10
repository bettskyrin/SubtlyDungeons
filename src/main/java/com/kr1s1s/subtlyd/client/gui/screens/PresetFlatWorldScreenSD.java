package com.kr1s1s.subtlyd.client.gui.screens;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.kr1s1s.subtlyd.client.gui.screens.worldselectionold.WorldCreationContextSD;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.FlatLevelGeneratorPresetTags;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class PresetFlatWorldScreenSD extends Screen {
    static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot");
    static final Logger LOGGER = LogUtils.getLogger();
    private static final int SLOT_BG_SIZE = 18;
    private static final int SLOT_STAT_HEIGHT = 20;
    private static final int SLOT_BG_X = 1;
    private static final int SLOT_BG_Y = 1;
    private static final int SLOT_FG_X = 2;
    private static final int SLOT_FG_Y = 2;
    private static final ResourceKey<Biome> DEFAULT_BIOME;
    public static final Component UNKNOWN_PRESET;
    private final CreateFlatWorldScreenSD parent;
    private Component shareText;
    private Component listText;
    private PresetFlatWorldScreenSD.PresetsListSD list;
    private Button selectButton;
    EditBox export;
    FlatLevelGeneratorSettings settings;

    public PresetFlatWorldScreenSD(CreateFlatWorldScreenSD createFlatWorldScreen) {
        super(Component.translatable("createWorld.customize.presets.title"));
        this.parent = createFlatWorldScreen;
    }

    private static @Nullable FlatLayerInfo getLayerInfoFromString(HolderGetter<Block> holderGetter, String string, int i) {
        List<String> list = Splitter.on('*').limit(2).splitToList(string);
        int j;
        String string2;
        if (list.size() == 2) {
            string2 = (String) list.get(1);

            try {
                j = Math.max(Integer.parseInt((String) list.get(0)), 0);
            } catch (NumberFormatException numberFormatException) {
                LOGGER.error("Error while parsing flat world string", numberFormatException);
                return null;
            }
        } else {
            string2 = (String) list.get(0);
            j = 1;
        }

        int k = Math.min(i + j, DimensionType.Y_SIZE);
        int l = k - i;

        Optional<Holder.Reference<Block>> optional;
        try {
            optional = holderGetter.get(ResourceKey.create(Registries.BLOCK, Identifier.parse(string2)));
        } catch (Exception exception) {
            LOGGER.error("Error while parsing flat world string", exception);
            return null;
        }

        if (optional.isEmpty()) {
            LOGGER.error("Error while parsing flat world string => Unknown block, {}", string2);
            return null;
        } else {
            return new FlatLayerInfo(l, (Block) ((Holder.Reference) optional.get()).value());
        }
    }

    private static List<FlatLayerInfo> getLayersInfoFromString(HolderGetter<Block> holderGetter, String string) {
        List<FlatLayerInfo> list = Lists.newArrayList();
        String[] strings = string.split(",");
        int i = 0;

        for (String string2 : strings) {
            FlatLayerInfo flatLayerInfo = getLayerInfoFromString(holderGetter, string2, i);
            if (flatLayerInfo == null) {
                return Collections.emptyList();
            }

            int j = DimensionType.Y_SIZE - i;
            if (j > 0) {
                list.add(flatLayerInfo.heightLimited(j));
                i += flatLayerInfo.getHeight();
            }
        }

        return list;
    }

    public static FlatLevelGeneratorSettings fromString(HolderGetter<Block> holderGetter, HolderGetter<Biome> holderGetter2, HolderGetter<StructureSet> holderGetter3, HolderGetter<PlacedFeature> holderGetter4, String string, FlatLevelGeneratorSettings flatLevelGeneratorSettings) {
        Iterator<String> iterator = Splitter.on(';').split(string).iterator();
        if (!iterator.hasNext()) {
            return FlatLevelGeneratorSettings.getDefault(holderGetter2, holderGetter3, holderGetter4);
        } else {
            List<FlatLayerInfo> list = getLayersInfoFromString(holderGetter, (String) iterator.next());
            if (list.isEmpty()) {
                return FlatLevelGeneratorSettings.getDefault(holderGetter2, holderGetter3, holderGetter4);
            } else {
                Holder.Reference<Biome> reference = holderGetter2.getOrThrow(DEFAULT_BIOME);
                Holder<Biome> holder = reference;
                if (iterator.hasNext()) {
                    String string2 = (String)iterator.next();
                    holder = (Holder<Biome>)Optional.ofNullable(Identifier.tryParse(string2))
                            .map(identifier -> ResourceKey.create(Registries.BIOME, identifier))
                            .flatMap(holderGetter2::get)
                            .orElseGet(() -> {
                                LOGGER.warn("Invalid biome: {}", string2);
                                return reference;
                            });
                }

                return flatLevelGeneratorSettings.withBiomeAndLayers(list, flatLevelGeneratorSettings.structureOverrides(), holder);
            }
        }
    }

    static String save(FlatLevelGeneratorSettings flatLevelGeneratorSettings) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < flatLevelGeneratorSettings.getLayersInfo().size(); ++i) {
            if (i > 0) {
                stringBuilder.append(",");
            }

            stringBuilder.append(flatLevelGeneratorSettings.getLayersInfo().get(i));
        }

        stringBuilder.append(";");
        stringBuilder.append(flatLevelGeneratorSettings.getBiome().unwrapKey().map(ResourceKey::identifier).orElseThrow(() -> new IllegalStateException("Biome not registered")));
        return stringBuilder.toString();
    }

    protected void init() {
        this.shareText = Component.translatable("createWorld.customize.presets.share");
        this.listText = Component.translatable("createWorld.customize.presets.list");
        this.export = new EditBox(this.font, 50, 40, this.width - 100, 20, this.shareText);
        this.export.setMaxLength(1230);
        WorldCreationContextSD worldCreationContext = this.parent.parent.getUiState().getSettings();
        RegistryAccess registryAccess = worldCreationContext.worldgenLoadContext();
        FeatureFlagSet featureFlagSet = worldCreationContext.dataConfiguration().enabledFeatures();
        HolderGetter<Biome> holderGetter = registryAccess.lookupOrThrow(Registries.BIOME);
        HolderGetter<StructureSet> holderGetter2 = registryAccess.lookupOrThrow(Registries.STRUCTURE_SET);
        HolderGetter<PlacedFeature> holderGetter3 = registryAccess.lookupOrThrow(Registries.PLACED_FEATURE);
        HolderGetter<Block> holderGetter4 = registryAccess.lookupOrThrow(Registries.BLOCK).filterFeatures(featureFlagSet);
        this.export.setValue(save(this.parent.settings()));
        this.settings = this.parent.settings();
        this.addWidget(this.export);
        this.list = (PresetFlatWorldScreenSD.PresetsListSD) this.addRenderableWidget(new PresetFlatWorldScreenSD.PresetsListSD(registryAccess, featureFlagSet));
        this.selectButton = (Button) this.addRenderableWidget(Button.builder(Component.translatable("createWorld.customize.presets.select"), (button) -> {
            FlatLevelGeneratorSettings flatLevelGeneratorSettings = fromString(holderGetter4, holderGetter, holderGetter2, holderGetter3, this.export.getValue(), this.settings);
            this.parent.setConfig(flatLevelGeneratorSettings);
            this.minecraft.setScreen(this.parent);
        }).bounds(this.width / 2 - 155, this.height - 28, 150, 20).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, (button) -> this.minecraft.setScreen(this.parent)).bounds(this.width / 2 + 5, this.height - 28, 150, 20).build());
        this.updateButtonValidity(this.list.getSelected() != null);
    }

    public boolean mouseScrolled(double d, double e, double f, double g) {
        return this.list.mouseScrolled(d, e, f, g);
    }

    public void resize(int i, int j) {
        String string = this.export.getValue();
        this.init(i, j);
        this.export.setValue(string);
    }

    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 8, -1);
        guiGraphics.drawString(this.font, this.shareText, 51, 30, -6250336);
        guiGraphics.drawString(this.font, this.listText, 51, 68, -6250336);
        this.export.render(guiGraphics, i, j, f);
    }

    public void updateButtonValidity(boolean bl) {
        this.selectButton.active = bl || this.export.getValue().length() > 1;
    }

    static {
        DEFAULT_BIOME = Biomes.PLAINS;
        UNKNOWN_PRESET = Component.translatable("flat_world_preset.unknown");
    }

    @Environment(EnvType.CLIENT)
    class PresetsListSD extends ObjectSelectionList<PresetsListSD.Entry> {
        public PresetsListSD(final RegistryAccess registryAccess, final FeatureFlagSet featureFlagSet) {
            super(PresetFlatWorldScreenSD.this.minecraft, PresetFlatWorldScreenSD.this.width, PresetFlatWorldScreenSD.this.height - 117, 80, 24);

            for (Holder<FlatLevelGeneratorPreset> holder : registryAccess.lookupOrThrow(Registries.FLAT_LEVEL_GENERATOR_PRESET).getTagOrEmpty(FlatLevelGeneratorPresetTags.VISIBLE)) {
                Set<Block> set = (Set) ((FlatLevelGeneratorPreset) holder.value()).settings().getLayersInfo().stream().map((flatLayerInfo) -> flatLayerInfo.getBlockState().getBlock()).filter((block) -> !block.isEnabled(featureFlagSet)).collect(Collectors.toSet());
                if (!set.isEmpty()) {
                    PresetFlatWorldScreenSD.LOGGER.info("Discarding flat world preset {} since it contains experimental blocks {}", holder.unwrapKey().map((resourceKey) -> resourceKey.identifier().toString()).orElse("<unknown>"), set);
                } else {
                    this.addEntry(new Entry(holder));
                }
            }

        }

        public void setSelected(PresetFlatWorldScreenSD.PresetsListSD.Entry entry) {
            super.setSelected(entry);
            PresetFlatWorldScreenSD.this.updateButtonValidity(entry != null);
        }

        public boolean keyPressed(KeyEvent keyEvent) {
            if (super.keyPressed(keyEvent)) {
                return true;
            } else {
                if (keyEvent.isSelection() && this.getSelected() != null) {
                    ((PresetFlatWorldScreenSD.PresetsListSD.Entry) this.getSelected()).select();
                }

                return false;
            }
        }

        @Environment(EnvType.CLIENT)
        public class Entry extends ObjectSelectionList.Entry<PresetFlatWorldScreenSD.PresetsListSD.Entry> {
            private static final Identifier STATS_ICON_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/stats_icons.png");
            private final FlatLevelGeneratorPreset preset;
            private final Component name;

            public Entry(final Holder<FlatLevelGeneratorPreset> holder) {
                this.preset = (FlatLevelGeneratorPreset) holder.value();
                this.name = (Component) holder.unwrapKey().map((resourceKey) -> Component.translatable(resourceKey.identifier().toLanguageKey("flat_world_preset"))).orElse((MutableComponent) PresetFlatWorldScreenSD.UNKNOWN_PRESET);
            }

            public void renderContent(GuiGraphics guiGraphics, int i, int j, boolean bl, float f) {
                this.blitSlot(guiGraphics, this.getContentX(), this.getContentY(), (Item) this.preset.displayItem().value());
                guiGraphics.drawString(PresetFlatWorldScreenSD.this.font, this.name, this.getContentX() + 18 + 5, this.getContentY() + 6, -1);
            }

            public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
                this.select();
                return super.mouseClicked(mouseButtonEvent, bl);
            }

            void select() {
                PresetFlatWorldScreenSD.PresetsListSD.this.setSelected(this);
                PresetFlatWorldScreenSD.this.settings = this.preset.settings();
                PresetFlatWorldScreenSD.this.export.setValue(PresetFlatWorldScreenSD.save(PresetFlatWorldScreenSD.this.settings));
                PresetFlatWorldScreenSD.this.export.moveCursorToStart(false);
            }

            private void blitSlot(GuiGraphics guiGraphics, int i, int j, Item item) {
                this.blitSlotBg(guiGraphics, i + 1, j + 1);
                guiGraphics.renderFakeItem(new ItemStack(item), i + 2, j + 2);
            }

            private void blitSlotBg(GuiGraphics guiGraphics, int i, int j) {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PresetFlatWorldScreenSD.SLOT_SPRITE, i, j, 18, 18);
            }

            public Component getNarration() {
                return Component.translatable("narrator.select", new Object[]{this.name});
            }
        }
    }
}
