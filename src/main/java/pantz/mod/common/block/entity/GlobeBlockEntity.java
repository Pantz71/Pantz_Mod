package pantz.mod.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import pantz.mod.common.block.GlobeBlock;
import pantz.mod.core.PantzMod;
import pantz.mod.core.registry.PMBlockEntityTypes;

public class GlobeBlockEntity extends BlockEntity {
    private float rotation = 0;
    private boolean rotating = false;
    private int spinTick = 0;
    private ResourceLocation texture;
    private boolean glow;

    private transient boolean clientSpinning;
    private transient int clientSpinTicks;
    private transient float clientRotation;

    private static final int FULL_SPIN = 360;
    private static final float ROTATE_SPEED = 1f;
    private static final float SPIN_SPEED = 15f;
    private static final int SPIN_TICKS = (int)(FULL_SPIN / SPIN_SPEED);

    public GlobeBlockEntity(BlockPos pos, BlockState state) {
        super(PMBlockEntityTypes.GLOBE.get(), pos, state);
        if (state.getBlock() instanceof GlobeBlock globeBlock) {
            this.texture = globeBlock.getTexture();
        }
        setRotating(false);
        setSpinTick(0);

        clientSpinning = false;
        clientSpinTicks = 0;
        clientRotation = 0f;
    }

    public ResourceLocation getTexture() {
        return texture != null ? texture : PantzMod.location("block/globe/planets/earth");
    }

    public boolean isGlow() {
        return glow;
    }

    public void setGlow(boolean value) {
        if (glow != value) {
            glow = value;
        }
    }

    public void spin(Level level) {
        if (level == null) return;

        if (!level.isClientSide()) {
            setSpinTick(SPIN_TICKS);
            setRotating(true);
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        } else {
            if (!clientSpinning) {
                clientSpinTicks = SPIN_TICKS;
                clientRotation = rotation;
                clientSpinning = true;
                spinTick = SPIN_TICKS;
            }
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, GlobeBlockEntity be) {
        if (state.getValue(GlobeBlock.POWERED)) {
            be.setRotation(rotate(be));
            return;
        }

        if (be.clientSpinning && be.clientSpinTicks > 0) {
            be.spinTick = Math.max(0, be.spinTick - 1);
            int elapsed = be.clientSpinTicks - be.spinTick;
            float progress = Math.min(1f, (float) elapsed / (float) be.clientSpinTicks);
            float easing = 1f - (float) Math.pow(1f - progress, 3);
            float newRotation = be.clientRotation + easing * FULL_SPIN;
            be.setRotation(newRotation % 360f);

            if (be.spinTick <= 0) {
                be.clientSpinning = false;
                be.clientSpinTicks = 0;
            }
        }
    }

    public float getRotation(float partialTicks) {
        if (this.getBlockState().getValue(GlobeBlock.POWERED)) {
            return (rotation + ROTATE_SPEED * partialTicks) % 360f;
        }

        if (clientSpinning && clientSpinTicks > 0) {
            int elapsed = clientSpinTicks - getSpinTick();
            float progress = (elapsed + partialTicks) / (float) clientSpinTicks;
            progress = Math.max(0f, Math.min(1f, progress));
            float eased = 1f - (float) Math.pow(1f - progress, 3);
            return (clientRotation + eased * FULL_SPIN) % 360f;
        }
        return rotation;
    }

    public static float rotate(GlobeBlockEntity be) {
        return (be.getRotation() + ROTATE_SPEED) % 360;
    }

    public float getRotation() {
        return rotation;
    }

    public boolean isRotating() {
        return rotating;
    }

    public int getSpinTick() {
        return spinTick;
    }

    public void decreaseSpinTick() {
        spinTick--;
    }

    public void setRotating(boolean value) {
        if (rotating != value) {
            rotating = value;
        }
    }

    public void setRotation(float value) {
        if (rotation != value) {
            rotation = value;
        }
    }

    public void setSpinTick(int tick) {
        if (spinTick != tick) {
            spinTick = tick;
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        rotating = tag.getBoolean("Rotating");
        rotation = tag.getFloat("Rotation");
        spinTick = tag.getInt("SpinTick");
        glow = tag.getBoolean("Glow");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("Rotating", isRotating());
        tag.putFloat("Rotation", getRotation());
        tag.putInt("SpinTick", getSpinTick());
        tag.putBoolean("Glow", isGlow());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
}
