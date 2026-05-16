package pantz.mod.common.block.glass;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import pantz.mod.common.utils.CollidableBlock;

public class MagicGlassBlock extends GlassBlock implements CollidableBlock {
    public MagicGlassBlock(Properties properties) {
        super(properties);
    }

    public boolean isAllowedToPass(CollisionContext context) {
        return context instanceof EntityCollisionContext;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return isAllowedToPass(context) ? Shapes.empty() : Shapes.block();
    }

}
