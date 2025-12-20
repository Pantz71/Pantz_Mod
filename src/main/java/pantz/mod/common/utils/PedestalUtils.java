package pantz.mod.common.utils;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;

public class PedestalUtils {
    public static final Map<Item, CarpetColor> ITEM_TO_CARPET_COLOR = new HashMap<>();

    static {
        ITEM_TO_CARPET_COLOR.put(Items.WHITE_CARPET, CarpetColor.WHITE);
        ITEM_TO_CARPET_COLOR.put(Items.ORANGE_CARPET, CarpetColor.ORANGE);
        ITEM_TO_CARPET_COLOR.put(Items.MAGENTA_CARPET, CarpetColor.MAGENTA);
        ITEM_TO_CARPET_COLOR.put(Items.LIGHT_BLUE_CARPET, CarpetColor.LIGHT_BLUE);
        ITEM_TO_CARPET_COLOR.put(Items.YELLOW_CARPET, CarpetColor.YELLOW);
        ITEM_TO_CARPET_COLOR.put(Items.LIME_CARPET, CarpetColor.LIME);
        ITEM_TO_CARPET_COLOR.put(Items.PINK_CARPET, CarpetColor.PINK);
        ITEM_TO_CARPET_COLOR.put(Items.GRAY_CARPET, CarpetColor.GRAY);
        ITEM_TO_CARPET_COLOR.put(Items.LIGHT_GRAY_CARPET, CarpetColor.LIGHT_GRAY);
        ITEM_TO_CARPET_COLOR.put(Items.CYAN_CARPET, CarpetColor.CYAN);
        ITEM_TO_CARPET_COLOR.put(Items.PURPLE_CARPET, CarpetColor.PURPLE);
        ITEM_TO_CARPET_COLOR.put(Items.BLUE_CARPET, CarpetColor.BLUE);
        ITEM_TO_CARPET_COLOR.put(Items.BROWN_CARPET, CarpetColor.BROWN);
        ITEM_TO_CARPET_COLOR.put(Items.GREEN_CARPET, CarpetColor.GREEN);
        ITEM_TO_CARPET_COLOR.put(Items.RED_CARPET, CarpetColor.RED);
        ITEM_TO_CARPET_COLOR.put(Items.BLACK_CARPET, CarpetColor.BLACK);
    }

    public static CarpetColor getCarpetColor(Item item) {
        return ITEM_TO_CARPET_COLOR.getOrDefault(item, CarpetColor.NONE);
    }

    public static Block getCarpetForColor(CarpetColor color) {
        return switch (color) {
            case WHITE -> Blocks.WHITE_CARPET;
            case ORANGE -> Blocks.ORANGE_CARPET;
            case MAGENTA -> Blocks.MAGENTA_CARPET;
            case LIGHT_BLUE -> Blocks.LIGHT_BLUE_CARPET;
            case YELLOW -> Blocks.YELLOW_CARPET;
            case LIME -> Blocks.LIME_CARPET;
            case PINK -> Blocks.PINK_CARPET;
            case GRAY -> Blocks.GRAY_CARPET;
            case LIGHT_GRAY -> Blocks.LIGHT_GRAY_CARPET;
            case CYAN -> Blocks.CYAN_CARPET;
            case PURPLE -> Blocks.PURPLE_CARPET;
            case BLUE -> Blocks.BLUE_CARPET;
            case BROWN -> Blocks.BROWN_CARPET;
            case GREEN -> Blocks.GREEN_CARPET;
            case RED -> Blocks.RED_CARPET;
            case BLACK -> Blocks.BLACK_CARPET;
            case NONE -> Blocks.AIR;
        };
    }
}
