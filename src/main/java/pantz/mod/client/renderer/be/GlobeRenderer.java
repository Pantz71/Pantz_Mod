package pantz.mod.client.renderer.be;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import pantz.mod.client.model.block.*;
import pantz.mod.common.block.GlobeBlock;
import pantz.mod.common.block.entity.GlobeBlockEntity;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.PMModelLayers;

import java.util.HashMap;
import java.util.Map;

public class GlobeRenderer implements BlockEntityRenderer<GlobeBlockEntity> {
    private final Map<String, Model> modelCache = new HashMap<>();

    public GlobeRenderer(BlockEntityRendererProvider.Context ctx) {
        modelCache.put("default", new GlobeModel(ctx.bakeLayer(PMModelLayers.GLOBE)));
        modelCache.put("small", new SmallGlobeModel(ctx.bakeLayer(PMModelLayers.SMALL_GLOBE)));
        modelCache.put("tiny", new TinyGlobeModel(ctx.bakeLayer(PMModelLayers.TINY_GLOBE)));
        modelCache.put("saturn", new SaturnGlobeModel(ctx.bakeLayer(PMModelLayers.SATURN_GLOBE)));
        modelCache.put("large", new LargeGlobeModel(ctx.bakeLayer(PMModelLayers.LARGE_GLOBE)));
        modelCache.put("giant", new GiantGlobeModel(ctx.bakeLayer(PMModelLayers.GIANT_GLOBE)));
        modelCache.put("uranus", new UranusGlobeModel(ctx.bakeLayer(PMModelLayers.URANUS_GLOBE)));
    }

    @Override
    public void render(GlobeBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        ResourceLocation texture = be.getRenderTexture();
        Model model = getModelForLocation(be.getTexture());

        if (model == null) return;

        poseStack.pushPose();

        Direction facing = be.getBlockState().getValue(GlobeBlock.FACING);
        rotateDirection(poseStack, facing);

        float rot = be.getRotation(partialTicks);
        poseStack.mulPose(Axis.YP.rotationDegrees(rot));

        boolean glow = be.isGlow();
        int renderLight = glow ? 0xF000F0 : light;
        RenderType type = glow ? RenderType.entityTranslucent(texture) : RenderType.entityCutout(texture);

        model.renderToBuffer(poseStack, buffer.getBuffer(type), renderLight, overlay, 1.0f, 1.0f, 1.0f, 1.0f);

        poseStack.popPose();
    }

    private void rotateDirection(PoseStack poseStack, Direction facing) {
        poseStack.translate(0.5, 0.0625, 0.5);
        switch (facing) {
            case NORTH -> {
                poseStack.translate(0, 0, 0.0625);
                poseStack.mulPose(Axis.YP.rotationDegrees(180f));
            }
            case SOUTH -> poseStack.translate(0, 0, -0.0625);
            case WEST -> {
                poseStack.translate(0.0625, 0, 0);
                poseStack.mulPose(Axis.YP.rotationDegrees(90f));
            }
            case EAST -> {
                poseStack.translate(-0.0625, 0, 0);
                poseStack.mulPose(Axis.YP.rotationDegrees(270f));
            }
        }
    }

    private Model getModelForLocation(ResourceLocation loc) {
        String path = loc.getPath();

        if (path.endsWith("/uranus")) return modelCache.get("uranus");
        if (path.endsWith("/saturn")) return modelCache.get("saturn");
        if (path.startsWith("stars/")) return modelCache.get("giant");
        if (path.startsWith("large_planets/")) return modelCache.get("large");
        if (path.startsWith("moons/")) return modelCache.get("small");
        if (path.startsWith("dwarf_planets/")) return modelCache.get("tiny");

        return modelCache.get("default");
    }

    @Override
    public boolean shouldRenderOffScreen(GlobeBlockEntity blockEntity) {
        return true;
    }
}
