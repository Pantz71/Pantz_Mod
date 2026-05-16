package pantz.mod.common.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WardenWorldData extends SavedData {
    private static final String NAME = "players_defeated_warden";
    private final Set<UUID> playersWhoKilledWarden = new HashSet<>();

    public static WardenWorldData get(Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            throw new RuntimeException("Tried to access world data on client!");
        }
        return serverLevel.getDataStorage().computeIfAbsent(WardenWorldData::load, WardenWorldData::new, NAME);
    }

    public static WardenWorldData load(CompoundTag tag) {
        WardenWorldData data = new WardenWorldData();
        ListTag list = tag.getList("PlayerList", Tag.TAG_INT_ARRAY);
        for (Tag value : list) {
            data.playersWhoKilledWarden.add(NbtUtils.loadUUID(value));
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (UUID uuid : playersWhoKilledWarden) {
            list.add(NbtUtils.createUUID(uuid));
        }
        tag.put("PlayerList", list);
        return tag;
    }

    public boolean hasPlayerKilled(UUID uuid) {
        return playersWhoKilledWarden.contains(uuid);
    }

    public void addPlayer(UUID uuid) {
        if (playersWhoKilledWarden.add(uuid)) {
            setDirty();
        }
    }
}
