package pantz.mod.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
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
    private long spinTime = -1;
    private ResourceLocation texture;
    private ResourceLocation cachedRenderTexture;
    private boolean glow;

    private static final float ROTATE_SPEED = 1f;
    private static final int SPIN_TICKS = 24;

    public GlobeBlockEntity(BlockPos pos, BlockState state) {
        super(PMBlockEntityTypes.GLOBE.get(), pos, state);
        if (state.getBlock() instanceof GlobeBlock globeBlock) {
            this.texture = globeBlock.getTexture();
        }
    }

    public ResourceLocation getTexture() {
        return this.texture != null ? this.texture : PantzMod.location("block/globe/planets/earth");
    }

    public ResourceLocation getRenderTexture() {
        if (this.cachedRenderTexture == null) {
            ResourceLocation location = getTexture();
            this.cachedRenderTexture = ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "textures/block/globe/" + location.getPath() + ".png");
        }
        return this.cachedRenderTexture;
    }

    public int getPower() {
        if (isGlow()) {
            return 7;
        } else if (this.getSpinTime() != -1) {
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
            setChanged();
        }
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public void spin() {
        if (this.level != null && !this.level.isClientSide()) {
            this.spinTime = this.level.getGameTime();
            setChanged();
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, GlobeBlockEntity be) {
        if (state.getValue(GlobeBlock.POWERED)) {
            be.rotation = (be.getRotation() + ROTATE_SPEED) % 360f;
        }
        else if (!level.isClientSide() && be.getSpinTime() != -1) {
            if (level.getGameTime() - be.getSpinTime() >= SPIN_TICKS) {
                be.rotation = (be.getRotation() + 360f) % 360f;
                be.spinTime = -1;
                be.setChanged();
            }
        }
    }

    public float getRotation(float partialTicks) {
        if (this.level == null) return this.getRotation();

        if (this.getBlockState().getValue(GlobeBlock.POWERED)) {
            return (this.getRotation() + (ROTATE_SPEED * partialTicks)) % 360f;
        }

        if (this.getSpinTime() != -1) {
            float gameTimeWithPartial = (float)(this.level.getGameTime() - this.getSpinTime()) + partialTicks;
            float progress = Math.min(1.0f, gameTimeWithPartial / (float) SPIN_TICKS);

            float easing = 1.0f - (float) Math.pow(1.0f - progress, 3);

            if (progress >= 1.0f) {
                if (this.level.isClientSide()) {
                    this.rotation = (this.getRotation() + 360f) % 360f;
                    this.spinTime = -1;
                }
                return this.getRotation();
            }

            return (this.getRotation() + (easing * 360f)) % 360f;
        }

        return this.getRotation();
    }

    public float getRotation() {
        return this.rotation;
    }

    public long getSpinTime() {
        return this.spinTime;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.rotation = tag.getFloat("Rotation");
        this.glow = tag.getBoolean("Glow");
        if (tag.contains("SpinTime")) {
            this.spinTime = tag.getLong("SpinTime");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putFloat("Rotation", getRotation());
        tag.putBoolean("Glow", isGlow());
        tag.putLong("SpinTime", getSpinTime());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        loadAdditional(pkt.getTag(), lookupProvider);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }
}
