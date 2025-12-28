package pantz.mod.core.registry;

import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.RegistryObject;
import pantz.mod.common.block.*;
import pantz.mod.common.utils.LogicGateConditions;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.PMConstant;
import pantz.mod.core.other.PMProperties;

import java.util.function.Predicate;

import static net.minecraft.world.item.CreativeModeTabs.*;
import static net.minecraft.world.item.crafting.Ingredient.of;

public class PMBlocks {
    public static final BlockSubRegistryHelper BLOCKS = PantzMod.REGISTRY_HELPER.getBlockSubHelper();

    public static final RegistryObject<Block> STEEL_BLOCK = BLOCKS.createBlock("steel_block", () -> new Block(PMProperties.STEEL_BLOCK));
    public static final RegistryObject<Block> STEEL_BARS = BLOCKS.createBlock("steel_bars", () -> new IronBarsBlock(PMProperties.STEEL_BARS));
    public static final RegistryObject<Block> STEEL_DOOR = BLOCKS.createBlock("steel_door", () -> new DoorBlock(PMProperties.STEEL_DOOR, PMProperties.STEEL));
    public static final RegistryObject<Block> STEEL_TRAPDOOR = BLOCKS.createBlock("steel_trapdoor", () -> new TrapDoorBlock(PMProperties.STEEL_TRAPDOOR, PMProperties.STEEL));

