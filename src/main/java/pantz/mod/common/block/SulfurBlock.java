package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import pantz.mod.core.registry.PMBlocks;

public class SulfurBlock extends Block {

    public SulfurBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos belowPos = pos.below();
        BlockPos abovePos = pos.above();
        BlockState belowState = level.getBlockState(belowPos);
        BlockState aboveState = level.getBlockState(abovePos);

        if ((belowState.getBlock() instanceof BaseFireBlock ||
                (belowState.getBlock() instanceof CampfireBlock && CampfireBlock.isLitCampfire(belowState)))
                && canClusterGrowAtState(aboveState)) {
            level.setBlock(abovePos, PMBlocks.SMALL_SULFUR_BUD.get().defaultBlockState(), 3);
        }
    }

    public static boolean canClusterGrowAtState(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8;
    }
}
