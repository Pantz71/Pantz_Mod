package pantz.mod.core.data.server.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import pantz.mod.core.PantzMod;

import java.util.concurrent.CompletableFuture;

import static pantz.mod.core.other.tags.PMBiomeTags.*;

public class PMBiomeTagsProvider extends BiomeTagsProvider {
    public PMBiomeTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, PantzMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(IS_BASALT_DELTAS).add(Biomes.BASALT_DELTAS);
        this.tag(HAS_SULFUR).add(Biomes.NETHER_WASTES);
    }
}
