package pantz.mod.client.renderer.be;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import pantz.mod.common.block.entity.EntityDetectorBlockEntity;
import pantz.mod.common.utils.FilterMode;
import pantz.mod.common.utils.TintedVertexConsumer;

import java.util.HashMap;
import java.util.Map;

public class EntityDetectorRenderer implements BlockEntityRenderer<EntityDetectorBlockEntity> {
    private final EntityRenderDispatcher dispatcher;
    private final Map<ResourceLocation, Entity> entityCache = new HashMap<>();

    public EntityDetectorRenderer(BlockEntityRendererProvider.Context ctx) {
        this.dispatcher = ctx.getEntityRenderer();
    }

    @Override
    public void render(EntityDetectorBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        EntityType<?> type = be.getSlideshowEntities();
        if (type == null) return;

        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        double clientTick = level.getGameTime() + partialTicks;
        float rotDegrees = (float) ((clientTick * 2f) % 360.0);

        ResourceLocation key = EntityType.getKey(type);
        Entity entity = this.entityCache.computeIfAbsent(key, id -> type.create(level));

        if (entity == null) return;

        entity.setYRot(rotDegrees);
        entity.yRotO = rotDegrees;
        if (entity instanceof LivingEntity living) {
            living.yBodyRot = rotDegrees;
            living.yBodyRotO = rotDegrees;
            living.setYHeadRot(rotDegrees);
            living.yHeadRotO = rotDegrees;
        }

        poseStack.pushPose();
        poseStack.translate(0.5d, 0.5d, 0.5d);

        float scale = 0.4f;
        float maxDimension = Math.max(entity.getBbWidth(), entity.getBbHeight());
        if (maxDimension > 1.5) scale = 0.2f;
        if (maxDimension > 3.0) scale = 0.1f;
        poseStack.scale(scale, scale, scale);

        if (be.getFilterMode() == FilterMode.EXCLUDE) {
            MultiBufferSource redTint = renderType -> {
                VertexConsumer original = buffer.getBuffer(renderType);
                return new TintedVertexConsumer(original, 255, 100, 100, 255);
            };
            this.dispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, poseStack, redTint, packedLight);
        } else {
            MultiBufferSource greenTint = renderType -> {
                VertexConsumer original = buffer.getBuffer(renderType);
                return new TintedVertexConsumer(original, 39, 186, 39, 255);
            };
            this.dispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, poseStack, greenTint, packedLight);
        }
        poseStack.popPose();
    }

}