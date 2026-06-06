package pantz.mod.common.utils;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class PMBlockStateProperties {
    public static final BooleanProperty FILTERED = BooleanProperty.create("filtered");
    public static final IntegerProperty ENDERPORTER_CHARGE = IntegerProperty.create("enderporter_charge", 0, 4);

    public static final BooleanProperty GLASS = BooleanProperty.create("glass");

    public static final EnumProperty<CarpetColor> CARPET = EnumProperty.create("carpet", CarpetColor.class);

    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");

    public static final BooleanProperty INPUT_LEFT = BooleanProperty.create("input_left");
    public static final BooleanProperty INPUT_RIGHT = BooleanProperty.create("input_right");
    public static final BooleanProperty INPUT_BACK = BooleanProperty.create("input_back");

    public static final BooleanProperty FILLED = BooleanProperty.create("filled");

    public static final EnumProperty<RandomizerOutput> OUTPUT = EnumProperty.create("output", RandomizerOutput.class);

    public enum CarpetColor implements StringRepresentable {
        NONE("none"),
        WHITE("white"),
        ORANGE("orange"),
        MAGENTA("magenta"),
        LIGHT_BLUE("light_blue"),
        YELLOW("yellow"),
        LIME("lime"),
        PINK("pink"),
        GRAY("gray"),
        LIGHT_GRAY("light_gray"),
        CYAN("cyan"),
        PURPLE("purple"),
        BLUE("blue"),
        BROWN("brown"),
        GREEN("green"),
        RED("red"),
        BLACK("black");

        private final String name;

        CarpetColor(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

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
}
