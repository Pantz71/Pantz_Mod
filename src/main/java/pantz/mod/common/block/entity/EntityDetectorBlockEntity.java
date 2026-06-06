package pantz.mod.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import pantz.mod.common.block.EntityDetectorBlock;
import pantz.mod.common.utils.FilterMode;
import pantz.mod.core.PMConfig;
import pantz.mod.core.registry.PMBlockEntityTypes;

import java.util.ArrayList;
import java.util.List;

public class EntityDetectorBlockEntity extends BlockEntity {
    public static int DETECTION_RANGE = PMConfig.Common.COMMON.entityDetectorDetectionRadius.get();
    private static final int SLIDESHOW_INTERVAL = 100;

    private final List<ResourceLocation> filters = new ArrayList<>();
    private FilterMode filterMode = FilterMode.INCLUDE;

    private int slideshowIndex = 0;
    public int slideshowTimer = 0;
    private int tickCounter = 0;

    public EntityDetectorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(PMBlockEntityTypes.ENTITY_DETECTOR.get(), pPos, pBlockState);
    }

    public void setFilterSettings(FilterMode mode, ListTag list) {
        this.filterMode = mode;
        this.filters.clear();

        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            this.filters.add(new ResourceLocation(entry.getString("Id")));
        }

        this.slideshowIndex = 0;
        this.sync();
    }

    private boolean matchesFilter(Entity entity) {
        ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());

        boolean found = this.filters.contains(entityId);
        return (this.filterMode == FilterMode.INCLUDE) == found;
    }

    public boolean hasFilters() {
        return !this.filters.isEmpty();
    }

    @Nullable
    public ResourceLocation removeLastEntity() {
        if (!this.filters.isEmpty()) {
            ResourceLocation removedId = this.filters.remove(this.filters.size() - 1);
            this.slideshowIndex = Math.max(0, this.filters.size() - 1);
            this.sync();
            return removedId;
        }
        return null;
    }

    private void sync() {
        this.setChanged();
        if (this.level != null && !this.level.isClientSide()) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Nullable
    public EntityType<?> getSlideshowEntities() {
        if (this.filters.isEmpty() || level == null) return null;
        ResourceLocation id = filters.get(this.slideshowIndex % this.filters.size());
        return ForgeRegistries.ENTITY_TYPES.getValue(id);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, EntityDetectorBlockEntity be) {
        be.tickCounter++;
        if (be.tickCounter >= 10) {
            be.tickCounter = 0;
            be.updatePower(level, pos, state);
        }

        if (!be.filters.isEmpty()) {
            be.slideshowTimer++;
            if (be.slideshowTimer >= SLIDESHOW_INTERVAL) {
                be.slideshowTimer = 0;
                be.slideshowIndex = (be.slideshowIndex + 1) % be.filters.size();
                level.sendBlockUpdated(pos, state, state, 3);
            }
        }
    }

    public void serverTick() {
        this.tickCounter = 1000;
        this.setChanged();
    }

    private void updatePower(Level level, BlockPos pos, BlockState state) {
        boolean inverted = state.getValue(EntityDetectorBlock.INVERTED);
        boolean shouldPower = this.detectEntities(level, pos, inverted);

        if (state.getValue(EntityDetectorBlock.POWERED) != shouldPower) {
            level.setBlockAndUpdate(pos, state.setValue(EntityDetectorBlock.POWERED, shouldPower));
            level.updateNeighborsAt(pos, state.getBlock());
            this.setChanged();
        }
    }

    public FilterMode getFilterMode() {
        return this.filterMode;
    }

    private boolean detectEntities(Level level, BlockPos center, boolean inverted) {
        int range = Math.max(1, DETECTION_RANGE);
        AABB box = new AABB(center).inflate(range / 2.0);

        List<Entity> entities = level.getEntities((Entity) null, box, entity -> {
            if (!(entity instanceof LivingEntity) || entity instanceof Player) return false;

            if (PMConfig.Common.COMMON.enableEntityFilter.get() && !this.filters.isEmpty()) {
                return this.matchesFilter(entity);
            } else {
                MobCategory category = entity.getType().getCategory();
                return (category == MobCategory.MONSTER) != inverted;
            }
        });

        return !entities.isEmpty();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("FilterMode", this.filterMode.getId());

        ListTag list = new ListTag();
        for (ResourceLocation id : this.filters) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putString("Id", id.toString());
            list.add(entryTag);
        }
        tag.put("Filters", list);
        tag.putInt("Index", this.slideshowIndex);
        tag.putInt("Timer", this.slideshowTimer);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.filterMode = FilterMode.byId(tag.getInt("FilterMode"));

        this.filters.clear();
        ListTag list = tag.getList("Filters", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entryTag = list.getCompound(i);
            this.filters.add(new ResourceLocation(entryTag.getString("Id")));
        }
        this.slideshowIndex = tag.getInt("Index");
        this.slideshowTimer = tag.getInt("Timer");
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
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }
}