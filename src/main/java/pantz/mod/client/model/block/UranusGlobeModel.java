package pantz.mod.client.model.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class UranusGlobeModel extends Model {
    private final ModelPart globe;

    public UranusGlobeModel(ModelPart root) {
        super(RenderType::entityTranslucentEmissive);
        this.globe = root.getChild("root");
    }

    public static LayerDefinition createModel() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition globe = root.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        globe.addOrReplaceChild("ring", CubeListBuilder.create().texOffs(0, 24).addBox(-10.0F, 0.0F, -10.0F, 20.0F, 1.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 1.1781F, (float) Math.PI, 0.0F));

        globe.addOrReplaceChild("globe", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, (float) Math.PI, (float) Math.PI, 0.0F));

        return LayerDefinition.create(mesh, 80, 80);
    }


    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        globe.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}