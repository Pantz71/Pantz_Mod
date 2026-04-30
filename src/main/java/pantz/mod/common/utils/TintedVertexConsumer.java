package pantz.mod.common.utils;

import com.mojang.blaze3d.vertex.VertexConsumer;

public record TintedVertexConsumer(VertexConsumer parent, int r, int g, int b, int a) implements VertexConsumer {
    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        return parent.vertex(x, y, z);
    }

    @Override
    public VertexConsumer color(int r, int g, int b, int a) {
        return parent.color((r * this.r) / 255, (g * this.g) / 255, (b * this.b) / 255, (a * this.a) / 255);
    }

    @Override
    public VertexConsumer uv(float u, float v) {
        return parent.uv(u, v);
    }

    @Override
    public VertexConsumer overlayCoords(int u, int v) {
        return parent.overlayCoords(u, v);
    }

    @Override
    public VertexConsumer uv2(int u, int v) {
        return parent.uv2(u, v);
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        return parent.normal(x, y, z);
    }

    @Override
    public void endVertex() {
        parent.endVertex();
    }

    @Override
    public void defaultColor(int r, int g, int b, int a) {
        parent.defaultColor(r, g, b, a);
    }

    @Override
    public void unsetDefaultColor() {
        parent.unsetDefaultColor();
    }
}
