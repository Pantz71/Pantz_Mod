package pantz.mod.common.block.glass;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;

public class EchoGlassPaneBlock extends MagicGlassPaneBlock {
    public EchoGlassPaneBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isAllowedToPass(CollisionContext context) {
        return context instanceof EntityCollisionContext ecc && ecc.getEntity() instanceof Projectile;
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }
}
