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

    @SuppressWarnings({"deprecation", "unchecked"})
    private static void registerRenderLayers() {
        for (RegistryObject<Block> block : new RegistryObject[]{
                STEEL_BARS, STEEL_DOOR, STEEL_TRAPDOOR,
                STONE_PEDESTAL, DEEPSLATE_PEDESTAL, BLACKSTONE_PEDESTAL, QUARTZ_PEDESTAL, PURPUR_PEDESTAL, PRISMARINE_PEDESTAL,
                NOT_GATE, AND_GATE, OR_GATE, NOR_GATE, NAND_GATE, XNOR_GATE, XOR_GATE,
                ADVANCED_AND_GATE, ADVANCED_OR_GATE, ADVANCED_NOR_GATE, ADVANCED_NAND_GATE, ADVANCED_XNOR_GATE, ADVANCED_XOR_GATE,
                MAJORITY_GATE, MINORITY_GATE
        }) {
            ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout());
        }
    }
}
