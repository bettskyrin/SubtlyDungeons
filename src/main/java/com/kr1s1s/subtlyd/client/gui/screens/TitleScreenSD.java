package com.kr1s1s.subtlyd.client.gui.screens;

import com.mojang.authlib.minecraft.BanDetails;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.CreditsAndAttributionScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.options.LanguageSelectScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Objects;

public class TitleScreenSD extends Screen {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Component TITLE = Component.translatable("narrator.screen.title");
    private static final Component COPYRIGHT_TEXT = Component.translatable("title.credits");
    private static final String DEMO_LEVEL_ID = "Demo_World";
    @Nullable
    private SplashRenderer splash;
    @Nullable
    private RealmsNotificationsScreen realmsNotificationsScreen;
    private boolean fading;
    private long fadeInStart;
    private final LogoRenderer logoRenderer;

    public TitleScreenSD() {
        this(false);
    }

    public TitleScreenSD(boolean bl) {
        this(bl, null);
    }

    public TitleScreenSD(boolean bl, @Nullable LogoRenderer logoRenderer) {
        super(TITLE);
        this.fading = bl;
        this.logoRenderer = Objects.requireNonNullElseGet(logoRenderer, () -> new LogoRenderer(false));
    }

    private boolean realmsNotificationsEnabled() {
        return this.realmsNotificationsScreen != null;
    }

    @Override
    public void tick() {
        if (this.realmsNotificationsEnabled()) {
            this.realmsNotificationsScreen.tick();
        }
    }

