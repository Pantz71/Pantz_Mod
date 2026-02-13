package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PaperLanternBlock extends LanternBlock {
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(6, 0, 6, 10, 1, 10),
            Block.box(4, 1, 4, 12, 8, 12),
            Block.box(6, 8, 6, 10, 9, 10)
    );
    private static final VoxelShape HANGING_SHAPE = Shapes.or(
            Block.box(6, 1, 6, 10, 2, 10),
            Block.box(4, 2, 4, 12, 9, 12),
            Block.box(6, 9, 6, 10, 10, 10)
    );
    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;

    public PaperLanternBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(HANGING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HANGING, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(HANGING)) {
            return HANGING_SHAPE;
        }
        return SHAPE;
    }
}
