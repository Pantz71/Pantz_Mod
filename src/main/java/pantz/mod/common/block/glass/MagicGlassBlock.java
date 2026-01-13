package pantz.mod.common.block.glass;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import pantz.mod.common.utils.CollidableBlock;

public class MagicGlassBlock extends StainedGlassBlock implements CollidableBlock {
    public MagicGlassBlock(DyeColor pDyeColor, Properties pProperties) {
        super(pDyeColor, pProperties);
    }

    public boolean isAllowedToPass(CollisionContext context) {
        return context instanceof EntityCollisionContext;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (isAllowedToPass(context)) {
            return Shapes.empty();
        }
        return Shapes.block();
    }
}
