package pantz.mod.client.renderer.be;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import pantz.mod.common.block.entity.EntityDetectorBlockEntity;

import java.util.HashMap;
import java.util.Map;

public class EntityDetectorRenderer implements BlockEntityRenderer<EntityDetectorBlockEntity> {
    private final EntityRenderDispatcher dispatcher;
    private final Map<ResourceLocation, Entity> entityCache = new HashMap<>();

    public EntityDetectorRenderer(BlockEntityRendererProvider.Context ctx) {
        this.dispatcher = ctx.getEntityRenderer();
    }

    @Override
    public void render(EntityDetectorBlockEntity be, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight, int packedOverlay) {
        EntityType<?> type = be.getSlideshowEntityType();
        if (type == null) return;

        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        double clientTick = level.getGameTime() + partialTicks;
        float rotDegrees = (float) ((clientTick * 2f) % 360.0);

        ResourceLocation key = EntityType.getKey(type);
        Entity entity = entityCache.computeIfAbsent(key, k -> type.create(level));
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
        poseStack.scale(0.4f, 0.4f, 0.4f);

        dispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, poseStack, buffer, packedLight);

        poseStack.popPose();
    }
}