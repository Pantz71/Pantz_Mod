package pantz.mod.core.other;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import pantz.mod.core.registry.PMBlocks;
import pantz.mod.core.registry.datapack.PMTrimMaterials;

public class PMClientCompat {
    public static void registerClientCompat() {
        registerRenderLayers();
        PMTrimMaterials.registerArmorMaterialOverrides();
    }

    @SuppressWarnings("deprecation")
    private static void registerRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer(PMBlocks.STEEL_BARS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(PMBlocks.STEEL_DOOR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(PMBlocks.STEEL_TRAPDOOR.get(), RenderType.cutout());

    }
}
