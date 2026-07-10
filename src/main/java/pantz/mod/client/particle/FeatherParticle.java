package pantz.mod.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FeatherParticle extends TextureSheetParticle {
    private final float rotSpeed;

    protected FeatherParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(level, x, y, z, vx, vy, vz);
        this.gravity = 0.03f;
        this.lifetime = 160 + this.random.nextInt(41);
        this.quadSize = 0.2f;
        this.setSprite(spriteSet.get(0, 1));
        this.rotSpeed = 0.04f + this.random.nextFloat() * 0.05f;
        this.roll = this.random.nextFloat() * ((float) Math.PI * 2F);
        this.oRoll = this.roll;
    }

    @Override
    public void tick() {
        this.oRoll = this.roll;
        this.roll += this.rotSpeed;

        this.yd -= 0.04f * this.gravity;
        this.move(this.xd, this.yd, this.zd);

        this.xd *= 0.85;
        this.yd *= 0.85;
        this.zd *= 0.85;

        if (++this.age >= this.lifetime) this.remove();
        super.tick();
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        Vec3 camPos = camera.getPosition();
        float px = (float)(Mth.lerp(partialTicks, this.xo, this.x) - camPos.x());
        float py = (float)(Mth.lerp(partialTicks, this.yo, this.y) - camPos.y());
        float pz = (float)(Mth.lerp(partialTicks, this.zo, this.z) - camPos.z());

        float interpolatedRoll = Mth.rotLerp(partialTicks, this.oRoll, this.roll);
        Quaternionf rotation = new Quaternionf(camera.rotation()).mul(Axis.ZP.rotation(interpolatedRoll));

        Vector3f[] corners = new Vector3f[] {
                new Vector3f(-1, -1, 0),
                new Vector3f(-1,  1, 0),
                new Vector3f( 1,  1, 0),
                new Vector3f( 1, -1, 0)
        };

        float size = this.getQuadSize(partialTicks);
        for (Vector3f corner : corners) {
            corner.rotate(rotation).mul(size).add(px, py + 0.1f, pz);
        }

        float minU = this.getU0();
        float maxU = this.getU1();
        float minV = this.getV0();
        float maxV = this.getV1();
        int light = this.getLightColor(partialTicks);
        float r = this.rCol;
        float g = this.gCol;
        float b = this.bCol;
        float a = this.alpha;

        buffer.addVertex(corners[0].x(), corners[0].y(), corners[0].z()).setUv(maxU, maxV).setColor(r, g, b, a).setLight(light);
        buffer.addVertex(corners[1].x(), corners[1].y(), corners[1].z()).setUv(maxU, minV).setColor(r, g, b, a).setLight(light);
        buffer.addVertex(corners[2].x(), corners[2].y(), corners[2].z()).setUv(minU, minV).setColor(r, g, b, a).setLight(light);
        buffer.addVertex(corners[3].x(), corners[3].y(), corners[3].z()).setUv(minU, maxV).setColor(r, g, b, a).setLight(light);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
            return new FeatherParticle(level, x, y, z, vx, vy, vz, sprite);
        }
    }
}
