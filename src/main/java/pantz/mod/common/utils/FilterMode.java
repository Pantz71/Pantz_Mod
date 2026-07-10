package pantz.mod.common.utils;

import net.minecraft.ChatFormatting;

public enum FilterMode {
    INCLUDE(0, ChatFormatting.GREEN, "tooltip.pantz_mod.mode.include"),
    EXCLUDE(1, ChatFormatting.RED, "tooltip.pantz_mod.mode.exclude");

    private final int id;
    private final ChatFormatting color;
    private final String translationKey;

    FilterMode(int id, ChatFormatting color, String translationKey) {
        this.id = id;
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
}
