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
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import pantz.mod.core.registry.PMBlocks;

public class SulfurBlock extends Block {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public SulfurBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(LIT, false));
    }

    private boolean isLavaNearby(LevelAccessor level, BlockPos pos) {
        int radius = 4;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= 0; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos checkPos = pos.offset(x, y, z);
                    BlockState state = level.getBlockState(checkPos);
                    if (state.is(Blocks.LAVA) || state.getFluidState().is(Fluids.LAVA)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean shouldLit(LevelAccessor level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        boolean isFireBelow = belowState.getBlock() instanceof BaseFireBlock ||
                (belowState.getBlock() instanceof CampfireBlock && CampfireBlock.isLitCampfire(belowState));

        return isFireBelow || isLavaNearby(level, pos);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        boolean growRequirement = shouldLit(level, pos);

        if (canClusterGrowAtState(aboveState)) {
            if (growRequirement) {
                level.setBlockAndUpdate(pos, state.setValue(LIT, true));
                level.setBlockAndUpdate(abovePos, PMBlocks.SMALL_SULFUR_BUD.get().defaultBlockState());
            } else {
                level.setBlockAndUpdate(pos, state.setValue(LIT, false));
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean isMoving) {
        boolean growRequirement = shouldLit(level, pos);
        if (state.getValue(LIT) != growRequirement) {
            level.setBlock(pos, state.setValue(LIT, growRequirement), 2);
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
        return this.defaultBlockState().setValue(LIT, shouldLit(context.getLevel(), context.getClickedPos()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}
