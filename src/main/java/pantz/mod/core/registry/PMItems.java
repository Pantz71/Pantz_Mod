package pantz.mod.core.registry;

import com.teamabnormals.blueprint.core.util.PropertyUtil;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.RegistryObject;
import pantz.mod.common.item.TrowelItem;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.PMTiers.*;

import java.util.function.Supplier;

import static net.minecraft.world.item.CreativeModeTabs.*;
import static net.minecraft.world.item.crafting.Ingredient.of;

public class PMItems {
    public static final ItemSubRegistryHelper ITEMS = PantzMod.REGISTRY_HELPER.getItemSubHelper();

    public static final RegistryObject<Item> STEEL_INGOT = ITEMS.createItem("steel_ingot", basicItem());
    public static final RegistryObject<Item> STEEL_NUGGET = ITEMS.createItem("steel_nugget", basicItem());

    public static final RegistryObject<Item> STEEL_SWORD = ITEMS.createItem("steel_sword", () -> new SwordItem(PMItemTiers.STEEL, 3, -2.4f, new Item.Properties()));
    public static final RegistryObject<Item> STEEL_SHOVEL = ITEMS.createItem("steel_shovel", () -> new ShovelItem(PMItemTiers.STEEL, 1.5f, -3f, new Item.Properties()));
    public static final RegistryObject<Item> STEEL_PICKAXE = ITEMS.createItem("steel_pickaxe", () -> new PickaxeItem(PMItemTiers.STEEL, 1, -2.8f, new Item.Properties()));
    public static final RegistryObject<Item> STEEL_AXE = ITEMS.createItem("steel_axe", () -> new AxeItem(PMItemTiers.STEEL, 6, -3f, new Item.Properties()));
    public static final RegistryObject<Item> STEEL_HOE = ITEMS.createItem("steel_hoe", () -> new HoeItem(PMItemTiers.STEEL, -3, -1f, new Item.Properties()));

    public static final RegistryObject<Item> STEEL_HELMET = ITEMS.createItem("steel_helmet", () -> new ArmorItem(PMArmorMaterials.STEEL, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> STEEL_CHESTPLATE = ITEMS.createItem("steel_chestplate", () -> new ArmorItem(PMArmorMaterials.STEEL, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> STEEL_LEGGINGS = ITEMS.createItem("steel_leggings", () -> new ArmorItem(PMArmorMaterials.STEEL, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> STEEL_BOOTS = ITEMS.createItem("steel_boots", () -> new ArmorItem(PMArmorMaterials.STEEL, ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> STEEL_HORSE_ARMOR = ITEMS.createItem("steel_horse_armor", () -> new HorseArmorItem(6, "steel", PropertyUtil.stacksOnce()));

    public static final RegistryObject<Item> SULFUR = ITEMS.createItem("sulfur", basicItem());

    public static final RegistryObject<Item> TROWEL = ITEMS.createItem("trowel", () -> new TrowelItem(new Item.Properties().durability(256)));

    public static final RegistryObject<Item> HONEY_DESERIALIZER = ITEMS.createItem("honey_deserializer", () -> new Item(PropertyUtil.stacksOnce()));

    private static Supplier<Item> basicItem() {
        return () -> new Item(new Item.Properties());
    }

    public static void setupTabs() {
        CreativeModeTabContentsPopulator.mod(PantzMod.MOD_ID)
                .tab(INGREDIENTS)
                .addItemsAfter(of(Items.IRON_INGOT), STEEL_INGOT)
                .addItemsAfter(of(Items.IRON_NUGGET), STEEL_NUGGET)
                .addItemsBefore(of(Items.BLAZE_ROD), SULFUR)

                .tab(COMBAT)
                .addItemsBefore(of(Items.GOLDEN_SWORD), STEEL_SWORD)
                .addItemsBefore(of(Items.GOLDEN_AXE), STEEL_AXE)
                .addItemsBefore(of(Items.GOLDEN_HELMET), STEEL_HELMET, STEEL_CHESTPLATE, STEEL_LEGGINGS, STEEL_BOOTS)
                .addItemsBefore(of(Items.GOLDEN_HORSE_ARMOR), STEEL_HORSE_ARMOR)

                .tab(TOOLS_AND_UTILITIES)
                .addItemsBefore(of(Items.GOLDEN_SHOVEL), STEEL_SHOVEL, STEEL_PICKAXE, STEEL_AXE, STEEL_HOE)
                .addItemsAfter(of(Items.SHEARS), TROWEL)
                .addItemsAfter(of(Items.BRUSH), HONEY_DESERIALIZER)

        ;
    }
}
