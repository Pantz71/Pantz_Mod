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

        poseStack.pushPose();
        float yOffset;
        if (item.getItem() instanceof BlockItem) {
            yOffset = 0.21875f;
        } else {
            yOffset = 0.1875f;
        }

        poseStack.translate(0.5, yOffset, 0.5);
        float yRot = switch (be.getBlockState().getValue(ItemStandBlock.FACING)) {
            case SOUTH -> 180f;
            case WEST -> 90f;
            case EAST -> -90f;
            default -> 0f;
        };
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));

        float zOffset;
        if (item.getItem() instanceof BlockItem) {
            zOffset = 0.2f;
        } else {
            zOffset = 0.1f;
        }
        poseStack.translate(0.0, 0.0, -zOffset);
        poseStack.mulPose(Axis.XP.rotationDegrees(75));

        float size;
        if (item.getItem() instanceof BlockItem) {
            size = 0.7f;
        } else {
            size = 0.65f;
        }

        poseStack.scale(size, size, size);

        itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, be.getBlockState().is(PMBlocks.GLOW_ITEM_STAND.get()) ? 0xf000f0 : light, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, be.getLevel(), (int) be.getBlockPos().asLong());

        poseStack.popPose();
    }

}
