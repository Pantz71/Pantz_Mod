package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;
import pantz.mod.core.registry.PMBlocks;

public class SulfurBlock extends Block {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public SulfurBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(LIT, false));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos belowPos = pos.below();
        BlockPos abovePos = pos.above();
        BlockState belowState = level.getBlockState(belowPos);
        BlockState aboveState = level.getBlockState(abovePos);

        boolean fire = belowState.getBlock() instanceof BaseFireBlock || (belowState.getBlock() instanceof CampfireBlock && CampfireBlock.isLitCampfire(belowState));

        if (canClusterGrowAtState(aboveState)) {
            if (fire) {
                level.setBlockAndUpdate(pos, state.setValue(LIT, true));
                level.setBlockAndUpdate(abovePos, PMBlocks.SMALL_SULFUR_BUD.get().defaultBlockState());
            } else {
                level.setBlockAndUpdate(pos, state.setValue(LIT, false));
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean isMoving) {
        if (neighborPos.equals(pos.below())) {
            BlockState belowState = level.getBlockState(neighborPos);

            boolean fire = belowState.getBlock() instanceof BaseFireBlock || (belowState.getBlock() instanceof CampfireBlock && CampfireBlock.isLitCampfire(belowState));

            if (state.getValue(LIT) != fire) {
                level.setBlock(pos, state.setValue(LIT, fire), 2);
            }
        }
    }

    public boolean isLit(BlockState state) {
        return state.getValue(LIT);
    }

    public static boolean canClusterGrowAtState(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor level = context.getLevel();
        BlockPos belowPos = context.getClickedPos().below();
        BlockState belowState = level.getBlockState(belowPos);
        boolean fire = belowState.getBlock() instanceof BaseFireBlock;
        boolean campfire = belowState.getBlock() instanceof CampfireBlock && CampfireBlock.isLitCampfire(belowState);
        return this.defaultBlockState().setValue(LIT, fire || campfire);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}