    public static void registerTextures(TextureManager textureManager) {
        textureManager.registerForNextReload(LogoRenderer.MINECRAFT_LOGO);
        textureManager.registerForNextReload(LogoRenderer.MINECRAFT_EDITION);
        textureManager.registerForNextReload(PanoramaRenderer.PANORAMA_OVERLAY);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void init() {
        if (this.splash == null) {
            this.splash = this.minecraft.getSplashManager().getSplash();
        }

        int copyrightSpacing = this.font.width(COPYRIGHT_TEXT);
        int copyrightHeight = 10;
        int copyrightXPos = this.width - copyrightSpacing - 2;
        int buttonSpacing = 24;
        int bottomYPos = this.height / 4 + 48;
        int largeButtonWidth = 200;
        int mediumButtonWidth = 98;
        int buttonHeight = 20;
        int largeButtonXPos = this.width / 2 - 100;
        int spriteIconButtonsXPos = this.width / 2 - 124;

        if (this.minecraft.isDemo()) {
            bottomYPos = this.createDemoMenuOptions(largeButtonXPos, bottomYPos, buttonSpacing, largeButtonWidth, buttonHeight);
        } else {
            bottomYPos = this.createNormalMenuOptions(largeButtonXPos, bottomYPos, buttonSpacing, largeButtonWidth, buttonHeight);
        }

        bottomYPos = this.createTestWorldButton(largeButtonXPos, bottomYPos, buttonSpacing, largeButtonWidth, buttonHeight);
        SpriteIconButton spriteIconButton = this.addRenderableWidget(
                CommonButtons.language(
                        buttonHeight, button -> this.minecraft.setScreen(new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager())), true
                )
        );
        bottomYPos += 36;
        spriteIconButton.setPosition(spriteIconButtonsXPos, bottomYPos);
        this.addRenderableWidget(
                Button.builder(Component.translatable("menu.options"), button -> this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options)))
                        .bounds(largeButtonXPos, bottomYPos, mediumButtonWidth, buttonHeight)
                        .build()
        );
        this.addRenderableWidget(Button.builder(Component.translatable("menu.quit"), button -> this.minecraft.stop()).bounds(this.width / 2 + 2, bottomYPos, mediumButtonWidth, buttonHeight).build());
        SpriteIconButton spriteIconButton2 = this.addRenderableWidget(
                CommonButtons.accessibility(buttonHeight, button -> this.minecraft.setScreen(new AccessibilityOptionsScreen(this, this.minecraft.options)), true)
        );
        spriteIconButton2.setPosition(spriteIconButtonsXPos + 228, bottomYPos);
        this.addRenderableWidget(
                new PlainTextButton(copyrightXPos, this.height - copyrightHeight, copyrightSpacing, copyrightHeight, COPYRIGHT_TEXT, button -> this.minecraft.setScreen(new CreditsAndAttributionScreen(this)), this.font)
        );
        if (this.realmsNotificationsScreen == null) {
            this.realmsNotificationsScreen = new RealmsNotificationsScreen();
        }

        if (this.realmsNotificationsEnabled()) {
            this.realmsNotificationsScreen.init(this.width, this.height);
        }
    }

    private int createTestWorldButton(int buttonXPos, int bottomYPos, int spacing, int width, int height) {
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            this.addRenderableWidget(
                    Button.builder(Component.literal("Create Test World"), button -> CreateWorldScreen.testWorld(this.minecraft, () -> this.minecraft.setScreen(this)))
                            .bounds(buttonXPos, bottomYPos += spacing, width, height)
                            .build()
            );
        }

        return bottomYPos;
    }

    private int createNormalMenuOptions(int buttonXPos, int buttonYPos, int spacing, int width, int height) {
        this.addRenderableWidget(
                Button.builder(Component.translatable("menu.singleplayer"), button -> this.minecraft.setScreen(new SelectWorldScreen(this)))
                        .bounds(buttonXPos, buttonYPos, width, height)
                        .build()
        );
        Component component = this.getMultiplayerDisabledReason();
        boolean bl = component == null;
        Tooltip tooltip = component != null ? Tooltip.create(component) : null;
        this.addRenderableWidget(Button.builder(Component.translatable("menu.multiplayer"), button -> {
            Screen screen = this.minecraft.options.skipMultiplayerWarning ? new JoinMultiplayerScreen(this) : new SafetyScreen(this);
            this.minecraft.setScreen(screen);
        }).bounds(buttonXPos, buttonYPos += spacing, width, height).tooltip(tooltip).build()).active = bl;

        this.addRenderableWidget(
                Button.builder(Component.translatable("menu.hub"), button -> this.minecraft.setScreen(new SelectWorldScreen(this)))
                        .bounds(buttonXPos, buttonYPos += spacing, width, height)
                        .build()
        );
//        this.addRenderableWidget(
//                Button.builder(Component.translatable("menu.online"), button -> this.minecraft.setScreen(new RealmsMainScreen(this)))
//                        .bounds(this.width / 2 - 100, i = var6 + spacing, 200, 20)
//                        .tooltip(tooltip)
//                        .build()
//        )
//                .active = bl;
        return buttonYPos;
    }

    @Nullable
    private Component getMultiplayerDisabledReason() {
        if (this.minecraft.allowsMultiplayer()) {
            return null;
        } else if (this.minecraft.isNameBanned()) {
            return Component.translatable("title.multiplayer.disabled.banned.name");
        } else {
            BanDetails banDetails = this.minecraft.multiplayerBan();
            if (banDetails != null) {
                return banDetails.expires() != null
                        ? Component.translatable("title.multiplayer.disabled.banned.temporary")
                        : Component.translatable("title.multiplayer.disabled.banned.permanent");
            } else {
                return Component.translatable("title.multiplayer.disabled");
            }
        }
    }

    private int createDemoMenuOptions(int xPos, int yPos, int spacing, int width, int height) {
        boolean bl = this.checkDemoWorldPresence();
        this.addRenderableWidget(
                Button.builder(
                                Component.translatable("menu.playdemo"),
                                buttonx -> {
                                    if (bl) {
                                        this.minecraft.createWorldOpenFlows().openWorld(DEMO_LEVEL_ID, () -> this.minecraft.setScreen(this));
                                    } else {
                                        this.minecraft
                                                .createWorldOpenFlows()
                                                .createFreshLevel(DEMO_LEVEL_ID, MinecraftServer.DEMO_SETTINGS, WorldOptions.DEMO_OPTIONS, WorldPresets::createNormalWorldDimensions, this);
                                    }
                                }
                        )
                        .bounds(xPos, yPos, width, height)
                        .build()
        );
        Button button = this.addRenderableWidget(
                Button.builder(
                                Component.translatable("menu.resetdemo"),
                                buttonx -> {
                                    LevelStorageSource levelStorageSource = this.minecraft.getLevelSource();

                                    try (LevelStorageSource.LevelStorageAccess levelStorageAccess = levelStorageSource.createAccess(DEMO_LEVEL_ID)) {
                                        if (levelStorageAccess.hasWorldData()) {
                                            this.minecraft
                                                    .setScreen(
                                                            new ConfirmScreen(
                                                                    this::confirmDemo,
                                                                    Component.translatable("selectWorld.deleteQuestion"),
                                                                    Component.translatable("selectWorld.deleteWarning", MinecraftServer.DEMO_SETTINGS.levelName()),
                                                                    Component.translatable("selectWorld.deleteButton"),
                                                                    CommonComponents.GUI_CANCEL
                                                            )
                                                    );
                                        }
                                    } catch (IOException var8) {
                                        SystemToast.onWorldAccessFailure(this.minecraft, DEMO_LEVEL_ID);
                                        LOGGER.warn("Failed to access demo world", var8);
                                    }
                                }
                        )
                        .bounds(xPos, yPos += spacing, width, height)
                        .build()
        );
        button.active = bl;
        return yPos;
    }

    private boolean checkDemoWorldPresence() {
        try {
            boolean var2;
            try (LevelStorageSource.LevelStorageAccess levelStorageAccess = this.minecraft.getLevelSource().createAccess(DEMO_LEVEL_ID)) {
                var2 = levelStorageAccess.hasWorldData();
            }

            return var2;
        } catch (IOException var6) {
            SystemToast.onWorldAccessFailure(this.minecraft, DEMO_LEVEL_ID);
            LOGGER.warn("Failed to read demo world data", var6);
            return false;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        if (this.fadeInStart == 0L && this.fading) {
            this.fadeInStart = Util.getMillis();
        }

        float g = 1.0F;
        if (this.fading) {
            float h = (float)(Util.getMillis() - this.fadeInStart) / 2000.0F;
            if (h > 1.0F) {
                this.fading = false;
            } else {
                h = Mth.clamp(h, 0.0F, 1.0F);
                g = Mth.clampedMap(h, 0.5F, 1.0F, 0.0F, 1.0F);
            }

            this.fadeWidgets(g);
        }

        this.renderPanorama(guiGraphics, f);
        super.render(guiGraphics, i, j, f);
        this.logoRenderer.renderLogo(guiGraphics, this.width, this.logoRenderer.keepLogoThroughFade() ? 1.0F : g);
        if (this.splash != null && !this.minecraft.options.hideSplashTexts().get()) {
            this.splash.render(guiGraphics, this.width, this.font, g);
        }

        String string = "Minecraft " + SharedConstants.getCurrentVersion().name();
        if (this.minecraft.isDemo()) {
            string = string + " Demo";
        } else {
            string = string + ("release".equalsIgnoreCase(this.minecraft.getVersionType()) ? "" : "/" + this.minecraft.getVersionType());
        }

        if (Minecraft.checkModStatus().shouldReportAsModified()) {
            string = string + I18n.get("menu.modded");
        }

        guiGraphics.drawString(this.font, string, 2, this.height - 10, ARGB.white(g));
        if (this.realmsNotificationsEnabled() && g >= 1.0F) {
            this.realmsNotificationsScreen.render(guiGraphics, i, j, f);
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f) {
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
        return super.mouseClicked(mouseButtonEvent, bl) || this.realmsNotificationsEnabled() && this.realmsNotificationsScreen.mouseClicked(mouseButtonEvent, bl);
    }

    @Override
    public void removed() {
        if (this.realmsNotificationsScreen != null) {
            this.realmsNotificationsScreen.removed();
        }
    }

    @Override
    public void added() {
        super.added();
        if (this.realmsNotificationsScreen != null) {
            this.realmsNotificationsScreen.added();
        }
    }

    private void confirmDemo(boolean bl) {
        if (bl) {
            try (LevelStorageSource.LevelStorageAccess levelStorageAccess = this.minecraft.getLevelSource().createAccess(DEMO_LEVEL_ID)) {
                levelStorageAccess.deleteLevel();
            } catch (IOException var7) {
                SystemToast.onWorldDeleteFailure(this.minecraft, DEMO_LEVEL_ID);
                LOGGER.warn("Failed to delete demo world", var7);
            }
        }

        this.minecraft.setScreen(this);
    }

    @Override
    public boolean canInterruptWithAnotherScreen() {
        return true;
    }
}
