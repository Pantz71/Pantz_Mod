package pantz.mod.core.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pantz.mod.common.block.AdvancedLogicGateBlock;
import pantz.mod.common.block.LogicGateBlock;
import pantz.mod.common.block.NotGateBlock;

@Mixin(RedStoneWireBlock.class)
public class RedstoneWireBlockMixin {
    @Inject(method = "getConnectingSide(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Lnet/minecraft/world/level/block/state/properties/RedstoneSide;",
            at = @At("HEAD"), cancellable = true)
    private void getConnectingSide(BlockGetter getter, BlockPos pos, Direction direction, boolean conductor, CallbackInfoReturnable<RedstoneSide> cir) {
        BlockPos targetPos = pos.relative(direction);
        BlockState state = getter.getBlockState(targetPos);

        if (!(state.getBlock() instanceof HorizontalDirectionalBlock))
            return;

        Direction facing = state.getValue(HorizontalDirectionalBlock.FACING).getOpposite();

        if (state.getBlock() instanceof NotGateBlock) {
            Direction front = facing.getOpposite();
            RedstoneSide result = (direction == front || direction == facing)
                    ? RedstoneSide.SIDE
                    : RedstoneSide.NONE;
            cir.setReturnValue(result);
            return;
        }

        if (direction == facing) {
            if (state.getBlock() instanceof AdvancedLogicGateBlock) {
                cir.setReturnValue(RedstoneSide.SIDE);
            }

            if (state.getBlock() instanceof LogicGateBlock && !(state.getBlock() instanceof AdvancedLogicGateBlock)) {
                cir.setReturnValue(RedstoneSide.NONE);
            }


        }
    }
}
