package pantz.mod.client.renderer.be;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import pantz.mod.common.block.ItemStandBlock;
import pantz.mod.common.block.entity.ItemStandBlockEntity;
import pantz.mod.core.registry.PMBlocks;

public class ItemStandRenderer implements BlockEntityRenderer<ItemStandBlockEntity> {
    private final ItemRenderer itemRenderer;
    public ItemStandRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(ItemStandBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        ItemStack item = be.getItem();
        if (item.isEmpty()) return;

        boolean isBlock = item.getItem() instanceof BlockItem;

        float yOffset = isBlock ? 0.21875f : 0.1875f;
        float zOffset = isBlock ? 0.2f : 0.1f;
        float scale = isBlock ? 0.7f : 0.65f;

        poseStack.pushPose();

        poseStack.translate(0.5, yOffset, 0.5);

        float yRot = be.getBlockState().getValue(ItemStandBlock.FACING).toYRot();
        poseStack.mulPose(Axis.YP.rotationDegrees(-yRot));

        poseStack.translate(0.0, 0.0, -zOffset);
        poseStack.mulPose(Axis.XP.rotationDegrees(75f));
        poseStack.scale(scale, scale, scale);

        int renderLight = be.getBlockState().is(PMBlocks.GLOW_ITEM_STAND.get()) ? 0xF000F0 : light;

        itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, renderLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, be.getLevel(), (int) be.getBlockPos().asLong());

        poseStack.popPose();
    }

}
