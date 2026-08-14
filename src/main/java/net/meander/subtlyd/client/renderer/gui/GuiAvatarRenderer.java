package net.meander.subtlyd.client.renderer.gui;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerCapeModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.core.ClientAsset;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.entity.player.PlayerSkin;

/**
 * @see AvatarRenderer
 */
public class GuiAvatarRenderer {
    public final PlayerSkin skin;
    private final Options options = Minecraft.getInstance().options;

    public GuiAvatarRenderer() {
        this(Minecraft.getInstance().getGameProfile());
    }

    public GuiAvatarRenderer(GameProfile profile) {
        skin = Minecraft.getInstance().getSkinManager().createLookup(profile, false).get();
    }

    public void extractCapeRenderState(final GuiGraphicsExtractor guiGraphics, final AvatarRenderState state, final int scale, int x0, int y0, int x1, int y1) {
        if (state.showCape) {
            ClientAsset.Texture cape = state.skin.cape();

            if (cape != null) {
                state.capeFlap = 1.0F;
                state.capeLean = 0.5F;
                state.capeLean2 = 0.5F;

                PlayerCapeModel capeModel = new PlayerCapeModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_CAPE));
                Model.Simple capeSimple = new Model.Simple(capeModel.root(), capeModel.renderType());

                capeModel.setupAnim(state);
                guiGraphics.skin(capeSimple, cape.texturePath(), (float) scale, 0, state.bodyRot, 0, x0, y0, x1, y1);
            }
        }
    }

    public void setSkin(final AvatarRenderState state) {
        state.isCrouching = false;
        state.skin = skin;
        state.showHat = options.isModelPartEnabled(PlayerModelPart.HAT);
        state.showJacket = options.isModelPartEnabled(PlayerModelPart.JACKET);
        state.showLeftSleeve = options.isModelPartEnabled(PlayerModelPart.LEFT_SLEEVE);
        state.showRightSleeve = options.isModelPartEnabled(PlayerModelPart.RIGHT_SLEEVE);
        state.showLeftPants = options.isModelPartEnabled(PlayerModelPart.LEFT_PANTS_LEG);
        state.showRightPants = options.isModelPartEnabled(PlayerModelPart.RIGHT_PANTS_LEG);
        state.showCape = options.isModelPartEnabled(PlayerModelPart.CAPE);
    }

    public void followCursor(final AvatarRenderState state, final int x, final int y, final float mouseX, final float mouseY) {
        float yaw = (float) Mth.atan2(x - mouseX, 40.0);
        float pitch = (float) Mth.atan2(y - 35.0 - mouseY, 40.0);

        state.yRot = yaw * 20.0F;
        state.xRot = -pitch * 5.0F;
        state.bodyRot = -state.yRot * 0.5F;
    }

    public void extractRenderState(final GuiGraphicsExtractor guiGraphics, final int x, final int y, final int entityScale, final float mouseX, final float mouseY) {
        AvatarRenderState state = new AvatarRenderState();
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        AvatarRenderer<AbstractClientPlayer> playerRenderer = dispatcher.playerRenderers.get(state.skin.model());

        if (playerRenderer != null) {
            int scaleModifier = entityScale * 2;
            int x0 = x - scaleModifier / 3;
            int y0 = y - scaleModifier;
            int x1 = x + scaleModifier / 3;
            int y1 = y + scaleModifier / 10;

            PlayerModel model = playerRenderer.getModel();
            Model.Simple playerModel = new Model.Simple(model.root(), model.renderType());

            setSkin(state);
            followCursor(state, x, y, mouseX, mouseY);
            model.setupAnim(state);
            extractCapeRenderState(guiGraphics, state, entityScale, x0, y0, x1, y1);
            guiGraphics.skin(playerModel, state.skin.body().texturePath(), (float) entityScale, 0.0F, state.bodyRot, 0.0F, x0, y0, x1, y1);
        }
    }
}
