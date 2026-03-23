package pantz.mod.common.utils;

import net.minecraft.util.StringRepresentable;

public enum RandomizerOutput implements StringRepresentable {
    NONE("none"),
    FRONT("front"),
    LEFT("left"),
    RIGHT("right")
    ;

    private final String name;

    RandomizerOutput(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
