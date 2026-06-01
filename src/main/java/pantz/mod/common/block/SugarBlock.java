package pantz.mod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class SugarBlock extends DissolveBlock {
    public SugarBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void dissolveEffectAndDrop(Level level, BlockPos pos, BlockState state) {
        super.dissolveEffectAndDrop(level, pos, state);
        if (!level.isClientSide()) {
            Block.popResource(level, pos, new ItemStack(Items.SUGAR, 9));
        }
    }
}
