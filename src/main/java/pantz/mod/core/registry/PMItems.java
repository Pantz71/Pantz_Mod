package pantz.mod.core.registry;

import com.teamabnormals.blueprint.core.util.PropertyUtil;
import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.ItemSubRegistryHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.*;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.RegistryObject;
import pantz.mod.common.item.*;
import pantz.mod.common.utils.DynamiteType;
import pantz.mod.core.PMConfig;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.PMTiers.*;

import java.util.function.Supplier;

import static net.minecraft.world.item.CreativeModeTabs.*;
import static net.minecraft.world.item.crafting.Ingredient.of;

// item count: 27
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
    public static final RegistryObject<Item> STEEL_HORSE_ARMOR = ITEMS.createItem("steel_horse_armor", () -> new HorseArmorItem(6, PantzMod.location("textures/entity/horse/armor/horse_armor_silver.png"), PropertyUtil.stacksOnce()));

    public static final RegistryObject<Item> SULFUR_DUST = ITEMS.createItem("sulfur_dust", basicItem());
    public static final RegistryObject<Item> SULFUR_SHARD = ITEMS.createItem("sulfur_shard", basicItem());

    public static final RegistryObject<Item> TROWEL = ITEMS.createItem("trowel", () -> new TrowelItem(new Item.Properties().durability(256)));
    public static final RegistryObject<Item> HONEY_DESERIALIZER = ITEMS.createItem("honey_deserializer", () -> new Item(PropertyUtil.stacksOnce()));

    public static final RegistryObject<Item> EXCAVATOR = ITEMS.createItem("excavator", () -> new AreaDiggerItem(-3f, -3f, PMItemTiers.STEEL, BlockTags.MINEABLE_WITH_SHOVEL, new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_EXCAVATOR = ITEMS.createItem("diamond_excavator", () -> new AreaDiggerItem(-3f, -3f, Tiers.DIAMOND, BlockTags.MINEABLE_WITH_SHOVEL, new Item.Properties()));
    public static final RegistryObject<Item> NETHERITE_EXCAVATOR = ITEMS.createItem("netherite_excavator", () -> new AreaDiggerItem(-4f, -2.5f, Tiers.NETHERITE, BlockTags.MINEABLE_WITH_SHOVEL, new Item.Properties()));

    public static final RegistryObject<Item> HAMMER = ITEMS.createItem("hammer", () -> new AreaDiggerItem(-3f, -3f, PMItemTiers.STEEL, BlockTags.MINEABLE_WITH_PICKAXE, new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_HAMMER = ITEMS.createItem("diamond_hammer", () -> new AreaDiggerItem(-3f, -3f, Tiers.DIAMOND, BlockTags.MINEABLE_WITH_PICKAXE, new Item.Properties()));
    public static final RegistryObject<Item> NETHERITE_HAMMER = ITEMS.createItem("netherite_hammer", () -> new AreaDiggerItem(-4f, -2.5f, Tiers.NETHERITE, BlockTags.MINEABLE_WITH_PICKAXE, new Item.Properties()));

    public static final RegistryObject<Item> ENTITY_FILTER = ITEMS.createItem("entity_filter", () -> new EntityFilterItem(PropertyUtil.stacksOnce()));
    public static final RegistryObject<Item> CACTUS_KEY = ITEMS.createItem("cactus_key", () -> new CactusKeyItem(PropertyUtil.stacksOnce()));

    public static final RegistryObject<Item> DYNAMITE = ITEMS.createItem("dynamite", () -> new DynamiteItem(new Item.Properties(), DynamiteType.GENERIC));
    public static final RegistryObject<Item> COMBAT_DYNAMITE = ITEMS.createItem("combat_dynamite", () -> new DynamiteItem(new Item.Properties(), DynamiteType.COMBAT));
    public static final RegistryObject<Item> FIERY_DYNAMITE = ITEMS.createItem("fiery_dynamite", () -> new DynamiteItem(new Item.Properties(), DynamiteType.FIERY));

    public static final RegistryObject<Item> RED_ENVELOPE = ITEMS.createItem("red_envelope", () -> new RedEnvelopeItem(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> KEY = ITEMS.createItem("key", () -> new Item(new Item.Properties()));

    private static Supplier<Item> basicItem() {
        return () -> new Item(new Item.Properties());
    }

    public static void setupTabs() {
        CreativeModeTabContentsPopulator.mod(PantzMod.MOD_ID)
                .tab(INGREDIENTS)
                .addItemsAfter(of(Items.IRON_INGOT), STEEL_INGOT)
                .addItemsAfter(of(Items.IRON_NUGGET), STEEL_NUGGET)
                .addItemsBefore(of(Items.BLAZE_ROD), SULFUR_SHARD, SULFUR_DUST)

                .tab(COMBAT)
                .addItemsBefore(of(Items.GOLDEN_SWORD), STEEL_SWORD)
                .addItemsBefore(of(Items.GOLDEN_AXE), STEEL_AXE)
                .addItemsBefore(of(Items.GOLDEN_HELMET), STEEL_HELMET, STEEL_CHESTPLATE, STEEL_LEGGINGS, STEEL_BOOTS)
                .addItemsBefore(of(Items.GOLDEN_HORSE_ARMOR), STEEL_HORSE_ARMOR)
                .addItemsAfter(of(Items.SNOWBALL), DYNAMITE, COMBAT_DYNAMITE, FIERY_DYNAMITE)

                .tab(TOOLS_AND_UTILITIES)
                .addItemsBefore(of(Items.GOLDEN_SHOVEL), STEEL_SHOVEL, STEEL_PICKAXE, STEEL_AXE, STEEL_HOE, EXCAVATOR, HAMMER)
                .addItemsAfter(of(Items.SHEARS), TROWEL)
                .addItemsAfter(of(Items.BRUSH), HONEY_DESERIALIZER)

                .addItemsAfter(of(Items.DIAMOND_HOE), DIAMOND_EXCAVATOR, DIAMOND_HAMMER)
                .addItemsAfter(of(Items.NETHERITE_HOE), NETHERITE_EXCAVATOR, NETHERITE_HAMMER)

                .addItemsAfter(of(Items.WRITABLE_BOOK), RED_ENVELOPE)
                .addItemsAfter(of(Items.BRUSH), KEY)

        ;

        CreativeModeTabContentsPopulator.mod(PantzMod.MOD_ID + "_config")
                .predicate(event -> modPredicate(event, TOOLS_AND_UTILITIES) && PMConfig.Common.COMMON.enableEntityFilter.get())
                .addItemsAfter(of(Items.NAME_TAG), ENTITY_FILTER)
                .predicate(event -> modPredicate(event, TOOLS_AND_UTILITIES) && PMConfig.Common.COMMON.enableCactusKey.get())
                .addItemsAfter(of(Items.BRUSH), CACTUS_KEY)
                ;
    }

    public static boolean modPredicate(BuildCreativeModeTabContentsEvent event, ResourceKey<CreativeModeTab> tab) {
        return event.getTabKey() == tab;
    }
}
