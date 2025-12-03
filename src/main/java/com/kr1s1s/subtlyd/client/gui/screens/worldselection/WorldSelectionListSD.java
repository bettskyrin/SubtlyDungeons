package com.kr1s1s.subtlyd.client.gui.screens.worldselection;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.CrashReport;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.SelectableEntry;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.AlertScreen;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.ErrorScreen;
import net.minecraft.client.gui.screens.FaviconTexture;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.LoadingDotsText;
import net.minecraft.client.gui.screens.NoticeWithLinkScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.EditWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.NbtException;
import net.minecraft.nbt.ReportedNbtException;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageException;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraft.world.level.validation.ContentValidationException;
import net.minecraft.world.level.validation.ForbiddenSymlinkInfo;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class WorldSelectionListSD extends ObjectSelectionList<WorldSelectionListSD.Entry> {
    public static final DateTimeFormatter DATE_FORMAT = Util.localizedDateFormatter(FormatStyle.SHORT);
    static final Identifier ERROR_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("world_list/error_highlighted");
    static final Identifier ERROR_SPRITE = Identifier.withDefaultNamespace("world_list/error");
    static final Identifier MARKED_JOIN_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("world_list/marked_join_highlighted");
    static final Identifier MARKED_JOIN_SPRITE = Identifier.withDefaultNamespace("world_list/marked_join");
    static final Identifier WARNING_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("world_list/warning_highlighted");
    static final Identifier WARNING_SPRITE = Identifier.withDefaultNamespace("world_list/warning");
    static final Identifier JOIN_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("world_list/join_highlighted");
    static final Identifier JOIN_SPRITE = Identifier.withDefaultNamespace("world_list/join");
    static final Logger LOGGER = LogUtils.getLogger();
    static final Component FROM_NEWER_TOOLTIP_1 = Component.translatable("selectWorld.tooltip.fromNewerVersion1").withStyle(ChatFormatting.RED);
    static final Component FROM_NEWER_TOOLTIP_2 = Component.translatable("selectWorld.tooltip.fromNewerVersion2").withStyle(ChatFormatting.RED);
    static final Component SNAPSHOT_TOOLTIP_1 = Component.translatable("selectWorld.tooltip.snapshot1").withStyle(ChatFormatting.GOLD);
    static final Component SNAPSHOT_TOOLTIP_2 = Component.translatable("selectWorld.tooltip.snapshot2").withStyle(ChatFormatting.GOLD);
    static final Component WORLD_LOCKED_TOOLTIP = Component.translatable("selectWorld.locked").withStyle(ChatFormatting.RED);
    static final Component WORLD_REQUIRES_CONVERSION = Component.translatable("selectWorld.conversion.tooltip").withStyle(ChatFormatting.RED);
    static final Component INCOMPATIBLE_VERSION_TOOLTIP = Component.translatable("selectWorld.incompatible.tooltip").withStyle(ChatFormatting.RED);
    static final Component WORLD_EXPERIMENTAL = Component.translatable("selectWorld.experimental");
    private final Screen screen;
    private static CompletableFuture<List<LevelSummary>> pendingLevels;
    @Nullable
    private List<LevelSummary> currentlyDisplayedLevels;
    private final WorldSelectionListSD.LoadingHeader loadingHeader;
    final WorldSelectionListSD.EntryType entryType;
    private String filter;
    private boolean hasPolled;
    @Nullable
    private final Consumer<LevelSummary> onEntrySelect;
    @Nullable
    final Consumer<WorldSelectionListSD.WorldListEntry> onEntryInteract;

    WorldSelectionListSD(
            Screen screen,
            Minecraft minecraft,
            int i,
            int j,
            String string,
            @Nullable WorldSelectionListSD worldSelectionList,
            @Nullable Consumer<LevelSummary> consumer,
            @Nullable Consumer<WorldSelectionListSD.WorldListEntry> consumer2,
            WorldSelectionListSD.EntryType entryType
    ) {
        super(minecraft, i, j, 0, 36);
        this.screen = screen;
        this.loadingHeader = new WorldSelectionListSD.LoadingHeader(minecraft);
        this.filter = string;
        this.onEntrySelect = consumer;
        this.onEntryInteract = consumer2;
        this.entryType = entryType;
        if (worldSelectionList == null) {
            pendingLevels = this.loadLevels();
        }

        this.addEntry(this.loadingHeader);
        this.handleNewLevels(this.pollLevelsIgnoreErrors());
    }

    @Override
    protected void clearEntries() {
        this.children().forEach(WorldSelectionListSD.Entry::close);
        super.clearEntries();
    }

    @Nullable
    private List<LevelSummary> pollLevelsIgnoreErrors() {
        try {
            List<LevelSummary> list = pendingLevels.getNow(null);
            if (this.entryType == WorldSelectionListSD.EntryType.UPLOAD_WORLD) {
                if (list == null || this.hasPolled) {
                    return null;
                }

                this.hasPolled = true;
                list = list.stream().filter(LevelSummary::canUpload).toList();
            }

            return list;
        } catch (CancellationException | CompletionException var2) {
            return null;
        }
    }

    public void reloadWorldList() {
        pendingLevels = this.loadLevels();
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        List<LevelSummary> list = this.pollLevelsIgnoreErrors();
        if (list != this.currentlyDisplayedLevels) {
            this.handleNewLevels(list);
        }

        super.renderWidget(guiGraphics, i, j, f);
    }

    private void handleNewLevels(@Nullable List<LevelSummary> list) {
        if (list != null) {
            if (list.isEmpty()) {
                switch (this.entryType) {
                    case SINGLEPLAYER:
                        CreateWorldScreen.openFresh(this.minecraft, () -> this.minecraft.setScreen(null));
                        break;
                    case UPLOAD_WORLD:
                        this.clearEntries();
                        this.addEntry(new WorldSelectionListSD.NoWorldsEntry(Component.translatable("mco.upload.select.world.none"), this.screen.getFont()));
                }
            } else {
                this.fillLevels(this.filter, list);
                this.currentlyDisplayedLevels = list;
            }
        }
    }

    public void updateFilter(String string) {
        if (this.currentlyDisplayedLevels != null && !string.equals(this.filter)) {
            this.fillLevels(string, this.currentlyDisplayedLevels);
        }

        this.filter = string;
    }

    private CompletableFuture<List<LevelSummary>> loadLevels() {
        LevelStorageSource.LevelCandidates levelCandidates;
        try {
            levelCandidates = this.minecraft.getLevelSource().findLevelCandidates();
        } catch (LevelStorageException var3) {
            LOGGER.error("Couldn't load level list", var3);
            this.handleLevelLoadFailure(var3.getMessageComponent());
            return CompletableFuture.completedFuture(List.of());
        }

        return this.minecraft.getLevelSource().loadLevelSummaries(levelCandidates).exceptionally(throwable -> {
            this.minecraft.delayCrash(CrashReport.forThrowable(throwable, "Couldn't load level list"));
            return List.of();
        });
    }

    private void fillLevels(String string, List<LevelSummary> list) {
        List<WorldSelectionListSD.Entry> list2 = new ArrayList<>();
        Optional<WorldSelectionListSD.WorldListEntry> optional = this.getSelectedOpt();
        WorldSelectionListSD.WorldListEntry worldListEntry = null;

        for (LevelSummary levelSummary : list.stream().filter(levelSummaryx -> this.filterAccepts(string.toLowerCase(Locale.ROOT), levelSummaryx)).toList()) {
            WorldSelectionListSD.WorldListEntry worldListEntry2 = new WorldSelectionListSD.WorldListEntry(this, levelSummary);
            if (optional.isPresent()
                    && optional.get().getLevelSummary().getLevelId().equals(worldListEntry2.getLevelSummary().getLevelId())) {
                worldListEntry = worldListEntry2;
            }

            list2.add(worldListEntry2);
        }

        this.removeEntries(this.children().stream().filter(entry -> !list2.contains(entry)).toList());
        list2.forEach(entry -> {
            if (!this.children().contains(entry)) {
                this.addEntry(entry);
            }
        });
        this.setSelected(worldListEntry);
        this.notifyListUpdated();
    }

    private boolean filterAccepts(String string, LevelSummary levelSummary) {
        return levelSummary.getLevelName().toLowerCase(Locale.ROOT).contains(string) || levelSummary.getLevelId().toLowerCase(Locale.ROOT).contains(string);
    }

    private void notifyListUpdated() {
        this.refreshScrollAmount();
        this.screen.triggerImmediateNarration(true);
    }

    private void handleLevelLoadFailure(Component component) {
        this.minecraft.setScreen(new ErrorScreen(Component.translatable("selectWorld.unable_to_load"), component));
    }

    @Override
    public int getRowWidth() {
        return getScreen().width - 8;
    }

    public void setSelected(WorldSelectionListSD.@Nullable Entry entry) {
        super.setSelected(entry);
        if (this.onEntrySelect != null) {
            this.onEntrySelect.accept(entry instanceof WorldSelectionListSD.WorldListEntry worldListEntry ? worldListEntry.summary : null);
        }
    }

    public Optional<WorldSelectionListSD.WorldListEntry> getSelectedOpt() {
        WorldSelectionListSD.Entry entry = this.getSelected();
        return entry instanceof WorldSelectionListSD.WorldListEntry worldListEntry ? Optional.of(worldListEntry) : Optional.empty();
    }

    public void returnToScreen() {
        this.reloadWorldList();
        this.minecraft.setScreen(this.screen);
    }

    public Screen getScreen() {
        return this.screen;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        if (this.children().contains(this.loadingHeader)) {
            this.loadingHeader.updateNarration(narrationElementOutput);
        } else {
            super.updateWidgetNarration(narrationElementOutput);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Builder {
        private final Minecraft minecraft;
        private final Screen screen;
        private int width;
        private int height;
        private String filter = "";
        private WorldSelectionListSD.EntryType type = WorldSelectionListSD.EntryType.SINGLEPLAYER;
        @Nullable
        private WorldSelectionListSD oldList = null;
        @Nullable
        private Consumer<LevelSummary> onEntrySelect = null;
        @Nullable
        private Consumer<WorldSelectionListSD.WorldListEntry> onEntryInteract = null;

        public Builder(Minecraft minecraft, Screen screen) {
            this.minecraft = minecraft;
            this.screen = screen;
        }

        public WorldSelectionListSD.Builder width(int i) {
            this.width = i;
            return this;
        }

        public WorldSelectionListSD.Builder height(int i) {
            this.height = i;
            return this;
        }

        public WorldSelectionListSD.Builder filter(String string) {
            this.filter = string;
            return this;
        }

        public WorldSelectionListSD.Builder oldList(@Nullable WorldSelectionListSD worldSelectionList) {
            this.oldList = worldSelectionList;
            return this;
        }

        public WorldSelectionListSD.Builder onEntrySelect(Consumer<LevelSummary> consumer) {
            this.onEntrySelect = consumer;
            return this;
        }

        public WorldSelectionListSD.Builder onEntryInteract(Consumer<WorldSelectionListSD.WorldListEntry> consumer) {
            this.onEntryInteract = consumer;
            return this;
        }

        public WorldSelectionListSD.Builder uploadWorld() {
            this.type = WorldSelectionListSD.EntryType.UPLOAD_WORLD;
            return this;
        }

        public WorldSelectionListSD build() {
            return new WorldSelectionListSD(
                    this.screen, this.minecraft, this.width, this.height, this.filter, this.oldList, this.onEntrySelect, this.onEntryInteract, this.type
            );
        }
    }

    @Environment(EnvType.CLIENT)
    public abstract static class Entry extends ObjectSelectionList.Entry<WorldSelectionListSD.Entry> implements AutoCloseable {
        public void close() {
        }

        @Nullable
        public LevelSummary getLevelSummary() {
            return null;
        }
    }

    @Environment(EnvType.CLIENT)
    public enum EntryType {
        SINGLEPLAYER,
        UPLOAD_WORLD
    }

    @Environment(EnvType.CLIENT)
    public static class LoadingHeader extends WorldSelectionListSD.Entry {
        private static final Component LOADING_LABEL = Component.translatable("selectWorld.loading_list");
        private final Minecraft minecraft;

        public LoadingHeader(Minecraft minecraft) {
            this.minecraft = minecraft;
        }

        @Override
        public void renderContent(GuiGraphics guiGraphics, int i, int j, boolean bl, float f) {
            int k = (this.minecraft.screen.width - this.minecraft.font.width(LOADING_LABEL)) / 2;
            int l = this.getContentY() + (this.getContentHeight() - 9) / 2;
            guiGraphics.drawString(this.minecraft.font, LOADING_LABEL, k, l, -1);
            String string = LoadingDotsText.get(Util.getMillis());
            int m = (this.minecraft.screen.width - this.minecraft.font.width(string)) / 2;
            int n = l + 9;
            guiGraphics.drawString(this.minecraft.font, string, m, n, -8355712);
        }

        @Override
        public Component getNarration() {
            return LOADING_LABEL;
        }
    }

    @Environment(EnvType.CLIENT)
    public static final class NoWorldsEntry extends WorldSelectionListSD.Entry {
        private final StringWidget stringWidget;

        public NoWorldsEntry(Component component, Font font) {
            this.stringWidget = new StringWidget(component, font);
        }

        @Override
        public Component getNarration() {
            return this.stringWidget.getMessage();
        }

        @Override
        public void renderContent(GuiGraphics guiGraphics, int i, int j, boolean bl, float f) {
            this.stringWidget.setPosition(this.getContentXMiddle() - this.stringWidget.getWidth() / 2, this.getContentYMiddle() - this.stringWidget.getHeight() / 2);
            this.stringWidget.render(guiGraphics, i, j, f);
        }
    }

    @Environment(EnvType.CLIENT)
    public final class WorldListEntry extends WorldSelectionListSD.Entry implements SelectableEntry {
        private static final int ICON_WIDTH = 57;
        private static final int ICON_HEIGHT = 32;
        private final WorldSelectionListSD list;
        private final Minecraft minecraft;
        private final Screen screen;
        final LevelSummary summary;
        private final FaviconTexture icon;
        private final StringWidget worldNameText;
        private final StringWidget idAndLastPlayedText;
        private final StringWidget infoText;
        @Nullable
        private Path iconFile;

        public WorldListEntry(final WorldSelectionListSD worldSelectionList2, final LevelSummary levelSummary) {
            this.list = worldSelectionList2;
            this.minecraft = worldSelectionList2.minecraft;
            this.screen = worldSelectionList2.getScreen();
            this.summary = levelSummary;
            this.icon = FaviconTexture.forWorld(this.minecraft.getTextureManager(), levelSummary.getLevelId());
            this.iconFile = levelSummary.getIcon();
            int i = worldSelectionList2.getRowWidth() - this.getTextX() - 2;
            Component component = Component.literal(levelSummary.getLevelName());
            this.worldNameText = new StringWidget(component, this.minecraft.font);
            this.worldNameText.setMaxWidth(i);
            if (this.minecraft.font.width(component) > i) {
                this.worldNameText.setTooltip(Tooltip.create(component));
            }

            String string = levelSummary.getLevelId();
            long l = levelSummary.getLastPlayed();
            if (l != -1L) {
                ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneId.systemDefault());
                string = string + " (" + WorldSelectionListSD.DATE_FORMAT.format(zonedDateTime) + ")";
            }

            Component component2 = Component.literal(string).withColor(-8355712);
            this.idAndLastPlayedText = new StringWidget(component2, this.minecraft.font);
            this.idAndLastPlayedText.setMaxWidth(i);
            if (this.minecraft.font.width(string) > i) {
                this.idAndLastPlayedText.setTooltip(Tooltip.create(component2));
            }

            Component component3 = ComponentUtils.mergeStyles(levelSummary.getInfo(), Style.EMPTY.withColor(-8355712));
            this.infoText = new StringWidget(component3, this.minecraft.font);
            this.infoText.setMaxWidth(i);
            if (this.minecraft.font.width(component3) > i) {
                this.infoText.setTooltip(Tooltip.create(component3));
            }

            this.validateIconFile();
            this.loadIcon();
        }

        private void validateIconFile() {
            if (this.iconFile != null) {
                try {
                    BasicFileAttributes basicFileAttributes = Files.readAttributes(this.iconFile, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
                    if (basicFileAttributes.isSymbolicLink()) {
                        List<ForbiddenSymlinkInfo> list = this.minecraft.directoryValidator().validateSymlink(this.iconFile);
                        if (!list.isEmpty()) {
                            WorldSelectionListSD.LOGGER.warn("{}", ContentValidationException.getMessage(this.iconFile, list));
                            this.iconFile = null;
                        } else {
                            basicFileAttributes = Files.readAttributes(this.iconFile, BasicFileAttributes.class);
                        }
                    }

                    if (!basicFileAttributes.isRegularFile()) {
                        this.iconFile = null;
                    }
                } catch (NoSuchFileException var3) {
                    this.iconFile = null;
                } catch (IOException var4) {
                    WorldSelectionListSD.LOGGER.error("could not validate symlink", var4);
                    this.iconFile = null;
                }
            }
        }

        @Override
        public Component getNarration() {
            Component component = Component.translatable(
                    "narrator.select.world_info", this.summary.getLevelName(), Component.translationArg(new Date(this.summary.getLastPlayed())), this.summary.getInfo()
            );
            if (this.summary.isLocked()) {
                component = CommonComponents.joinForNarration(component, WorldSelectionListSD.WORLD_LOCKED_TOOLTIP);
            }

            if (this.summary.isExperimental()) {
                component = CommonComponents.joinForNarration(component, WorldSelectionListSD.WORLD_EXPERIMENTAL);
            }

            return Component.translatable("narrator.select", component);
        }

        @Override
        public void renderContent(GuiGraphics guiGraphics, int i, int j, boolean bl, float f) {
            int k = this.getTextX();
            int midIcon = this.getContentX() + ICON_WIDTH / 4;
            this.worldNameText.setPosition(k, this.getContentY() + 1);
            this.worldNameText.render(guiGraphics, i, j, f);
            this.idAndLastPlayedText.setPosition(k, this.getContentY() + 9 + 3);
            this.idAndLastPlayedText.render(guiGraphics, i, j, f);
            this.infoText.setPosition(k, this.getContentY() + 9 + 9 + 3);
            this.infoText.render(guiGraphics, i, j, f);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, this.icon.textureLocation(), this.getContentX(), this.getContentY(), 0.0F, 0.0F, ICON_WIDTH, ICON_HEIGHT, ICON_WIDTH, ICON_HEIGHT);
            if (this.list.entryType == WorldSelectionListSD.EntryType.SINGLEPLAYER && (this.minecraft.options.touchscreen().get() || bl)) {
                guiGraphics.fill(this.getContentX(), this.getContentY(), this.getContentX() + ICON_WIDTH, this.getContentY() + ICON_HEIGHT, -1601138544);
                boolean bl2 = isMouseWithin(i, j, this.getContentX() + ICON_WIDTH, this.getContentY() + ICON_HEIGHT);
                Identifier identifier = bl2 ? WorldSelectionListSD.JOIN_HIGHLIGHTED_SPRITE : WorldSelectionListSD.JOIN_SPRITE;
                Identifier identifier2 = bl2 ? WorldSelectionListSD.WARNING_HIGHLIGHTED_SPRITE : WorldSelectionListSD.WARNING_SPRITE;
                Identifier identifier3 = bl2 ? WorldSelectionListSD.ERROR_HIGHLIGHTED_SPRITE : WorldSelectionListSD.ERROR_SPRITE;
                Identifier identifier4 = bl2 ? WorldSelectionListSD.MARKED_JOIN_HIGHLIGHTED_SPRITE : WorldSelectionListSD.MARKED_JOIN_SPRITE;
                if (this.summary instanceof LevelSummary.SymlinkLevelSummary || this.summary instanceof LevelSummary.CorruptedLevelSummary) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, identifier3, midIcon, this.getContentY(), ICON_HEIGHT, ICON_HEIGHT);
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, identifier4, midIcon, this.getContentY(), ICON_HEIGHT, ICON_HEIGHT);
                    return;
                }

                if (this.summary.isLocked()) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, identifier3, midIcon, this.getContentY(), ICON_HEIGHT, ICON_HEIGHT);
                    if (bl2) {
                        guiGraphics.setTooltipForNextFrame(this.minecraft.font.split(WorldSelectionListSD.WORLD_LOCKED_TOOLTIP, 175), i, j);
                    }
                } else if (this.summary.requiresManualConversion()) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, identifier3, midIcon, this.getContentY(), ICON_HEIGHT, ICON_HEIGHT);
                    if (bl2) {
                        guiGraphics.setTooltipForNextFrame(this.minecraft.font.split(WorldSelectionListSD.WORLD_REQUIRES_CONVERSION, 175), i, j);
                    }
                } else if (!this.summary.isCompatible()) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, identifier3, midIcon, this.getContentY(), ICON_HEIGHT, ICON_HEIGHT);
                    if (bl2) {
                        guiGraphics.setTooltipForNextFrame(this.minecraft.font.split(WorldSelectionListSD.INCOMPATIBLE_VERSION_TOOLTIP, 175), i, j);
                    }
                } else if (this.summary.shouldBackup()) {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, identifier4, midIcon, this.getContentY(), ICON_HEIGHT, ICON_HEIGHT);
                    if (this.summary.isDowngrade()) {
                        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, identifier3, midIcon, this.getContentY(), ICON_HEIGHT, ICON_HEIGHT);
                        if (bl2) {
                            guiGraphics.setTooltipForNextFrame(
                                    ImmutableList.of(WorldSelectionListSD.FROM_NEWER_TOOLTIP_1.getVisualOrderText(), WorldSelectionListSD.FROM_NEWER_TOOLTIP_2.getVisualOrderText()), i, j
                            );
                        }
                    } else if (!SharedConstants.getCurrentVersion().stable()) {
                        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, identifier2, midIcon, this.getContentY(), ICON_HEIGHT, ICON_HEIGHT);
                        if (bl2) {
                            guiGraphics.setTooltipForNextFrame(
                                    ImmutableList.of(WorldSelectionListSD.SNAPSHOT_TOOLTIP_1.getVisualOrderText(), WorldSelectionListSD.SNAPSHOT_TOOLTIP_2.getVisualOrderText()), i, j
                            );
                        }
                    }

                    if (bl2) {
                        WorldSelectionListSD.this.handleCursor(guiGraphics);
                    }
                } else {
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, identifier, midIcon, this.getContentY(), ICON_HEIGHT, ICON_HEIGHT);
                    if (bl2) {
                        WorldSelectionListSD.this.handleCursor(guiGraphics);
                    }
                }
            }
        }

        private int getTextX() {
            return this.getContentX() + ICON_WIDTH + 3;
        }

        public boolean isMouseWithin(int i, int j, int right, int top) {
            return i >= this.getContentX()
                    && i < right
                    && j >= this.getContentY()
                    && j < top;
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
            if (this.canInteract()) {
                if (bl || isMouseWithin((int) mouseButtonEvent.x(), (int) mouseButtonEvent.y(), this.getContentX() + ICON_WIDTH, this.getContentY() + ICON_HEIGHT) && this.list.entryType == WorldSelectionListSD.EntryType.SINGLEPLAYER) {
                    this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    Consumer<WorldSelectionListSD.WorldListEntry> consumer = this.list.onEntryInteract;
                    if (consumer != null) {
                        consumer.accept(this);
                        return true;
                    }
                }
            }
            return super.mouseClicked(mouseButtonEvent, bl);
        }

        @Override
        public boolean keyPressed(KeyEvent keyEvent) {
            if (keyEvent.isSelection() && this.canInteract()) {
                this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                Consumer<WorldSelectionListSD.WorldListEntry> consumer = this.list.onEntryInteract;
                if (consumer != null) {
                    consumer.accept(this);
                    return true;
                }
            }

            return super.keyPressed(keyEvent);
        }

        public boolean canInteract() {
            return this.summary.primaryActionActive() || this.list.entryType == WorldSelectionListSD.EntryType.UPLOAD_WORLD;
        }

        public void joinWorld() {
            if (this.summary.primaryActionActive()) {
                if (this.summary instanceof LevelSummary.SymlinkLevelSummary) {
                    this.minecraft.setScreen(NoticeWithLinkScreen.createWorldSymlinkWarningScreen(() -> this.minecraft.setScreen(this.screen)));
                } else {
                    this.minecraft.createWorldOpenFlows().openWorld(this.summary.getLevelId(), this.list::returnToScreen);
                }
            }
        }

        public void deleteWorld() {
            this.minecraft
                    .setScreen(
                            new ConfirmScreen(
                                    bl -> {
                                        if (bl) {
                                            this.minecraft.setScreen(new ProgressScreen(true));
                                            this.doDeleteWorld();
                                        }

                                        this.list.returnToScreen();
                                    },
                                    Component.translatable("selectWorld.deleteQuestion"),
                                    Component.translatable("selectWorld.deleteWarning", this.summary.getLevelName()),
                                    Component.translatable("selectWorld.deleteButton"),
                                    CommonComponents.GUI_CANCEL
                            )
                    );
        }

        public void doDeleteWorld() {
            LevelStorageSource levelStorageSource = this.minecraft.getLevelSource();
            String string = this.summary.getLevelId();

            try (LevelStorageSource.LevelStorageAccess levelStorageAccess = levelStorageSource.createAccess(string)) {
                levelStorageAccess.deleteLevel();
            } catch (IOException var8) {
                SystemToast.onWorldDeleteFailure(this.minecraft, string);
                WorldSelectionListSD.LOGGER.error("Failed to delete world {}", string, var8);
            }
        }

        public void editWorld() {
            this.queueLoadScreen();
            String string = this.summary.getLevelId();

            LevelStorageSource.LevelStorageAccess levelStorageAccess;
            try {
                levelStorageAccess = this.minecraft.getLevelSource().validateAndCreateAccess(string);
            } catch (IOException var6) {
                SystemToast.onWorldAccessFailure(this.minecraft, string);
                WorldSelectionListSD.LOGGER.error("Failed to access level {}", string, var6);
                this.list.reloadWorldList();
                return;
            } catch (ContentValidationException var7) {
                WorldSelectionListSD.LOGGER.warn("{}", var7.getMessage());
                this.minecraft.setScreen(NoticeWithLinkScreen.createWorldSymlinkWarningScreen(() -> this.minecraft.setScreen(this.screen)));
                return;
            }

            EditWorldScreen editWorldScreen;
            try {
                editWorldScreen = EditWorldScreen.create(this.minecraft, levelStorageAccess, bl -> {
                    levelStorageAccess.safeClose();
                    this.list.returnToScreen();
                });
            } catch (NbtException | ReportedNbtException | IOException var5) {
                levelStorageAccess.safeClose();
                SystemToast.onWorldAccessFailure(this.minecraft, string);
                WorldSelectionListSD.LOGGER.error("Failed to load world data {}", string, var5);
                this.list.reloadWorldList();
                return;
            }

            this.minecraft.setScreen(editWorldScreen);
        }

        public void recreateWorld() {
            this.queueLoadScreen();

            try (LevelStorageSource.LevelStorageAccess levelStorageAccess = this.minecraft.getLevelSource().validateAndCreateAccess(this.summary.getLevelId())) {
                Pair<LevelSettings, WorldCreationContext> pair = this.minecraft.createWorldOpenFlows().recreateWorldData(levelStorageAccess);
                LevelSettings levelSettings = pair.getFirst();
                WorldCreationContext worldCreationContext = pair.getSecond();
                Path path = CreateWorldScreen.createTempDataPackDirFromExistingWorld(levelStorageAccess.getLevelPath(LevelResource.DATAPACK_DIR), this.minecraft);
                worldCreationContext.validate();
                if (worldCreationContext.options().isOldCustomizedWorld()) {
                    this.minecraft
                            .setScreen(
                                    new ConfirmScreen(
                                            bl -> this.minecraft
                                                    .setScreen(
                                                            bl
                                                                    ? CreateWorldScreen.createFromExisting(this.minecraft, this.list::returnToScreen, levelSettings, worldCreationContext, path)
                                                                    : this.screen
                                                    ),
                                            Component.translatable("selectWorld.recreate.customized.title"),
                                            Component.translatable("selectWorld.recreate.customized.text"),
                                            CommonComponents.GUI_PROCEED,
                                            CommonComponents.GUI_CANCEL
                                    )
                            );
                } else {
                    this.minecraft.setScreen(CreateWorldScreen.createFromExisting(this.minecraft, this.list::returnToScreen, levelSettings, worldCreationContext, path));
                }
            } catch (ContentValidationException var8) {
                WorldSelectionListSD.LOGGER.warn("{}", var8.getMessage());
                this.minecraft.setScreen(NoticeWithLinkScreen.createWorldSymlinkWarningScreen(() -> this.minecraft.setScreen(this.screen)));
            } catch (Exception var9) {
                WorldSelectionListSD.LOGGER.error("Unable to recreate world", var9);
                this.minecraft
                        .setScreen(
                                new AlertScreen(
                                        () -> this.minecraft.setScreen(this.screen),
                                        Component.translatable("selectWorld.recreate.error.title"),
                                        Component.translatable("selectWorld.recreate.error.text")
                                )
                        );
            }
        }

        private void queueLoadScreen() {
            this.minecraft.setScreenAndShow(new GenericMessageScreen(Component.translatable("selectWorld.data_read")));
        }

        private void loadIcon() {
            boolean bl = this.iconFile != null && Files.isRegularFile(this.iconFile);
            if (bl) {
                try {
                    InputStream inputStream = Files.newInputStream(this.iconFile);

                    try {
                        this.icon.upload(NativeImage.read(inputStream));
                    } catch (Throwable var6) {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (Throwable var5) {
                                var6.addSuppressed(var5);
                            }
                        }

                        throw var6;
                    }

                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Throwable var7) {
                    WorldSelectionListSD.LOGGER.error("Invalid icon for world {}", this.summary.getLevelId(), var7);
                    this.iconFile = null;
                }
            } else {
                this.icon.clear();
            }
        }

        @Override
        public void close() {
            if (!this.icon.isClosed()) {
                this.icon.close();
            }
        }

        public String getLevelName() {
            return this.summary.getLevelName();
        }

        @Override
        public LevelSummary getLevelSummary() {
            return this.summary;
        }
    }
}

