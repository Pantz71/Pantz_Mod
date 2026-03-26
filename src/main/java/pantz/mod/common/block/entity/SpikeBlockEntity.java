package pantz.mod.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import pantz.mod.common.block.SpikeBlock;
import pantz.mod.core.PMConfig;
import pantz.mod.core.registry.PMBlockEntityTypes;

import java.util.ArrayList;
import java.util.List;

public class SpikeBlockEntity extends BlockEntity {
    private final List<MobEffectInstance> storedEffects = new ArrayList<>();
    private static final int maxEffects = PMConfig.Common.COMMON.spikeMaxEffects.get();

    public SpikeBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(PMBlockEntityTypes.SPIKE.get(), pPos, pBlockState);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, SpikeBlockEntity spike) {
        if (!state.getValue(SpikeBlock.POWERED)) return;

        List<MobEffectInstance> effects = spike.getEffects();
        if (effects.isEmpty()) return;

        RandomSource random = level.getRandom();

        if (random.nextFloat() < 0.4F) return;

        MobEffectInstance effect = effects.get(random.nextInt(effects.size()));
        int color = effect.getEffect().getColor();

        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;

        int count = 1 + random.nextInt(2);

        for (int i = 0; i < count; i++) {
            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.6;
            double y = pos.getY() + 0.05 + random.nextDouble() * 0.4;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.6;

            level.addParticle(ParticleTypes.ENTITY_EFFECT, x, y, z, r, g, b);
        }
    }

    public boolean addOrUpgradeEffect(MobEffectInstance mobEffect) {
        for (int i = 0; i < this.storedEffects.size(); i++) {
            MobEffectInstance stored = this.storedEffects.get(i);

            if (stored.getEffect().equals(mobEffect.getEffect())) {
                int existingAmp = stored.getAmplifier();
                int incomingAmp = mobEffect.getAmplifier();

                int existingDur = stored.getDuration();
                int incomingDur = mobEffect.getDuration();

                boolean upgrade = incomingAmp > existingAmp || (incomingAmp == existingAmp && incomingDur > existingDur);

                if (!upgrade) {
                    return false;
                }

                this.storedEffects.set(i, new MobEffectInstance(mobEffect));

                setChanged();
                if (this.level != null && !this.level.isClientSide()) {
                    this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
                }
                return true;
            }
        }

        if (this.storedEffects.size() >= maxEffects) return false;

        this.storedEffects.add(new MobEffectInstance(mobEffect));

        setChanged();
        if (this.level != null && !this.level.isClientSide()) {
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
        }
        return true;
    }

    public void removeLastEffect() {
        if (!this.storedEffects.isEmpty()) {
            this.storedEffects.remove(this.storedEffects.size() - 1);
            setChanged();
            if (this.level != null && !this.level.isClientSide()) {
                this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    }

    public List<MobEffectInstance> getEffects() {
        return this.storedEffects;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        ListTag list = new ListTag();
        for (MobEffectInstance effect : this.storedEffects) {
            list.add(effect.save(new CompoundTag()));
        }

        tag.put("Effects", list);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.storedEffects.clear();

        ListTag list = tag.getList("Effects", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            this.storedEffects.add(MobEffectInstance.load(list.getCompound(i)));
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }
}
