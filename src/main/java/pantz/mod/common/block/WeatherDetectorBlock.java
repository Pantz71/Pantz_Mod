package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WeatherDetectorBlock extends Block {
    public static final IntegerProperty POWER = BlockStateProperties.POWER;
    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 6, 16);
    public WeatherDetectorBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(POWER, 0).setValue(INVERTED, false));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return true;
    }

    @Override
    public boolean isSignalSource(BlockState pState) {
        return true;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction) {
        return state.getValue(POWER);
    }

    public void updateSignalStrength(BlockState state, Level level, BlockPos pos) {
        if (!level.dimensionType().hasSkyLight() || !level.canSeeSky(pos)) {
            if (state.getValue(POWER) != 0) {
                level.setBlock(pos, state.setValue(POWER, 0), 3);
            }
            return;
        }

        boolean inverted = state.getValue(INVERTED);
        boolean isRaining = level.isRaining();
        boolean isThundering = level.isThundering();
        boolean isClear = !isRaining && !isThundering;

        int targetPower;
        if (isClear) {
            targetPower = inverted ? 0 : 15;
        } else if (isThundering) {
            targetPower = inverted ? 15 : 1;
        } else {
            targetPower = inverted ? 7 : 10;
        }

        int power = state.getValue(POWER);
        if (power != targetPower) {
            level.setBlockAndUpdate(pos, state.setValue(POWER, targetPower));
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide()) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        updateSignalStrength(state, level, pos);
        level.scheduleTick(pos, this, 1);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.mayBuild()) {
            if (level.isClientSide()) return InteractionResult.SUCCESS;

            BlockState newState = state.cycle(INVERTED);
            level.setBlock(pos, newState, 4);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));
            updateSignalStrength(newState, level, pos);

            return InteractionResult.CONSUME;
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWER, INVERTED);
    }
}
