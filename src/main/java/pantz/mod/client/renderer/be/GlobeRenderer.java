package pantz.mod.client.renderer.be;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import pantz.mod.client.model.block.*;
import pantz.mod.common.block.GlobeBlock;
import pantz.mod.common.block.entity.GlobeBlockEntity;
import pantz.mod.core.other.PMModelLayers;

public class GlobeRenderer implements BlockEntityRenderer<GlobeBlockEntity> {
    private final GlobeModel globeModel;
    private final SmallGlobeModel smallGlobeModel;
    private final TinyGlobeModel tinyGlobeModel;
    private final SaturnGlobeModel saturnGlobeModel;
    private final LargeGlobeModel largeGlobeModel;
    private final GiantGlobeModel giantGlobeModel;
    private final UranusGlobeModel uranusGlobeModel;

    public GlobeRenderer(BlockEntityRendererProvider.Context ctx) {
        this.globeModel = new GlobeModel(ctx.bakeLayer(PMModelLayers.GLOBE));
        this.smallGlobeModel = new SmallGlobeModel(ctx.bakeLayer(PMModelLayers.SMALL_GLOBE));
        this.tinyGlobeModel = new TinyGlobeModel(ctx.bakeLayer(PMModelLayers.TINY_GLOBE));
        this.saturnGlobeModel = new SaturnGlobeModel(ctx.bakeLayer(PMModelLayers.SATURN_GLOBE));
        this.largeGlobeModel = new LargeGlobeModel(ctx.bakeLayer(PMModelLayers.LARGE_GLOBE));
        this.giantGlobeModel = new GiantGlobeModel(ctx.bakeLayer(PMModelLayers.GIANT_GLOBE));
        this.uranusGlobeModel = new UranusGlobeModel(ctx.bakeLayer(PMModelLayers.URANUS_GLOBE));
    }

    @Override
    public void render(GlobeBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        poseStack.pushPose();
        ResourceLocation textureLoc = be.getTexture();

        Object modelToUse = getGlobeModel(textureLoc);

        switch (be.getBlockState().getValue(GlobeBlock.FACING)) {
            case NORTH -> {
                poseStack.translate(0.5, 0.0625, 0.5625);
                poseStack.mulPose(Axis.YP.rotationDegrees(180f));
            }
            case SOUTH -> poseStack.translate(0.5, 0.0625, 0.4375);
            case WEST -> {
                poseStack.translate(0.5625, 0.0625, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(90f));
            }
            case EAST -> {
                poseStack.translate(0.4375, 0.0625, 0.5);
                poseStack.mulPose(Axis.YN.rotationDegrees(90f));
            }
        }
        float rot = be.getRotation(partialTicks);
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));
        ResourceLocation texture = new ResourceLocation(textureLoc.getNamespace(), "textures/block/globe/" + textureLoc.getPath() + ".png");

        boolean glow = be.isGlow();
        int renderLight = glow ? 0xF000F0 : light;
        RenderType renderType = glow
                ? RenderType.entityTranslucent(texture)
                : RenderType.entityCutout(texture);

        VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
        if (modelToUse instanceof GlobeModel model) {
            model.renderToBuffer(poseStack, vertexConsumer, renderLight, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
        }

        if (modelToUse instanceof SmallGlobeModel model) {
            model.renderToBuffer(poseStack, vertexConsumer, renderLight, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
        }

        if (modelToUse instanceof TinyGlobeModel model) {
            model.renderToBuffer(poseStack, vertexConsumer, renderLight, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
        }

        if (modelToUse instanceof SaturnGlobeModel model) {
            model.renderToBuffer(poseStack, vertexConsumer, renderLight, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
        }

        if (modelToUse instanceof LargeGlobeModel model) {
            model.renderToBuffer(poseStack, vertexConsumer, renderLight, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
        }

        if (modelToUse instanceof GiantGlobeModel model) {
            model.renderToBuffer(poseStack, vertexConsumer, renderLight, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
        }

        if (modelToUse instanceof UranusGlobeModel model) {
            model.renderToBuffer(poseStack, vertexConsumer, renderLight, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
        }
        poseStack.popPose();


    }

    private Object getGlobeModel(ResourceLocation textureLoc) {
        Object modelToUse;
        if (textureLoc.getPath().endsWith("/uranus")) {
            modelToUse = uranusGlobeModel;
        } else if (textureLoc.getPath().endsWith("/saturn")) {
            modelToUse = saturnGlobeModel;
        } else if (textureLoc.getPath().startsWith("stars/")) {
            modelToUse = giantGlobeModel;
        } else if (textureLoc.getPath().startsWith("large_planets/") && !(textureLoc.getPath().endsWith("/uranus") || textureLoc.getPath().endsWith("/saturn"))) {
            modelToUse = largeGlobeModel;
        } else if (textureLoc.getPath().startsWith("planets/")) {
            modelToUse = globeModel;
        } else if (textureLoc.getPath().startsWith("moons/")) {
            modelToUse = smallGlobeModel;
        } else if (textureLoc.getPath().startsWith("dwarf_planets/")) {
            modelToUse = tinyGlobeModel;
        } else {
            modelToUse = globeModel;
        }
        return modelToUse;
    }

    @Override
    public boolean shouldRenderOffScreen(GlobeBlockEntity blockEntity) {
        return true;
    }
}
