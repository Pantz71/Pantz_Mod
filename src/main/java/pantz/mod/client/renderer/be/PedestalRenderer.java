package pantz.mod.client.renderer.be;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.tags.ItemTags;
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

        boolean isTool = item.is(ItemTags.TOOLS);
        boolean isWeapon = item.is(PMItemTags.WEAPONS);
        boolean spinning = be.isSpinning();

        float yOffset;

        if (be.isSpinning()) {
            yOffset = 1.1875f;
        } else if (isTool) {
            yOffset = 1.25f;
        } else {
            yOffset = 1.125f;
        }

        stack.pushPose();
        stack.translate(0.5, yOffset, 0.5);

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
                stack.scale(0.75f, 0.75f, 0.75f);
            }
        }

        this.itemRenderer.renderStatic(item, ItemDisplayContext.GROUND, light, overlay, stack, buffer, be.getLevel(), (int)be.getBlockPos().asLong());
        stack.popPose();
    }
}
