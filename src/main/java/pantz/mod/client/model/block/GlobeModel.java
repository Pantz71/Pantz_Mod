package pantz.mod.client.model.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class GlobeModel extends Model {
    private final ModelPart globe;

    public GlobeModel(ModelPart root) {
        super(RenderType::entityCutout);
        this.globe = root.getChild("root");
    }

    public static LayerDefinition createModel() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition globe = root.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        globe.addOrReplaceChild("globe", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, (float) Math.PI, (float) Math.PI, 0.0F));

        return LayerDefinition.create(mesh, 80, 80);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer buffer, int light, int overlay, float red, float green, float blue, float alpha) {
        globe.render(stack, buffer, light, overlay, red, green, blue, alpha);
    }
}
