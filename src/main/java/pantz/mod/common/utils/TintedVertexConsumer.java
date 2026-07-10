package pantz.mod.common.utils;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.FastColor;

public record TintedVertexConsumer(VertexConsumer parent, int r, int g, int b, int a) implements VertexConsumer {
    @Override
    public VertexConsumer addVertex(float x, float y, float z) {
        return parent.addVertex(x, y, z);
    }

    @Override
    public VertexConsumer setColor(int red, int green, int blue, int alpha) {
        int nRed = (red * this.r) / 255;
        int nGreen = (green * this.g) / 255;
        int nBlue = (blue * this.b) / 255;
        int nAlpha = (alpha * this.a) / 255;
        return parent.setColor(nRed, nGreen, nBlue, nAlpha);
    }

    @Override
    public VertexConsumer setColor(int color) {
        return this.setColor(FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color), FastColor.ARGB32.alpha(color));
    }

    @Override
    public void addVertex(float x, float y, float z, int color, float u, float v, int packedOverlay, int packedLight, float normalX, float normalY, float normalZ) {
        int nr = (FastColor.ARGB32.red(color) * this.r) / 255;
        int ng = (FastColor.ARGB32.green(color) * this.g) / 255;
        int nb = (FastColor.ARGB32.blue(color) * this.b) / 255;
        int na = (FastColor.ARGB32.alpha(color) * this.a) / 255;
        int tintedColor = FastColor.ARGB32.color(na, nr, ng, nb);

        parent.addVertex(x, y, z, tintedColor, u, v, packedOverlay, packedLight, normalX, normalY, normalZ);
    }

    @Override
    public VertexConsumer setUv(float u, float v) {
        return parent.setUv(u, v);
    }

    @Override
    public VertexConsumer setUv1(int u, int v) {
        return parent.setUv1(u, v);
    }

    @Override
    public VertexConsumer setUv2(int u, int v) {
        return parent.setUv2(u, v);
    }

    @Override
    public VertexConsumer setNormal(float x, float y, float z) {
        return parent.setNormal(x, y, z);
    }
}
