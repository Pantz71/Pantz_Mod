package pantz.mod.common.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CustomTextureButton extends Button {
    private final ResourceLocation normalTexture;
    private final ResourceLocation hoverTexture;

    public CustomTextureButton(int x, int y, int width, int height, ResourceLocation normalTexture, ResourceLocation hoverTexture, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, Button.DEFAULT_NARRATION);
        this.normalTexture = normalTexture;
        this.hoverTexture = hoverTexture;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mX, int mY, float partialTick) {
        ResourceLocation texture = this.isHovered() ? this.hoverTexture : this.normalTexture;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(texture, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);
        RenderSystem.disableBlend();
    }
}
