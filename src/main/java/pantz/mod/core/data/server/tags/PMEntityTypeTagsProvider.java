package pantz.mod.core.data.server.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import pantz.mod.core.PantzMod;

import java.util.concurrent.CompletableFuture;

import static pantz.mod.core.other.tags.PMEntityTypeTags.*;

public class PMEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public PMEntityTypeTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, PantzMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ENDER_SCANNER_IMMUNE_TYPES).add(EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.ENDER_DRAGON);
    }
}