    public static final RegistryObject<Block> NETHER_SULFUR_ORE = BLOCKS.createBlock("nether_sulfur_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.NETHER_QUARTZ_ORE), UniformInt.of(2, 4)));
    public static final RegistryObject<Block> SULFUR_BLOCK = BLOCKS.createBlock("sulfur_block", () -> new SulfurBlock(PMProperties.SULFUR_BLOCK));
    public static final RegistryObject<Block> SULFUR_BRICKS = BLOCKS.createBlock("sulfur_bricks", () -> new Block(PMProperties.SULFUR_BRICKS));
    public static final RegistryObject<Block> SULFUR_BRICK_STAIRS = BLOCKS.createBlock("sulfur_brick_stairs", () -> new StairBlock(() -> SULFUR_BRICKS.get().defaultBlockState(), PMProperties.SULFUR_BRICKS));
    public static final RegistryObject<Block> SULFUR_BRICK_SLAB = BLOCKS.createBlock("sulfur_brick_slab", () -> new SlabBlock(PMProperties.SULFUR_BRICKS));
    public static final RegistryObject<Block> SULFUR_BRICK_WALL = BLOCKS.createBlock("sulfur_brick_wall", () -> new WallBlock(PMProperties.SULFUR_BRICKS));
    public static final RegistryObject<Block> CHISELED_SULFUR_BRICKS = BLOCKS.createBlock("chiseled_sulfur_bricks", () -> new Block(PMProperties.SULFUR_BRICKS));

    public static final RegistryObject<Block> SMALL_SULFUR_BUD = BLOCKS.createBlock("small_sulfur_bud", () -> new NetherClusterBlock(3, 4, PMProperties.SMALL_SULFUR_BUD));
    public static final RegistryObject<Block> MEDIUM_SULFUR_BUD = BLOCKS.createBlock("medium_sulfur_bud", () -> new NetherClusterBlock(4, 3, PMProperties.MEDIUM_SULFUR_BUD));
    public static final RegistryObject<Block> LARGE_SULFUR_BUD = BLOCKS.createBlock("large_sulfur_bud", () -> new NetherClusterBlock(5, 3, PMProperties.LARGE_SULFUR_BUD));
    public static final RegistryObject<Block> SULFUR_CLUSTER = BLOCKS.createBlock("sulfur_cluster", () -> new NetherClusterBlock(7, 5, PMProperties.SULFUR_CLUSTER));

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

    public static final RegistryObject<Block> ADVANCED_AND_GATE = BLOCKS.createBlock("advanced_and_gate", () -> new AdvancedLogicGateBlock(PMProperties.DIODE, LogicGateConditions.ADVANCED_AND));
    public static final RegistryObject<Block> ADVANCED_OR_GATE = BLOCKS.createBlock("advanced_or_gate", () -> new AdvancedLogicGateBlock(PMProperties.DIODE, LogicGateConditions.ADVANCED_OR));
    public static final RegistryObject<Block> ADVANCED_NOR_GATE = BLOCKS.createBlock("advanced_nor_gate", () -> new AdvancedLogicGateBlock(PMProperties.DIODE, LogicGateConditions.ADVANCED_NOR));
    public static final RegistryObject<Block> ADVANCED_NAND_GATE = BLOCKS.createBlock("advanced_nand_gate", () -> new AdvancedLogicGateBlock(PMProperties.DIODE, LogicGateConditions.ADVANCED_NAND));
    public static final RegistryObject<Block> ADVANCED_XNOR_GATE = BLOCKS.createBlock("advanced_xnor_gate", () -> new AdvancedLogicGateBlock(PMProperties.DIODE, LogicGateConditions.ADVANCED_XNOR));
    public static final RegistryObject<Block> ADVANCED_XOR_GATE = BLOCKS.createBlock("advanced_xor_gate", () -> new AdvancedLogicGateBlock(PMProperties.DIODE, LogicGateConditions.ADVANCED_XOR));

    public static final RegistryObject<Block> MAJORITY_GATE = BLOCKS.createBlock("majority_gate", () -> new AdvancedLogicGateBlock(PMProperties.DIODE, LogicGateConditions.MAJORITY));
    public static final RegistryObject<Block> MINORITY_GATE = BLOCKS.createBlock("minority_gate", () -> new AdvancedLogicGateBlock(PMProperties.DIODE, LogicGateConditions.MINORITY));

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

    public static void setupTabs() {
        CreativeModeTabContentsPopulator.mod(PantzMod.MOD_ID)
                .tab(BUILDING_BLOCKS)
                .addItemsBefore(of(Blocks.GOLD_BLOCK), STEEL_BLOCK)
                .addItemsBefore(modLoaded(Blocks.GOLD_BLOCK, PMConstant.CAVERNS_AND_CHASMS), STEEL_BARS)
                .addItemsBefore(of(Blocks.GOLD_BLOCK), STEEL_DOOR, STEEL_TRAPDOOR)
                .addItemsBefore(of(Blocks.AMETHYST_BLOCK), SULFUR_BLOCK, SULFUR_BRICKS, CHISELED_SULFUR_BRICKS, SULFUR_BRICK_STAIRS, SULFUR_BRICK_SLAB, SULFUR_BRICK_WALL)

                .tab(NATURAL_BLOCKS)
                .addItemsAfter(of(Blocks.NETHER_QUARTZ_ORE), NETHER_SULFUR_ORE)
                .addItemsBefore(of(Blocks.AMETHYST_BLOCK), SULFUR_BLOCK, SMALL_SULFUR_BUD, MEDIUM_SULFUR_BUD, LARGE_SULFUR_BUD, SULFUR_CLUSTER)

                .tab(FUNCTIONAL_BLOCKS)
                .addItemsAfter(of(Blocks.DAMAGED_ANVIL), STONE_PEDESTAL, DEEPSLATE_PEDESTAL, BLACKSTONE_PEDESTAL, QUARTZ_PEDESTAL, PRISMARINE_PEDESTAL, PURPUR_PEDESTAL)
                .addItemsAfter(of(Blocks.REDSTONE_LAMP), WHITE_REDSTONE_LAMP, ORANGE_REDSTONE_LAMP, MAGENTA_REDSTONE_LAMP,
                        LIGHT_BLUE_REDSTONE_LAMP, YELLOW_REDSTONE_LAMP, LIME_REDSTONE_LAMP, PINK_REDSTONE_LAMP, GRAY_REDSTONE_LAMP, LIGHT_GRAY_REDSTONE_LAMP,
                        CYAN_REDSTONE_LAMP, PURPLE_REDSTONE_LAMP, BLUE_REDSTONE_LAMP, BROWN_REDSTONE_LAMP, GREEN_REDSTONE_LAMP, RED_REDSTONE_LAMP, BLACK_REDSTONE_LAMP)

                .tab(REDSTONE_BLOCKS)
                .addItemsAfter(of(Blocks.LECTERN), STONE_PEDESTAL)
                .addItemsAfter(of(Blocks.TARGET), ENDER_SCANNER)
                .addItemsAfter(of(Blocks.REDSTONE_BLOCK), REDSTONE_CONFIGURATOR, POWER_DISPLAYER)
                .addItemsAfter(of(Blocks.DAYLIGHT_DETECTOR), WEATHER_DETECTOR, ENTITY_DETECTOR)
                .addItemsAfter(of(Blocks.COMPARATOR), NOT_GATE, AND_GATE, OR_GATE, NAND_GATE, NOR_GATE, XOR_GATE, XNOR_GATE,
                        ADVANCED_AND_GATE, ADVANCED_OR_GATE, ADVANCED_NAND_GATE, ADVANCED_NOR_GATE, ADVANCED_XOR_GATE, ADVANCED_XNOR_GATE, MAJORITY_GATE, MINORITY_GATE)

                .tab(COLORED_BLOCKS)
                .addItems(() -> Blocks.REDSTONE_LAMP, WHITE_REDSTONE_LAMP, ORANGE_REDSTONE_LAMP, MAGENTA_REDSTONE_LAMP,
                        LIGHT_BLUE_REDSTONE_LAMP, YELLOW_REDSTONE_LAMP, LIME_REDSTONE_LAMP, PINK_REDSTONE_LAMP, GRAY_REDSTONE_LAMP, LIGHT_GRAY_REDSTONE_LAMP,
                        CYAN_REDSTONE_LAMP, PURPLE_REDSTONE_LAMP, BLUE_REDSTONE_LAMP, BROWN_REDSTONE_LAMP, GREEN_REDSTONE_LAMP, RED_REDSTONE_LAMP, BLACK_REDSTONE_LAMP)

        ;
    }

    public static Predicate<ItemStack> modLoaded(ItemLike item, String... modids) {
        return stack -> of(item).test(stack) && BlockSubRegistryHelper.areModsLoaded(modids);
    }
}
