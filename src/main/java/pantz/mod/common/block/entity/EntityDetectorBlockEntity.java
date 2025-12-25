package pantz.mod.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import pantz.mod.common.block.EntityDetectorBlock;
import pantz.mod.core.PMConfig;
import pantz.mod.core.registry.PMBlockEntityTypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EntityDetectorBlockEntity extends BlockEntity {
    public static int DETECTION_RANGE = PMConfig.Common.COMMON.entityDetectorDetectionRadius.get();
    private static final int SLIDESHOW_INTERVAL = 100;

    private final List<ResourceLocation> mobList = new ArrayList<>();

    private int slideshowIndex = 0;
    public int slideshowTimer = 0;

    private int tickCounter = 0;
    private boolean powered = false;
    public EntityDetectorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(PMBlockEntityTypes.ENTITY_DETECTOR.get(), pPos, pBlockState);
    }

    public List<ResourceLocation> getMobList() {
        return new ArrayList<>(mobList);
    }

    public void removeLastMob() {
        if (!mobList.isEmpty()) {
            mobList.remove(mobList.size() - 1);

            if (slideshowIndex >= mobList.size()) {
                slideshowIndex = Math.max(0, mobList.size() - 1);
            }

            setChanged();
            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    }

    public void setFilterMobs(Collection<ResourceLocation> mobs) {
        this.mobList.clear();
        this.mobList.addAll(mobs);
        this.slideshowIndex = 0;
        this.slideshowTimer = 0;
        this.setChanged();
        
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public boolean isPowered() {
        return powered;
    }

    @Nullable
    public EntityType<?> getSlideshowEntityType() {
        if (mobList.isEmpty() || level == null) return null;
        ResourceLocation id = mobList.get(slideshowIndex % mobList.size());
        return ForgeRegistries.ENTITY_TYPES.getValue(id);
    }

    public void tick() {
        this.tickCounter = 1000; 
        this.setChanged();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, EntityDetectorBlockEntity be) {
        if (level.isClientSide()) return;
        be.tickCounter++;
        if (be.tickCounter >= 10) {
            be.tickCounter = 0;
            be.runDetection(level, pos, state);
        }
        be.slideshowTimer++;
        if (be.slideshowTimer >= SLIDESHOW_INTERVAL) {
            be.slideshowTimer = 0;
            if (!be.mobList.isEmpty()) {
                be.slideshowIndex = (be.slideshowIndex + 1) % be.mobList.size();
                be.setChanged();
                level.sendBlockUpdated(pos, state, state, 3); 
            }
        }
    }

    private void runDetection(Level level, BlockPos pos, BlockState state) {
        boolean inverted = state.getValue(EntityDetectorBlock.INVERTED);
        boolean shouldPower = detectEntities(level, pos, inverted);

        if (shouldPower != powered) {
            powered = shouldPower;
            setChanged();
            level.updateNeighborsAt(pos, state.getBlock());
        }
    }

    private boolean detectEntities(Level level, BlockPos center, boolean inverted) {
        int range = Math.max(1, DETECTION_RANGE);
        int half = range / 2;
        int offset = (range % 2 == 0) ? 0 : 1;

        AABB box = new AABB(
                center.offset(-half, -half, -half),
                center.offset(half + offset, half + offset, half + offset)
        );

        return !level.getEntities((Entity) null, box, entity -> {
            if (!(entity instanceof LivingEntity)) return false;

            if (PMConfig.Common.COMMON.enableEntityFilter.get() && !mobList.isEmpty()) {
                ResourceLocation id = EntityType.getKey(entity.getType());
                return mobList.contains(id);
            } else {
                return switch (entity.getType().getCategory()) {
                    case MONSTER -> !inverted;
                    case CREATURE, AMBIENT, WATER_CREATURE, WATER_AMBIENT, UNDERGROUND_WATER_CREATURE, AXOLOTLS -> inverted;
                    default -> false;
                };
            }
        }).isEmpty();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("Powered", powered);
        ListTag list = new ListTag();
        for (ResourceLocation id : mobList) {
            list.add(StringTag.valueOf(id.toString()));
        }
        tag.put("FilteredMobs", list);
        tag.putInt("Index", slideshowIndex);
        tag.putInt("Timer", slideshowTimer);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        powered = tag.getBoolean("Powered");
        this.mobList.clear();
        if (tag.contains("FilteredMobs")) {
            ListTag list = tag.getList("FilteredMobs", 8);
            for (int i = 0; i < list.size(); i++) {
                this.mobList.add(new ResourceLocation(list.getString(i)));
            }
        }
        if (tag.contains("Index")) slideshowIndex = tag.getInt("Index");
        if (tag.contains("Timer")) slideshowTimer = tag.getInt("Timer");
    }


    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }

}
