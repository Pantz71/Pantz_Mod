package pantz.mod.core.registry;

import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.RegistryObject;
import pantz.mod.common.block.*;
import pantz.mod.common.block.glass.*;
import pantz.mod.common.utils.CompatHelper;
import pantz.mod.common.utils.LogicGateConditions;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.PMConstant;
import pantz.mod.core.other.PMProperties;
import pantz.mod.core.registry.helper.PMBlockSubRegistryHelper;

import java.util.List;
import java.util.function.Predicate;

import static net.minecraft.world.item.CreativeModeTabs.*;
import static net.minecraft.world.item.crafting.Ingredient.of;

public class PMBlocks {
    public static final PMBlockSubRegistryHelper BLOCKS = PantzMod.REGISTRY_HELPER.getBlockSubHelper();

    public static final RegistryObject<Block> STEEL_BLOCK = BLOCKS.createBlock("steel_block", () -> new Block(PMProperties.STEEL_BLOCK));
    public static final RegistryObject<Block> STEEL_BARS = BLOCKS.createBlock("steel_bars", () -> new IronBarsBlock(PMProperties.STEEL_BARS));
    public static final RegistryObject<Block> STEEL_DOOR = BLOCKS.createBlock("steel_door", () -> new DoorBlock(PMProperties.STEEL_DOOR, PMProperties.STEEL));
    public static final RegistryObject<Block> STEEL_TRAPDOOR = BLOCKS.createBlock("steel_trapdoor", () -> new TrapDoorBlock(PMProperties.STEEL_TRAPDOOR, PMProperties.STEEL));
    public static final RegistryObject<Block> STEEL_LANTERN = BLOCKS.createBlock("steel_lantern", () -> new SteelLanternBlock(PMProperties.STEEL_LANTERN));

    public static final RegistryObject<Block> STEEL_BRICKS = BLOCKS.createBlock("steel_bricks", () -> new Block(PMProperties.STEEL_PLATED_BRICKS));
    public static final RegistryObject<Block> STEEL_BRICK_STAIRS = BLOCKS.createBlock("steel_brick_stairs", () -> new StairBlock(() -> STEEL_BRICKS.get().defaultBlockState(), PMProperties.STEEL_PLATED_BRICKS));
    public static final RegistryObject<Block> STEEL_BRICK_SLAB = BLOCKS.createBlock("steel_brick_slab", () -> new SlabBlock(PMProperties.STEEL_PLATED_BRICKS));
    public static final RegistryObject<Block> STEEL_BRICK_WALL = BLOCKS.createBlock("steel_brick_wall", () -> new WallBlock(PMProperties.STEEL_PLATED_BRICKS));
    public static final RegistryObject<Block> CHISELED_STEEL_BRICKS = BLOCKS.createBlock("chiseled_steel_bricks", () -> new Block(PMProperties.STEEL_PLATED_BRICKS));

    public static final RegistryObject<Block> STEEL_INGOT = BLOCKS.createPlacedItem("steel_ingot", CompatHelper.block(List.of(PMConstant.CAVERNS_AND_CHASMS), $ -> PMConstant.STEEL_INGOT.get(), () -> BlockBehaviour.Properties.copy(STEEL_BLOCK.get())));

