package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class DissolveBlock extends FallingBlock {
    public DissolveBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getDustColor(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.getMapColor(pLevel, pPos).col;
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replacedState, FallingBlockEntity entity) {
        if (replacedState.getFluidState().is(FluidTags.WATER)) {
            level.setBlockAndUpdate(pos, replacedState.getFluidState().createLegacyBlock());
            this.dissolveEffectAndDrop(level, pos, state);
            entity.disableDrop();
        } else if (shouldDissolve(level, pos)) {
            dissolve(level, pos, state);
            entity.disableDrop();
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && level.getBlockState(currentPos.below()).isAir()) {
            level.scheduleTick(currentPos, this, this.getDelayAfterPlace());
        }
        if (touchesLiquid(level, currentPos)) {
            level.scheduleTick(currentPos, this, 1);
        }
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }


    private boolean shouldDissolve(Level level, BlockPos pos) {
        return touchesLiquid(level, pos) || isExposedToRain(level, pos);
    }

    private boolean isExposedToRain(Level level, BlockPos pos) {
        return level.isRaining() && level.canSeeSky(pos);
    }

    private boolean touchesLiquid(BlockGetter level, BlockPos pos) {
        boolean touchLiquid = false;
        BlockPos.MutableBlockPos mutableBlockPos = pos.mutable();
        BlockState blockState = level.getBlockState(mutableBlockPos);
        if (isWater(blockState)) {
            return true;
        }
        for (Direction direction : Direction.values()) {
            if (direction == Direction.DOWN) continue;

            mutableBlockPos.setWithOffset(pos, direction);
            blockState = level.getBlockState(mutableBlockPos);
            if (isWater(blockState) && !blockState.isFaceSturdy(level, pos, direction.getOpposite())) {
                touchLiquid = true;
                break;
            }
        }
        return touchLiquid;
    }

    private boolean isWater(BlockState state) {
        return state.getFluidState().is(FluidTags.WATER);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (shouldDissolve(level, pos)) {
            dissolve(level, pos, state);
        } else {
            super.tick(state, level, pos, random);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (isExposedToRain(level, pos)) {
            dissolve(level, pos, state);
        }
    }

    public void dissolve(Level level, BlockPos pos, BlockState state) {
        FluidState fluid = level.getFluidState(pos);
        if (!fluid.isEmpty()) {
            level.setBlockAndUpdate(pos, fluid.createLegacyBlock());
        } else {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
        this.dissolveEffectAndDrop(level, pos, state);
    }

    public void dissolveEffectAndDrop(Level level, BlockPos pos, BlockState state) {
        level.levelEvent(2001, pos, Block.getId(state));
    }
}
