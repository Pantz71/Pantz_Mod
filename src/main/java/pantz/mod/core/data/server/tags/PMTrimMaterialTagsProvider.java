package pantz.mod.core.data.server.tags;

import com.teamabnormals.blueprint.core.other.tags.BlueprintTrimMaterialTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import pantz.mod.core.PantzMod;
import pantz.mod.core.registry.datapack.PMTrimMaterials;

import java.util.concurrent.CompletableFuture;

public class PMTrimMaterialTagsProvider extends TagsProvider<TrimMaterial> {
    public PMTrimMaterialTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, Registries.TRIM_MATERIAL, pLookupProvider, PantzMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlueprintTrimMaterialTags.GENERATES_OVERRIDES).add(PMTrimMaterials.STEEL).add(PMTrimMaterials.SULFUR);
    }
}
