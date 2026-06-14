package net.meander.subtlyd.client.renderer;

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
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.entity.player.PlayerSkin;

public class GuiPlayerRenderer {
    public static void renderPlayer(GuiGraphicsExtractor guiGraphics, int x, int y, int scale, float mouseX, float mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        Options options = minecraft.options;
        PlayerSkin skin = minecraft.getSkinManager().createLookup(minecraft.getGameProfile(), false).get();
        EntityRenderDispatcher dispatcher = minecraft.getEntityRenderDispatcher();
        AvatarRenderer<AbstractClientPlayer> playerRenderer = dispatcher.playerRenderers.get(skin.model());

        if (playerRenderer != null) {
            int scaleModifier = scale * 2;
            int x0 = x - scaleModifier / 3;
            int y0 = y - scaleModifier;
            int x1 = x + scaleModifier / 3;
            int y1 = y + (scaleModifier / 10);
            float yaw = (float) Math.atan((x - mouseX) / 40.0F);
            float pitch = (float) Math.atan((y - 35 - mouseY) / 40.0F);
            PlayerModel model = playerRenderer.getModel();
            Model.Simple simple = new Model.Simple(model.root(), model.renderType());
            AvatarRenderState state = new AvatarRenderState();

            state.skin = skin;
            state.capeFlap = 1F;
            state.capeLean = 0.5F;
            state.capeLean2 = 0.5F;
            state.isCrouching = false;
            state.showHat = options.isModelPartEnabled(PlayerModelPart.HAT);
            state.showJacket = options.isModelPartEnabled(PlayerModelPart.JACKET);
            state.showLeftSleeve = options.isModelPartEnabled(PlayerModelPart.LEFT_SLEEVE);
            state.showRightSleeve = options.isModelPartEnabled(PlayerModelPart.RIGHT_SLEEVE);
            state.showLeftPants = options.isModelPartEnabled(PlayerModelPart.LEFT_PANTS_LEG);
            state.showRightPants = options.isModelPartEnabled(PlayerModelPart.RIGHT_PANTS_LEG);
            state.showCape = options.isModelPartEnabled(PlayerModelPart.CAPE);
            state.yRot = yaw * 20F;
            state.xRot = -pitch * 5F;
            state.bodyRot = -state.yRot * 0.5F;

            model.setupAnim(state);

            if (state.showCape && skin.cape() != null) {
                PlayerCapeModel capeModel = new PlayerCapeModel(minecraft.getEntityModels().bakeLayer(ModelLayers.PLAYER_CAPE));
                Model.Simple capeSimple = new Model.Simple(capeModel.root(), capeModel.renderType());
                capeModel.setupAnim(state);

                guiGraphics.skin(capeSimple, skin.cape().texturePath(), (float) scale, 0, state.bodyRot, 0, x0, y0, x1, y1);
            }

            guiGraphics.skin(simple, skin.body().texturePath(), (float) scale, 0, state.bodyRot, 0.0F, x0, y0, x1, y1);
        }
    }
}
