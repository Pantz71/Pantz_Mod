package pantz.mod.core.other;

import com.teamabnormals.caverns_and_chasms.common.block.IngotBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import pantz.mod.core.registry.PMBlocks;
import pantz.mod.core.registry.PMItems;

import java.util.function.Supplier;

public class PMConstant {
    public static final String CAVERNS_AND_CHASMS = "caverns_and_chasms";

    public static final Supplier<Block> STEEL_INGOT = () -> new IngotBlock(PMItems.STEEL_INGOT, BlockBehaviour.Properties.ofFullCopy(PMBlocks.STEEL_BLOCK.get()));

}
