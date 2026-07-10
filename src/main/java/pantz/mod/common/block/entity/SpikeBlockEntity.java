package pantz.mod.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.FastColor;
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
    private int particleTimer = 0;

    public SpikeBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(PMBlockEntityTypes.SPIKE.get(), pPos, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SpikeBlockEntity spike) {
        if (state.getValue(SpikeBlock.POWERED)) {

            List<MobEffectInstance> effects = spike.getEffects();
            if (effects.isEmpty()) return;

            if (spike.particleTimer > 0) {
                spike.particleTimer--;
                return;
            }

            RandomSource random = level.getRandom();

            if (random.nextFloat() < 0.4F) return;

            MobEffectInstance effect = effects.get(random.nextInt(effects.size()));
            int color = effect.getEffect().value().getColor();

            int count = 2 + random.nextInt(3);

            for (int i = 0; i < count; i++) {
                double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.6;
                double y = pos.getY() + 0.05 + random.nextDouble() * 0.4;
                double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.6;

                level.addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, FastColor.ARGB32.color(255, color)), x, y, z, 0, 0, 0);
            }

            spike.particleTimer = 20 + random.nextInt(10);
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
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        ListTag list = new ListTag();
        for (MobEffectInstance effect : this.storedEffects) {
            list.add(effect.save());
        }

        tag.put("Effects", list);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        this.storedEffects.clear();

        ListTag list = tag.getList("Effects", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            this.storedEffects.add(MobEffectInstance.load(list.getCompound(i)));
        }
    }


    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        this.loadAdditional(tag, lookupProvider);
    }
}
