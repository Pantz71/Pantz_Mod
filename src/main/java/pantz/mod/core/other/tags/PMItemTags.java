package pantz.mod.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.PMConstant;

public class PMItemTags {

    public static final TagKey<Item> INGOTS_STEEL = forgeTag("ingots/steel");
    public static final TagKey<Item> NUGGETS_STEEL = forgeTag("nuggets/steel");
    public static final TagKey<Item> STORAGE_BLOCKS_STEEL = forgeTag("storage_blocks/steel");
    public static final TagKey<Item> COALS = forgeTag("coals");
    public static final TagKey<Item> INGOTS_SILVER = forgeTag("ingots/silver");

    public static final TagKey<Item> STORAGE_BLOCKS_SULFUR = forgeTag("storage_blocks/sulfur");
    public static final TagKey<Item> DUSTS_SULFUR = forgeTag("dusts/sulfur");
    public static final TagKey<Item> ORES_SULFUR = forgeTag("ores/sulfur");
    public static final TagKey<Item> GEMS_SULFUR = forgeTag("gems/sulfur");

    public static final TagKey<Item> TOOLS_TROWEL = forgeTag("tools/trowel");

    public static final TagKey<Item> REDSTONE_LAMPS = forgeTag("redstone_lamps");

    public static final TagKey<Item> GLASS_CHORUS = forgeTag("glass/chorus");
    public static final TagKey<Item> GLASS_PANES_CHORUS = forgeTag("glass_panes/chorus");
    public static final TagKey<Item> GLASS_SOUL = forgeTag("glass/soul");
    public static final TagKey<Item> GLASS_PANES_SOUL = forgeTag("glass_panes/soul");
    public static final TagKey<Item> GLASS_ECHO = forgeTag("glass/echo");
    public static final TagKey<Item> GLASS_PANES_ECHO = forgeTag("glass_panes/echo");

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

    public static TagKey<Item> forgeTag(String name) {
        return TagUtil.itemTag("forge", name);
    }
}
