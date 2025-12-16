package pantz.mod.core.data.client;

import com.teamabnormals.blueprint.core.data.client.BlueprintBlockStateProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import pantz.mod.core.PantzMod;
import pantz.mod.core.other.PMBlockFamilies;

import static pantz.mod.core.registry.PMBlocks.*;

public class PMBlockStateProvider extends BlueprintBlockStateProvider {
    public PMBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PantzMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.block(STEEL_BLOCK);
        this.ironBarsBlock(STEEL_BARS);
        this.doorBlocks(STEEL_DOOR.get(), STEEL_TRAPDOOR.get());

        this.block(SULFUR_BLOCK);
        this.blockFamily(PMBlockFamilies.SULFUR_BRICKS_FAMILY);
        this.block(NETHER_SULFUR_ORE);
        this.block(CHISELED_SULFUR_BRICKS);
    }
}
