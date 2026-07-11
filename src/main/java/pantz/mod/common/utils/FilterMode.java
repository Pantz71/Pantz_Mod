package pantz.mod.common.utils;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

public enum FilterMode implements StringRepresentable {
    INCLUDE(0, "include", ChatFormatting.GREEN, "tooltip.pantz_mod.mode.include"),
    EXCLUDE(1, "exclude", ChatFormatting.RED, "tooltip.pantz_mod.mode.exclude");

    private final int id;
    private final String name;
    private final ChatFormatting color;
    private final String translationKey;

    public static final Codec<FilterMode> CODEC = StringRepresentable.fromEnum(FilterMode::values);

    public static final StreamCodec<ByteBuf, FilterMode> STREAM_CODEC = ByteBufCodecs.idMapper(
            FilterMode::byId, FilterMode::getId
    );

    FilterMode(int id, String name, ChatFormatting color, String translationKey) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.translationKey = translationKey;
    }

    public int getId() {
        return id;
    }

    public ChatFormatting getColor() {
        return color;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public static FilterMode byId(int id) {
        return id == 1 ? EXCLUDE : INCLUDE;
    }

    public FilterMode next() {
        return byId((this.id + 1) % values().length);
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
