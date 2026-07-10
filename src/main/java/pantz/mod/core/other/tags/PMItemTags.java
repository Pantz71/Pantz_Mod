package pantz.mod.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.PMConstant;

public class PMItemTags {

    public static final TagKey<Item> INGOTS_STEEL = commonTag("ingots/steel");
    public static final TagKey<Item> NUGGETS_STEEL = commonTag("nuggets/steel");
    public static final TagKey<Item> STORAGE_BLOCKS_STEEL = commonTag("storage_blocks/steel");
    public static final TagKey<Item> COALS = commonTag("coals");
    public static final TagKey<Item> INGOTS_SILVER = commonTag("ingots/silver");

    public static final TagKey<Item> STORAGE_BLOCKS_SULFUR = commonTag("storage_blocks/sulfur");
    public static final TagKey<Item> DUSTS_SULFUR = commonTag("dusts/sulfur");
    public static final TagKey<Item> ORES_SULFUR = commonTag("ores/sulfur");
    public static final TagKey<Item> GEMS_SULFUR = commonTag("gems/sulfur");

    public static final TagKey<Item> TOOLS_TROWEL = commonTag("tools/trowel");
    public static final TagKey<Item> REDSTONE_LAMPS = commonTag("redstone_lamps");

    public static final TagKey<Item> GLASS_BLOCKS_CHORUS = commonTag("glass_blocks/chorus");
    public static final TagKey<Item> GLASS_PANES_CHORUS = commonTag("glass_panes/chorus");
    public static final TagKey<Item> GLASS_BLOCKS_SOUL = commonTag("glass_blocks/soul");
    public static final TagKey<Item> GLASS_PANES_SOUL = commonTag("glass_panes/soul");
    public static final TagKey<Item> GLASS_BLOCKS_ECHO = commonTag("glass_blocks/echo");
    public static final TagKey<Item> GLASS_PANES_ECHO = commonTag("glass_panes/echo");

    public static final TagKey<Item> STORAGE_BLOCKS_LEATHER = commonTag("storage_blocks/leather");
    public static final TagKey<Item> STORAGE_BLOCKS_RABBIT_HIDE = commonTag("storage_blocks/rabbit_hide");
    public static final TagKey<Item> STORAGE_BLOCKS_PHANTOM_MEMBRANE = commonTag("storage_blocks/phantom_membrane");
    public static final TagKey<Item> STORAGE_BLOCKS_FEATHER = commonTag("storage_blocks/feather");
    public static final TagKey<Item> STORAGE_BLOCKS_SUGAR = commonTag("storage_blocks/sugar");
    public static final TagKey<Item> STORAGE_BLOCKS_SUGAR_CANE = commonTag("storage_blocks/sugar_cane");
    public static final TagKey<Item> STORAGE_BLOCKS_BLAZE_POWDER = commonTag("storage_blocks/blaze_powder");

    public static final TagKey<Item> EXCAVATORS = itemTag("excavators");
    public static final TagKey<Item> HAMMERS = itemTag("hammers");
    public static final TagKey<Item> TOOLS = itemTag("tools");
    public static final TagKey<Item> WEAPONS = itemTag("weapons");
    public static final TagKey<Item> PEDESTALS = itemTag("pedestals");
    public static final TagKey<Item> ENDER_SCANNER_IMMUNITIES = itemTag("ender_scanner_immunities");
    public static final TagKey<Item> LOGIC_GATES = itemTag("logic_gates");
    public static final TagKey<Item> DYNAMITES = itemTag("dynamites");
    public static final TagKey<Item> PAPER_LANTERNS = itemTag("paper_lanterns");
    public static final TagKey<Item> KEYS = itemTag("keys");

    public static final TagKey<Item> PLACEABLE_ITEMS = TagUtil.itemTag(PMConstant.CAVERNS_AND_CHASMS, "placeable_items");

    public static TagKey<Item> itemTag(String name) {
        return TagUtil.itemTag(PantzMod.MOD_ID, name);
    }

    public static TagKey<Item> commonTag(String name) {
        return TagUtil.itemTag("c", name);
    }
}
