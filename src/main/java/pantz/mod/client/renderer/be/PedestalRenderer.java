package pantz.mod.client.renderer.be;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import pantz.mod.common.block.PedestalBlock;
import pantz.mod.common.block.entity.PedestalBlockEntity;
import pantz.mod.core.other.tags.PMItemTags;

public class PedestalRenderer implements BlockEntityRenderer<PedestalBlockEntity> {
    private static final float SPIN_SPEED = 2f;
    private final ItemRenderer itemRenderer;

    public PedestalRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(PedestalBlockEntity be, float partialTicks, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        ItemStack item = be.getItem();

        if (item.isEmpty()) return;

        boolean isTool = item.is(PMItemTags.TOOLS);
        boolean isWeapon = item.is(PMItemTags.WEAPONS);
        boolean spinning = be.isSpinning();

        float yOffset;

        if (spinning) {
            yOffset = 1.3125f;
        } else if (isWeapon) {
            yOffset = 1.25f;
        } else if (isTool) {
            yOffset = 1.375f;
        } else {
            yOffset = 1.3125f;
        }

        stack.pushPose();

        stack.translate(0.5f, yOffset, 0.5f);
        stack.scale(0.5f, 0.5f, 0.5f);
        if (spinning) {
            float spin = (Util.getMillis() / 20f) % 360f;
            stack.mulPose(Axis.YP.rotationDegrees(spin * SPIN_SPEED));
        } else {
            switch (be.getBlockState().getValue(PedestalBlock.FACING)) {
                case SOUTH -> stack.mulPose(Axis.YN.rotationDegrees(180));
                case EAST -> stack.mulPose(Axis.YN.rotationDegrees(90));
                case WEST -> stack.mulPose(Axis.YP.rotationDegrees(90));
                default -> {}
            }
            if (isWeapon) {
                stack.mulPose(Axis.ZP.rotationDegrees(135));
                stack.scale(2, 2, 2);
            }
        }

        this.itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, light, overlay, stack, buffer, be.getLevel(), (int)be.getBlockPos().asLong());
        stack.popPose();
    }
}
