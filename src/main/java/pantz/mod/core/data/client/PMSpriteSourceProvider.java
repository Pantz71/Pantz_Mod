package pantz.mod.core.data.client;

import com.teamabnormals.blueprint.core.api.BlueprintTrims;
import com.teamabnormals.clayworks.core.api.ClayworksTrims;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;
import pantz.mod.core.PantzMod;
import pantz.mod.core.registry.datapack.PMTrimMaterials;

public class PMSpriteSourceProvider extends SpriteSourceProvider {
    public PMSpriteSourceProvider(PackOutput output, ExistingFileHelper fileHelper) {
        super(output, fileHelper, PantzMod.MOD_ID);
    }

    @Override
    protected void addSources() {
        this.atlas(BlueprintTrims.ARMOR_TRIMS_ATLAS)
                .addSource(BlueprintTrims.materialPatternPermutations(
                        PMTrimMaterials.STEEL, PMTrimMaterials.STEEL_DARKER,
                        PMTrimMaterials.SULFUR
                ));

        this.atlas(SpriteSourceProvider.BLOCKS_ATLAS)
                .addSource(BlueprintTrims.materialPermutationsForItemLayers(
                        PMTrimMaterials.STEEL, PMTrimMaterials.STEEL_DARKER,
                        PMTrimMaterials.SULFUR
                ));

        this.atlas(ClayworksTrims.DECORATED_POT_ATLAS)
                .addSource(ClayworksTrims.materialPatternPermutations(
                        PMTrimMaterials.STEEL,
                        PMTrimMaterials.SULFUR
                ));
    }
}
