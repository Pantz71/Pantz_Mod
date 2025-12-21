package pantz.mod.core.registry;

import com.teamabnormals.blueprint.core.util.item.CreativeModeTabContentsPopulator;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.RegistryObject;
import pantz.mod.common.block.EnderScannerBlock;
import pantz.mod.common.block.NetherClusterBlock;
import pantz.mod.common.block.SulfurBlock;
import pantz.mod.common.block.PedestalBlock;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.PMConstant;
import pantz.mod.core.other.PMProperties;

import java.util.function.Predicate;
import java.util.function.ToIntFunction;

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

                .tab(REDSTONE_BLOCKS)
                .addItemsAfter(of(Blocks.LECTERN), STONE_PEDESTAL)
                .addItemsAfter(of(Blocks.TARGET), ENDER_SCANNER)

        ;
    }

    public static Predicate<ItemStack> modLoaded(ItemLike item, String... modids) {
        return stack -> of(item).test(stack) && BlockSubRegistryHelper.areModsLoaded(modids);
    }
}
