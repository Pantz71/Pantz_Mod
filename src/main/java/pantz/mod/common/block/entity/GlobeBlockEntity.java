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

        this.clientSpinning = false;
        this.clientSpinTicks = 0;
        this.clientRotation = 0f;
    }

    public ResourceLocation getTexture() {
        return this.texture != null ? this.texture : PantzMod.location("block/globe/planets/earth");
    }

    public int getPower() {
        if (isGlow()) {
            return 7;
        } else if (isRotating()) {
            return 15;
        }
        return 0;
    }

    public boolean isGlow() {
        return this.glow;
    }

    public void setGlow(boolean value) {
        if (this.glow != value) {
            this.glow = value;
        }
        setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public void spin(Level level) {
        if (level == null) return;

        if (!level.isClientSide()) {
            setSpinTick(SPIN_TICKS);
            setRotating(true);
            setChanged();
            level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
        } else {
            if (!this.clientSpinning) {
                this.clientSpinTicks = SPIN_TICKS;
                this.clientRotation = this.rotation;
                this.clientSpinning = true;
                this.spinTick = SPIN_TICKS;
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

    public static void tick(Level level, BlockPos pos, BlockState state, GlobeBlockEntity be) {
        if (!level.isClientSide()) {
            boolean powered = state.getValue(GlobeBlock.POWERED);

            if (powered) {
                if (!be.isRotating()) {
                    be.setRotating(true);
                    be.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                }
                return;
            }

            if (be.getSpinTick() > 0) {
                be.decreaseSpinTick();
                if (be.getSpinTick() <= 0) {
                    be.setSpinTick(0);
                    be.setRotating(false);
                    be.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                }
            }
        } else {
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
    }

    public float getRotation(float partialTicks) {
        if (this.getBlockState().getValue(GlobeBlock.POWERED)) {
            return (this.rotation + ROTATE_SPEED * partialTicks) % 360f;
        }

        if (this.clientSpinning && this.clientSpinTicks > 0) {
            int elapsed = this.clientSpinTicks - getSpinTick();
            float progress = (elapsed + partialTicks) / (float) this.clientSpinTicks;
            progress = Math.max(0f, Math.min(1f, progress));
            float eased = 1f - (float) Math.pow(1f - progress, 3);
            return (this.clientRotation + eased * FULL_SPIN) % 360f;
        }
        return this.rotation;
    }

    public static float rotate(GlobeBlockEntity be) {
        return (be.getRotation() + ROTATE_SPEED) % 360;
    }

    public float getRotation() {
        return this.rotation;
    }

    public boolean isRotating() {
        return this.rotating;
    }

    public int getSpinTick() {
        return this.spinTick;
    }

    public void decreaseSpinTick() {
        this.spinTick--;
    }

    public void setRotating(boolean value) {
        if (this.rotating != value) {
            this.rotating = value;
        }
    }

    public void setRotation(float value) {
        if (this.rotation != value) {
            this.rotation = value;
        }
    }

    public void setSpinTick(int tick) {
        if (this.spinTick != tick) {
            this.spinTick = tick;
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.rotating = tag.getBoolean("Rotating");
        this.rotation = tag.getFloat("Rotation");
        this.spinTick = tag.getInt("SpinTick");
        this.glow = tag.getBoolean("Glow");
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
