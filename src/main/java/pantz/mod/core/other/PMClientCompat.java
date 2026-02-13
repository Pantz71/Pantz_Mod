package pantz.mod.core.other;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import pantz.mod.core.registry.datapack.PMTrimMaterials;

import static pantz.mod.core.registry.PMBlocks.*;

public class PMClientCompat {
    public static void registerClientCompat() {
        registerRenderLayers();
        PMTrimMaterials.registerArmorMaterialOverrides();
    }

    @SuppressWarnings("deprecation")
    private static void registerRenderLayers() {
        for (RegistryObject<?> block : new RegistryObject[]{
                STEEL_BARS, STEEL_DOOR, STEEL_TRAPDOOR, STEEL_LANTERN,
                SULFUR_CLUSTER, SMALL_SULFUR_BUD, MEDIUM_SULFUR_BUD, LARGE_SULFUR_BUD,
                STONE_PEDESTAL, DEEPSLATE_PEDESTAL, BLACKSTONE_PEDESTAL, QUARTZ_PEDESTAL, PURPUR_PEDESTAL, PRISMARINE_PEDESTAL,
                NOT_GATE, AND_GATE, OR_GATE, NOR_GATE, NAND_GATE, XNOR_GATE, XOR_GATE,
                ADVANCED_AND_GATE, ADVANCED_OR_GATE, ADVANCED_NOR_GATE, ADVANCED_NAND_GATE, ADVANCED_XNOR_GATE, ADVANCED_XOR_GATE,
                MAJORITY_GATE, MINORITY_GATE,
                ROPE_LADDER,
                PACKED_ICE_DOOR, PACKED_ICE_TRAPDOOR, BLUE_ICE_DOOR, BLUE_ICE_TRAPDOOR, ICE_LANTERN,
                WHITE_PAPER_LANTERN, ORANGE_PAPER_LANTERN, MAGENTA_PAPER_LANTERN,
                LIGHT_BLUE_PAPER_LANTERN, YELLOW_PAPER_LANTERN, LIME_PAPER_LANTERN, PINK_PAPER_LANTERN, GRAY_PAPER_LANTERN, LIGHT_GRAY_PAPER_LANTERN,
                CYAN_PAPER_LANTERN, PURPLE_PAPER_LANTERN, BLUE_PAPER_LANTERN, BROWN_PAPER_LANTERN, GREEN_PAPER_LANTERN, RED_PAPER_LANTERN, BLACK_PAPER_LANTERN,
                ORNAMENT_FIRECRACKERS, ORNAMENT_LUCKY_COINS
        }) {
            ItemBlockRenderTypes.setRenderLayer((Block) block.get(), RenderType.cutout());
        }

        for (RegistryObject<?> block : new RegistryObject[]{
                ITEM_STAND, GLOW_ITEM_STAND,
                QUARTZ_GLASS, QUARTZ_GLASS_PANE, LAPIS_GLASS, LAPIS_GLASS_PANE, REDSTONE_GLASS, REDSTONE_GLASS_PANE
        }) {
            ItemBlockRenderTypes.setRenderLayer((Block) block.get(), RenderType.cutoutMipped());
        }
    }


}
