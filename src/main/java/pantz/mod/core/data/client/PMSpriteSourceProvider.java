package pantz.mod.core.data.client;

import com.teamabnormals.blueprint.core.api.BlueprintTrims;
import com.teamabnormals.clayworks.core.api.ClayworksTrims;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;
import pantz.mod.core.PantzMod;
import pantz.mod.core.registry.datapack.PMTrimMaterials;

import java.util.concurrent.CompletableFuture;

public class PMSpriteSourceProvider extends SpriteSourceProvider {
    public PMSpriteSourceProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, PantzMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void gather() {
        this.atlas(BlueprintTrims.ARMOR_TRIMS_ATLAS)
                .addSource(BlueprintTrims.materialPatternPermutations(
                        PMTrimMaterials.STEEL,
                        PMTrimMaterials.STEEL_DARKER,
                        PMTrimMaterials.SULFUR
                ));

        this.atlas(SpriteSourceProvider.BLOCKS_ATLAS)
                .addSource(BlueprintTrims.materialPermutationsForItemLayers(
                        PMTrimMaterials.STEEL,
                        PMTrimMaterials.STEEL_DARKER,
                        PMTrimMaterials.SULFUR
                ));

        this.atlas(ClayworksTrims.DECORATED_POT_ATLAS)
                .addSource(ClayworksTrims.materialPatternPermutations(
                        PMTrimMaterials.STEEL,
                        PMTrimMaterials.SULFUR
                ));
    }
}
