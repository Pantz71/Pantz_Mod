package pantz.mod.common.block.glass;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;

public class LapisGlassPaneBlock extends MagicGlassPaneBlock {
    public LapisGlassPaneBlock(DyeColor pDyeColor, Properties pProperties) {
        super(pDyeColor, pProperties);
    }

    public LapisGlassPaneBlock(Properties pProperties) {
        super(DyeColor.BLUE, pProperties);
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return true;
    }

    @Override
    public boolean isAllowedToPass(CollisionContext context) {
        return context instanceof EntityCollisionContext ecc && !(ecc.getEntity() instanceof Player);
    }
}