    public static final RegistryObject<Block> SULFUR_ORE = BLOCKS.createBlock("sulfur_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.COAL_ORE), UniformInt.of(2, 4)));
    public static final RegistryObject<Block> DEEPSLATE_SULFUR_ORE = BLOCKS.createBlock("deepslate_sulfur_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_COAL_ORE), UniformInt.of(2, 4)));
    public static final RegistryObject<Block> NETHER_SULFUR_ORE = BLOCKS.createBlock("nether_sulfur_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.NETHER_QUARTZ_ORE), UniformInt.of(2, 4)));
    public static final RegistryObject<Block> SULFUR = BLOCKS.createBlock("sulfur", () -> new Block(PMProperties.SULFUR));
    public static final RegistryObject<Block> SULFUR_BLOCK = BLOCKS.createBlock("sulfur_block", () -> new SulfurBlock(PMProperties.SULFUR_BLOCK));
    public static final RegistryObject<Block> POLISHED_SULFUR = BLOCKS.createBlock("polished_sulfur", () -> new Block(PMProperties.SULFUR_BLOCK));
    public static final RegistryObject<Block> POLISHED_SULFUR_STAIRS = BLOCKS.createBlock("polished_sulfur_stairs", () -> new StairBlock(() -> POLISHED_SULFUR.get().defaultBlockState(), PMProperties.SULFUR_BLOCK));
    public static final RegistryObject<Block> POLISHED_SULFUR_SLAB = BLOCKS.createBlock("polished_sulfur_slab", () -> new SlabBlock(PMProperties.SULFUR_BLOCK));
    public static final RegistryObject<Block> POLISHED_SULFUR_WALL = BLOCKS.createBlock("polished_sulfur_wall", () -> new WallBlock(PMProperties.SULFUR_BLOCK));
    public static final RegistryObject<Block> SULFUR_BRICKS = BLOCKS.createBlock("sulfur_bricks", () -> new Block(PMProperties.SULFUR_BLOCK));
    public static final RegistryObject<Block> SULFUR_BRICK_STAIRS = BLOCKS.createBlock("sulfur_brick_stairs", () -> new StairBlock(() -> SULFUR_BRICKS.get().defaultBlockState(), PMProperties.SULFUR_BLOCK));
    public static final RegistryObject<Block> SULFUR_BRICK_SLAB = BLOCKS.createBlock("sulfur_brick_slab", () -> new SlabBlock(PMProperties.SULFUR_BLOCK));
    public static final RegistryObject<Block> SULFUR_BRICK_WALL = BLOCKS.createBlock("sulfur_brick_wall", () -> new WallBlock(PMProperties.SULFUR_BLOCK));
    public static final RegistryObject<Block> CHISELED_SULFUR_BRICKS = BLOCKS.createBlock("chiseled_sulfur_bricks", () -> new Block(PMProperties.SULFUR_BLOCK));
    public static final RegistryObject<Block> SULFUR_LAMP = BLOCKS.createBlock("sulfur_lamp", () -> new Block(PMProperties.SULFUR_LAMP));

    public static final RegistryObject<Block> SMALL_SULFUR_BUD = BLOCKS.createBlock("small_sulfur_bud", () -> new SulfurClusterBlock(3, 4, PMProperties.SMALL_SULFUR_BUD));
    public static final RegistryObject<Block> MEDIUM_SULFUR_BUD = BLOCKS.createBlock("medium_sulfur_bud", () -> new SulfurClusterBlock(4, 3, PMProperties.MEDIUM_SULFUR_BUD));
    public static final RegistryObject<Block> LARGE_SULFUR_BUD = BLOCKS.createBlock("large_sulfur_bud", () -> new SulfurClusterBlock(5, 3, PMProperties.LARGE_SULFUR_BUD));
    public static final RegistryObject<Block> SULFUR_CLUSTER = BLOCKS.createBlock("sulfur_cluster", () -> new SulfurClusterBlock(7, 5, PMProperties.SULFUR_CLUSTER));

    public static final RegistryObject<Block> STONE_PEDESTAL = BLOCKS.createBlock("stone_pedestal", () -> new PedestalBlock(PMProperties.PEDESTAL));
    public static final RegistryObject<Block> DEEPSLATE_PEDESTAL = BLOCKS.createBlock("deepslate_pedestal", () -> new PedestalBlock(PMProperties.DEEPSLATE_PEDESTAL));
    public static final RegistryObject<Block> BLACKSTONE_PEDESTAL = BLOCKS.createBlock("blackstone_pedestal", () -> new PedestalBlock(PMProperties.BLACKSTONE_PEDESTAL));
    public static final RegistryObject<Block> QUARTZ_PEDESTAL = BLOCKS.createBlock("quartz_pedestal", () -> new PedestalBlock(PMProperties.QUARTZ_PEDESTAL));
    public static final RegistryObject<Block> PURPUR_PEDESTAL = BLOCKS.createBlock("purpur_pedestal", () -> new PedestalBlock(PMProperties.PURPUR_PEDESTAL));
    public static final RegistryObject<Block> PRISMARINE_PEDESTAL = BLOCKS.createBlock("prismarine_pedestal", () -> new PedestalBlock(PMProperties.PRISMARINE_PEDESTAL));

    public static final RegistryObject<Block> ENDER_SCANNER = BLOCKS.createBlock("ender_scanner", () -> new EnderScannerBlock(PMProperties.ENDER_SCANNER));
    public static final RegistryObject<Block> REDSTONE_CONFIGURATOR = BLOCKS.createBlock("redstone_configurator", () -> new RedstoneConfiguratorBlock(PMProperties.REDSTONE_CONFIGURATOR));
    public static final RegistryObject<Block> WEATHER_DETECTOR = BLOCKS.createBlock("weather_detector", () -> new WeatherDetectorBlock(PMProperties.WEATHER_DETECTOR));
    public static final RegistryObject<Block> ENTITY_DETECTOR = BLOCKS.createBlock("entity_detector", () -> new EntityDetectorBlock(PMProperties.ENTITY_DETECTOR));
    public static final RegistryObject<Block> POWER_DISPLAYER = BLOCKS.createBlock("power_displayer", () -> new PowerDisplayerBlock(PMProperties.POWER_DISPLAYER));

    public static final RegistryObject<Block> NOT_GATE = BLOCKS.createBlock("not_gate", () -> new NotGateBlock(PMProperties.DIODE));

    public static final RegistryObject<Block> AND_GATE = BLOCKS.createBlock("and_gate", () -> new LogicGateBlock(PMProperties.DIODE, LogicGateConditions.AND));
    public static final RegistryObject<Block> OR_GATE = BLOCKS.createBlock("or_gate", () -> new LogicGateBlock(PMProperties.DIODE, LogicGateConditions.OR));
    public static final RegistryObject<Block> NOR_GATE = BLOCKS.createBlock("nor_gate", () -> new LogicGateBlock(PMProperties.DIODE, LogicGateConditions.NOR));
    public static final RegistryObject<Block> NAND_GATE = BLOCKS.createBlock("nand_gate", () -> new LogicGateBlock(PMProperties.DIODE, LogicGateConditions.NAND));
    public static final RegistryObject<Block> XNOR_GATE = BLOCKS.createBlock("xnor_gate", () -> new LogicGateBlock(PMProperties.DIODE, LogicGateConditions.XNOR));
    public static final RegistryObject<Block> XOR_GATE = BLOCKS.createBlock("xor_gate", () -> new LogicGateBlock(PMProperties.DIODE, LogicGateConditions.XOR));

    public static final RegistryObject<Block> ADVANCED_AND_GATE = BLOCKS.createBlock("advanced_and_gate", () -> new AdvancedLogicGateBlock(PMProperties.ADVANCED_DIODE, LogicGateConditions.ADVANCED_AND));
    public static final RegistryObject<Block> ADVANCED_OR_GATE = BLOCKS.createBlock("advanced_or_gate", () -> new AdvancedLogicGateBlock(PMProperties.ADVANCED_DIODE, LogicGateConditions.ADVANCED_OR));
    public static final RegistryObject<Block> ADVANCED_NOR_GATE = BLOCKS.createBlock("advanced_nor_gate", () -> new AdvancedLogicGateBlock(PMProperties.ADVANCED_DIODE, LogicGateConditions.ADVANCED_NOR));
    public static final RegistryObject<Block> ADVANCED_NAND_GATE = BLOCKS.createBlock("advanced_nand_gate", () -> new AdvancedLogicGateBlock(PMProperties.ADVANCED_DIODE, LogicGateConditions.ADVANCED_NAND));
    public static final RegistryObject<Block> ADVANCED_XNOR_GATE = BLOCKS.createBlock("advanced_xnor_gate", () -> new AdvancedLogicGateBlock(PMProperties.ADVANCED_DIODE, LogicGateConditions.ADVANCED_XNOR));
    public static final RegistryObject<Block> ADVANCED_XOR_GATE = BLOCKS.createBlock("advanced_xor_gate", () -> new AdvancedLogicGateBlock(PMProperties.ADVANCED_DIODE, LogicGateConditions.ADVANCED_XOR));

    public static final RegistryObject<Block> MAJORITY_GATE = BLOCKS.createBlock("majority_gate", () -> new AdvancedLogicGateBlock(PMProperties.ADVANCED_DIODE, LogicGateConditions.MAJORITY));
    public static final RegistryObject<Block> MINORITY_GATE = BLOCKS.createBlock("minority_gate", () -> new AdvancedLogicGateBlock(PMProperties.ADVANCED_DIODE, LogicGateConditions.MINORITY));

    public static final RegistryObject<Block> EARTH_GLOBE = BLOCKS.createCustomRarityBlock("earth_globe", Rarity.UNCOMMON, () -> new GlobeBlock(PMProperties.GLOBE, PantzMod.location("planets/earth")));
    public static final RegistryObject<Block> MERCURY_GLOBE = BLOCKS.createCustomRarityBlock("mercury_globe", Rarity.UNCOMMON, () -> new GlobeBlock(PMProperties.GLOBE, PantzMod.location("planets/mercury")));
    public static final RegistryObject<Block> VENUS_GLOBE = BLOCKS.createCustomRarityBlock("venus_globe", Rarity.UNCOMMON, () -> new GlobeBlock(PMProperties.GLOBE, PantzMod.location("planets/venus")));
    public static final RegistryObject<Block> MARS_GLOBE = BLOCKS.createCustomRarityBlock("mars_globe", Rarity.UNCOMMON, () -> new GlobeBlock(PMProperties.GLOBE, PantzMod.location("planets/mars")));

    public static final RegistryObject<Block> JUPITER_GLOBE = BLOCKS.createCustomRarityBlock("jupiter_globe", Rarity.UNCOMMON, () -> new GlobeBlock(PMProperties.GAS_GLOBE, PantzMod.location("large_planets/jupiter")));
    public static final RegistryObject<Block> SATURN_GLOBE = BLOCKS.createCustomRarityBlock("saturn_globe", Rarity.UNCOMMON, () -> new GlobeBlock(PMProperties.GAS_GLOBE, PantzMod.location("large_planets/saturn")));
    public static final RegistryObject<Block> URANUS_GLOBE = BLOCKS.createCustomRarityBlock("uranus_globe", Rarity.UNCOMMON, () -> new GlobeBlock(PMProperties.GLOBE, PantzMod.location("large_planets/uranus")));
    public static final RegistryObject<Block> NEPTUNE_GLOBE = BLOCKS.createCustomRarityBlock("neptune_globe", Rarity.UNCOMMON, () -> new GlobeBlock(PMProperties.GLOBE, PantzMod.location("large_planets/neptune")));

    public static final RegistryObject<Block> PLUTO_GLOBE = BLOCKS.createCustomRarityBlock("pluto_globe", Rarity.UNCOMMON, () -> new GlobeBlock(PMProperties.GLOBE, PantzMod.location("dwarf_planets/pluto")));
    public static final RegistryObject<Block> CERES_GLOBE = BLOCKS.createCustomRarityBlock("ceres_globe", Rarity.UNCOMMON, () -> new GlobeBlock(PMProperties.GLOBE, PantzMod.location("dwarf_planets/ceres")));
    public static final RegistryObject<Block> MAKEMAKE_GLOBE = BLOCKS.createCustomRarityBlock("makemake_globe", Rarity.UNCOMMON, () -> new GlobeBlock(PMProperties.GLOBE, PantzMod.location("dwarf_planets/makemake")));

    public static final RegistryObject<Block> MOON_GLOBE = BLOCKS.createCustomRarityBlock("moon_globe", Rarity.UNCOMMON, () -> new GlobeBlock(PMProperties.GLOBE, PantzMod.location("moons/moon")));
    public static final RegistryObject<Block> IO_GLOBE = BLOCKS.createCustomRarityBlock("io_globe", Rarity.UNCOMMON, () -> new GlobeBlock(PMProperties.GLOBE, PantzMod.location("moons/io")));
    public static final RegistryObject<Block> EUROPA_GLOBE = BLOCKS.createCustomRarityBlock("europa_globe", Rarity.UNCOMMON, () -> new GlobeBlock(PMProperties.GLOBE, PantzMod.location("moons/europa")));
    public static final RegistryObject<Block> CALLISTO_GLOBE = BLOCKS.createCustomRarityBlock("callisto_globe", Rarity.UNCOMMON, () -> new GlobeBlock(PMProperties.GLOBE, PantzMod.location("moons/callisto")));
    public static final RegistryObject<Block> GANYMEDE_GLOBE = BLOCKS.createCustomRarityBlock("ganymede_globe", Rarity.UNCOMMON, () -> new GlobeBlock(PMProperties.GLOBE, PantzMod.location("moons/ganymede")));

    public static final RegistryObject<Block> SUN_GLOBE = BLOCKS.createCustomRarityBlock("sun_globe", Rarity.RARE, () -> new GlobeBlock(PMProperties.STAR_GLOBE, PantzMod.location("stars/sun")));
    public static final RegistryObject<Block> BLUE_SUN_GLOBE = BLOCKS.createCustomRarityBlock("blue_sun_globe", Rarity.RARE, () -> new GlobeBlock(PMProperties.STAR_GLOBE, PantzMod.location("stars/blue_sun")));

    public static final RegistryObject<Block> IRIS_GLOBE = BLOCKS.createCustomRarityBlock("iris_globe", Rarity.EPIC, () -> new GlobeBlock(PMProperties.GLOBE, PantzMod.location("large_planets/iris")));

    public static final RegistryObject<Block> WHITE_REDSTONE_LAMP = BLOCKS.createBlock("white_redstone_lamp", () -> new RedstoneLampBlock(PMProperties.REDSTONE_LAMP.mapColor(DyeColor.WHITE)));
    public static final RegistryObject<Block> ORANGE_REDSTONE_LAMP = BLOCKS.createBlock("orange_redstone_lamp", () -> new RedstoneLampBlock(PMProperties.REDSTONE_LAMP.mapColor(DyeColor.ORANGE)));
    public static final RegistryObject<Block> MAGENTA_REDSTONE_LAMP = BLOCKS.createBlock("magenta_redstone_lamp", () -> new RedstoneLampBlock(PMProperties.REDSTONE_LAMP.mapColor(DyeColor.MAGENTA)));
    public static final RegistryObject<Block> LIGHT_BLUE_REDSTONE_LAMP = BLOCKS.createBlock("light_blue_redstone_lamp", () -> new RedstoneLampBlock(PMProperties.REDSTONE_LAMP.mapColor(DyeColor.LIGHT_BLUE)));
    public static final RegistryObject<Block> YELLOW_REDSTONE_LAMP = BLOCKS.createBlock("yellow_redstone_lamp", () -> new RedstoneLampBlock(PMProperties.REDSTONE_LAMP.mapColor(DyeColor.YELLOW)));
    public static final RegistryObject<Block> LIME_REDSTONE_LAMP = BLOCKS.createBlock("lime_redstone_lamp", () -> new RedstoneLampBlock(PMProperties.REDSTONE_LAMP.mapColor(DyeColor.LIME)));
    public static final RegistryObject<Block> PINK_REDSTONE_LAMP = BLOCKS.createBlock("pink_redstone_lamp", () -> new RedstoneLampBlock(PMProperties.REDSTONE_LAMP.mapColor(DyeColor.PINK)));
    public static final RegistryObject<Block> GRAY_REDSTONE_LAMP = BLOCKS.createBlock("gray_redstone_lamp", () -> new RedstoneLampBlock(PMProperties.REDSTONE_LAMP.mapColor(DyeColor.GRAY)));
    public static final RegistryObject<Block> LIGHT_GRAY_REDSTONE_LAMP = BLOCKS.createBlock("light_gray_redstone_lamp", () -> new RedstoneLampBlock(PMProperties.REDSTONE_LAMP.mapColor(DyeColor.LIGHT_GRAY)));
    public static final RegistryObject<Block> CYAN_REDSTONE_LAMP = BLOCKS.createBlock("cyan_redstone_lamp", () -> new RedstoneLampBlock(PMProperties.REDSTONE_LAMP.mapColor(DyeColor.CYAN)));
    public static final RegistryObject<Block> PURPLE_REDSTONE_LAMP = BLOCKS.createBlock("purple_redstone_lamp", () -> new RedstoneLampBlock(PMProperties.REDSTONE_LAMP.mapColor(DyeColor.PURPLE)));
    public static final RegistryObject<Block> BLUE_REDSTONE_LAMP = BLOCKS.createBlock("blue_redstone_lamp", () -> new RedstoneLampBlock(PMProperties.REDSTONE_LAMP.mapColor(DyeColor.BLUE)));
    public static final RegistryObject<Block> BROWN_REDSTONE_LAMP = BLOCKS.createBlock("brown_redstone_lamp", () -> new RedstoneLampBlock(PMProperties.REDSTONE_LAMP.mapColor(DyeColor.BROWN)));
    public static final RegistryObject<Block> GREEN_REDSTONE_LAMP = BLOCKS.createBlock("green_redstone_lamp", () -> new RedstoneLampBlock(PMProperties.REDSTONE_LAMP.mapColor(DyeColor.GREEN)));
    public static final RegistryObject<Block> RED_REDSTONE_LAMP = BLOCKS.createBlock("red_redstone_lamp", () -> new RedstoneLampBlock(PMProperties.REDSTONE_LAMP.mapColor(DyeColor.RED)));
    public static final RegistryObject<Block> BLACK_REDSTONE_LAMP = BLOCKS.createBlock("black_redstone_lamp", () -> new RedstoneLampBlock(PMProperties.REDSTONE_LAMP.mapColor(DyeColor.BLACK)));

    public static final RegistryObject<Block> ITEM_STAND = BLOCKS.createBlock("item_stand", () -> new ItemStandBlock(PMProperties.ITEM_STAND));
    public static final RegistryObject<Block> GLOW_ITEM_STAND = BLOCKS.createBlock("glow_item_stand", () -> new ItemStandBlock(PMProperties.ITEM_STAND));
    public static final RegistryObject<Block> TRASH_CAN = BLOCKS.createBlock("trash_can", () -> new TrashCanBlock(PMProperties.TRASH_CAN));
    public static final RegistryObject<Block> ENDERPORTER = BLOCKS.createBlock("enderporter", () -> new EnderporterBlock(PMProperties.ENDERPORTER));
    public static final RegistryObject<Block> ROPE_LADDER = BLOCKS.createBlock("rope_ladder", () -> new RopeLadderBlock(BlockBehaviour.Properties.copy(Blocks.LADDER)));

    public static final RegistryObject<Block> CHORUS_GLASS = BLOCKS.createBlock("chorus_glass", () -> new ChorusGlassBlock(PMProperties.CHORUS_GLASS));
    public static final RegistryObject<Block> CHORUS_GLASS_PANE = BLOCKS.createBlock("chorus_glass_pane", () -> new ChorusGlassPaneBlock(PMProperties.CHORUS_GLASS_PANE));
    public static final RegistryObject<Block> SOUL_GLASS = BLOCKS.createBlock("soul_glass", () -> new SoulGlassBlock(PMProperties.SOUL_GLASS));
    public static final RegistryObject<Block> SOUL_GLASS_PANE = BLOCKS.createBlock("soul_glass_pane", () -> new SoulGlassPaneBlock(PMProperties.SOUL_GLASS_PANE));
    public static final RegistryObject<Block> ECHO_GLASS = BLOCKS.createBlock("echo_glass", () -> new EchoGlassBlock(PMProperties.ECHO_GLASS));
    public static final RegistryObject<Block> ECHO_GLASS_PANE = BLOCKS.createBlock("echo_glass_pane", () -> new EchoGlassPaneBlock(PMProperties.ECHO_GLASS_PANE));

    public static final RegistryObject<Block> WHITE_PAPER_LANTERN = BLOCKS.createBlock("white_paper_lantern", () -> new PaperLanternBlock(PMProperties.PAPER_LANTERN));
    public static final RegistryObject<Block> ORANGE_PAPER_LANTERN = BLOCKS.createBlock("orange_paper_lantern", () -> new PaperLanternBlock(PMProperties.PAPER_LANTERN));
    public static final RegistryObject<Block> MAGENTA_PAPER_LANTERN = BLOCKS.createBlock("magenta_paper_lantern", () -> new PaperLanternBlock(PMProperties.PAPER_LANTERN));
    public static final RegistryObject<Block> LIGHT_BLUE_PAPER_LANTERN = BLOCKS.createBlock("light_blue_paper_lantern", () -> new PaperLanternBlock(PMProperties.PAPER_LANTERN));
    public static final RegistryObject<Block> YELLOW_PAPER_LANTERN = BLOCKS.createBlock("yellow_paper_lantern", () -> new PaperLanternBlock(PMProperties.PAPER_LANTERN));
    public static final RegistryObject<Block> LIME_PAPER_LANTERN = BLOCKS.createBlock("lime_paper_lantern", () -> new PaperLanternBlock(PMProperties.PAPER_LANTERN));
    public static final RegistryObject<Block> PINK_PAPER_LANTERN = BLOCKS.createBlock("pink_paper_lantern", () -> new PaperLanternBlock(PMProperties.PAPER_LANTERN));
    public static final RegistryObject<Block> GRAY_PAPER_LANTERN = BLOCKS.createBlock("gray_paper_lantern", () -> new PaperLanternBlock(PMProperties.PAPER_LANTERN));
    public static final RegistryObject<Block> LIGHT_GRAY_PAPER_LANTERN = BLOCKS.createBlock("light_gray_paper_lantern", () -> new PaperLanternBlock(PMProperties.PAPER_LANTERN));
    public static final RegistryObject<Block> CYAN_PAPER_LANTERN = BLOCKS.createBlock("cyan_paper_lantern", () -> new PaperLanternBlock(PMProperties.PAPER_LANTERN));
    public static final RegistryObject<Block> PURPLE_PAPER_LANTERN = BLOCKS.createBlock("purple_paper_lantern", () -> new PaperLanternBlock(PMProperties.PAPER_LANTERN));
    public static final RegistryObject<Block> BLUE_PAPER_LANTERN = BLOCKS.createBlock("blue_paper_lantern", () -> new PaperLanternBlock(PMProperties.PAPER_LANTERN));
    public static final RegistryObject<Block> BROWN_PAPER_LANTERN = BLOCKS.createBlock("brown_paper_lantern", () -> new PaperLanternBlock(PMProperties.PAPER_LANTERN));
    public static final RegistryObject<Block> GREEN_PAPER_LANTERN = BLOCKS.createBlock("green_paper_lantern", () -> new PaperLanternBlock(PMProperties.PAPER_LANTERN));
    public static final RegistryObject<Block> RED_PAPER_LANTERN = BLOCKS.createBlock("red_paper_lantern", () -> new PaperLanternBlock(PMProperties.PAPER_LANTERN));
    public static final RegistryObject<Block> BLACK_PAPER_LANTERN = BLOCKS.createBlock("black_paper_lantern", () -> new PaperLanternBlock(PMProperties.PAPER_LANTERN));

    public static final RegistryObject<Block> ORNAMENT_FIRECRACKERS = BLOCKS.createBlock("ornament_firecrackers", () -> new DoubleOrnamentBlock(PMProperties.ORNAMENT));
    public static final RegistryObject<Block> ORNAMENT_LUCKY_COINS = BLOCKS.createBlock("ornament_lucky_coins", () -> new DoubleOrnamentBlock(PMProperties.ORNAMENT));

    public static final RegistryObject<Block> LOCK = BLOCKS.createBlock("lock", () -> new LockBlock(PMProperties.LOCK));
    public static final RegistryObject<Block> UNIVERSAL_LOCK = BLOCKS.createBlock("universal_lock", () -> new UniversalLockBlock(PMProperties.UNIVERSAL_LOCK));
    public static final RegistryObject<Block> SAFE = BLOCKS.createBlock("safe", () -> new SafeBlock(PMProperties.SAFE));
    public static final RegistryObject<Block> RANDOMIZER = BLOCKS.createBlock("randomizer", () -> new RandomizerBlock(PMProperties.DIODE));
    public static final RegistryObject<Block> EQUALIZER = BLOCKS.createBlock("equalizer", () -> new EqualizerBlock(PMProperties.DIODE));
    public static final RegistryObject<Block> SPIKE = BLOCKS.createBlock("spike", () -> new SpikeBlock(PMProperties.SPIKE));
    public static final RegistryObject<Block> SPRINKLER = BLOCKS.createBlock("sprinkler", () -> new SprinklerBlock(PMProperties.SPRINKLER));

    // TODO: Update texture for Steel Door, Steel Trapdoor, Dynamites, Magic Glasses
    // TODO: Port Warden Defeated Loot condition to NeoForge
    // TODO: Update ORNAMENT_LUCKY_COINS' model
    // TODO: Remove Packed Ice Bricks, Blue Ice Bricks, Snow Bricks on NeoForge
    public static void setupTabs() {
        CreativeModeTabContentsPopulator.mod(PantzMod.MOD_ID)
                .tab(BUILDING_BLOCKS)
                .addItemsBefore(of(Blocks.GOLD_BLOCK), STEEL_BLOCK)
                .addItemsBefore(modLoaded(Blocks.GOLD_BLOCK, PMConstant.CAVERNS_AND_CHASMS), STEEL_BRICKS, STEEL_BRICK_STAIRS, STEEL_BRICK_SLAB, STEEL_BRICK_WALL, CHISELED_STEEL_BRICKS, STEEL_BARS)
                .addItemsBefore(of(Blocks.GOLD_BLOCK), STEEL_DOOR, STEEL_TRAPDOOR)
                .addItemsBefore(of(Blocks.AMETHYST_BLOCK), SULFUR_BLOCK, POLISHED_SULFUR, POLISHED_SULFUR_STAIRS, POLISHED_SULFUR_SLAB, POLISHED_SULFUR_WALL,
                        SULFUR_BRICKS, CHISELED_SULFUR_BRICKS, SULFUR_BRICK_STAIRS, SULFUR_BRICK_SLAB, SULFUR_BRICK_WALL, SULFUR_LAMP)

                .tab(NATURAL_BLOCKS)
                .addItemsBefore(of(Blocks.AMETHYST_BLOCK), SULFUR, SMALL_SULFUR_BUD, MEDIUM_SULFUR_BUD, LARGE_SULFUR_BUD, SULFUR_CLUSTER)
                .addItemsAfter(of(Blocks.DEEPSLATE_DIAMOND_ORE), SULFUR_ORE, DEEPSLATE_SULFUR_ORE)
                .addItemsAfter(of(Blocks.NETHER_QUARTZ_ORE), NETHER_SULFUR_ORE)

                .tab(FUNCTIONAL_BLOCKS)
                .addItemsBefore(of(Blocks.TINTED_GLASS), CHORUS_GLASS, CHORUS_GLASS_PANE, SOUL_GLASS, SOUL_GLASS_PANE, ECHO_GLASS, ECHO_GLASS_PANE)
                .addItemsBefore(of(Items.PAINTING), ITEM_STAND, GLOW_ITEM_STAND)
                .addItemsBefore(of(Blocks.SCAFFOLDING), ROPE_LADDER)
                .addItemsBefore(of(Blocks.BARREL), TRASH_CAN, SAFE)
                .addItemsBefore(of(Blocks.CHAIN), STEEL_LANTERN,
                        WHITE_PAPER_LANTERN, ORANGE_PAPER_LANTERN, MAGENTA_PAPER_LANTERN,
                        LIGHT_BLUE_PAPER_LANTERN, YELLOW_PAPER_LANTERN, LIME_PAPER_LANTERN, PINK_PAPER_LANTERN, GRAY_PAPER_LANTERN, LIGHT_GRAY_PAPER_LANTERN,
                        CYAN_PAPER_LANTERN, PURPLE_PAPER_LANTERN, BLUE_PAPER_LANTERN, BROWN_PAPER_LANTERN, GREEN_PAPER_LANTERN, RED_PAPER_LANTERN, BLACK_PAPER_LANTERN)
                .addItemsAfter(of(Blocks.DAMAGED_ANVIL), STONE_PEDESTAL, DEEPSLATE_PEDESTAL, BLACKSTONE_PEDESTAL, QUARTZ_PEDESTAL, PRISMARINE_PEDESTAL, PURPUR_PEDESTAL)
                .addItemsAfter(of(Blocks.BELL), MERCURY_GLOBE, VENUS_GLOBE, EARTH_GLOBE, MARS_GLOBE, JUPITER_GLOBE, SATURN_GLOBE, URANUS_GLOBE, NEPTUNE_GLOBE, PLUTO_GLOBE, CERES_GLOBE, MAKEMAKE_GLOBE, MOON_GLOBE, IO_GLOBE, EUROPA_GLOBE, CALLISTO_GLOBE, GANYMEDE_GLOBE, SUN_GLOBE, BLUE_SUN_GLOBE, IRIS_GLOBE)
                .addItemsAfter(of(Blocks.REDSTONE_LAMP), WHITE_REDSTONE_LAMP, ORANGE_REDSTONE_LAMP, MAGENTA_REDSTONE_LAMP,
                        LIGHT_BLUE_REDSTONE_LAMP, YELLOW_REDSTONE_LAMP, LIME_REDSTONE_LAMP, PINK_REDSTONE_LAMP, GRAY_REDSTONE_LAMP, LIGHT_GRAY_REDSTONE_LAMP,
                        CYAN_REDSTONE_LAMP, PURPLE_REDSTONE_LAMP, BLUE_REDSTONE_LAMP, BROWN_REDSTONE_LAMP, GREEN_REDSTONE_LAMP, RED_REDSTONE_LAMP, BLACK_REDSTONE_LAMP)
                .addItemsAfter(of(Blocks.RESPAWN_ANCHOR), ENDERPORTER)
                .addItemsAfter(of(Raid.getLeaderBannerInstance()), ORNAMENT_FIRECRACKERS, ORNAMENT_LUCKY_COINS)
                .addItemsAfter(of(Blocks.RESPAWN_ANCHOR), SPRINKLER)

                .tab(REDSTONE_BLOCKS)
                .addItemsBefore(of(Blocks.DAYLIGHT_DETECTOR), EARTH_GLOBE)
                .addItemsBefore(of(Blocks.LEVER), ENDER_SCANNER)
                .addItemsBefore(of(Blocks.JUKEBOX), TRASH_CAN)
                .addItemsBefore(of(Blocks.TNT), SPIKE)
                .addItemsBefore(modNotLoaded(Blocks.TARGET, PMConstant.CAVERNS_AND_CHASMS), RANDOMIZER)
                .addItemsBefore(of(Blocks.TARGET), EQUALIZER, NOT_GATE, AND_GATE, OR_GATE, NAND_GATE, NOR_GATE, XOR_GATE, XNOR_GATE,
                        ADVANCED_AND_GATE, ADVANCED_OR_GATE, ADVANCED_NAND_GATE, ADVANCED_NOR_GATE, ADVANCED_XOR_GATE, ADVANCED_XNOR_GATE, MAJORITY_GATE, MINORITY_GATE)
                .addItemsAfter(of(Blocks.REDSTONE_BLOCK), REDSTONE_CONFIGURATOR, POWER_DISPLAYER)
                .addItemsAfter(of(Blocks.TARGET), LOCK, UNIVERSAL_LOCK)
                .addItemsAfter(of(Blocks.DAYLIGHT_DETECTOR), WEATHER_DETECTOR, ENTITY_DETECTOR)

                .tab(COLORED_BLOCKS)
                .addItemsBefore(of(Blocks.TINTED_GLASS), CHORUS_GLASS, SOUL_GLASS, ECHO_GLASS)
                .addItemsBefore(of(Blocks.WHITE_STAINED_GLASS_PANE), CHORUS_GLASS_PANE, SOUL_GLASS_PANE, ECHO_GLASS_PANE)
                .addItems(() -> Blocks.REDSTONE_LAMP, WHITE_REDSTONE_LAMP, ORANGE_REDSTONE_LAMP, MAGENTA_REDSTONE_LAMP,
                        LIGHT_BLUE_REDSTONE_LAMP, YELLOW_REDSTONE_LAMP, LIME_REDSTONE_LAMP, PINK_REDSTONE_LAMP, GRAY_REDSTONE_LAMP, LIGHT_GRAY_REDSTONE_LAMP,
                        CYAN_REDSTONE_LAMP, PURPLE_REDSTONE_LAMP, BLUE_REDSTONE_LAMP, BROWN_REDSTONE_LAMP, GREEN_REDSTONE_LAMP, RED_REDSTONE_LAMP, BLACK_REDSTONE_LAMP,
                        WHITE_PAPER_LANTERN, ORANGE_PAPER_LANTERN, MAGENTA_PAPER_LANTERN,
                        LIGHT_BLUE_PAPER_LANTERN, YELLOW_PAPER_LANTERN, LIME_PAPER_LANTERN, PINK_PAPER_LANTERN, GRAY_PAPER_LANTERN, LIGHT_GRAY_PAPER_LANTERN,
                        CYAN_PAPER_LANTERN, PURPLE_PAPER_LANTERN, BLUE_PAPER_LANTERN, BROWN_PAPER_LANTERN, GREEN_PAPER_LANTERN, RED_PAPER_LANTERN, BLACK_PAPER_LANTERN)


        ;
    }

    public static Predicate<ItemStack> modNotLoaded(ItemLike item, String... modids) {
        return stack -> of(item).test(stack) && areModsNotLoaded(modids);
    }

    public static Predicate<ItemStack> modLoaded(ItemLike item, String... modids) {
        return stack -> of(item).test(stack) && PMBlockSubRegistryHelper.areModsLoaded(modids);
    }

    public static boolean areModsNotLoaded(String... modIds) {
        if ("true".equals(System.getProperty("blueprint.indev")))
            return false;
        ModList modList = ModList.get();
        for (String mod : modIds)
            if (modList.isLoaded(mod))
                return false;
        return true;
    }
}
