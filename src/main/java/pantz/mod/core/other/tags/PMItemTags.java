package pantz.mod.core.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import pantz.mod.core.PantzMod;

public class PMItemTags {

    public static final TagKey<Item> INGOTS_STEEL = forgeTag("ingots/steel");
    public static final TagKey<Item> NUGGETS_STEEL = forgeTag("nuggets/steel");
    public static final TagKey<Item> STORAGE_BLOCKS_STEEL = forgeTag("storage_blocks/steel");
    public static final TagKey<Item> COALS = forgeTag("coals");

    public static final TagKey<Item> STORAGE_BLOCKS_SULFUR = forgeTag("storage_blocks/sulfur");
    public static final TagKey<Item> DUSTS_SULFUR = forgeTag("dusts/sulfur");
    public static final TagKey<Item> ORES_SULFUR = forgeTag("ores/sulfur");
    public static final TagKey<Item> GEMS_SULFUR = forgeTag("gems/sulfur");

    public static final TagKey<Item> TOOLS_TROWEL = forgeTag("tools/trowel");

    public static final TagKey<Item> EXCAVATORS = itemTag("excavators");
    public static final TagKey<Item> TOOLS = itemTag("tools");
    public static final TagKey<Item> WEAPONS = itemTag("weapons");
    public static final TagKey<Item> PEDESTALS = itemTag("pedestals");

    public static TagKey<Item> itemTag(String name) {
        return TagUtil.itemTag(PantzMod.MOD_ID, name);
    }

    public static TagKey<Item> forgeTag(String name) {
        return TagUtil.itemTag("forge", name);
    }
}
