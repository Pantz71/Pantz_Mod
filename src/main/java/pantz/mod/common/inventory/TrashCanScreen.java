package pantz.mod.common.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import pantz.mod.common.utils.CustomTextureButton;
import pantz.mod.core.PantzMod;

public class TrashCanScreen extends AbstractContainerScreen<TrashCanMenu> {
    private static final ResourceLocation TEXTURE = PantzMod.location("textures/gui/container/trash_can.png");
    private static final ResourceLocation BUTTON = PantzMod.location("textures/gui/container/destroy.png");
    private static final ResourceLocation BUTTON_HIGHLIGHTED = PantzMod.location("textures/gui/container/destroy_highlighted.png");

    public TrashCanScreen(TrashCanMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();

        int buttonX = this.leftPos + 174;
        int buttonY = this.topPos + 4;
        int buttonSize = 18;
        CustomTextureButton trashButton = new CustomTextureButton(buttonX, buttonY, buttonSize, buttonSize, BUTTON, BUTTON_HIGHLIGHTED, button -> {
            if (this.minecraft != null && this.minecraft.gameMode != null) {
                this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, 0);
                this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
        });
        trashButton.setTooltip(Tooltip.create(Component.translatable("tooltip.pantz_mod.trash")));

        this.addRenderableWidget(trashButton);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int width = 196;
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, width, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
